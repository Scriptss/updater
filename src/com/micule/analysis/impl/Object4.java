package com.micule.analysis.impl;

import com.micule.analysis.Analyser;
import com.micule.hook.HookContainer;
import org.objectweb.asm.tree.ClassNode;

import java.lang.reflect.Modifier;

public class Object4 extends Analyser {
	@Override
	public ClassSpec specify(ClassNode cn) {
		if (!Modifier.isAbstract(cn.access) && cn.fieldCount() <= 12 && cn.fieldCount("L" + HookContainer.ANIMABLE.getInternalName() + ";") == 3) {
			return new ClassSpec(HookContainer.OBJECT4, cn);
		}
		return null;
	}

	@Override
	public void evaluate(ClassNode cn) {

	}
}