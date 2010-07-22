package com.dafrito.lua.script;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.RandomAccess;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

public class LuaList extends AbstractList<Object> implements RandomAccess {
	private static final LuaLibrary lua = LuaLibrary.INSTANCE;
	
	private final LuaBindings b;
	private final LuaReference ref;
	private final lua_State s;
	
	public LuaList(LuaBindings b) {
		this(LuaReference.newTable(b));
	}

	public LuaList(LuaReference ref) {
		this.b = ref.getBindings();
		this.ref = ref;
		this.s = b.getState();
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public Object remove(int i) {
		check(i);
		Object v = get(i);
		ref.get();
		int sz = size();
		i++; // Increment to get this value into lua-terms
		for(; i < sz; i++) {
			lua.lua_rawgeti(s, -2, i+1);
			lua.lua_rawseti(s, -2, i);
		}
		lua.lua_pushnil(s);
		lua.lua_rawseti(s, -2, i);
		lua.lua_settop(s, -2);
		return v;
	}

	public boolean add(Object v) {
		if(v == null) {
			throw new IllegalArgumentException("null elements are not allowed");
		}
		ref.get();
		int sz = size();
		this.b.toLua(v);
		lua.lua_rawseti(s, -2, sz+1);
		lua.lua_settop(s, -2);
		return true;
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
	
	@Override
	public Object get(int index) {
		check(index);
		ref.get();
		lua.lua_rawgeti(s, -1, index + 1);
		Object v = b.fromLua(-1);
		lua.lua_settop(s, -2);
		return v;
	}

	@Override
	public Object set(int index, Object element) {
		check(index);
		if(element == null) {
			throw new IllegalArgumentException("null elements are not allowed");
		}
		ref.get();
		lua.lua_rawgeti(s, -1, index + 1);
		Object v = b.fromLua(-1);
		lua.lua_settop(s, -2);
		b.toLua(element);
		lua.lua_rawseti(s, -2, index + 1);
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

	@Override
	public void clear() {
		ref.get();
		int sz = size();
		for(int i=0; i < sz; i++) {
			lua.lua_pushnil(s);
			lua.lua_rawseti(s, -2, i);
		}
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
				LuaList.this.remove(--i);
			}
		};
		
	}

	private void check(int idx) {
		if(idx < 0 || idx >= size()) {
			throw new IndexOutOfBoundsException("Index: " + idx + ", Size: " + size());
		}
	}

}
