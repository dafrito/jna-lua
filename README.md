jna-lua is a JSR-223 compliant Lua language binding for Java.

This project is a complete and small language binding for Lua 5.1. It uses 
the existing [reference implementation](http://lua-users.org/wiki/LuaBinaries) 
from PUC-Rio, written in C. Access to the native code is provided by 
[JNA](https://jna.dev.java.net/), so no JNI code is required. 

This implementation provides two-way integration between Java and Lua. It also 
strives to be a fully compliant implementation of the 
[JSR-233 specification](http://download.oracle.com/docs/cd/E17409_01/javase/6/docs/technotes/guides/scripting/index.html).

The C API can be found in lua.jar. This jar was created by
[JNAerator](http://code.google.com/p/jnaerator/), and it
contains the interfaces to the functions contained in lua.h, lauxlib.h, and
lualib.h. The function names have not been modified, so they remain prefixed 
with lua_* or luaL_. While this makes the method names uglier, it also keeps 
the API as simple as possible.

jna-lua is intended to be small and simple, so it excludes some features found
in other implementations. Specifically, jna-lua does not include a separate Lua 
compiler or Java implementation of Lua. It also does not include any native code;
it depends entirely on the existing Lua C libraries being available.

### Alternatives

* [JNLua](http://www.naef.com/jnlua/) -- similar to this project 
* [LuaJava](http://www.keplerproject.org/luajava/) -- oldest and arguably the most popular
* [kahlua](http://code.google.com/p/kahlua/) -- Lua re-implemented in Java

### See also

* [Binding code to Lua](http://lua-users.org/wiki/BindingCodeToLua) -- lists bindings for other languages
* [JSR 223 explanation](http://java.sun.com/developer/technicalArticles/J2SE/Desktop/scripting/)
