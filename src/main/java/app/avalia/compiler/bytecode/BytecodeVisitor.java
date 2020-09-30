package app.avalia.compiler.bytecode;

import app.avalia.compiler.lang.type.AILType;
import app.avalia.compiler.pool.BasePoolProvider;
import app.avalia.compiler.pool.info.CommandPoolInfo;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

public class BytecodeVisitor {

    private final ClassWriter CLASS_WRITER = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    private MethodVisitor CURRENT_METHOD_VISITOR;

    public ClassWriter get() {
        return CLASS_WRITER;
    }

    public MethodVisitor current() {
        return CURRENT_METHOD_VISITOR;
    }

    public void visitPushInsn(AILType type, Object value) {
        switch (type) {
            case INT:
            case BYTE:
            case CHAR:
            case SHORT:
            case BOOL: {
                int val = (int)value;
                if (val <= Short.MAX_VALUE && val >= Short.MIN_VALUE) {
                    if (val <= 5 && val >= -1) {
                        current().visitInsn(val + 3);
                        break;
                    }
                    if (val <= Byte.MAX_VALUE && val >= Byte.MIN_VALUE) {
                        current().visitIntInsn(Opcodes.BIPUSH, val);
                    } else {
                        current().visitIntInsn(Opcodes.SIPUSH, val);
                    }
                    break;
                }
                current().visitLdcInsn(val);
                break;
            }
            case LONG:
                long val0 = (long)value;
                if (val0 == 0)
                    current().visitInsn(Opcodes.LCONST_0);
                if (val0 == 1)
                    current().visitInsn(Opcodes.LCONST_1);
                else current().visitLdcInsn(val0);
                break;
            case FLOAT:
                float val1 = (float)value;
                if (val1 == 0)
                    current().visitInsn(Opcodes.FCONST_0);
                if (val1 == 1)
                    current().visitInsn(Opcodes.FCONST_1);
                if (val1 == 2)
                    current().visitInsn(Opcodes.FCONST_2);
                else current().visitLdcInsn(val1);
                break;
            case DOUBLE:
                double val2 = (double)value;
                if (val2 == 0)
                    current().visitInsn(Opcodes.DCONST_0);
                if (val2 == 1)
                    current().visitInsn(Opcodes.DCONST_1);
                else current().visitLdcInsn(val2);
                break;
            case REF:
            case TEXT:
                current().visitLdcInsn(value);
                break;
        }
    }

    public void visitEvent(String name, String descriptor) {
        MethodVisitor mv = CLASS_WRITER.visitMethod(ACC_PUBLIC,
                name, descriptor, null, null);
        mv.visitAnnotation(MinecraftDescriptors.EVENT_HANDLER_ANNOTATION, true)
                .visitEnd();
        CURRENT_METHOD_VISITOR = mv;
    }

    public void visitMethod(String name, String descriptor) {
        MethodVisitor mv = get().visitMethod(ACC_PUBLIC,
                name,
                descriptor,
                null,
                null);
        CURRENT_METHOD_VISITOR = mv;
    }

    public void visitInit() {
        current().visitMethodInsn(Opcodes.INVOKESTATIC,
                "org/bukkit/Bukkit",
                "getPluginManager",
                MinecraftDescriptors.PLUGIN_MANAGER,
                false);
        current().visitVarInsn(Opcodes.ALOAD, 0);
        current().visitVarInsn(Opcodes.ALOAD, 0);
        current().visitMethodInsn(Opcodes.INVOKEINTERFACE,
                "org/bukkit/plugin/PluginManager",
                "registerEvents",
                MinecraftDescriptors.PLUGIN_MANAGER$REGISTER_LISTENERS,
                true);

        for (CommandPoolInfo info : BasePoolProvider.getCommandPool().getPool().values()) {
            current().visitVarInsn(Opcodes.ALOAD, 0);
            current().visitLdcInsn(info.getName());
            current().visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "AvaliaAssembly",
                    "getCommand",
                    "(Ljava/lang/String;)Lorg/bukkit/command/PluginCommand;",
                    false);
            current().visitVarInsn(Opcodes.ALOAD, 0);
            current().visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                    "org/bukkit/command/PluginCommand",
                    "setExecutor",
                    "(Lorg/bukkit/command/CommandExecutor;)V",
                    false);
        }

    }

}
