package com.micule.analysis.impl;

import com.micule.analysis.Analyser;
import com.micule.hook.HookContainer;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.lang.reflect.Modifier;

public class Scene extends Analyser {
	@Override
	public ClassSpec specify(ClassNode cn) {
		if (!Modifier.isAbstract(cn.access) && cn.fieldCount("[[[L" + HookContainer.GROUND.getInternalName() + ";") == 1) {
			return new ClassSpec(HookContainer.SCENE, cn);
		}
		return null;
	}

	@Override
	public void evaluate(ClassNode cn) {
		for (FieldNode fn : cn.fields) {
			if (fn.desc.equals("[[[L" + HookContainer.GROUND.getInternalName() + ";")) {
				HookContainer.SCENE.addHook(fn, "getGroundArray");
			}
		}
	}
}