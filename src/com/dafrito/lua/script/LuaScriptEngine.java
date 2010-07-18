package com.dafrito.lua.script;

import java.io.Reader;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

public class LuaScriptEngine implements ScriptEngine {

	private static final LuaLibrary lua = LuaLibrary.INSTANCE;

	private final LuaScriptEngineFactory factory;
	private LuaScriptContext context;

	public LuaScriptEngine(LuaScriptEngineFactory luaScriptEngineFactory) {
		this.factory = luaScriptEngineFactory;
		this.context = new LuaScriptContext();
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
		if (!(context instanceof LuaScriptContext)) {
			// TODO: This behavior is required by the spec, but it's not
			// important right now.
			throw new UnsupportedOperationException(
					"Importing a foreign context is not yet supported");
		}
		LuaScriptContext lcontext = (LuaScriptContext) context;
		lua_State s = lcontext.getState();
		lua.luaL_loadstring(s, script);
		lua.lua_call(s, 0, 1);
		double rv = lua.lua_tonumber(s, 1);
		lua.lua_remove(s, 1);
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

	@Override
	public Object eval(String script) throws ScriptException {
		return this.eval(script, this.getContext());
	}

	@Override
	public Object eval(Reader reader) throws ScriptException {
		return this.eval(reader, this.getContext());
	}

	@Override
	public Object eval(String script, Bindings n) throws ScriptException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object eval(Reader reader, Bindings n) throws ScriptException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object get(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bindings getBindings(int scope) {
		return this.getContext().getBindings(scope);
	}

	@Override
	public ScriptContext getContext() {
		return this.context;
	}

	@Override
	public void put(String key, Object value) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setBindings(Bindings bindings, int scope) {
		this.getContext().setBindings(bindings, scope);
	}

	@Override
	public void setContext(ScriptContext context) {
		if (!(context instanceof LuaScriptContext)) {
			// TODO: This is required by the specification, but it's not
			// important yet.
			throw new UnsupportedOperationException(
					"Foreign ScriptContext objects are not yet supported");
		}
		this.context = (LuaScriptContext) context;
	}

}
