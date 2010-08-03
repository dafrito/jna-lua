package com.dafrito.lua.script;

import lua.LuaLibrary;

public interface Variable {

	public abstract LuaBindings getBindings();

	public abstract void stage();

	public abstract Object get();

	public abstract void set(Object v);

}