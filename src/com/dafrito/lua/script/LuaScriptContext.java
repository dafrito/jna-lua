package com.dafrito.lua.script;

import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.SimpleBindings;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

public class LuaScriptContext implements ScriptContext {

	private static final Bindings GLOBAL_BINDINGS = new SimpleBindings();
	private Bindings globalBindings = GLOBAL_BINDINGS;
	private LuaBindings engineBindings;
	private final lua_State state;
	private LuaTranslator translator = new PrimitiveLuaTranslator();

	public LuaScriptContext() {
		this.state = LuaLibrary.INSTANCE.luaL_newstate();
		this.engineBindings = new LuaBindings(this.state, this.translator);
	}

	public LuaScriptContext(ScriptContext context) {
		this();
		this.engineBindings.putAll(context.getBindings(ScriptContext.ENGINE_SCOPE));
	}

	@Override
	public List<Integer> getScopes() {
		return Collections.unmodifiableList(Arrays.asList(ScriptContext.ENGINE_SCOPE, ScriptContext.GLOBAL_SCOPE));
	}
	
	@Override
	public Object getAttribute(String name) {
		int scope = this.getAttributesScope(name);
		if(scope == -1) {
			return null;
		}
		return this.getAttribute(name, scope);
	}

	@Override
	public Object getAttribute(String name, int scope) {
		return this.getBindings(scope).get(name);
	}

	@Override
	public int getAttributesScope(String name) {
		for(int scope : this.getScopes()) {
			if(this.getAttribute(name, scope) != null) {
				return scope;
			}
		}
		return -1;
	}

	@Override
	public void setAttribute(String name, Object value, int scope) {
		this.getBindings(scope).put(name, value);
	}

	@Override
	public Object removeAttribute(String name, int scope) {
		return this.getBindings(scope).remove(name);
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
	public void setErrorWriter(Writer writer) {
		// TODO Auto-generated method stub

	}

	@Override
	public Reader getReader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setReader(Reader reader) {
		// TODO Auto-generated method stub

	}

	@Override
	public Writer getWriter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWriter(Writer writer) {
		// TODO Auto-generated method stub

	}
	@Override
	public void setBindings(Bindings bindings, int scope) {
		switch (scope) {
		case ScriptContext.ENGINE_SCOPE:
			if(bindings instanceof LuaBindings) {
				this.engineBindings=(LuaBindings)bindings;
			} else {
				this.engineBindings.putAll(bindings);
			}
			break;
		case ScriptContext.GLOBAL_SCOPE:
			this.globalBindings=bindings;
			break;
		default:
			throw new IllegalArgumentException("Scope is not supported. Scope: " + scope);
		}
	}

	public lua_State getState() {
		return state;
	}

	public LuaTranslator getTranslator() {
		return this.translator;
	}
	
	public void setTranslator(LuaTranslator translator) {
		this.translator = translator;
	}

}
