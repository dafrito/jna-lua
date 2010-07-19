package com.dafrito.lua.script;

import lua.LuaLibrary;

public class LuaTranslator {

	private static final LuaLibrary lua = LuaLibrary.INSTANCE;

	public void toLua(LuaLibrary.lua_State state, Object v) {
		if (v instanceof String) {
			lua.lua_pushstring(state, (String) v);
		} else if(v instanceof Boolean) {
			lua.lua_pushboolean(state, ((Boolean)v).booleanValue() ? 1 : 0);
		} else if(v instanceof Number) {
			lua.lua_pushnumber(state, ((Number) v).doubleValue());
		} else {
			throw new UnsupportedOperationException("Type is not supported for conversion to Lua. Type: "
					+ v);
		}
	}

	public Object fromLua(LuaLibrary.lua_State state, int idx) {
		int type = lua.lua_type(state, idx);
		switch (type) {
		case LuaLibrary.LUA_TSTRING:
			return lua.lua_tolstring(state, idx, null).getString(0);
		case LuaLibrary.LUA_TBOOLEAN:
			return 1 == lua.lua_toboolean(state, idx);
		case LuaLibrary.LUA_TNUMBER:
			return lua.lua_tonumber(state, idx);
		case LuaLibrary.LUA_TNIL:
		case LuaLibrary.LUA_TNONE:
			return null;
		default:
			throw new UnsupportedOperationException("Type is not supported for conversion to Java.");
		}
	}

}
