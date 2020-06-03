package com.micule.util;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.objectweb.asm.Handle;
import org.objectweb.asm.commons.cfg.BasicBlock;
import org.objectweb.asm.commons.cfg.graph.CallGraph;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.micule.Application;

public class DummyUtil {

	// fast hack method
	public static boolean isInvoked(MethodNode mn) {
		for (ClassNode node : Application.CLASS_NODE_MAP.values()) {
			for (MethodNode m : node.methods) {
				for (BasicBlock block : m.graph()) {
					for (AbstractInsnNode insnNode : block.instructions) {
						if (insnNode.type() == AbstractInsnNode.METHOD_INSN) {
							MethodInsnNode methNode = (MethodInsnNode) insnNode;
							if (methNode.name.equals(mn.name)
									&& methNode.owner.equals((mn.owner.name))) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	// proper method.. ;d
	public static boolean isInvoked(ClassNode cn, MethodNode mn, CallGraph graph) {
		if (mn.name.contains("<")) {
			return true;
		}

		// check interfaces
		for (String className : cn.interfaces) {
			className = className.replace('/', '.');
			if (Application.CLASS_NODE_MAP.containsKey(className)) {
				for (MethodNode m : Application.CLASS_NODE_MAP.get(className).methods) {
					if (m.name.equals(mn.name) && m.desc.equals(mn.desc)) {
						return true;
					}
				}
			} else {
				try {
					Class<?> clazz = Class.forName(className);
					for (Method m : clazz.getDeclaredMethods()) {
						if (m.getName().equals(mn.name)) {
							// TODO: check type but meh
							return true;
						}
					}
				} catch (ClassNotFoundException ignored) {
				}
			}
		}

		// check supers, TODO: check interfaces from super if super class is
		// abstract
		String superClass = cn.superName;
		while (true) {
			if (superClass == null) {
				break;
			}
			superClass = superClass.replace('/', '.');

			if (Application.CLASS_NODE_MAP.containsKey(superClass)) {
				for (MethodNode m : Application.CLASS_NODE_MAP.get(superClass).methods) {
					if (m.name.equals(mn.name) && m.desc.equals(mn.desc)) {
						return true;
					}
				}
				superClass = Application.CLASS_NODE_MAP.get(superClass).superName;
			} else {
				try {
					Class<?> clazz = Class.forName(superClass);
					for (Method m : clazz.getDeclaredMethods()) {
						if (m.getName().equals(mn.name)) {
							// TODO: check type but meh
							return true;
						}
					}

					if (clazz.getSuperclass() != null) {
						superClass = clazz.getSuperclass().getName();
					} else {
						superClass = null;
					}
				} catch (ClassNotFoundException ignored) {
				}
			}

		}
		
		// reverse usages
		Set<String> strings = getClassUsageAsSuper(cn.name, Application.CLASS_NODE_MAP);
		for(String s : strings) {
			if(graph.containsVertex(new Handle(0, s, mn.name, mn.desc))) {
				return true;
			}
		}
		
		return false;
	}

	public static Set<String> getClassUsageAsSuper(String className,
			Map<String, ClassNode> classes) {
		Set<String> related = new HashSet<>();
		related.add(className);
		for (Entry<String, ClassNode> entry : classes.entrySet()) {
			ClassNode node = entry.getValue();
			if (!related.contains(node.name)
					&& related.contains(node.superName)) {
				related.add(node.name);
			}
		}
		return related;
	}

}
