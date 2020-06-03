package org.objectweb.asm.dogbytes.expression;

import org.objectweb.asm.dogbytes.Tree;
import org.objectweb.asm.tree.LookupSwitchInsnNode;

import java.util.*;

/**
 * Project: AST
 * Created by Dogerina.
 * Copyright under GPL license by Dogerina.
 */
public class LookupSwitchExpression extends Expression<LookupSwitchInsnNode> {

    private final Expression value;
    /* All the cases associated with this instruction */
    private final List<Expression> caseValues;

    public LookupSwitchExpression(Tree tree, LookupSwitchInsnNode node, Expression value) {
        super(tree, node);
        this.value = value;
        this.caseValues = new ArrayList<>();
    }

    public Expression getValue() {
        return value;
    }

    public List<Expression> getCaseValues() {
        return caseValues;
    }
}
