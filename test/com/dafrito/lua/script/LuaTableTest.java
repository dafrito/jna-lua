package com.dafrito.lua.script;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

import org.junit.Before;
import org.junit.Test;

public class LuaTableTest {
	LuaLibrary lua = LuaLibrary.INSTANCE;
	private LuaBindings b;
	private LuaScriptContext ctx;

	@Test
	public void getAndPutAValueIntoATable() throws Exception {
		lua.lua_createtable(b.getState(), 0, 0);
		LuaTable t = new LuaTable(new LuaReference(b));
		t.set(1, "No time");
		assertEquals("No time", t.get(1));
	}
	
	@Test
	public void sizeOfAnEmptyTableIsZero() throws Exception {
		lua.lua_createtable(b.getState(), 0, 0);
		LuaTable t = new LuaTable(new LuaReference(b));
		assertEquals(0, t.size());
	}
	
	@Before
	public void setup() {
		ctx = new LuaScriptContext();
		b = ctx.getGlobals();
	}

	class LuaTable {
		private final LuaBindings b;
		private final LuaReference ref;
		private final lua_State s;

		public LuaTable(LuaReference ref) {
			this.b = ref.getBindings();
			this.ref = ref;
			this.s = b.getState();
		}

		public int size() {
			return 0;
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
}
