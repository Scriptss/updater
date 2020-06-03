package com.micule.analysis.impl;

import com.micule.analysis.Analyser;
import com.micule.hook.HookContainer;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.cfg.BasicBlock;
import org.objectweb.asm.commons.cfg.BlockVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class Character extends Analyser {

    @Override
    public ClassSpec specify(ClassNode cn) {
        if (cn.access == 33 && !cn.superName.equals("java/lang/Object") && cn.fieldCount() > 55 && cn.fieldCount() < 65) {
            return new ClassSpec(HookContainer.CHARACTER, cn);
        }
        return null;
    }

    @Override
    public void evaluate(ClassNode cn) {
        for (MethodNode methodNode : cn.methods) {
            if (methodNode.access == 17 && methodNode.owner.name.equals(HookContainer.CHARACTER.getInternalName()) && methodNode.desc.equals("(ZI)V")) {
                for (BasicBlock block : methodNode.graph()) {
                    block.accept(new AnimationVisitor());
                }
            }
        }
    }

    private class AnimationVisitor extends BlockVisitor {
        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public void visit(BasicBlock block) {
            block.accept(new MethodVisitor() {
                @Override
                public void visitFieldInsn(FieldInsnNode fin) {
                    if (fin.desc.equals("I") && fin.owner.equals(HookContainer.CHARACTER.getInternalName())) {
//                        System.out.println(fin.name.equals("F") + " = " + fin.opcode() + " = " + fin.name + " = " + fin.owner);
                    }
                }
            });
        }
    }
}
