package com.dafrito.lua.script;

import java.lang.reflect.Array;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

public class PrimitiveLuaTranslator implements LuaTranslator {

	private static final LuaLibrary lua = LuaLibrary.INSTANCE;

	/* (non-Javadoc)
	 * @see com.dafrito.lua.script.Translator#toLua(lua.LuaLibrary.lua_State, java.lang.Object)
	 */
	public void toLua(LuaBindings b, Object v) {
		lua_State state = b.getState();
		if (v == null) {
			lua.lua_pushnil(state);
		} else if (v instanceof String) {
			lua.lua_pushstring(state, (String) v);
		} else if(v instanceof Boolean) {
			lua.lua_pushboolean(state, ((Boolean)v).booleanValue() ? 1 : 0);
		} else if(v instanceof Number) {
			lua.lua_pushnumber(state, ((Number) v).doubleValue());
		} else if(v.getClass().isArray()) {
			lua.lua_createtable(state, 0, 0);
			for (int i = 0; i < Array.getLength(v); i++) {
				lua.lua_pushnumber(state, i + 1);
				this.toLua(b, Array.get(v, i));
				lua.lua_rawset(state, -3);
			}
		} else {
			throw new UnsupportedOperationException("Type is not supported for conversion to Lua. Type: "
					+ v);
		}
	}

	/* (non-Javadoc)
	 * @see com.dafrito.lua.script.Translator#fromLua(lua.LuaLibrary.lua_State, int)
	 */
	public Object fromLua(LuaBindings b, int idx) {
		lua_State state = b.getState();
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
		case LuaLibrary.LUA_TTABLE:
		case LuaLibrary.LUA_TFUNCTION:
		case LuaLibrary.LUA_TUSERDATA:
		case LuaLibrary.LUA_TLIGHTUSERDATA:
		case LuaLibrary.LUA_TTHREAD:
			throw new UnsupportedOperationException("Type is not yet supported for conversion to Java.");
		default:
			throw new IllegalArgumentException("Unexpected type: " + type);
		}
	}

}
