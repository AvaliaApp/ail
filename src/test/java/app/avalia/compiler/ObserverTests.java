package app.avalia.compiler;

import app.avalia.compiler.bytecode.observer.LabelObserver;
import app.avalia.compiler.bytecode.observer.StackObserver;
import app.avalia.compiler.lang.type.AILType;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import static org.junit.jupiter.api.Assertions.*;

public class ObserverTests {

    private static StackObserver stackObserver;
    private static LabelObserver labelObserver;

    @Before
    public void init() {
        stackObserver = new StackObserver();
        labelObserver = new LabelObserver();
    }

    @Test
    public void stackObserverTest() {
        // Verify pushing and polling
        stackObserver.push(AILType.INT);
        stackObserver.push(AILType.TEXT);
        stackObserver.push(AILType.DOUBLE);
        assertArrayEquals(new AILType[] {AILType.INT, AILType.TEXT, AILType.DOUBLE},
                stackObserver.last(3));

        // Verify popping
        stackObserver.push(AILType.DOUBLE);
        stackObserver.push(AILType.TEXT);
        stackObserver.pop(1);
        assertArrayEquals(new AILType[] {AILType.TEXT},
                stackObserver.last(1));

        // Verify stack size
        stackObserver.push(AILType.INT);
        assertEquals(1, stackObserver.getStackSize());
        stackObserver.pop(1);

        // Verify isEmpty
        assertTrue(stackObserver.isEmpty());

        // Verify flush
        stackObserver.push(AILType.INT);
        stackObserver.flush();
        assertTrue(stackObserver.isEmpty());

        // Verify store
        stackObserver.store(1, AILType.INT);
        assertEquals(AILType.INT, stackObserver.byId(1));

        stackObserver.store(2, AILType.TEXT);
        assertEquals(AILType.TEXT, stackObserver.byId(2));

        stackObserver.store(3, AILType.DOUBLE);
        assertEquals(AILType.DOUBLE, stackObserver.byId(3));
    }

    @Test
    public void labelObserverTest() {
        // Verify marking
        labelObserver.mark(1);
        assertTrue(labelObserver.contains(1));

        // Verify markOrGet while existing
        Label label = labelObserver.markOrGet(1);
        assertNotNull(label);

        // Verify flush
        labelObserver.flush();
        assertFalse(labelObserver.contains(1));

        // Verify markOrGet while not existing
        label = labelObserver.markOrGet(1);
        assertNotNull(label);

        MethodVisitor visitor = Mockito.mock(MethodVisitor.class);

        // Verify existing visit
        labelObserver.mark(1);
        assertDoesNotThrow(() -> {
            labelObserver.visit(visitor, 1);
        });

        // Verify non-existing visit
        labelObserver.flush();
        assertDoesNotThrow(() -> {
            labelObserver.visit(visitor, 1);
        });
    }
}
