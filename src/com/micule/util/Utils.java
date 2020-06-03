package com.micule.util;

import org.objectweb.asm.commons.cfg.tree.node.ReferenceNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.micule.Application;
import com.micule.hook.MemberHookSpecification;

public class Utils {
	
	public static ClassNode classNode(String className) {
		for(ClassNode cn : Application.CLASS_NODE_MAP.values()) {
			if(cn.name.equals(className)) {
				return cn;
			}
		}
		return null;
	}
	
	public static MethodNode method(ReferenceNode ref) {
		return method(ref.owner(), ref.name(), ref.desc());
	}
	
	public static MethodNode method(MemberHookSpecification spec) {
		return method(spec.getOwner(), spec.getName(), spec.getDescriptor());
	}
	
	public static MethodNode method(String owner, String name, String desc) {
		for(ClassNode cn : Application.CLASS_NODE_MAP.values()) {
			if(cn.name.equals(owner)) {
				for(MethodNode mn : cn.methods) {
					if(mn.name.equals(name) && mn.desc.equals(desc)) {
						return mn;
					}
				}
			}
		}
		return null;
	}
	
	public static FieldNode fieldNode(ReferenceNode ref) {
		return fieldNode(ref.owner(), ref.name(), ref.desc());
	}
	
	public static FieldNode fieldNode(MemberHookSpecification spec) {
		return fieldNode(spec.getOwner(), spec.getName(), spec.getDescriptor());
	}
	
	public static FieldNode fieldNode(String owner, String fieldName, String desc) {
		for(ClassNode cn : Application.CLASS_NODE_MAP.values()) {
			if(cn.name.equals(owner)) {
				for(FieldNode fn : cn.fields) {
					if(fn.name.equals(fieldName) && fn.desc.equals(desc)) {
						return fn;
					}
				}
			}
		}
		return null;
	}

}
