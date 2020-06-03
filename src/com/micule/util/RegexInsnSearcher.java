package com.micule.util;

import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
 
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
 

public class RegexInsnSearcher {
 
    private static final Map<Integer, String> OPCODE_NAME_MAP;
    private static final Pattern[] NON_INSTRUCTION_CONST_PATTERNS = new Pattern[]{
            Pattern.compile("acc_.+"), Pattern.compile("t_.+"), Pattern.compile("v1_.+")
    };
 
    private InsnList insns;
    private Map<AbstractInsnNode, Integer> instrIndexMap;
    private int currentIndex = 0;
    private String mappedCode;
 
    public RegexInsnSearcher(final InsnList insns) {
        this.insns = insns;
        reload();
    }
 
    public void reload() {
        StringBuffer buffer = new StringBuffer();
        instrIndexMap = new HashMap<>();
        Iterator<AbstractInsnNode> iterator = insns.iterator();
        while (iterator.hasNext()) {
            AbstractInsnNode insn = iterator.next();
            if (insn.opcode() < 0) {
                continue;
            }
            instrIndexMap.put(insn, buffer.length());
            buffer.append(OPCODE_NAME_MAP.get(insn.opcode())).append(" ");
        }
        mappedCode = buffer.toString();
    }
 
    private AbstractInsnNode getKey(final Integer val) {
        for (Map.Entry<AbstractInsnNode, Integer> entry : instrIndexMap.entrySet()) {
            if (entry.getValue().equals(val)) {
                return entry.getKey();
            }
        }
        return null;
    }
 
    private AbstractInsnNode[] getMatchFromRange(int start, int end) {
        AbstractInsnNode startInsn = getKey(start);
        int realEndIdx = -1;
        for (int x = end - 1; x >= start; --x) {
            if (mappedCode.charAt(x) == ' ') {
                realEndIdx = x + 1;
                break;
            }
        }
        AbstractInsnNode endInsn = getKey(realEndIdx);
        int startInsnIdx = 0;
        if (startInsn != null)
            startInsnIdx = insns.indexOf(startInsn);
        AbstractInsnNode[] match = new AbstractInsnNode[insns.indexOf(endInsn) - startInsnIdx + 1];
        for (int idx = 0; idx < match.length; ++idx) {
            match[idx] = insns.get(startInsnIdx + idx);
        }
        return match;
    }
 
    public int getCurrentIndex() {
        return this.currentIndex;
    }
 
    public List<AbstractInsnNode[]> search(final String pattern, final AbstractInsnNode from) {
        return search(Pattern.compile(pattern.toLowerCase()), from, null);
    }
 
    public List<AbstractInsnNode[]> search(final String pattern, final Constraint constraint) {
        return search(Pattern.compile(pattern.toLowerCase()), insns.getFirst(), constraint);
    }
 
    public List<AbstractInsnNode[]> search(final String pattern) {
        return search(Pattern.compile(pattern.toLowerCase()), insns.getFirst());
    }
 
    public List<AbstractInsnNode[]> search(final Pattern pattern, final AbstractInsnNode from) {
        return search(pattern, from, null);
    }
 
    public List<AbstractInsnNode[]> search(Pattern pattern, Constraint constraint) {
        return search(pattern, insns.getFirst(), constraint);
    }
 
    public List<AbstractInsnNode[]> search(Pattern pattern) {
        return search(pattern, insns.getFirst());
    }
 
    public List<AbstractInsnNode[]> search(Pattern pattern, AbstractInsnNode from, Constraint constraint) {
        Matcher matcher = pattern.matcher(mappedCode);
        Integer ret = instrIndexMap.get(from);
        int startIdx = 0;
        if (ret != null)
            startIdx = ret.intValue();
        List<AbstractInsnNode[]> matches = new LinkedList<AbstractInsnNode[]>();
        while (matcher.find(startIdx)) {
            int start = matcher.start();
            int end = matcher.end();
            AbstractInsnNode[] match = getMatchFromRange(start, end);
            if (constraint == null || constraint.accept(match)) {
                matches.add(match);
            }
            startIdx = end;
        }
        currentIndex = startIdx;
        return matches;
    }
 
    public static interface Constraint {
        public boolean accept(final AbstractInsnNode[] match);
    }
 
    static {
        OPCODE_NAME_MAP = new HashMap<>();
        Class<?> opcodes = Opcodes.class;
        Field[] declaredFields = opcodes.getDeclaredFields();
        for (Field field : declaredFields) {
            int modifiers = field.getModifiers();
            if (isPublic(modifiers) && isStatic(modifiers) && isFinal(modifiers) && field.getType() == Integer.TYPE) {
                try {
                    String name = field.getName().toLowerCase();
                    boolean failed = false;
                    for (final Pattern pattern : NON_INSTRUCTION_CONST_PATTERNS) {
                        Matcher matcher = pattern.matcher(name);
                        if (matcher.find() && matcher.start() == 0) {
                            failed = true;
                            break;
                        }
                    }
                    if (failed) {
                        continue;
                    }
                    int constant = field.getInt(null);
                    OPCODE_NAME_MAP.put(constant, name);
                } catch (IllegalAccessException ex) {
                }
            }
        }
    }
}