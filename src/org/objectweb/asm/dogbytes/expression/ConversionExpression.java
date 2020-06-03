package org.objectweb.asm.dogbytes.expression;

import org.objectweb.asm.dogbytes.Tree;
import org.objectweb.asm.tree.InsnNode;

/**
 * Project: AST
 * Created by Dogerina.
 * Copyright under GPL license by Dogerina.
 */
public class ConversionExpression extends Expression<InsnNode> {

    public ConversionExpression(Tree tree, InsnNode node) {
        super(tree, node);
    }

    public char getFrom() {
        return super.getWord().toString().charAt(0);
    }

    public char getTo() {
        final String word = super.getWord().toString();
        return word.charAt(word.length() - 1);
    }
}
