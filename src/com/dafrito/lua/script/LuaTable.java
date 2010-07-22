package com.dafrito.lua.script;

import java.util.Iterator;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

public class LuaTable implements Iterable<Object> {
	private static final LuaLibrary lua = LuaLibrary.INSTANCE;
	
	private final LuaBindings b;
	private final LuaReference ref;
	private final lua_State s;
	
	public LuaTable(LuaBindings b) {
		this.b=b;
		this.s=b.getState();
		this.ref = LuaReference.newTable(b);
	}

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
	
	public Iterator<Object> iterator() {
		ref.get();
		return new Iterator<Object>() {
			private int i=0;

			@Override
			public boolean hasNext() {
				return size() > i;
			}

			@Override
			public Object next() {
				lua.lua_rawgeti(s, -1, ++i);
				Object v = b.fromLua(-1);
				lua.lua_settop(s, -2);
				return v;
			}

			@Override
			public void remove() {
				LuaTable.this.remove(--i);
			}
		};
		
	}

}
