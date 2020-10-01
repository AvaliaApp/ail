package app.avalia.compiler.bytecode.observer;

import app.avalia.compiler.lang.type.AILType;

import java.util.*;

public class StackObserver {

    private final Queue<AILType> typeQueue = new LinkedList<>();
    private final Map<Integer, AILType> typeContainer = new HashMap<>();

    public void push(AILType type) {
        typeQueue.offer(type);
    }

    public void store(int id, AILType type) {
        typeContainer.put(id, type);
    }

    public AILType byId(int id) {
        return typeContainer.get(id);
    }

    public void pop(int amount) {
        for (int i = 0; i < amount; i++)
            typeQueue.poll();
    }

    public AILType[] last(int amount) {
        AILType[] types = new AILType[amount];
        for (int i = 0; i < amount; i++) {
            types[i] = typeQueue.poll();
        }
        return types;
    }

    public void flush() {
        typeQueue.clear();
        typeContainer.clear();
    }

    public boolean isEmpty() {
        return typeQueue.isEmpty();
    }

    public int getStackSize() {
        return typeQueue.size();
    }

}
