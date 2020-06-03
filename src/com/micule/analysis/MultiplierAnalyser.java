package com.micule.analysis;

import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.micule.util.RegexInsnSearcher;
  
public class MultiplierAnalyser implements Opcodes {
  
    private static final String[] PATTERNS = {
            "(ldc|getstatic|aload) (aload|getfield|getstatic|ldc|invokevirtual) (imul|putstatic|getfield|ldc) (imul)?",
            "(ldc|getstatic|aload) (aload|getfield|getstatic|ldc|invokevirtual) (getfield|ldc) imul putstatic"
    };
  
    public static void findMultipliers(List<org.objectweb.asm.tree.ClassNode> classes2) {
        final Iterator<org.objectweb.asm.tree.ClassNode> classes = classes2.iterator();
        while (classes.hasNext()) {
            org.objectweb.asm.tree.ClassNode cn = classes.next();
            List<MethodNode> methods = (List<MethodNode>) cn.methods;
            for (MethodNode mn : methods) {
                RegexInsnSearcher searcher = new RegexInsnSearcher(mn.instructions);
                for(String pattern : PATTERNS) {
                    List<AbstractInsnNode[]> matches = searcher.search(pattern);
                    for (AbstractInsnNode[] match : matches) {
                        Integer value = null, refHash = null;
                        for (AbstractInsnNode insn : match) {
                            if (insn.opcode() == LDC) {
                                try {
                                    value = (Integer) ((LdcInsnNode) insn).cst;
                                } catch (ClassCastException cce) {
                                    break;
                                }
                            }
                            if (insn.opcode() == GETFIELD || insn.opcode() == GETSTATIC) {
                                FieldInsnNode fieldInsn = ((FieldInsnNode) insn);
                                refHash = Mul.getHash(fieldInsn.owner, fieldInsn.name);
                            }
                            if(insn.opcode() == PUTSTATIC) {
                                FieldInsnNode fieldInsn = ((FieldInsnNode) insn);
                                refHash = Mul.getHash(fieldInsn.owner, fieldInsn.name);
                            }
                        }
                        if (refHash != null && value != null) {
                            Mul.put(refHash, value);
                        }
                    }
                }
            }
        }
    }
}
