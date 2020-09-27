package app.avalia.compiler.provider;

import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.lang.AILClass;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

public class ClassProvider implements AILProvider<AILClass> {

    @Override
    public void parse(AILClass provider) {
    }

    @Override
    public void begin(BytecodeVisitor visitor, AILClass component) {
        visitor.get().visit(49,
                ACC_PUBLIC + ACC_SUPER,
                "AvaliaAssembly",
                null,
                "org/bukkit/plugin/java/JavaPlugin",
                new String[]{"org/bukkit/event/Listener", "org/bukkit/command/CommandExecutor"});

        visitor.get().visitSource("AvaliaAssembly.java", null);

        MethodVisitor mv = visitor.get().visitMethod(ACC_PUBLIC,
                "<init>", "()V", null, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKESPECIAL,
                "org/bukkit/plugin/java/JavaPlugin",
                "<init>",
                "()V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
    }

    @Override
    public void end(BytecodeVisitor visitor, AILClass component) {
        visitor.get().visitEnd();
    }

}
