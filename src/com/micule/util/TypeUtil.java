package com.micule.util;

import org.objectweb.asm.Type;

public class TypeUtil {
	
	public static int getCount(Type[] haystack, Type needle) {
		int count = 0;
		for(Type t : haystack) {
			if(t.equals(needle)) {
				count++;
			}
		}
		return count;
	}
	
	public static int getObjectCount(Type[] types) {
		int count = 0;
		for(Type t : types) {
			if(t.getSort() == Type.ARRAY) {
				if(t.getElementType().getDescriptor().startsWith("L")) {
					count++;
				}
			} else if(t.getDescriptor().startsWith("L")) {
				count++;
			}
		}
		return count;
	}

}
