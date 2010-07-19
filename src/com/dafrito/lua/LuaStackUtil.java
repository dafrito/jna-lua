package com.dafrito.lua;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.dafrito.lua.script.LuaTranslator;

import lua.LuaLibrary;
import lua.LuaLibrary.lua_State;

public class LuaStackUtil {
	
	private static final LuaLibrary lua = LuaLibrary.INSTANCE; 
	
	public static final LuaStackUtil INSTANCE=new LuaStackUtil();
	
	private LuaStackUtil() {
	}
	
	public boolean isEmpty(lua_State s) {
		return lua.lua_gettop(s) == 0;
	}
	
	public int size(lua_State s) {
		return lua.lua_gettop(s);
	}
	
	public Object get(lua_State s, LuaTranslator t, int idx) {
		return t.fromLua(s, idx);
	}
	
	public List<Object> asList(lua_State s, LuaTranslator t) {
		List<Object> stack = new ArrayList<Object>();
		for(int i=0; i < size(s); i++) {
			stack.add(get(s, t, i+1));
		}
		return stack;
	}
	
	public void print(lua_State s, LuaTranslator t, OutputStream os) {
		if(!(os instanceof PrintStream)) {
			os = new PrintStream(os);
		}
		PrintStream ps=(PrintStream)os;
		List<Object> stack = asList(s, t);
		ps.println("Stack (size:" + stack.size() + ")");
		for(int i=0; i < stack.size(); i++) {
			ps.println("[" + (i+1) + "] " + stack.get(i));
		}
	}
}
