package app.avalia.compiler.bytecode.observer;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.HashMap;
import java.util.Map;

/**
 * Observes the creation and deletion of labels in bytecode
 * Used mainly for bytecode jumps
 */
public class LabelObserver {

    private final Map<Integer, Label> labels = new HashMap<>();

    public boolean contains(int id) {
        return labels.containsKey(id);
    }

    /**
     * Creates or gets an existing label
     * @param id Id of the label
     * @return {@link Label}
     */
    public Label markOrGet(int id) {
        if (labels.containsKey(id))
            return labels.get(id);
        return mark(id);
    }

    /**
     * Creates a new label
     * @param id Id of the label
     * @return {@link Label}
     */
    public Label mark(int id) {
        Label label = new Label();
        labels.put(id, label);
        return label;
    }

    /**
     * Visits a label in bytecode
     * @param visitor {@link MethodVisitor}
     * @param id Id of the label
     * @return {@link Label}
     */
    public Label visit(MethodVisitor visitor, int id) {
        Label label = labels.get(id);
        if (label == null) {
            label = mark(id);
        }

        visitor.visitLabel(label);
        return label;
    }

    public void flush() {
        labels.clear();
    }

}
