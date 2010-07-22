package com.dafrito.lua.script;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

public class LuaTable {
	private static final LuaLibrary lua = LuaLibrary.INSTANCE;
	
	private final LuaBindings b;
	private final LuaReference ref;
	private final lua_State s;

	public LuaTable(LuaReference ref) {
		this.b = ref.getBindings();
		this.ref = ref;
		this.s = b.getState();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public void remove(int i) {
		i++;
		ref.get();
		int sz = size();
		for(; i < sz; i++) {
			lua.lua_rawgeti(s, -2, i+1);
			lua.lua_rawseti(s, -2, i);
		}
		lua.lua_pushnil(s);
		lua.lua_rawseti(s, -2, i);
		lua.lua_settop(s, -2);
	}

	public void add(Object v) {
		ref.get();
		int sz = size();
		this.b.toLua(v);
		lua.lua_rawseti(s, -2, sz+1);
		lua.lua_settop(s, -2);
	}

	public int size() {
		ref.get();
		int sz = lua.lua_objlen(s, -1).intValue();
		lua.lua_settop(s, -2);
		return sz;
	}
	
	public Object get(Object k) {
		ref.get();
		b.toLua(k);
		lua.lua_gettable(s, -2);
		Object v = b.fromLua(-1);
		lua.lua_settop(s, -2);
		return v;
	}

	public void set(Object k, Object v) {
		ref.get();
		b.toLua(k);
		b.toLua(v);
		lua.lua_settable(s, -3);
		lua.lua_settop(s, -2);
	}
}
