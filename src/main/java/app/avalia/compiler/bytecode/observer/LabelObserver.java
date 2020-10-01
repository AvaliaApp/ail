package app.avalia.compiler.bytecode.observer;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.HashMap;
import java.util.Map;

public class LabelObserver {

    private final Map<Integer, Label> labels = new HashMap<>();

    public boolean contains(int id) {
        return labels.containsKey(id);
    }

    public Label markOrGet(int id) {
        if (labels.containsKey(id))
            return labels.get(id);
        return mark(id);
    }

    public Label mark(int id) {
        Label label = new Label();
        labels.put(id, label);
        return label;
    }

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
