package org.objectweb.asm.dogbytes.expression;

import org.objectweb.asm.dogbytes.Tree;
import org.objectweb.asm.tree.LabelNode;

import java.util.*;

/**
 * Project: AST
 * Created by Dogerina.
 * Copyright under GPL license by Dogerina.
 */
public class TargetExpression extends Expression<LabelNode> {

    private final List<BranchExpression> targeters;

    public TargetExpression(Tree tree, LabelNode node) {
        super(tree, node);
        this.targeters = new ArrayList<>();
    }

    public List<BranchExpression> getTargeters() {
        return targeters;
    }
}
