package com.dafrito.lua.script;

import java.io.Reader;

import javax.script.AbstractScriptEngine;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

public class LuaScriptEngine extends AbstractScriptEngine implements ScriptEngine {

	private static final LuaLibrary lua = LuaLibrary.INSTANCE;
	
	private final LuaScriptEngineFactory factory;
	private final lua_State state;

	public LuaScriptEngine(LuaScriptEngineFactory luaScriptEngineFactory) {
		this.factory = luaScriptEngineFactory;
		this.state = LuaLibrary.INSTANCE.luaL_newstate();
	}

	public LuaScriptEngine() {
		this(null); 
	}

	@Override
	public Bindings createBindings() {
		return new SimpleBindings();
	}

	@Override
	public Object eval(String script, ScriptContext context)
			throws ScriptException {
		lua.luaL_loadstring(this.state, script);
		lua.lua_call(this.state, 0, 1);
		double rv=lua.lua_tonumber(this.state, 1);
		lua.lua_remove(this.state, 1);
		return rv;
	}

	@Override
	public Object eval(Reader reader, ScriptContext context)
			throws ScriptException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScriptEngineFactory getFactory() {
		return this.factory;
	}

}
