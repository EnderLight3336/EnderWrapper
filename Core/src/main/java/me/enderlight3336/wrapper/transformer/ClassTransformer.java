package me.enderlight3336.wrapper.transformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public final class ClassTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(ClassLoader loader, String name, Class<?> clazz, ProtectionDomain domain, byte[] bytes) throws IllegalClassFormatException {
        return bytes;
    }
}
