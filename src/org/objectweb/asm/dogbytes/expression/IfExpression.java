package org.objectweb.asm.dogbytes.expression;

import org.objectweb.asm.dogbytes.Tree;
import org.objectweb.asm.tree.JumpInsnNode;

/**
 * Project: AST
 * Created by Dogerina.
 * Copyright under GPL license by Dogerina.
 */
public class IfExpression extends BranchExpression {
    public IfExpression(Tree tree, JumpInsnNode node) {
        super(tree, node);
    }
}
