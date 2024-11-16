package me.enderlight3336.wrapper.security;

@FunctionalInterface
public interface Domain {
    boolean isIn(Class<?> clazz);

    public class Clazzes implements Domain {
        public final Class<?>[] clazzes;
        public Clazzes(Class<?>[] classes) {
            this.clazzes = classes;
        }
        @Override
        public boolean isIn(Class<?> clazz) {
            for (Class<?> claz : clazzes)
                if (claz == clazz)
                    return true;
            return false;
        }
    }
    public final class PkgDomain implements Domain {
        public final String pkgName;
        public PkgDomain(String pkgName) {
            this.pkgName = pkgName;
        }
        @Override
        public boolean isIn(Class<?> clazz) {
            return clazz.getPackageName().equals(pkgName);
        }
    }
}
