package com.dafrito.lua.script;

import static org.junit.Assert.*;
import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

import org.junit.Test;

import com.dafrito.lua.LuaStackUtil;

public class LuaTableTest {
	LuaLibrary lua = LuaLibrary.INSTANCE;

	@Test
	public void getAndPutAValueIntoATable() throws Exception {
		LuaScriptContext ctx = new LuaScriptContext();
		LuaBindings b = ctx.getGlobals();
		lua.lua_createtable(b.getState(), 0, 0);
		LuaTable t = new LuaTable(new LuaReference(b));
		t.set(1, "No time");
		assertEquals("No time", t.get(1));
	}

	class LuaTable {
		private LuaBindings b;
		private LuaReference ref;
		private lua_State s;

		public LuaTable(LuaReference ref) {
			this.b = ref.getBindings();
			this.ref = ref;
			this.s = b.getState();
		}

		public Object get(Object k) {
			ref.get();
			b.toLua(k);
			lua.lua_gettable(s, -2);
			Object v = b.fromLua(-1);
			lua.lua_settop(s, -2);
			return v;
		}

		public void set(Object k, Object v) {
			ref.get();
			b.toLua(k);
			b.toLua(v);
			lua.lua_settable(s, -3);
			lua.lua_settop(s, -2);
		}
	}

	class LuaReference {
		private final LuaBindings b;
		private int ref;

		public LuaReference(LuaBindings b) {
			this.b = b;
			this.ref = lua.luaL_ref(b.getState(), LuaLibrary.LUA_REGISTRYINDEX);
			check();
		}

		public LuaBindings getBindings() {
			return this.b;
		}

		public void get() {
			check();
			lua.lua_rawgeti(b.getState(), LuaLibrary.LUA_REGISTRYINDEX, this.ref);
		}

		private void check() {
			if (this.isClosed()) {
				throw new RuntimeException();
			}
		}

		public boolean isClosed() {
			return this.ref == LuaLibrary.LUA_REFNIL;
		}

		public void close() {
			if (this.isClosed()) {
				return;
			}
			lua.luaL_unref(b.getState(), LuaLibrary.LUA_REGISTRYINDEX, this.ref);
			this.ref = LuaLibrary.LUA_REFNIL;
		}
	}
}
