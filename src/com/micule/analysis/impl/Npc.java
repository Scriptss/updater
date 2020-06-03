package com.micule.analysis.impl;

import com.micule.Application;
import com.micule.analysis.Analyser;
import com.micule.hook.HookContainer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

public class Npc extends Analyser {

    @Override
    public ClassSpec specify(ClassNode cn) {
        if (cn.superName.equals(HookContainer.CHARACTER.getInternalName()) && cn.fieldCount() < 10){
            return new ClassSpec(HookContainer.NPC, cn);
        }
        return null;
    }

    @Override
    public void evaluate(ClassNode cn) {
        HookContainer.NPC.addHook(cn.getFields()[0], "getDef");

        for(ClassNode classNode : Application.CLASS_NODE_MAP.values()) {
        }

        for (FieldNode fieldNode : cn.fields){
        }
    }
}
