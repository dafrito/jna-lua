A JSR-223 compliant Lua language binding for Java.

This project is a complete and small language binding for Lua 5.1. It uses 
the existing [reference implementation](http://lua-users.org/wiki/LuaBinaries) 
from PUC-Rio, written in C. Access to the native code is provided by 
[JNA](https://jna.dev.java.net/), so no JNI code is required. 

This implementation provides two-way integration between Java and Lua. It also 
strives to be a fully compliant implementation of the
[http://download.oracle.com/docs/cd/E17409_01/javase/6/docs/technotes/guides/scripting/index.html](JSR-223 specification).

It does not provide custom APIs, libraries, or a Lua compiler. It also
does not use any custom C code; native access is exclusively provided through
JNA.

The C API can be found in lua.jar. This jar was created by
[JNAerator](http://code.google.com/p/jnaerator/), and it
contains the interfaces to the functions contained in lua.h, lauxlib.h, and
lualib.h. The function names have not been modified, so they're prefixed with
lua_* or luaL_. While this makes the method names uglier, it also keeps the
learning curve as small as possible.

This is not a Java implementation of Lua. It depends entirely on a existing Lua
C libraries being available. 

### Alternatives

* [http://lua-users.org/wiki/BindingCodeToLua] -- a list of bindings
* [JNLua](http://www.naef.com/jnlua/) -- similar to this project 
* [LuaJava](http://www.keplerproject.org/luajava/) -- oldest and arguably the most popular
* [kahlua](http://code.google.com/p/kahlua/) -- Lua re-implemented in Java

### See also

* [JSR 223 explanation](http://java.sun.com/developer/technicalArticles/J2SE/Desktop/scripting/)
