package enderwrapper.internal.instrument;

import me.enderlight3336.wrapper.security.NamedModuleSP;
import me.enderlight3336.wrapper.security.SecurityProvider;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class SecureTransformer extends DefaultTransformer{
    @Override
    public byte[] transform(ClassLoader loader, String name, Class<?> clazz, ProtectionDomain domain, byte[] bytes)
            throws IllegalClassFormatException {

    }

    @Override
    public byte[] transform(Module module, ClassLoader loader, String name, Class<?> clazz, ProtectionDomain domain, byte[] bytes) throws IllegalClassFormatException {
        SecurityProvider provider = NamedModuleSP.of(module);
        super.transform(module, loader, name, clazz, domain, bytes);
    }
}
