package com.dafrito.lua.script;

import java.io.Reader;
import java.io.Writer;
import java.util.List;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.SimpleBindings;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

public class LuaScriptContext implements ScriptContext {

	private static final Bindings GLOBAL_BINDINGS = new SimpleBindings();
	private Bindings globalBindings = GLOBAL_BINDINGS;
	private Bindings engineBindings;
	private final lua_State state;

	public LuaScriptContext() {
		this.state = LuaLibrary.INSTANCE.luaL_newstate();
		this.engineBindings = new LuaBindings(this.state);
	}

	@Override
	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAttribute(String name, int scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getAttributesScope(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Bindings getBindings(int scope) {
		switch (scope) {
		case ScriptContext.ENGINE_SCOPE:
			return this.engineBindings;
		case ScriptContext.GLOBAL_SCOPE:
			return this.globalBindings;
		default:
			return null;
		}
	}

	@Override
	public Writer getErrorWriter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getReader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Integer> getScopes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Writer getWriter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object removeAttribute(String name, int scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAttribute(String name, Object value, int scope) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBindings(Bindings bindings, int scope) {
		switch (scope) {
		case ScriptContext.ENGINE_SCOPE:
			// TODO: This is incorrect. New bindings should be copied to the lua environment. 
			this.engineBindings=bindings;
			break;
		case ScriptContext.GLOBAL_SCOPE:
			this.globalBindings=bindings;
			break;
		default:
			throw new IllegalArgumentException("invalid scope");
		}
	}

	@Override
	public void setErrorWriter(Writer writer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setReader(Reader reader) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWriter(Writer writer) {
		// TODO Auto-generated method stub

	}

	public lua_State getState() {
		return state;
	}

}
