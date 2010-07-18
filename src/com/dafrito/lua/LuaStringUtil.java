package com.dafrito.lua;

public final class LuaStringUtil {
	
	public static final LuaStringUtil INSTANCE=new LuaStringUtil();
	
	private LuaStringUtil() {
	}
	
	public String escape(String s) {
		StringBuilder sb = new StringBuilder(s);
		escape(sb);
		return sb.toString();
	}
		
	public void escape(StringBuilder sb) {
		for (int i = 0; i < sb.length(); i++) {
			String r=null;
			switch (sb.charAt(i)) {
			case '\u0007': r = "\\a"; break;
			case '\b': r = "\\b"; break;
			case '\f': r = "\\f"; break;
			case '\n': r = "\\n"; break;
			case '\r': r = "\\r"; break;
			case '\t': r = "\\t"; break;
			case '\u000b': r = "\\v"; break;
			case '\\': r = "\\\\"; break;
			case '"': r = "\\\""; break;
			}
			if(r != null) {
				sb.replace(i, i+1, r);
				i += r.length();
			}
		}
	}

	public String quote(String s) {
		StringBuilder sb = new StringBuilder(s);
		quote(sb);
		return sb.toString();
	}
	
	public void quote(StringBuilder sb) {
		escape(sb);
		sb.insert(0, '"');
		sb.append('"');
	}
}
