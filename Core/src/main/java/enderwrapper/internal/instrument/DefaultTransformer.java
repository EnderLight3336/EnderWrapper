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
        byte[] ret = bytes;
        if (!InstrumentUtil.transMap.isEmpty()) {
            InstrumentUtil.readLock1();
            for (ClassFileTransformer transformer : InstrumentUtil.transMap.keySet()) {
                byte[] b = transformer.transform(module, loader, name, clazz, domain, ret);
                if (b != null)
                    ret = b;
            }
            InstrumentUtil.readUnlock1();
        }
        if (!InstrumentUtil.unretransMap.isEmpty()) {
            InstrumentUtil.readLock2();
            for (ClassFileTransformer transformer : InstrumentUtil.unretransMap.keySet()) {
                byte[] b = transformer.transform(module, loader, name, clazz, domain, bytes);
                if (b != null)
                    return b;
            }
            InstrumentUtil.readUnlock2();
        }
        return ret == bytes ? null : ret;
    }
}
