package org.objectweb.asm.dogbytes.expression;

import org.objectweb.asm.dogbytes.StackWord;
import org.objectweb.asm.dogbytes.Tree;
import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Project: AST
 * Created by Dogerina.
 * Copyright under GPL license by Dogerina.
 */
public abstract class Expression<T extends AbstractInsnNode> {

    protected final Tree tree;
    protected final T node;
    protected final List<Expression> children;

    public Expression(final Tree tree, final T node) {
        this.tree = tree;
        this.node = node;
        this.children = new CopyOnWriteArrayList<>();
    }

    public T getInstruction() {
        return node;
    }

    /**
     * @return The {@link org.objectweb.asm.dogbytes.StackWord} of this Expression
     */
    public StackWord getWord() {
        return StackWord.get(node);
    }

    /**
     * @return The opcode of the instruction
     */
    public int getOpcode() {
        return node.opcode();
    }
}
