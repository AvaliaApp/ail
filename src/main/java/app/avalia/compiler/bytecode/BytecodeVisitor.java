package app.avalia.compiler.bytecode;

import app.avalia.compiler.bytecode.observer.LabelObserver;
import app.avalia.compiler.bytecode.observer.StackObserver;
import app.avalia.compiler.lang.type.AILType;
import app.avalia.compiler.pool.BasePoolProvider;
import app.avalia.compiler.pool.info.CommandPoolInfo;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;

/**
 * Bytecode manipulation class
 */
public class BytecodeVisitor {

    private final ClassWriter CLASS_WRITER = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
    private final StackObserver STACK_OBSERVER = new StackObserver();
    private final LabelObserver LABEL_OBSERVER = new LabelObserver();

    private MethodVisitor currentMethodVisitor;

    /**
     * @return {@link ClassWriter}
     */
    public ClassWriter get() {
        return CLASS_WRITER;
    }

    /**
     * Gets the current method visitor
     * @return {@link MethodVisitor}
     */
    public MethodVisitor current() {
        return currentMethodVisitor;
    }

    /**
     * Gets the stack observer
     * @return {@link StackObserver}
     */
    public StackObserver stack() {
        return STACK_OBSERVER;
    }

    /**
     * Gets the label observer
     * @return {@link LabelObserver}
     */
    public LabelObserver label() {
        return LABEL_OBSERVER;
    }

    /**
     * Visits a const type push instruction
     *
     * Short values are pushed through "sipush" bytecode instruction
     *
     * Byte values are pushed through "bipush" bytecode instruction
     *
     * Boolean values are pushed as an integer value of 1 or 0
     *
     * Char values are pushed as "bipush" bytecode instruction
     *
     * Int values are pushed using "iconst_n", "sipush", "bipush" or "ldc" bytecode instructions
     * depending on the size of the value.
     *
     * Long, double, float are pushed through their corresponding "const_n" instructions or "ldc"/"ldc2_w"
     *
     * Text and object is pushed through "ldc"
     *
     * @param type Type of the const value
     * @param value Pushed value
     */
    public void visitPushInsn(AILType type, Object value) {
        if (value == null) {
            current().visitInsn(Opcodes.ACONST_NULL);
            return;
        }

        switch (type) {
            case SHORT:
                short vals = (short)value;
                current().visitIntInsn(Opcodes.SIPUSH, vals);
                break;
            case BYTE:
                byte valb = (byte)value;
                current().visitIntInsn(Opcodes.BIPUSH, valb);
                break;
            case BOOL:
                boolean valbool = (boolean)value;
                if (valbool)
                    current().visitInsn(Opcodes.ICONST_1);
                else current().visitInsn(Opcodes.ICONST_0);
                break;
            case CHAR:
                char valc = (char)value;
                current().visitIntInsn(Opcodes.BIPUSH, valc);
                break;
            case INT: {
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

    /**
     * Visits a bukkit event listener
     * @param name Name of the method
     * @param descriptor Descriptor of the method
     */
    public void visitEvent(String name, String descriptor) {
        currentMethodVisitor = CLASS_WRITER.visitMethod(ACC_PUBLIC,
                name, descriptor, null, null);
        currentMethodVisitor
                .visitAnnotation(MinecraftDescriptors.EVENT_HANDLER_ANNOTATION, true)
                .visitEnd();
    }

    /**
     * Visits a public method declaration
     * @param name Name of the method
     * @param descriptor Descriptor of the method
     */
    public void visitMethod(String name, String descriptor) {
        currentMethodVisitor = get().visitMethod(ACC_PUBLIC,
                name,
                descriptor,
                null,
                null);
    }

    /**
     * Bukkit initializer, registers events and commands
     */
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
