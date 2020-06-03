package com.micule.analysis.impl;

import com.micule.analysis.Analyser;
import com.micule.hook.HookContainer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.lang.reflect.Modifier;

public class Node extends Analyser {

    public String pred = null;

    @Override
    public ClassSpec specify(ClassNode cn) {
        if (cn.superName.contains("Object") && cn.fieldCount() == 3 && cn.fieldCount(long.class) == 1 && cn.fieldCount(cn.getName(true)) == 2) {
            return new ClassSpec(HookContainer.NODE, cn);
        }
        return null;
    }

    @Override
    public void evaluate(ClassNode cn) {
        for (FieldNode fn : cn.fields) {
            if (!Modifier.isStatic(fn.access)) {
                if (fn.desc.contains(cn.getName(true)) && !fn.name.equals(pred)) {
                    HookContainer.NODE.addHook(fn, "getNext");
                } else if (fn.desc.equals("J")) {
                    HookContainer.NODE.addHook(fn, "getKey");
                }
            }
        }
    }
}
