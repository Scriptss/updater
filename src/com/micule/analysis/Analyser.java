package com.micule.analysis;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.cfg.BasicBlock;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.micule.Application;
import com.micule.hook.HookContainer;
import com.micule.hook.HookSpecification;


public abstract class Analyser {

    public abstract ClassSpec specify(final ClassNode cn);
    public abstract void evaluate(final ClassNode cn);

    public final void execute() {
        Application.CLASS_NODE_MAP.values().stream().filter(cn -> specify(cn) != null).forEach(this::evaluate);
    }

    protected final class ClassSpec implements HookSpecification {

        private final HookContainer container;
        private final String definedName, name;

        public ClassSpec(final HookContainer container, final String name) {
            this.container = container;
            this.definedName = container.getDefinedName();
            this.name = name;
            container.setInternalName(name);
        }

        public ClassSpec(final HookContainer container, final ClassNode node) {
            this(container, node.name);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getDefinedName() {
            return definedName;
        }

        public HookContainer getContainer() {
            return container;
        }
    }


    public final FieldInsnNode load(final MethodNode methodNode, final int index, final int loadOpcode) {
        for (final BasicBlock block : methodNode.graph()) {
            for (final AbstractInsnNode abstractInsnNode : block.instructions) {
                if (abstractInsnNode instanceof VarInsnNode) {
                    final VarInsnNode varInsnNode = (VarInsnNode) abstractInsnNode;
                    if (varInsnNode.var == index && varInsnNode.opcode() == loadOpcode) {
                        AbstractInsnNode node = varInsnNode;
                        if (node != null) {
                            for (int i = 0; i < 7; i++) {
                                if (node == null)
                                    break;
                                if (node.opcode() == Opcodes.PUTFIELD) {
                                    final FieldInsnNode fn = (FieldInsnNode) node;
                                    return fn;
                                }
                                node = node.next();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public final FieldInsnNode load(final MethodNode methodNode, final int index, final int loadOpcode, final String owner) {
        for (final BasicBlock block : methodNode.graph()) {
            for (final AbstractInsnNode abstractInsnNode : block.instructions) {
                if (abstractInsnNode instanceof VarInsnNode) {
                    final VarInsnNode varInsnNode = (VarInsnNode) abstractInsnNode;
                    if (varInsnNode.var == index  && varInsnNode.opcode() == loadOpcode) {
                        AbstractInsnNode node = varInsnNode;
                        if (node != null) {
                            for (int i = 0; i < 7; i++) {
                                if (node == null)
                                    break;
                                if (node.opcode() == Opcodes.PUTFIELD) {
                                    final FieldInsnNode fn = (FieldInsnNode) node;
                                    if (fn.owner.equals(owner))
                                        return fn;
                                }
                                node = node.next();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public final FieldInsnNode load(final BasicBlock block, final int index, final int loadOpcode) {
        for (final AbstractInsnNode abstractInsnNode : block.instructions) {
            if (abstractInsnNode instanceof VarInsnNode) {
                final VarInsnNode varInsnNode = (VarInsnNode) abstractInsnNode;
                if (varInsnNode.var == index && varInsnNode.opcode() == loadOpcode) {
                    AbstractInsnNode node = varInsnNode;
                    if (node != null) {
                        for (int i = 0; i < 7; i++) {
                            if (node == null)
                                break;
                            if (node.opcode() == Opcodes.PUTFIELD) {
                                final FieldInsnNode fn = (FieldInsnNode) node;
                                return fn;
                            }
                            node = node.next();
                        }
                    }
                }
            }
        }
        return null;
    }

    public final FieldInsnNode load(final BasicBlock block, final int index, final int loadOpcode, final String owner) {
        for (final AbstractInsnNode abstractInsnNode : block.instructions) {
            if (abstractInsnNode instanceof VarInsnNode) {
                final VarInsnNode varInsnNode = (VarInsnNode) abstractInsnNode;
                if (varInsnNode.var == index && varInsnNode.opcode() == loadOpcode) {
                    AbstractInsnNode node = varInsnNode;
                    if (node != null) {
                        for (int i = 0; i < 7; i++) {
                            if (node == null)
                                break;
                            if (node.opcode() == Opcodes.PUTFIELD) {
                                final FieldInsnNode fn = (FieldInsnNode) node;
                                if (fn.owner.equals(owner))
                                    return fn;
                            }
                            node = node.next();
                        }
                    }
                }
            }
        }
        return null;
    }

}