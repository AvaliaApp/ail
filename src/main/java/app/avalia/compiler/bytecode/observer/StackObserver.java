package app.avalia.compiler.bytecode.observer;

import app.avalia.compiler.lang.type.AILType;

import java.util.*;

public class StackObserver {

    private static final Queue<AILType> typeQueue = new LinkedList<>();
    private static final Map<Integer, AILType> typeContainer = new HashMap<>();

    public static void push(AILType type) {
        typeQueue.offer(type);
        System.out.println("PUSHED " + type.name());
    }

    public static void store(int id, AILType type) {
        System.out.println("STORED " + type.name() + " AT " + id);
        typeContainer.put(id, type);
    }

    public static AILType byId(int id) {
        return typeContainer.get(id);
    }

    public static void pop(int amount) {
        System.out.println("POPPED " + amount);
        for (int i = 0; i < amount; i++)
            typeQueue.poll();
    }

    public static AILType[] last(int amount) {
        AILType[] types = new AILType[amount];
        for (int i = 0; i < amount; i++) {
            types[i] = typeQueue.poll();
        }
        return types;
    }

    public static void flush() {
        typeQueue.clear();
        typeContainer.clear();
    }

    public static boolean isEmpty() {
        return typeQueue.isEmpty();
    }

    public static int getStackSize() {
        return typeQueue.size();
    }

}
