package com.dafrito.lua.script;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import com.dafrito.lua.LuaStringUtil;

public class LuaScriptEngineFactory implements ScriptEngineFactory {

	private final List<String> extensions;
	private final List<String> mimeTypes;
	private final Map<String, String> parameters;

	public LuaScriptEngineFactory() {
		this.extensions = Collections.unmodifiableList(Arrays.asList("lua", "Lua"));
		this.mimeTypes = Collections.unmodifiableList(Arrays.asList("application/x-lua", "text/x-lua"));
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(ScriptEngine.ENGINE, this.getEngineName());
		params.put(ScriptEngine.ENGINE_VERSION, this.getEngineVersion());
		params.put(ScriptEngine.LANGUAGE, this.getLanguageName());
		params.put(ScriptEngine.LANGUAGE_VERSION, this.getLanguageVersion());
		params.put(ScriptEngine.NAME, this.extensions.get(0));
		this.parameters = Collections.unmodifiableMap(params);
	}

	@Override
	public Object getParameter(String k) {
		return this.parameters.get(k);
	}

	@Override
	public String getEngineName() {
		return "JSR 223 JNA-Lua Engine"; 
	}

	@Override
	public String getEngineVersion() {
		return "1.0";
	}

	@Override
	public List<String> getExtensions() {
		return this.extensions;
	}

	@Override
	public String getLanguageName() {
		return "Lua";
	}

	@Override
	public String getLanguageVersion() {
		return "5.1";
	}

	@Override
	public String getMethodCallSyntax(String arg0, String arg1, String... arg2) {
		return "process:execute(a, b)";
	}

	@Override
	public List<String> getMimeTypes() {
		return this.mimeTypes;
	}

	@Override
	public List<String> getNames() {
		return this.extensions;
	}

	@Override
	public String getOutputStatement(String v) {
		StringBuilder sb = new StringBuilder(v);
		LuaStringUtil.INSTANCE.quote(sb);
		return "print(" + sb.toString() + ")";
	}

	@Override
	public String getProgram(String... arg0) {
		return "a = 1\nreturn b\n";
	}

	@Override
	public ScriptEngine getScriptEngine() {
		return new LuaScriptEngine(this);
	}
	
}
