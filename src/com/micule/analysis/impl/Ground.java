package com.micule.analysis.impl;

import com.micule.analysis.Analyser;
import com.micule.hook.HookContainer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.lang.reflect.Modifier;

/**
 * @author Empathy
 * Ground
 */
public class Ground extends Analyser {
	@Override
	public ClassSpec specify(ClassNode cn) {
		if (!Modifier.isAbstract(cn.access) && cn.fields.size() < 40 && cn.fieldCount("[L" + HookContainer.OBJECT5.getInternalName() + ";") == 1) {
			return new ClassSpec(HookContainer.GROUND, cn);
		}
		return null;
	}

	@Override
	public void evaluate(ClassNode cn) {
		for (FieldNode fn : cn.fields) {
			if (fn.desc.equals("L" + HookContainer.OBJECT1.getInternalName() + ";")) {
				HookContainer.GROUND.addHook(fn, "getWallObject");
			} else if (fn.desc.equals("L" + HookContainer.OBJECT2.getInternalName() + ";")) {
				HookContainer.GROUND.addHook(fn, "getWallDecorations");
			} else if (fn.desc.equals("L" + HookContainer.OBJECT3.getInternalName() + ";")) {
				HookContainer.GROUND.addHook(fn, "getGroundDecoration");
			} else if (fn.desc.equals("L" + HookContainer.OBJECT4.getInternalName() + ";")) {
				HookContainer.GROUND.addHook(fn, "getGroundItem");
			} else if (fn.desc.equals("[L" + HookContainer.OBJECT5.getInternalName() + ";")) {
				HookContainer.GROUND.addHook(fn, "getInteractiveObjects");
			}
		}
	}
}