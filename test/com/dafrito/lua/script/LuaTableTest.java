package com.dafrito.lua.script;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import lua.LuaLibrary;

import org.junit.Before;
import org.junit.Test;

public class LuaTableTest {
	private static final String V = "No time";
	LuaLibrary lua = LuaLibrary.INSTANCE;
	private LuaBindings b;
	private LuaScriptContext ctx;

	@Before
	public void setup() {
		ctx = new LuaScriptContext();
		b = ctx.getGlobals();
	}
	
	@Test
	public void getAndSetAValueInATable() throws Exception {
		LuaTable t = new LuaTable(b);
		t.set(1, V);
		assertEquals(V, t.get(1));
		assertEquals(1, t.size());
	}
	
	@Test
	public void testRemovingAnElementMakesTheTableEmpty() throws Exception {
		LuaTable t = new LuaTable(b);
		t.add(V);
		t.remove(0);
		assertTrue(t.isEmpty());
	}

	@Test
	public void iterateOverAnOneElementTable() throws Exception {
		LuaTable t = new LuaTable(b);
		t.add(V);
		t.add(42.0d);
		Iterator<Object> i = t.iterator();
		assertTrue(i.hasNext());
		assertEquals(V, i.next());	
		assertEquals(42.0d, i.next());	
		assertFalse(i.hasNext());
	}

	@Test
	public void removeAnElementUsingAnIterator() throws Exception {
		LuaTable t = new LuaTable(b);
		t.add("A");
		t.add("B");
		t.add("C");
		int counter=0;
		for(Iterator<Object> i = t.iterator(); i.hasNext();) {
			counter++;
			Object v = i.next();
			if(v.equals("B")) {
				i.remove();
			}
		}
		assertEquals(3, counter);
		assertEquals(2, t.size());
	}
	
}
