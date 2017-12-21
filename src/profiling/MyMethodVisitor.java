package profiling;


import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * Created by hasselba on 12.12.17.
 */
public class MyMethodVisitor extends AdviceAdapter implements Opcodes {
    private String methodName = null;
    private Integer variableID = new Integer(0);
    private Integer timeVarID = new Integer(0);

    protected MyMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc) {
        super(api, mv, access, name, desc);

        this.methodName = name;
    }

    @Override
    public void onMethodEnter() {
        /*
        mv.visitLdcInsn(this.methodName);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
        mv.visitMethodInsn(INVOKESTATIC, "profiling/Holder", "trackStartTime", "(Ljava/lang/String;J)V", false);
        */

        this.variableID = this.newLocal(Type.getObjectType("java/lang/Integer"));
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "profiling/Holder", "getMethodID", "()Ljava/lang/Integer;", false);
        mv.visitVarInsn(Opcodes.ASTORE, this.variableID);

        System.out.println(this.variableID);
        this.timeVarID = this.newLocal(Type.LONG_TYPE);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System" ,"nanoTime", "()J", false);
        mv.visitVarInsn(Opcodes.LSTORE, this.timeVarID);

        System.out.println(this.timeVarID);
        //mv.visitLdcInsn(this.methodName);
        //mv.visitVarInsn(Opcodes.ALOAD, this.variableID);
        //mv.visitMethodInsn(Opcodes.INVOKESTATIC, "profiling/Holder", "print1", "(Ljava/lang/String;Ljava/lang/Integer;)V", false);

        super.onMethodEnter();
    }

    @Override
    public void onMethodExit(int opcode) {
        /*
        mv.visitLdcInsn(this.methodName);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "nanoTime", "()J", false);
        mv.visitMethodInsn(INVOKESTATIC, "profiling/Holder", "calculateTimes", "(Ljava/lang/String;J)V", false);
        */

        mv.visitLdcInsn(this.methodName);
        mv.visitVarInsn(Opcodes.ALOAD, this.variableID);
        /*
        mv.visitInsn(Opcodes.DUP2_X1);
        mv.visitInsn(Opcodes.POP2);
        mv.visitInsn(Opcodes.DUP2_X2);
        */
        mv.visitVarInsn(Opcodes.LLOAD, this.timeVarID);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System" ,"nanoTime", "()J", false);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "profiling/Holder", "print1", "(Ljava/lang/String;Ljava/lang/Integer;JJ)V", false);

        super.onMethodExit(opcode);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        //mv.visitFieldInsn(GETSTATIC, "java/lang/System", "err", "Ljava/io/PrintStream;");
        //mv.visitLdcInsn("CALL " + name);
        //mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);

        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }



    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack + 8, maxLocals+8);
    }
}
