package com.dafrito.lua.script;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;
import javax.script.SimpleBindings;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

public class LuaBindings implements Bindings {

	private static final LuaLibrary lua = LuaLibrary.INSTANCE;
	
	private final lua_State state;

	private LuaTranslator translator;

	public LuaBindings(LuaLibrary.lua_State state) {
		this(state, new LuaTranslator());
	}
	
	public LuaBindings(LuaLibrary.lua_State state, LuaTranslator translator) {
		this.state = state;
		this.translator = translator;
	}
	
	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object get(Object key) {
		if(key == null) {
			throw new NullPointerException("key must not be null");
		}
		// TODO: This method pollutes the stack if it fails.
		this.translator.toLua(state, key);
		lua.lua_gettable(state, LuaLibrary.LUA_GLOBALSINDEX);
		return this.translator.fromLua(state, 1);
	}

	@Override
	public Object put(String name, Object value) {
		if(name == null) {
			throw new NullPointerException("key must not be null");
		}
		Object old = this.get(name);
		// TODO: This method pollutes the stack if it fails.
		this.translator.toLua(state, name);
		this.translator.toLua(state, value);
		lua.lua_settable(state, LuaLibrary.LUA_GLOBALSINDEX);
		return old;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> toMerge) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object remove(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Set<String> keySet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Collection<Object> values() {
		// TODO Auto-generated method stub
		return null;
	}


}
