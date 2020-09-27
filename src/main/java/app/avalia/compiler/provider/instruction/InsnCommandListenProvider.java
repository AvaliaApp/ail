package app.avalia.compiler.provider.instruction;

import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.lang.type.AILType;
import app.avalia.compiler.pool.BasePoolProvider;
import app.avalia.compiler.pool.info.CommandPoolInfo;
import app.avalia.compiler.provider.AILProvider;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

public class InsnCommandListenProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {
    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        visitor.visitMethod("onCommand",
                "(Lorg/bukkit/command/CommandSender;" +
                        "Lorg/bukkit/command/Command;Ljava/lang/String;" +
                        "[Ljava/lang/String;)Z");
        visitor.current().visitCode();
        for (CommandPoolInfo info : BasePoolProvider.getCommandPool().getPool().values()) {
            String target = decapitalize(info.getTarget());
            String name = info.getName();

            visitor.current().visitVarInsn(Opcodes.ALOAD, 2);
            visitor.current().visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "org/bukkit/command/Command",
                    "getName",
                    "()Ljava/lang/String;", false);
            visitor.current().visitLdcInsn(name);
            visitor.current().visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "java/lang/String",
                    "equalsIgnoreCase",
                    "(Ljava/lang/String;)Z", false);
            Label label = new Label();
            visitor.current().visitJumpInsn(Opcodes.IFEQ, label);

            visitor.current().visitVarInsn(Opcodes.ALOAD, 0);
            visitor.current().visitVarInsn(Opcodes.ALOAD, 1);
            visitor.current().visitVarInsn(Opcodes.ALOAD, 2);
            visitor.current().visitVarInsn(Opcodes.ALOAD, 3);
            visitor.current().visitVarInsn(Opcodes.ALOAD, 4);
            visitor.current().visitMethodInsn(
                    Opcodes.INVOKEVIRTUAL,
                    "AvaliaAssembly",
                    target,
                    "(Lorg/bukkit/command/CommandSender;" +
                            "Lorg/bukkit/command/Command;Ljava/lang/String;" +
                            "[Ljava/lang/String;)Z", false);

            visitor.current().visitInsn(AILType.BOOL.toRetInsn());
            visitor.current().visitLabel(label);
        }
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {
        visitor.visitPushInsn(AILType.BOOL, visitor.current(), 1);
        visitor.current().visitInsn(AILType.BOOL.toRetInsn());
        visitor.current().visitMaxs(0, 0);
        visitor.current().visitEnd();
    }

    private String decapitalize(String text) {
        return text.substring(0, 1).toLowerCase() + text.substring(1);
    }
}
