# EnderWrapper

**The project is still in early development progress.**

Our goal is to provide some APIs to use such as redefining class, transforming class, modifying module, etc, which is under user's security config and target SecurityProvider.

## Features
### Easy to use
You should only add this text to your java arguments and then start, it will work!
```
-javaagent %ENDERWRPAAER_PATH%
```
### Visual Console
We make a special console, which print our log instead of printing them to System.out
### Extension System
We make a extension system, which will load our extension when start. Also has a good loading mechanism.
### Security System
We are committed to ensuring the security of the application and the legality of access, and unauthorized access will be denied.
Also we have flexible access control mechanisms that are composed of both user configuration and target application securityProvider.
### Flexible Configuration
You can specify the configuration  in copy text and then replace %CFG_PATH% to your configuration path, and then copy it to your java arguments'
```
-Denderwrapper.config=%CFG_PATH%
```
