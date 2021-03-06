package com.dafrito.lua.script;

import java.util.AbstractList;
import java.util.RandomAccess;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

public class LuaList extends AbstractList<Object> implements RandomAccess {
	private static final LuaLibrary lua = LuaLibrary.INSTANCE;

	private final LuaBindings b;
	private final Variable ref;
	private final lua_State s;

	public LuaList(LuaBindings b) {
		this(b.newTable());
	}

	public LuaList(Variable ref) {
		this.b = ref.getBindings();
		this.ref = ref;
		this.s = b.getState();
	}

	public boolean add(Object v) {
		if (v == null) {
			throw new IllegalArgumentException("null elements are not allowed");
		}
		ref.stage();
		int sz = size();
		this.b.toLua(v);
		lua.lua_rawseti(s, -2, sz + 1);
		lua.lua_settop(s, -2);
		return true;
	}

	@Override
	public void add(int pos, Object element) {
		check(pos);
		ref.stage();
		pos++;
		for (int i = size() + 1; i >= pos ; i--) {
			lua.lua_rawgeti(s, -1, i);
			lua.lua_rawseti(s, -2, i + 1);
		}
		b.toLua(element);
		lua.lua_rawseti(s, -2, pos);
		lua.lua_settop(s, -2);
	}

	@Override
	public Object get(int index) {
		check(index);
		ref.stage();
		lua.lua_rawgeti(s, -1, index + 1);
		Object v = b.fromLua(-1);
		lua.lua_settop(s, -2);
		return v;
	}

	@Override
	public Object set(int index, Object element) {
		check(index);
		if (element == null) {
			throw new IllegalArgumentException("null elements are not allowed");
		}
		ref.stage();
		lua.lua_rawgeti(s, -1, index + 1);
		Object v = b.fromLua(-1);
		lua.lua_settop(s, -2);
		b.toLua(element);
		lua.lua_rawseti(s, -2, index + 1);
		lua.lua_settop(s, -2);
		return v;
	}

	public int size() {
		ref.stage();
		int sz = lua.lua_objlen(s, -1).intValue();
		lua.lua_settop(s, -2);
		return sz;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public Object remove(int i) {
		check(i);
		Object v = get(i);
		ref.stage();
		int sz = size();
		i++; // Increment to get this value into lua-terms
		for (; i < sz; i++) {
			lua.lua_rawgeti(s, -2, i + 1);
			lua.lua_rawseti(s, -2, i);
		}
		lua.lua_pushnil(s);
		lua.lua_rawseti(s, -2, i);
		lua.lua_settop(s, -2);
		return v;
	}

	@Override
	public void clear() {
		ref.stage();
		int sz = size();
		for (int i = 0; i < sz; i++) {
			lua.lua_pushnil(s);
			lua.lua_rawseti(s, -2, i);
		}
		lua.lua_settop(s, -2);
	}

	// TODO: This is more map-like than List-like. We should eventually move it.
	public Object get(Object k) {
		ref.stage();
		b.toLua(k);
		lua.lua_gettable(s, -2);
		Object v = b.fromLua(-1);
		lua.lua_settop(s, -2);
		return v;
	}

	public void set(Object k, Object v) {
		ref.stage();
		b.toLua(k);
		b.toLua(v);
		lua.lua_settable(s, -3);
		lua.lua_settop(s, -2);
	}

	private void check(int idx) {
		if (idx < 0 || idx >= size()) {
			throw new IndexOutOfBoundsException("Index: " + idx + ", Size: " + size());
		}
	}

}
