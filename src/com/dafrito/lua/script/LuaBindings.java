package com.dafrito.lua.script;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import javax.script.Bindings;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

public class LuaBindings extends AbstractMap<String, Object> implements Bindings {

	private static final LuaLibrary lua = LuaLibrary.INSTANCE;
	
	private final lua_State state;

	private final LuaTranslator translator;

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
		if(key instanceof String && ((String)key).startsWith("javax.script.")) {
			return this.getSpecialProperty((String)key);
		}
		// TODO: This method pollutes the stack if it fails.
		this.getTranslator().toLua(getState(), key);
		lua.lua_gettable(getState(), LuaLibrary.LUA_GLOBALSINDEX);
		Object v = this.getTranslator().fromLua(getState(), lua.lua_gettop(getState()));
		lua.lua_settop(getState(), -2);
		return v;
	}

	@Override
	public Object put(String name, Object value) {
		return this.doPut(name, value);
	}
	
	private Object doPut(Object name, Object value) {
		if(name == null) {
			throw new NullPointerException("key must not be null");
		}
		if(name instanceof String && ((String)name).startsWith("javax.script.")) {
			return this.setSpecialProperty((String)name, value);
		}
		Object old = this.get(name);
		// TODO: This method pollutes the stack if it fails.
		this.getTranslator().toLua(getState(), name);
		this.getTranslator().toLua(getState(), value);
		lua.lua_settable(getState(), LuaLibrary.LUA_GLOBALSINDEX);
		return old;
	}

	private Object setSpecialProperty(String name, Object value) {
		if(name.equals("javax.script.argv")) {
			return this.put("arg", (Object[])value);
		} else {
			// We ignore other special properties
			return null;
		}
	}
	
	private Object getSpecialProperty(String name) {
		if(name.equals("javax.script.argv")) {
			return this.get("arg");
		} else {
			// We ignore other special properties
			return null;
		}
	}

	@Override
	public Object remove(Object key) {
		return this.doPut(key, null);
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

	public lua_State getState() {
		return state;
	}
	
	public LuaTranslator getTranslator() {
		return translator;
	}

	public Object fromLua(int idx) {
		return translator.fromLua(state, idx);
	}

	public void toLua(Object v) {
		translator.toLua(state, v);		
	}

}
