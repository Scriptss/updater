package org.objectweb.asm.dogbytes.expression;

import org.objectweb.asm.dogbytes.Tree;
import org.objectweb.asm.tree.JumpInsnNode;

/**
 * Project: AST
 * Created by Dogerina.
 * Copyright under GPL license by Dogerina.
 */
public class BranchExpression extends Expression<JumpInsnNode> {

    private TargetExpression target;

    public BranchExpression(Tree tree, JumpInsnNode node) {
        super(tree, node);
    }

    public TargetExpression getTarget() {
        return target;
    }

    public void setTarget(TargetExpression target) {
        this.target = target;
    }
}
