package org.objectweb.asm.commons.cfg.query;

import org.objectweb.asm.tree.*;

/**
 * @author Tyler Sedlar
 */
public class MemberQuery extends InsnQuery {

    protected String owner;
    protected final String name;
    protected final String desc;
    private boolean endsWith = false;

    public MemberQuery(int opcode, String owner, String name, String desc) {
        super(opcode);
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }

    public MemberQuery(int opcode, String owner, String desc) {
        this(opcode, owner, null, desc);
    }

    public MemberQuery(int opcode, String desc) {
        this(opcode, null, desc);
    }

    public MemberQuery(String desc) {
        this(-1, desc);
    }

    public MemberQuery(int opcode) {
        this(opcode, null, null, null);
    }

    @Override
    public boolean matches(AbstractInsnNode ain) {
        if (!(ain instanceof FieldInsnNode) && !(ain instanceof MethodInsnNode)) return false;
        int opcode = ain.opcode();
        String owner, name, desc;
        if (ain instanceof FieldInsnNode) {
            FieldInsnNode fin = (FieldInsnNode) ain;
            owner = fin.owner;
            name = fin.name;
            desc = fin.desc;
        } else {
            MethodInsnNode min = (MethodInsnNode) ain;
            owner = min.owner;
            name = min.name;
            desc = min.desc;
        }
        if ((this.opcode == -1 || this.opcode == opcode) && (this.owner == null || this.owner.equals(owner))) {
            if (this.name == null || this.name.equals(name)) {
                if (this.desc == null || (endsWith ? this.desc.endsWith(desc) : this.desc.equals(desc))) { //|| desc.matches(this.desc)) {
                    return true;
                }
            }
        }
        return false;
    }

    public MemberQuery owner(final String owner) {
        this.owner = owner;
        return this;
    }

    public MemberQuery endsWith(final boolean endsWith) {
        this.endsWith = endsWith;
        return this;
    }
}
