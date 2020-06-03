package com.micule.analysis.impl;

import com.micule.analysis.Analyser;
import com.micule.hook.HookContainer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.lang.reflect.Modifier;

public class Deque extends Analyser {
    @Override
    public ClassSpec specify(ClassNode cn) {
        if (!Modifier.isAbstract(cn.access) && cn.fieldCount("L" + HookContainer.NODE.getInternalName() + ";") == 2 && cn.fields.size() == 2){
            return new ClassSpec(HookContainer.DEQUE, cn);
        }
        return null;
    }

    @Override
    public void evaluate(ClassNode cn) {
    	for (FieldNode fn : cn.getFields()) {
    		if (Modifier.isFinal(fn.access)) {
    			HookContainer.DEQUE.addHook(fn, "getHead");
    		} else {
    			HookContainer.DEQUE.addHook(fn, "getCurrent");
    		}
    	}
    }
}
