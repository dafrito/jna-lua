package com.dafrito.lua.script;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

import com.google.common.base.FinalizablePhantomReference;
import com.google.common.base.FinalizableReferenceQueue;

public class LuaReference {
	private static final FinalizableReferenceQueue queue = new FinalizableReferenceQueue();
	private static final Set<Reference<?>> references = new HashSet<Reference<?>>();
	private static final LuaLibrary lua = LuaLibrary.INSTANCE;
	
	private final LuaBindings bindings;
	private final int ref;

	private LuaReference(LuaBindings b) {
		this.bindings = b;
		this.ref = lua.luaL_ref(b.getState(), LuaLibrary.LUA_REGISTRYINDEX);
		references.add(new LuaPhantomReference(this, queue));
	}

	public void stage() {
		lua.lua_rawgeti(bindings.getState(), LuaLibrary.LUA_REGISTRYINDEX, ref);
	}
	
	
	public LuaBindings getBindings() {
		return bindings;
	}
	
	private static class LuaPhantomReference extends FinalizablePhantomReference<LuaReference> {

		private final int ref;
		private final WeakReference<lua_State> state;
		
		protected LuaPhantomReference(LuaReference referent, FinalizableReferenceQueue queue) {
			super(referent, queue);
			this.state = new WeakReference<lua_State>(referent.getBindings().getState());
			this.ref = referent.ref;
		}
		
		@Override
		public void finalizeReferent() {
			references.remove(this);
			lua_State state = this.state.get();
			if(state != null) {
				this.state.clear();
				lua.luaL_unref(state, LuaLibrary.LUA_REGISTRYINDEX, ref);	
			}
		}
	}
	
	public static LuaReference newTable(LuaBindings b) {
		lua.lua_createtable(b.getState(), 0, 0);
		return new LuaReference(b);
	}
	
	public static LuaReference fromStack(LuaBindings b, int idx) {
		lua.lua_pushvalue(b.getState(), idx);
		return new LuaReference(b);
	}

}