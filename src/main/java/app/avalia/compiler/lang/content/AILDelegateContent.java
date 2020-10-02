package app.avalia.compiler.lang.content;

import app.avalia.compiler.lang.AILContent;

/**
 * Delegate type
 * Example: "delegate Function"
 */
public class AILDelegateContent implements AILContent {

    private String delegateTarget;

    /**
     * Gets the target of the delegate expression
     * Example: "delegate Function" -> Function
     * @return Target of the delegate expression
     */
    public String getDelegateTarget() {
        return delegateTarget;
    }

    public void setDelegateTarget(String delegateTarget) {
        this.delegateTarget = delegateTarget;
    }

}
