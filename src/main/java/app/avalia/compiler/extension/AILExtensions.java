package app.avalia.compiler.extension;

import app.avalia.compiler.lang.AILFunction;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.pool.BaseFunctions;
import app.avalia.compiler.pool.BaseInstructions;
import app.avalia.compiler.provider.AILProvider;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class AILExtensions {

    private static final Map<String, AILExtension> extensions = new HashMap<>();

    public static void loadAll(Set<String> paths) {
        for (String path : paths) {
            try {
                AILExtension extension = load(path);

                Map<String, AILProvider<AILInstruction>> instructions = new HashMap<>();
                Map<String, AILProvider<AILFunction>> functions = new HashMap<>();

                extension.fetchInstructions(instructions);
                extension.fetchFunctions(functions);

                System.out.println("--- " + extension.getName() + " v" + extension.getVersion() + " ---");
                System.out.println("Loaded " + instructions.size() + " instructions");
                System.out.println("Loaded " + functions.size() + " functions");

                for (Map.Entry<String, AILProvider<AILInstruction>> entry : instructions.entrySet())
                    BaseInstructions.push(entry.getKey(), entry.getValue());

                for (Map.Entry<String, AILProvider<AILFunction>> entry : functions.entrySet())
                    BaseFunctions.push(entry.getKey(), entry.getValue());

            } catch (IOException e) {
                throw new RuntimeException(
                        "An error occurred while loading extension from " + path, e);
            }
        }
    }

    private static AILExtension load(String path) throws IOException {
        JarFile jarFile = new JarFile(path);
        Enumeration<JarEntry> entries = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + path + "!/") };
        URLClassLoader classLoader = URLClassLoader.newInstance(urls);

        AILExtension extension = null;
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if(entry.isDirectory() || !entry.getName().endsWith(".class")){
                continue;
            }
            String className = entry.getName().substring(0, entry.getName().length()-6);
            className = className.replace('/', '.');
            try {
                Class<?> clazz = classLoader.loadClass(className);
                if (AILExtension.class.isAssignableFrom(clazz)) {
                    extension = (AILExtension) clazz.newInstance();
                }
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                throw new RuntimeException("Could not load an extension from " + path, e);
            }
        }
        if (extension == null) {
            throw new RuntimeException("Could not load an extension from " + path);
        }
        return extensions.put(extension.getName(), extension);
    }

}
