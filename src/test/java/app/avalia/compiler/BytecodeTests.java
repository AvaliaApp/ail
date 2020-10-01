package app.avalia.compiler;

import app.avalia.compiler.bytecode.BytecodeDescriptors;
import app.avalia.compiler.bytecode.BytecodeType;
import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.lang.type.AILType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BytecodeTests {

    private static BytecodeVisitor visitor;

    @Before
    public void init() {
        visitor = new BytecodeVisitor();
    }

    @Test
    public void bytecodeTypeTest() {
        assertEquals(BytecodeType.BOOL, BytecodeType.byAILType(AILType.BOOL));
        assertEquals(BytecodeType.CHAR, BytecodeType.byAILType(AILType.CHAR));
        assertEquals(BytecodeType.BYTE, BytecodeType.byAILType(AILType.BYTE));
        assertEquals(BytecodeType.SHORT, BytecodeType.byAILType(AILType.SHORT));
        assertEquals(BytecodeType.INT, BytecodeType.byAILType(AILType.INT));
        assertEquals(BytecodeType.FLOAT, BytecodeType.byAILType(AILType.FLOAT));
        assertEquals(BytecodeType.LONG, BytecodeType.byAILType(AILType.LONG));
        assertEquals(BytecodeType.DOUBLE, BytecodeType.byAILType(AILType.DOUBLE));
        assertEquals(BytecodeType.TEXT, BytecodeType.byAILType(AILType.TEXT));
        assertEquals(BytecodeType.REF, BytecodeType.byAILType(AILType.REF));

        assertEquals(AILType.BOOL, BytecodeType.toAILType(BytecodeType.BOOL));
        assertEquals(AILType.CHAR, BytecodeType.toAILType(BytecodeType.CHAR));
        assertEquals(AILType.BYTE, BytecodeType.toAILType(BytecodeType.BYTE));
        assertEquals(AILType.SHORT, BytecodeType.toAILType(BytecodeType.SHORT));
        assertEquals(AILType.INT, BytecodeType.toAILType(BytecodeType.INT));
        assertEquals(AILType.FLOAT, BytecodeType.toAILType(BytecodeType.FLOAT));
        assertEquals(AILType.LONG, BytecodeType.toAILType(BytecodeType.LONG));
        assertEquals(AILType.DOUBLE, BytecodeType.toAILType(BytecodeType.DOUBLE));
        assertEquals(AILType.TEXT, BytecodeType.toAILType(BytecodeType.TEXT));
        assertEquals(AILType.REF, BytecodeType.toAILType(BytecodeType.REF));

        assertEquals(BytecodeType.BOOL, BytecodeType.byDescriptor("Z"));
        assertEquals(BytecodeType.CHAR, BytecodeType.byDescriptor("C"));
        assertEquals(BytecodeType.BYTE, BytecodeType.byDescriptor("B"));
        assertEquals(BytecodeType.SHORT, BytecodeType.byDescriptor("S"));
        assertEquals(BytecodeType.INT, BytecodeType.byDescriptor("I"));
        assertEquals(BytecodeType.FLOAT, BytecodeType.byDescriptor("F"));
        assertEquals(BytecodeType.LONG, BytecodeType.byDescriptor("J"));
        assertEquals(BytecodeType.DOUBLE, BytecodeType.byDescriptor("D"));
        assertEquals(BytecodeType.VOID, BytecodeType.byDescriptor("V"));
        assertEquals(BytecodeType.TEXT, BytecodeType.byDescriptor("Ljava/lang/String;"));
        assertEquals(BytecodeType.REF, BytecodeType.byDescriptor("Ljava/lang/Object;"));
    }

    @Test
    public void bytecodeVisitorTest() {
        assertNotNull(visitor);
        assertNotNull(visitor.get());
        assertNotNull(visitor.stack());
        assertNotNull(visitor.label());

        assertNull(visitor.current());

        assertDoesNotThrow(() -> visitor.visitMethod("test", "()V"));

        assertNotNull(visitor.current());

        assertDoesNotThrow(() -> visitor.visitInit());
        assertDoesNotThrow(() -> visitor.visitPushInsn(AILType.TEXT, "text"));
    }

    @Test
    public void bytecodeDescriptorsTest() {
        BytecodeDescriptors.BytecodeMethodInfo info;

        info = assertDoesNotThrow(() -> BytecodeDescriptors.parse("(I)V"));
        assertNotNull(info.getArguments());
        assertEquals(1, info.getArguments().size());
        assertEquals(AILType.INT, info.getArguments().get(0));
        assertNull(info.getReturnType());
        assertTrue(info.isVoid());

        info = assertDoesNotThrow(() ->
                BytecodeDescriptors.parse("()I"));
        assertNotNull(info.getArguments());
        assertTrue(info.getArguments().isEmpty());
        assertNotNull(info.getReturnType());
        assertEquals(AILType.INT, info.getReturnType());

        info = assertDoesNotThrow(() ->
                BytecodeDescriptors.parse("(Ljava/lang/Object;)Ljava/lang/Object;"));
        assertNotNull(info.getArguments());
        assertEquals(1, info.getArguments().size());
        assertEquals(AILType.REF, info.getArguments().get(0));
        assertNotNull(info.getReturnType());
        assertEquals(AILType.REF, info.getReturnType());

        info = assertDoesNotThrow(() ->
                BytecodeDescriptors.parse("(IIBLjava/lang/Object;Ljava/lang/Object;)V"));
        assertNotNull(info.getArguments());
        assertEquals(5, info.getArguments().size());
        assertEquals(AILType.INT, info.getArguments().get(0));
        assertEquals(AILType.INT, info.getArguments().get(1));
        assertEquals(AILType.BYTE, info.getArguments().get(2));
        assertEquals(AILType.REF, info.getArguments().get(3));
        assertEquals(AILType.REF, info.getArguments().get(4));
        assertNull(info.getReturnType());
        assertTrue(info.isVoid());

        info = assertDoesNotThrow(() ->
                BytecodeDescriptors.parse("(ILjava/lang/Object;ZZJLjava/lang/Object;)J"));
        assertNotNull(info.getArguments());
        assertEquals(6, info.getArguments().size());
        assertEquals(AILType.INT, info.getArguments().get(0));
        assertEquals(AILType.REF, info.getArguments().get(1));
        assertEquals(AILType.BOOL, info.getArguments().get(2));
        assertEquals(AILType.BOOL, info.getArguments().get(3));
        assertEquals(AILType.LONG, info.getArguments().get(4));
        assertEquals(AILType.REF, info.getArguments().get(5));
        assertNotNull(info.getReturnType());
        assertEquals(AILType.LONG, info.getReturnType());

    }

}
