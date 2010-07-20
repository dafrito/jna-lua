package com.dafrito.lua.script;

import java.lang.ref.WeakReference;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

import com.google.common.base.FinalizablePhantomReference;
import com.google.common.base.FinalizableReferenceQueue;

public class LuaReference {
	private static final FinalizableReferenceQueue queue = new FinalizableReferenceQueue();
	private static final LuaLibrary lua = LuaLibrary.INSTANCE;
	
	private final LuaBindings b;
	private final int ref;

	public LuaReference(LuaBindings b) {
		this.b = b;
		this.ref = lua.luaL_ref(b.getState(), LuaLibrary.LUA_REGISTRYINDEX);
		new LuaPhantomReference(this, queue);
	}

	public void get() {
		lua.lua_rawgeti(b.getState(), LuaLibrary.LUA_REGISTRYINDEX, ref);
	}

	private static class LuaPhantomReference extends FinalizablePhantomReference<LuaReference> {

		private final int ref;
		private final WeakReference<lua_State> state;
		
		protected LuaPhantomReference(LuaReference referent, FinalizableReferenceQueue queue) {
			super(referent, queue);
			this.state = new WeakReference<lua_State>(referent.b.getState());
			this.ref = referent.ref;
		}
		
		@Override
		public void finalizeReferent() {
			lua_State state = this.state.get();
			if(state != null) {
				this.state.clear();
				lua.luaL_unref(state, LuaLibrary.LUA_REGISTRYINDEX, ref);	
			}
		}
	}

}