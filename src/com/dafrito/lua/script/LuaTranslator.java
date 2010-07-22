package com.dafrito.lua.script;


public interface LuaTranslator {

	public void toLua(LuaBindings b, Object v);

	public Object fromLua(LuaBindings b, int idx);

}