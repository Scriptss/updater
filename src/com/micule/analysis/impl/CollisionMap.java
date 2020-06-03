package com.micule.analysis.impl;

import com.micule.Application;
import com.micule.analysis.Analyser;
import com.micule.hook.HookContainer;
import com.micule.util.TypeUtil;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Modifier;

public class CollisionMap extends Analyser {

	@Override
	public ClassSpec specify(ClassNode cn) {
		if(!Modifier.isAbstract(cn.access) && cn.fieldCount("[[I") == 1) {
			for(MethodNode mn : cn.methods) {
				if(Modifier.isStatic(mn.access)) {
					continue;
				}
				Type[] types = Type.getArgumentTypes(mn.desc);
				if(TypeUtil.getCount(types, Type.INT_TYPE) >= 5 && TypeUtil.getCount(types, Type.BOOLEAN_TYPE) >= 1) {
					return new ClassSpec(HookContainer.COLLISION_MAP, cn);
				}
			}
		}
		return null;
	}

	@Override
	public void evaluate(ClassNode cn) {
		for(FieldNode fn : cn.fields) {
			if(fn.isStatic() || !fn.desc.equals("[[I")) {
				continue;
			}
			
			HookContainer.COLLISION_MAP.addHook(fn, "getFlags");
		}
		
		for(ClassNode classNode : Application.CLASS_NODE_MAP.values()) {
			for(FieldNode fn : classNode.fields) {
				if(fn.isStatic() && fn.desc.equals("[L" + cn.name + ";")) {
//					HookContainer.CLIENT.addHook(fn, "collisionMap");
				}
			}
		}
		
	}

}
