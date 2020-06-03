package com.micule.analysis.impl;

import com.micule.analysis.Analyser;
import com.micule.hook.HookContainer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

/**
 * @author JKetelaar
 */
public class NodeSub extends Analyser {
    @Override
    public ClassSpec specify(ClassNode cn) {
        if (cn.superName.equals(HookContainer.NODE.getInternalName())){
            if (cn.fieldCount() < 5){
                for (FieldNode fieldNode : cn.fields){
                    if (fieldNode.desc.equals("L" + cn.name + ";")){
                        return new ClassSpec(HookContainer.NODE_SUB, cn);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void evaluate(ClassNode cn) {

    }
}
