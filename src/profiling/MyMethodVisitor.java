package profiling;


import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * Created by hasselba on 12.12.17.
 */
public class MyMethodVisitor extends AdviceAdapter implements Opcodes {
    private String methodName = null;
    //private Integer variableID = new Integer(0);
    private Integer timeVarID = new Integer(0);


    protected MyMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc) {
        super(api, mv, access, name, desc);



        this.methodName = name;
    }

    @Override
    public void onMethodEnter() {

        //System.out.println(this.methodName);
        //this.variableID = this.newLocal(Type.getObjectType("java/lang/Integer"));
        //mv.visitMethodInsn(Opcodes.INVOKESTATIC, "profiling/MyLogger", "getMethodID", "()Ljava/lang/Integer;", false);
        //mv.visitVarInsn(Opcodes.ASTORE, this.variableID);
        this.timeVarID = this.newLocal(Type.LONG_TYPE);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System" ,"nanoTime", "()J", false);
        mv.visitVarInsn(Opcodes.LSTORE, this.timeVarID);

        super.onMethodEnter();
    }

    @Override
    public void onMethodExit(int opcode) {

        mv.visitLdcInsn(this.methodName);
        //mv.visitVarInsn(Opcodes.ALOAD, this.variableID);
        mv.visitVarInsn(Opcodes.LLOAD, this.timeVarID);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System" ,"nanoTime", "()J", false);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "profiling/MyLogger", "log", "(Ljava/lang/String;JJ)V", false);

        super.onMethodExit(opcode);
    }
    /*
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        super.visitMethodInsn(opcode, owner, name, desc, itf);
    }



    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        super.visitMaxs(maxStack + 8, maxLocals+8);
    }
    */
}
