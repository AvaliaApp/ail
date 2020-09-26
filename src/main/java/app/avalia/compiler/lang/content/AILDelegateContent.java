package app.avalia.compiler.lang.content;

import app.avalia.compiler.lang.AILContent;

public class AILDelegateContent implements AILContent {

    private String delegateTarget;

    public String getDelegateTarget() {
        return delegateTarget;
    }

    public void setDelegateTarget(String delegateTarget) {
        this.delegateTarget = delegateTarget;
    }

}
