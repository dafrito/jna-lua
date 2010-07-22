package com.dafrito.lua.script;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import lua.LuaLibrary;

import org.junit.Before;
import org.junit.Test;

public class LuaListTest {
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
		LuaList t = new LuaList(b);
		t.add(V);
		assertEquals(V, t.get(0));
		assertEquals(1, t.size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testIAEonNullSet() throws Exception {
		LuaList t = new LuaList(b);
		t.add(null);
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void testIOOBEonEmptyListGet() throws Exception {
		LuaList t = new LuaList(b);
		t.get(0);
	}
	
	@Test
	public void testRemovingAnElementMakesTheTableEmpty() throws Exception {
		LuaList t = new LuaList(b);
		t.add(V);
		assertEquals(1 ,t.size());
		t.remove(0);
		assertTrue(t.isEmpty());
	}

	@Test
	public void iterateOverAnOneElementTable() throws Exception {
		LuaList t = new LuaList(b);
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
		LuaList t = new LuaList(b);
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

	@Test
	public void clearEmptiesTheList() throws Exception {
		LuaList t = new LuaList(b);
		t.add("A");
		t.add("B");
		t.add("C");
		t.clear();
		assertTrue(t.isEmpty());
	}
}
