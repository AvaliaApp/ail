package app.avalia.compiler.provider.instruction;

import app.avalia.compiler.asm.BytecodeVisitor;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.pool.PoolProvider;
import app.avalia.compiler.pool.data.InvokePoolInfo;
import app.avalia.compiler.provider.AILProvider;
import org.objectweb.asm.Opcodes;

public class InsnNewProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {
    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        int id = component.getId();

        InvokePoolInfo pool = PoolProvider.getInvokePool().get(id);

        visitor.current().visitTypeInsn(Opcodes.NEW, pool.getInstanceSig());
        visitor.current().visitInsn(Opcodes.DUP);
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {

    }
}
