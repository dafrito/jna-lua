package com.dafrito.lua.script;

import lua.LuaLibrary;

public interface LuaTranslator {

	public void toLua(LuaLibrary.lua_State state, Object v);

	public Object fromLua(LuaLibrary.lua_State state, int idx);

}