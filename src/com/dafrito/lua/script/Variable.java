package com.dafrito.lua.script;


public interface Variable {

	public abstract LuaBindings getBindings();

	public abstract void stage();

	public abstract Object get();

	public abstract void set(Object v);

}