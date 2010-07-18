package com.dafrito.lua.script;

import java.io.Reader;
import java.io.Writer;
import java.util.List;

import javax.script.Bindings;
import javax.script.ScriptContext;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

public class LuaScriptContext implements ScriptContext {

	private final lua_State state;

	public LuaScriptContext() {
		this.state = LuaLibrary.INSTANCE.luaL_newstate();
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
		return new LuaBindings();
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
		// TODO Auto-generated method stub
		
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
