package org.objectweb.asm.dogbytes;

import org.objectweb.asm.tree.*;

/**
 * Project: AST
 * Created by Dogerina.
 * Copyright under GPL license by Dogerina.
 */
public final class TreeBuilder {

    private TreeBuilder() {}

    public static Tree build(final InsnList instructions) {
        return null;
    }

    public static Tree build(final MethodNode meth) {
        return build(meth.instructions);
    }
}
