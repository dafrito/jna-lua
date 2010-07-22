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
			return this.eval(script, new LuaScriptContext(context));
		}
		LuaBindings b = ((LuaScriptContext) context).getGlobals();
		lua_State s = b.getState();
		lua.luaL_loadstring(s, script);
		Object args = this.get(ScriptEngine.ARGV);
		if(args != null) {
		}
		if(lua.lua_pcall(s, 0, LuaLibrary.LUA_MULTRET, 0) != 0) {
			throw new RuntimeException(b.fromLua(1).toString());
		}
		if(lua.lua_gettop(s) > 1) {
			throw new UnsupportedOperationException("Multiple return values is not yet supported. Values returned: " + lua.lua_gettop(s));
		}
		Object v = b.fromLua(-1);
		lua.lua_settop(s, -2);
		return v;
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
		return this.getContext().getBindings(ScriptContext.ENGINE_SCOPE).get(key);
	}

	@Override
	public void put(String key, Object value) {
		this.getContext().getBindings(ScriptContext.ENGINE_SCOPE).put(key, value);
	}

	@Override
	public Bindings getBindings(int scope) {
		return this.getContext().getBindings(scope);
	}
	
	@Override
	public void setBindings(Bindings bindings, int scope) {
		this.getContext().setBindings(bindings, scope);
	}

	@Override
	public LuaScriptContext getContext() {
		return this.context;
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
