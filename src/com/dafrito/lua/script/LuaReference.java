package com.dafrito.lua.script;

import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.FinalizablePhantomReference;
import com.google.common.base.FinalizableReferenceQueue;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

public class LuaReference {
	private static final FinalizableReferenceQueue queue = new FinalizableReferenceQueue();
	private static final LuaLibrary lua = LuaLibrary.INSTANCE;
	
	private final LuaBindings b;
	private AtomicInteger ref;
	private final LuaPhantomReference finalizer;

	public LuaReference(LuaBindings b) {
		this.b = b;
		this.ref = new AtomicInteger(lua.luaL_ref(b.getState(), LuaLibrary.LUA_REGISTRYINDEX));
		this.finalizer = new LuaPhantomReference(this, queue);
		check();
	}

	public LuaBindings getBindings() {
		return this.b;
	}

	public void get() {
		check();
		lua.lua_rawgeti(b.getState(), LuaLibrary.LUA_REGISTRYINDEX, this.ref.get());
	}

	private void check() {
		if (this.isClosed()) {
			throw new RuntimeException();
		}
	}

	public boolean isClosed() {
		return this.ref.get() == LuaLibrary.LUA_REFNIL;
	}
	
	public void close() {
		this.finalizer.close();
	}

	private static class LuaPhantomReference extends FinalizablePhantomReference<LuaReference> {

		private AtomicInteger ref;
		private lua_State state;
		
		protected LuaPhantomReference(LuaReference referent, FinalizableReferenceQueue queue) {
			super(referent, queue);
			this.state = referent.getBindings().getState();
			this.ref = referent.ref;
		}
		
		public void close() {
			int v = this.ref.getAndSet(LuaLibrary.LUA_REFNIL);
			if(v != LuaLibrary.LUA_REFNIL) {
				lua.luaL_unref(state, LuaLibrary.LUA_REGISTRYINDEX, v);	
			}
		}

		@Override
		public void finalizeReferent() {
			this.close();
		}
	}

}