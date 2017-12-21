package profiling;


import org.objectweb.asm.*;

/**
 * Created by hasselba on 12.12.17.
 */
class ProfilingClassVisitor extends ClassVisitor implements Opcodes
{
    private static final int API_VERSION = ASM5;

    ProfilingClassVisitor(ClassVisitor cv) {
        super(API_VERSION, cv);
    }
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);

        return new MyMethodVisitor(API_VERSION, mv, access, name, desc);
    }


}

