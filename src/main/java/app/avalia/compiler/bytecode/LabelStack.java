package app.avalia.compiler.bytecode;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import java.util.HashMap;
import java.util.Map;

public class LabelStack {

    private static final Map<Integer, Label> labels = new HashMap<>();

    public static boolean contains(int id) {
        return labels.containsKey(id);
    }

    public static Label markOrGet(int id) {
        if (labels.containsKey(id))
            return labels.get(id);
        return mark(id);
    }

    public static Label mark(int id) {
        Label label = new Label();
        labels.put(id, label);
        return label;
    }

    public static Label visit(MethodVisitor visitor, int id) {
        Label label = labels.get(id);
        if (label == null) {
            label = mark(id);
        }

        visitor.visitLabel(label);
        return label;
    }

    public static void clear() {
        labels.clear();
    }

}
