package com.dafrito.lua.script;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

import com.google.common.base.FinalizablePhantomReference;
import com.google.common.base.FinalizableReferenceQueue;

public class ReferenceVariable implements Variable {
	private static final FinalizableReferenceQueue queue = new FinalizableReferenceQueue();
	private static final Set<Reference<?>> references = new HashSet<Reference<?>>();
	private static final LuaLibrary lua = LuaLibrary.INSTANCE;
	
	private final LuaBindings bindings;
	private final int ref;

	public ReferenceVariable(LuaBindings b, Object v) {
		this.bindings = b;
		b.toLua(v);
		this.ref = lua.luaL_ref(b.getState(), LuaLibrary.LUA_REGISTRYINDEX);
		references.add(new LuaPhantomReference(this, queue));
	}
	
	public ReferenceVariable(LuaBindings b, int idx) {
		this.bindings=b;
		lua.lua_pushvalue(b.getState(), idx);
		this.ref = lua.luaL_ref(b.getState(), LuaLibrary.LUA_REGISTRYINDEX);
		references.add(new LuaPhantomReference(this, queue));
	}

	public LuaBindings getBindings() {
		return bindings;
	}

	public void stage() {
		lua.lua_rawgeti(bindings.getState(), LuaLibrary.LUA_REGISTRYINDEX, ref);
	}

	public Object get() {
		stage();
		Object v = bindings.fromLua(-1);
		lua.lua_settop(bindings.getState(), -2);
		return v;
	}

	public void set(Object v) {
		bindings.toLua(v);
		lua.lua_rawseti(bindings.getState(), LuaLibrary.LUA_REGISTRYINDEX, ref);
	}

	private static class LuaPhantomReference extends FinalizablePhantomReference<ReferenceVariable> {

		private final int ref;
		private final WeakReference<lua_State> state;
		
		protected LuaPhantomReference(ReferenceVariable referent, FinalizableReferenceQueue queue) {
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
}