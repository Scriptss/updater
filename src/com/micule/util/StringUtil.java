package com.micule.util;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.util.Assembly;
import org.objectweb.asm.tree.AbstractInsnNode;

public class StringUtil {
	
	public static String center(String text, int len){
	    String out = String.format("%"+len+"s%s%"+len+"s", "",text,"");
	    float mid = (out.length()/2);
	    float start = mid - (len/2);
	    float end = start + len; 
	    return out.substring((int)start, (int)end);
	}
	
	public static String getMethodDesc(String methodDesc) {
		StringBuilder sb = new StringBuilder();
		sb.append('(');
		int arg = 0;
		Type[] types = Type.getArgumentTypes(methodDesc);
		if(types.length > 0) {
			for(Type t : types) {
				String className = t.getClassName();
				if(className.contains(".")) {
					className = className.substring(className.lastIndexOf('.')+1);
				}
				sb.append(className).append(" n").append(arg).append(", ");
				arg++;
			}
			sb.setLength(sb.length() - 2);
		}
		
		sb.append(')');
		
		return sb.toString();
	}
	
	public static String getTypeDesc(String desc) {
		Type type = Type.getType(desc);
		String className = type.getClassName();
		if(className == null) {
			return "";
		}
		if(className.contains(".")) {
			className = className.substring(className.lastIndexOf('.')+1);
		}
		return className;
	}
	
	public static void print(AbstractInsnNode insn) {
	    System.out.println(Assembly.toString(insn));

	}

}
