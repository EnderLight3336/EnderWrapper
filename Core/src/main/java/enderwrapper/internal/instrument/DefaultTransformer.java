package enderwrapper.internal.instrument;

import enderwrapper.internal.log.LogUtil;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class DefaultTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String name, Class<?> clazz, ProtectionDomain domain, byte[] bytes)
            throws IllegalClassFormatException {
        return ClassFileTransformer.super.transform(loader, name, clazz, domain, bytes);
    }

    @Override
    public byte[] transform(Module module, ClassLoader loader, String name, Class<?> clazz, ProtectionDomain domain, byte[] bytes) throws IllegalClassFormatException {
        if (!module.isNamed())
            LogUtil.MAIN.info("UNNAMED MODULE CALL!!!!!DEBUG");//todo: debug
        byte[] b;
        if (!InstrumentUtil.transMap.isEmpty()) {
            InstrumentUtil.readLock1();
            //todo
            InstrumentUtil.readUnlock1();
        }
        if (!InstrumentUtil.unretransMap.isEmpty()) {
            InstrumentUtil.readLock2();
            //todo
            InstrumentUtil.readUnlock2();
        }

        for (ClassFileTransformer transformer1 : buffer) {
            if (!module.isOpen(pkgName, transformer1.getClass().getModule()))
                continue;
            byte[] ret = transformer1.transform(module, loader, name, clazz, domain, b);
            if (ret != null)
                b = ret;
        }
        return b == bytes ? null : b;
    }
}
