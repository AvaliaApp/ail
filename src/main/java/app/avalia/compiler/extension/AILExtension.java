package app.avalia.compiler.extension;

import app.avalia.compiler.lang.AILFunction;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.provider.AILProvider;

import java.util.Map;

/**
 * Extension interface.
 * Must be implemented in every extension to properly load it to the compiler.
 *
 * Fetch functions are executed during loading
 */
public interface AILExtension {

    String getName();

    String getVersion();

    void fetchInstructions(Map<String, AILProvider<AILInstruction>> instructions);

    void fetchFunctions(Map<String, AILProvider<AILFunction>> functions);

}
