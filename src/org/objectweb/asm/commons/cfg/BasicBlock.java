package org.objectweb.asm.commons.cfg;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.cfg.query.InsnQuery;
import org.objectweb.asm.commons.cfg.tree.NodeTree;
import org.objectweb.asm.commons.cfg.tree.util.TreeBuilder;
import org.objectweb.asm.commons.util.Assembly;
import org.objectweb.asm.tree.*;

import java.util.*;

/**
 * @author Tyler Sedlar
 */
public class BasicBlock implements Comparable<BasicBlock> {

    public MethodNode owner = null;

    public final Label label;
    public final List<AbstractInsnNode> instructions = new LinkedList<>();
    public final List<BasicBlock> preds = new ArrayList<>();
    public final List<BasicBlock> succs = new ArrayList<>();

    public BasicBlock next, target;

    public Stack<AbstractInsnNode> stack = new Stack<>();

    private NodeTree tree;

    private InsnList insnList = new InsnList();

    private int index = -1;
    
    private boolean visited;

    /**
     * Constructs a block for the given label.
     *
     * @param label The label in which to create a block from.
     */
    public BasicBlock(Label label) {
        this.label = label;
        this.instructions.add(new LabelNode(label));
    }

    /**
     * Constructs a NodeTree for the current block.
     */
    public NodeTree tree() {
        if (tree != null) return tree;
        return (tree = TreeBuilder.build(this));
    }

    /**
     * Sets this block's index.
     *
     * @param index The index to set.
     */
    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    /**
     * Checks if the block is empty.
     *
     * @return <t>true</t> if the block is empty, otherwise <t>false.</t>
     */
    public boolean isEmpty() {
        return preds.isEmpty() && instructions.size() <= 1;
    }

    @Override
    public int compareTo(BasicBlock block) {
        return index > block.index ? 1 : -1;
    }

    @Override
    public String toString() {
        String s = "Block #" + index + "\r\n";
        for (AbstractInsnNode ain : instructions)
            s += " " + Assembly.toString(ain) + "\r\n";
        return s;
    }

    /**
     * Gets the amount of times the given opcodes has been matched
     *
     * @param opcode The opcode to match
     * @return The amount of times the given opcode has been matched.
     */
    public int count(int opcode) {
        int count = 0;
        for (AbstractInsnNode ain : instructions) {
            if (ain.opcode() == opcode)
                count++;
        }
        return count;
    }

    public int count(Class<? extends AbstractInsnNode> clazz) {
        int count = 0;
        for (AbstractInsnNode ain : instructions) {
            if (ain.getClass() == clazz)
                count++;
        }
        return count;
    }

    /**
     * Gets the amount of times the given query has been matched
     *
     * @param query The query to match
     * @return The amount of times the given query has been matched.
     */
    public int count(InsnQuery query) {
        int count = 0;
        for (AbstractInsnNode ain : instructions) {
            if (query.matches(ain))
                count++;
        }
        return count;
    }

    /**
     * Gets the matched instruction at the given index
     *
     * @param opcode The opcode of the instruction to match
     * @param index  The index to match at
     * @return The matched instruction at the given index
     */
    public AbstractInsnNode get(int opcode, int index) {
        int i = 0;
        for (AbstractInsnNode ain : instructions) {
            if (ain.opcode() == opcode) {
                if (i == index)
                    return ain;
                i++;
            }
        }
        return null;
    }


    /**
     * Gets the first matched instruction
     *
     * @param opcode The opcode of the instruction to match
     * @return The first matched instruction
     */
    public AbstractInsnNode get(int opcode) {
        return get(opcode, 0);
    }

    /**
     * Gets the matched instruction at the given index
     *
     * @param query The query to match
     * @param index The index to match at
     * @return The matched instruction at the given index
     */
    public AbstractInsnNode get(InsnQuery query, int index) {
        int i = 0;
        for (AbstractInsnNode ain : instructions) {
            if (query.matches(ain)) {
                if (i == index)
                    return ain;
                i++;
            }
        }
        return null;
    }

    /**
     * Gets the first matched instruction
     *
     * @param query The query to match
     * @return The first matched instruction
     */
    public AbstractInsnNode get(InsnQuery query) {
        return get(query, 0);
    }

    public InsnList stack() {
        if (insnList != null) return insnList;
        InsnList stack = new InsnList();
        for (AbstractInsnNode ain : this.instructions) {
            stack.add(ain);
        }
        return insnList = stack;
    }

    public int weight() {
        return instructions.size() * (preds.size() == 0 ? 1 : preds.size());
    }

    public boolean weight(final int left, final int right) {
        final int weight = weight();
        return weight >= left && weight <= right;
    }

    public AbstractInsnNode last() {
        return instructions.get(instructions.size() - 1);
    }

    public AbstractInsnNode first() {
        return instructions.get(0);
    }

    public int size() {
        return instructions.size();
    }

    public void accept(BlockVisitor blockVisitor) {
        if (blockVisitor.validate()) {
            blockVisitor.visit(this);
        }
    }
    
    public void accept(MethodVisitor mv) {
        if (!visited) {
            visited = true;
            
            InsnList list = new InsnList();
            for(AbstractInsnNode node : this.instructions) {
            	list.add(node);
            }
       
            list.accept(mv);
        }
    }

    public AbstractInsnNode getLast(InsnQuery query) {
        AbstractInsnNode a = null;
        for (AbstractInsnNode ain : instructions) {
            if (query.matches(ain)) {
                a = ain;
            }
        }
        return a;
    }

    public <T extends AbstractInsnNode> List<T> all(Class<T> type) {
        final List<T> ret = new ArrayList<>();
        for (final AbstractInsnNode ain : instructions) {
            if (ain.getClass() == type) {
                ret.add((T) ain);
            }
        }
        return ret;
    }

    public int[] countAll(final int... opcodes) {
        int[] counts = new int[opcodes.length];
        for (final AbstractInsnNode ain : instructions) {
            for (int i = 0; i < opcodes.length; i++) {
                if (opcodes[i] == ain.opcode())
                    counts[i]++;
            }
        }
        return counts;
    }

    public final FieldInsnNode[] fields(String desc) {
        final List<FieldInsnNode> fields = new ArrayList<>();
        for (FieldInsnNode fin : fields()) {
            if (fin.desc.equals(desc)) {
                fields.add(fin);
            }
        }
        return fields.toArray(new FieldInsnNode[fields.size()]);
    }

    public final FieldInsnNode[] fieldsIn(String owner, String desc) {
        final List<FieldInsnNode> fields = new ArrayList<>();
        for (FieldInsnNode fin : fields()) {
            if (fin.desc.equals(desc) && fin.owner.equals(owner)) {
                fields.add(fin);
            }
        }
        return fields.toArray(new FieldInsnNode[fields.size()]);
    }

    public final FieldInsnNode[] fields(String owner, String name) {
        final List<FieldInsnNode> fields = new ArrayList<>();
        for (FieldInsnNode fin : fields()) {
            if (fin.name.equals(name) && fin.owner.equals(owner)) {
                fields.add(fin);
            }
        }
        return fields.toArray(new FieldInsnNode[fields.size()]);
    }

    public final FieldInsnNode[] fields() {
        final List<FieldInsnNode> fields = new ArrayList<>();
        for (final AbstractInsnNode ain : instructions) {
            if (ain instanceof FieldInsnNode) {
                fields.add((FieldInsnNode) ain);
            }
        }
        return fields.toArray(new FieldInsnNode[fields.size()]);
    }

}