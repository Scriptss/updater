package org.objectweb.asm.commons.cfg.tree.node;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.cfg.tree.NodeTree;
import org.objectweb.asm.tree.*;

/**
 * @author Tyler Sedlar
 */
public class FieldMemberNode extends ReferenceNode {

	public FieldMemberNode(NodeTree tree, AbstractInsnNode insn, int collapsed, int producing) {
		super(tree, insn, collapsed, producing);
	}

    public FieldInsnNode fin() {
        return (FieldInsnNode) insn();
    }

	public boolean getting() {
		return opcode() == GETFIELD || opcode() == GETSTATIC;
	}

	public boolean putting() {
		return opcode() == PUTFIELD || opcode() == PUTSTATIC;
	}

    public boolean isType(Class type) {
        return desc().equals(Type.getDescriptor(type));
    }

	public String type() {
		return desc().replace("[", "").replace("L", "").replace(";", "");
	}
}
