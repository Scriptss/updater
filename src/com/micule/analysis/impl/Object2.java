package com.micule.analysis.impl;

import com.micule.analysis.Analyser;
import com.micule.hook.HookContainer;
import org.objectweb.asm.tree.ClassNode;

import java.lang.reflect.Modifier;

public class Object2 extends Analyser {
	@Override
	public ClassSpec specify(ClassNode cn) {
		if (!Modifier.isAbstract(cn.access) && cn.fieldCount("I") >= 6 && cn.fieldCount("I") <= 7 && cn.fieldCount("B") == 1 && cn.fieldCount("L" + HookContainer.ANIMABLE.getInternalName() + ";") == 1) {
			return new ClassSpec(HookContainer.OBJECT2, cn);
		}
		return null;
	}

	@Override
	public void evaluate(ClassNode cn) {

	}
}