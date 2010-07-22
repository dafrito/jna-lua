package com.dafrito.lua;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

import com.dafrito.lua.script.LuaBindings;

public class LuaStackUtil {
	private LuaStackUtil() {
	}

	private static final LuaLibrary lua = LuaLibrary.INSTANCE;

	public static final LuaStackUtil INSTANCE = new LuaStackUtil();

	public boolean isEmpty(lua_State s) {
		return lua.lua_gettop(s) == 0;
	}

	public int size(lua_State s) {
		return lua.lua_gettop(s);
	}

	public Object get(LuaBindings b, int idx) {
		return b.fromLua(idx);
	}

	public List<Object> asList(LuaBindings b) {
		List<Object> stack = new ArrayList<Object>();
		for (int i = 0; i < size(b.getState()); i++) {
			stack.add(b.get(i + 1));
		}
		return stack;
	}

	public void print(LuaBindings b) {
		print(b, System.out);
	}

	public void print(LuaBindings b, OutputStream os) {
		if (!(os instanceof PrintStream)) {
			os = new PrintStream(os);
		}
		PrintStream ps = (PrintStream) os;
		List<Object> stack = asList(b);
		ps.println("Stack (size:" + stack.size() + ")");
		for (int i = 0; i < stack.size(); i++) {
			ps.println("[" + (i + 1) + "] " + stack.get(i));
		}
	}
}
