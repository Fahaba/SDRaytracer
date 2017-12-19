package profiling;


import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Created by hasselba on 12.12.17.
 */
public class MyMethodVisitor extends AdviceAdapter implements Opcodes {
    protected MyMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc) {
        super(api, mv, access, name, desc);
    }
    @Override
    public void visitMethodInsn(int opcode, String owner, String name,
                                String desc, boolean itf) {
        //mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
        //mv.visitLdcInsn("CALL " + name);
        //mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream",
                //"println", "(Ljava/lang/String;)V", false);

        mv.visitFieldInsn(GETSTATIC, "profiling/Holder", "print", "(Ljava/io/PrintStream;)V");
        mv.visitLdcInsn("asd123");
        mv.visitMethodInsn(INVOKESTATIC, "profiling/Holder", "print", "(Ljava/lang/String)V", false);
        System.out.println(opcode + " " + owner + " "+ name + " " +desc + " " + " " +itf);

        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }
}
