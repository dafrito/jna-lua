package com.dafrito.lua.script;

import static org.junit.Assert.*;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleBindings;
import javax.script.SimpleScriptContext;

import org.junit.Before;
import org.junit.Test;

public class LuaScriptEngineTest {

	private LuaScriptEngine e;
	private LuaScriptEngineFactory factory;

	@Before
	public void setUp() {
		this.factory = new LuaScriptEngineFactory();
		this.e = (LuaScriptEngine)this.factory.getScriptEngine();
	}

	@Test
	public void testAnonymousEngineHasNoFactory() throws Exception {
		assertNull("Anonymous script engine has no factory", new LuaScriptEngine().getFactory());
	}

	@Test
	public void testEval() throws Exception {
		assertEquals(1.0, e.eval("return 1"));
		assertEquals(2.0, e.eval("return 1+1"));
		assertEquals(3.0, e.eval("a=2; return a+1"));
	}
	
	@Test
	public void testCreateBindings() throws Exception {
		assertNotNull(e.createBindings());
	}

	@Test
	public void testExplicitBindings() throws Exception {
		Bindings bindings = e.createBindings();
		assertNotNull(bindings);

		bindings.put("t", "42");
		assertEquals("42", bindings.get("t"));
	}

	@Test
	public void testScriptEngineHasGlobalAndEngineBindings() throws Exception {
		assertNotNull(e.getBindings(ScriptContext.GLOBAL_SCOPE));
		assertNotNull(e.getBindings(ScriptContext.ENGINE_SCOPE));
	}
	
	@Test
	public void testEngineHasContext() throws Exception {
		assertNotNull(e.getContext());
		assertSame(e.getContext(), e.getContext());
	}
	
	@Test
	public void testEngineSharesBindingsOfItsDefaultContext() throws Exception {
		assertSame(e.getContext().getBindings(ScriptContext.ENGINE_SCOPE), e.getBindings(ScriptContext.ENGINE_SCOPE));
		assertSame(e.getContext().getBindings(ScriptContext.GLOBAL_SCOPE), e.getBindings(ScriptContext.GLOBAL_SCOPE));
	}

	@Test
	public void testBindingsCreation() throws Exception {
		assertNotNull(e.createBindings());
		assertNotNull(e.getBindings(ScriptContext.ENGINE_SCOPE));
		assertNotNull(e.getBindings(ScriptContext.GLOBAL_SCOPE));
	}
	
	@Test
	public void testEngineBindingsCanBeChanged() throws Exception {
		e.put("A", 1);
		Bindings b = e.createBindings();
		b.put("A", 2);
		e.setBindings(b, ScriptContext.ENGINE_SCOPE);
		assertEquals(2, e.get("A"));
	}
	
	@Test
	public void testEngineBindingsAreDefaultBindings() throws Exception {
		e.put("a", true);
		assertEquals(e.get("a"), e.getBindings(ScriptContext.ENGINE_SCOPE).get("a"));
	}

	@Test
	public void testBindingsChangeScriptResults() throws Exception {
		e.put("a", true);
		assertEquals(true, e.get("a"));
		assertEquals(true, e.eval("return a"));
	}
	
	@Test
	public void testBindingsWithExplicitContext() throws Exception {
		Bindings bindings = e.createBindings();
		ScriptContext context = new SimpleScriptContext();
		
		context.setBindings(bindings, ScriptContext.ENGINE_SCOPE);
		bindings.put("a", true);
		assertEquals(true, e.eval("return a", context));

		bindings.put("a", false);
		assertEquals(false, e.eval("return a", context));
		
		bindings.remove("a");
		assertEquals(null, e.eval("return a", context));
	}
	
	@Test
	public void testAttributes() throws Exception {
		ScriptContext sc = e.getContext();
		sc.setAttribute("a", true, ScriptContext.ENGINE_SCOPE);
		assertEquals(true, sc.getAttribute("a"));
		assertEquals(true, e.eval("return a"));
		
		sc.removeAttribute("a", ScriptContext.ENGINE_SCOPE);
		assertEquals(null, sc.getAttribute("a"));		
	}
	
	@Test
	public void testIOArguments() throws Exception {
		ScriptContext sc = e.getContext();
		assertSame(sc.getReader(), e.eval("return reader"));
		assertSame(sc.getWriter(), e.eval("return writer"));
		assertSame(sc.getErrorWriter(), e.eval("return errorWriter"));
	}
	
	@Test
	public void testPositionalArguments() throws Exception {
		e.put(ScriptEngine.ARGV, new Object[] { 1.0d });
		assertEquals(1.0d, e.eval("return ..."));
	}
	
	@Test
	public void testScriptExceptionFromSyntaxError() throws Exception {
		e.put(ScriptEngine.FILENAME, "myfile");
		try {
			e.eval("a bad script");
			fail("Syntax errors must cause exceptions");
		} catch (ScriptException e) {
			assertEquals("myfile", e.getFileName());
			assertEquals(1, e.getLineNumber());
		}
	}
	
	@Test
	public void testScriptExceptionFromExplicitError() throws Exception {
		e.put(ScriptEngine.FILENAME, "myfile");
		try {
			e.eval("error(\"error\")");
			fail("Explicit errors must cause exceptions");
		} catch (ScriptException e) {
			assertEquals("myfile", e.getFileName());
			assertEquals(1, e.getLineNumber());
		}
	}

	@Test
	public void testCompilable() throws Exception {
		if(!(e instanceof Compilable)) {
			return;
		}
		Compilable compilable = (Compilable) e;
		
		e.put("a", true);
		
		CompiledScript cs = compilable.compile("return a");
		assertEquals(true, cs.eval());
		
		Bindings b = new SimpleBindings();
		b.put("a", false);
		assertEquals(false, cs.eval(b));
	}

	@Test
	public void testInvocableFunction() throws Exception {
		if(!(e instanceof Invocable)) {
			return;
		}
		Invocable invocable = (Invocable) e;
		e.eval("function double(s) return s * 2 end");
		assertEquals(2.0d, invocable.invokeFunction("double", 1.0d));
	}
	
	@Test
	public void testInvocableMethod() throws Exception {
		if(!(e instanceof Invocable)) {
			return;
		}
		Invocable invocable = (Invocable) e;
		e.eval("runnable = { run = function() hasRun = true end }");
		assertEquals(null, e.get("hasRun"));
		invocable.invokeMethod(e.get("runnable"), "run");
		assertEquals(true, e.get("hasRun"));
	}
	
	@Test
	public void testInvocableInterfaceWithGlobalFunctions() throws Exception {
		if(!(e instanceof Invocable)) {
			return;
		}
		Invocable invocable = (Invocable) e;
		e.eval("function run() hasRun = true end }");
		assertEquals(null, e.get("hasRun"));
		Runnable runnable = invocable.getInterface(Runnable.class);
		runnable.run();
		assertEquals(true, e.get("hasRun"));
	}

	@Test
	public void testInvocableInterfaceWithObject() throws Exception {
		if(!(e instanceof Invocable)) {
			return;
		}
		Invocable invocable = (Invocable) e;
		e.eval("runnable = { run = function() hasRun = true end }");
		Object o = e.get("runnable");
		Runnable runnable = invocable.getInterface(o, Runnable.class);
		runnable.run();
		assertEquals(true, e.get("hasRun"));
	}
}
