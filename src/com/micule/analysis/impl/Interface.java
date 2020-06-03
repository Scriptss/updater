package com.micule.analysis.impl;

import com.micule.Application;
import com.micule.analysis.Analyser;
import com.micule.hook.HookContainer;
import com.micule.util.TypeUtil;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.DepthFirstIterator;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.cfg.BasicBlock;
import org.objectweb.asm.commons.cfg.BlockVisitor;
import org.objectweb.asm.commons.cfg.FlowSortVisitor;
import org.objectweb.asm.commons.cfg.tree.NodeVisitor;
import org.objectweb.asm.commons.cfg.tree.node.AbstractNode;
import org.objectweb.asm.commons.cfg.tree.node.ArrayLoadNode;
import org.objectweb.asm.commons.cfg.tree.node.FieldMemberNode;
import org.objectweb.asm.commons.cfg.tree.node.MethodMemberNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Modifier;

public class Interface extends Analyser {

    @Override
    public ClassSpec specify(ClassNode cn) {
        if (cn.fieldCount() > 61 && cn.fieldCount() < 150) {
            for (FieldNode fieldNode : cn.fields) {
                if (fieldNode.desc.equals("[L" + cn.name + ";")) {
                    return new ClassSpec(HookContainer.INTERFACE, cn);
                }
            }
        }
        return null;
    }

    @Override
    public void evaluate(ClassNode cn) {
        for (MethodNode mn : cn.methods) {
            if (mn.access == 9 && mn.maxLocals > 10) {
                for (BasicBlock block : mn.graph()) {
                    block.accept(new MessageVisistor());
                }
            }
            if (!Modifier.isStatic(mn.access)) {
                int intCount = TypeUtil.getCount(mn.argumentTypes(), Type.INT_TYPE);
                Type returnType = Type.getReturnType(mn.desc);
                if (intCount >= 2 && intCount <= 3 && mn.argumentTypes().length <= 3 && returnType.equals(Type.VOID_TYPE)) {
                    FlowSortVisitor visitor = new FlowSortVisitor(mn);
                    DepthFirstIterator<BasicBlock, DefaultEdge> bfi = new DepthFirstIterator<>(visitor.graph, visitor.blocks.get(0));
                    while (bfi.hasNext()) {
                        BasicBlock block = bfi.next();
                        block.tree().accept(new NodeVisitor() {

                            @Override
                            public void visitArrayLoad(ArrayLoadNode aln) {
                                AbstractNode node = aln.array();
                                if (node.opcode() == Opcodes.GETFIELD) {
                                    FieldMemberNode fmn = (FieldMemberNode) node;
                                    if (HookContainer.INTERFACE.getHook("getItems") == null) {
                                        HookContainer.INTERFACE.addHook(fmn, "getItems");
                                    } else {
                                        if (!fmn.name().equals(HookContainer.INTERFACE.getHook("getItems").getName())) {
                                            HookContainer.INTERFACE.addHook(fmn, "getStackSizes");
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }
        for (ClassNode node : Application.CLASS_NODE_MAP.values()) {
            for (MethodNode mn : node.methods) {
                for (BasicBlock block : mn.graph()) {
                    block.accept(new MessageVisistor());
                }
            }
        }
        if (previousField != null) {
            for (FieldNode fieldNode : cn.fields) {
                if (fieldNode.name.equals(previousField)) {
                    HookContainer.INTERFACE.addHook(fieldNode, "getMessage");
                }
            }
        }
    }

    private String previousField;

    private class MessageVisistor extends BlockVisitor {

        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public void visit(BasicBlock block) {
            block.tree().accept(new NodeVisitor() {
                @Override
                public void visitMethod(MethodMemberNode mmn) {
                    if (mmn.nextField() != null) {
                        if (mmn.nextField().owner().equals(HookContainer.INTERFACE.getInternalName()) && mmn.nextField().type().equals("java/lang/String") && mmn.name().equals("<init>")) {
                            previousField = mmn.nextField().name();
                        }
                    }
                }
            });

        }

    }
}
