package com.micule.analysis.impl;

import com.micule.analysis.Analyser;
import com.micule.hook.HookContainer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class Animable extends Analyser {
    @Override
    public ClassSpec specify(ClassNode cn) {
        if (cn.superName.equals(HookContainer.NODE_SUB.getInternalName()) && cn.methods.size() < 5 && cn.constructors().size() == 1) {
            for (MethodNode methodNode : cn.methods) {
                if (methodNode.desc.startsWith("(IIIIIII")) {
                    return new ClassSpec(HookContainer.ANIMABLE, cn);
                }
            }
        }
        return null;
    }

    @Override
    public void evaluate(ClassNode cn) {

    }
}