package org.objectweb.asm.dogbytes;

import org.objectweb.asm.tree.*;

/**
 * Project: AST
 * Created by Dogerina.
 * Copyright under GPL license by Dogerina.
 * Represents a stack word
 */
public enum StackWord implements StackOpcodes {

    /*               O   P   C   O   D   E                        CONSUMING       PRODUCING   TYPE          */
    NOP             (StackOpcodes.NOP,                            0,              1,               Type.INSN),
    ACONST_NULL     (StackOpcodes.ACONST_NULL,                    0,              1,               Type.INSN),
    ICONST_M1       (StackOpcodes.ICONST_M1,                      0,              1,             Type.NUMBER),
    ICONST_0        (StackOpcodes.ICONST_0,                       0,              1,             Type.NUMBER),
    ICONST_1        (StackOpcodes.ICONST_1,                       0,              1,             Type.NUMBER),
    ICONST_2        (StackOpcodes.ICONST_2,                       0,              1,             Type.NUMBER),
    ICONST_3        (StackOpcodes.ICONST_3,                       0,              1,             Type.NUMBER),
    ICONST_4        (StackOpcodes.ICONST_4,                       0,              1,             Type.NUMBER),
    ICONST_5        (StackOpcodes.ICONST_5,                       0,              1,             Type.NUMBER),
    LCONST_0        (StackOpcodes.LCONST_0,                       0,              2,             Type.NUMBER),
    LCONST_1        (StackOpcodes.LCONST_1,                       0,              2,             Type.NUMBER),
    FCONST_0        (StackOpcodes.FCONST_0,                       0,              1,             Type.NUMBER),
    FCONST_1        (StackOpcodes.FCONST_1,                       0,              1,             Type.NUMBER),
    FCONST_2        (StackOpcodes.FCONST_2,                       0,              1,             Type.NUMBER),
    DCONST_0        (StackOpcodes.DCONST_0,                       0,              2,             Type.NUMBER),
    DCONST_1        (StackOpcodes.DCONST_1,                       0,              2,             Type.NUMBER),
    BIPUSH          (StackOpcodes.BIPUSH,                         0,              1,             Type.NUMBER),
    SIPUSH          (StackOpcodes.SIPUSH,                         0,              1,             Type.NUMBER),
    LDC             (StackOpcodes.LDC,                            0,              1,           Type.CONSTANT),
    ILOAD           (StackOpcodes.ILOAD,                          0,              1,               Type.LOAD),
    LLOAD           (StackOpcodes.LLOAD,                          0,              2,               Type.LOAD),
    FLOAD           (StackOpcodes.FLOAD,                          0,              1,               Type.LOAD),
    DLOAD           (StackOpcodes.DLOAD,                          0,              2,               Type.LOAD),
    ALOAD           (StackOpcodes.ALOAD,                          0,              1,               Type.LOAD),
    IALOAD          (StackOpcodes.IALOAD,                         2,              1,         Type.ARRAY_LOAD),
    LALOAD          (StackOpcodes.LALOAD,                         2,              2,         Type.ARRAY_LOAD),
    FALOAD          (StackOpcodes.FALOAD,                         2,              1,         Type.ARRAY_LOAD),
    DALOAD          (StackOpcodes.DALOAD,                         2,              2,         Type.ARRAY_LOAD),
    AALOAD          (StackOpcodes.AALOAD,                         2,              1,         Type.ARRAY_LOAD),
    BALOAD          (StackOpcodes.BALOAD,                         2,              1,         Type.ARRAY_LOAD),
    CALOAD          (StackOpcodes.CALOAD,                         2,              1,         Type.ARRAY_LOAD),
    SALOAD          (StackOpcodes.SALOAD,                         2,              1,         Type.ARRAY_LOAD),
    ISTORE          (StackOpcodes.ISTORE,                         1,              0,              Type.STORE),
    LSTORE          (StackOpcodes.LSTORE,                         2,              0,              Type.STORE),
    FSTORE          (StackOpcodes.FSTORE,                         1,              0,              Type.STORE),
    DSTORE          (StackOpcodes.DSTORE,                         2,              0,              Type.STORE),
    ASTORE          (StackOpcodes.ASTORE,                         1,              0,              Type.STORE),
    IASTORE         (StackOpcodes.IASTORE,                        3,              0,        Type.ARRAY_STORE),
    LASTORE         (StackOpcodes.LASTORE,                        4,              0,        Type.ARRAY_STORE),
    FASTORE         (StackOpcodes.FASTORE,                        3,              0,        Type.ARRAY_STORE),
    DASTORE         (StackOpcodes.DASTORE,                        4,              0,        Type.ARRAY_STORE),
    AASTORE         (StackOpcodes.AASTORE,                        3,              0,        Type.ARRAY_STORE),
    BASTORE         (StackOpcodes.BASTORE,                        3,              0,        Type.ARRAY_STORE),
    CASTORE         (StackOpcodes.CASTORE,                        3,              0,        Type.ARRAY_STORE),
    SASTORE         (StackOpcodes.SASTORE,                        3,              0,        Type.ARRAY_STORE),
    POP             (StackOpcodes.POP,                            1,              0,               Type.INSN),
    POP2            (StackOpcodes.POP2,                           2,              0,               Type.INSN),
    DUP             (StackOpcodes.DUP,                            1,              2,               Type.INSN),
    DUP_X1          (StackOpcodes.DUP_X1,                         2,              3,               Type.INSN),
    DUP_X2          (StackOpcodes.DUP_X2,                         3,              4,               Type.INSN),
    DUP2            (StackOpcodes.DUP2,                           2,              4,               Type.INSN),
    DUP2_X1         (StackOpcodes.DUP2_X1,                        3,              5,               Type.INSN),
    DUP2_X2         (StackOpcodes.DUP_X2,                         4,              6,               Type.INSN),
    SWAP            (StackOpcodes.SWAP,                           2,              2,               Type.INSN),
    IADD            (StackOpcodes.IADD,                           2,              1,         Type.ARITHMETIC),
    LADD            (StackOpcodes.LADD,                           4,              2,         Type.ARITHMETIC),
    FADD            (StackOpcodes.LADD,                           2,              1,         Type.ARITHMETIC),
    DADD            (StackOpcodes.DADD,                           4,              2,         Type.ARITHMETIC),
    ISUB            (StackOpcodes.ISUB,                           2,              1,         Type.ARITHMETIC),
    LSUB            (StackOpcodes.LSUB,                           4,              2,         Type.ARITHMETIC),
    FSUB            (StackOpcodes.FSUB,                           2,              1,         Type.ARITHMETIC),
    DSUB            (StackOpcodes.DSUB,                           4,              2,         Type.ARITHMETIC),
    IMUL            (StackOpcodes.IMUL,                           2,              1,         Type.ARITHMETIC),
    LMUL            (StackOpcodes.LMUL,                           4,              2,         Type.ARITHMETIC),
    FMUL            (StackOpcodes.FMUL,                           2,              1,         Type.ARITHMETIC),
    DMUL            (StackOpcodes.DMUL,                           4,              2,         Type.ARITHMETIC),
    IDIV            (StackOpcodes.IDIV,                           2,              1,         Type.ARITHMETIC),
    LDIV            (StackOpcodes.LDIV,                           4,              2,         Type.ARITHMETIC),
    FDIV            (StackOpcodes.FDIV,                           2,              1,         Type.ARITHMETIC),
    DDIV            (StackOpcodes.DDIV,                           4,              2,         Type.ARITHMETIC),
    IREM            (StackOpcodes.IREM,                           2,              1,         Type.ARITHMETIC),
    LREM            (StackOpcodes.LREM,                           4,              2,         Type.ARITHMETIC),
    FREM            (StackOpcodes.FREM,                           2,              1,         Type.ARITHMETIC),
    DREM            (StackOpcodes.DREM,                           4,              2,         Type.ARITHMETIC),
    INEG            (StackOpcodes.INEG,                           1,              1,         Type.ARITHMETIC),
    LNEG            (StackOpcodes.LNEG,                           2,              2,         Type.ARITHMETIC),
    FNEG            (StackOpcodes.FNEG,                           1,              1,         Type.ARITHMETIC),
    DNEG            (StackOpcodes.DNEG,                           2,              2,         Type.ARITHMETIC),
    ISHL            (StackOpcodes.ISHL,                           2,              1,         Type.ARITHMETIC),
    LSHL            (StackOpcodes.LSHL,                           3,              2,         Type.ARITHMETIC),
    ISHR            (StackOpcodes.ISHR,                           2,              1,         Type.ARITHMETIC),
    LSHR            (StackOpcodes.LSHR,                           3,              2,         Type.ARITHMETIC),
    IUSHR           (StackOpcodes.IUSHR,                          2,              1,         Type.ARITHMETIC),
    LUSHR           (StackOpcodes.LUSHR,                          3,              2,         Type.ARITHMETIC),
    IAND            (StackOpcodes.IAND,                           2,              1,         Type.ARITHMETIC),
    LAND            (StackOpcodes.LAND,                           4,              2,         Type.ARITHMETIC),
    IOR             (StackOpcodes.IOR,                            2,              1,         Type.ARITHMETIC),
    LOR             (StackOpcodes.LOR,                            4,              2,         Type.ARITHMETIC),
    IXOR            (StackOpcodes.IXOR,                           2,              1,         Type.ARITHMETIC),
    LXOR            (StackOpcodes.LXOR,                           4,              2,         Type.ARITHMETIC),
    IINC            (StackOpcodes.IINC,                           0,              0,          Type.INCREMENT),
    I2L             (StackOpcodes.I2L,                            1,              2,         Type.CONVERSION),
    I2F             (StackOpcodes.I2F,                            1,              1,         Type.CONVERSION),
    I2D             (StackOpcodes.I2D,                            1,              2,         Type.CONVERSION),
    L2I             (StackOpcodes.L2I,                            2,              1,         Type.CONVERSION),
    L2F             (StackOpcodes.L2F,                            2,              1,         Type.CONVERSION),
    L2D             (StackOpcodes.L2D,                            2,              2,         Type.CONVERSION),
    F2I             (StackOpcodes.F2I,                            1,              1,         Type.CONVERSION),
    F2L             (StackOpcodes.F2L,                            1,              2,         Type.CONVERSION),
    F2D             (StackOpcodes.F2D,                            1,              2,         Type.CONVERSION),
    D2I             (StackOpcodes.D2I,                            2,              1,         Type.CONVERSION),
    D2L             (StackOpcodes.D2L,                            2,              2,         Type.CONVERSION),
    D2F             (StackOpcodes.D2F,                            2,              1,         Type.CONVERSION),
    I2B             (StackOpcodes.I2B,                            1,              1,         Type.CONVERSION),
    I2C             (StackOpcodes.I2C,                            1,              1,         Type.CONVERSION),
    I2S             (StackOpcodes.I2S,                            1,              1,         Type.CONVERSION),
    LCMP            (StackOpcodes.LCMP,                           4,              1,             Type.BRANCH),
    FCMPL           (StackOpcodes.FCMPL,                          1,              2,             Type.BRANCH),
    FCMPG           (StackOpcodes.FCMPG,                          1,              2,             Type.BRANCH),
    DCMPL           (StackOpcodes.DCMPL,                          4,              1,             Type.BRANCH),
    DCMPG           (StackOpcodes.DCMPG,                          4,              1,             Type.BRANCH),
    IFEQ            (StackOpcodes.IFEQ,                           1,              0,             Type.BRANCH),
    IFNE            (StackOpcodes.IFNE,                           1,              0,             Type.BRANCH),
    IFLT            (StackOpcodes.IFLT,                           1,              0,             Type.BRANCH),
    IFGE            (StackOpcodes.IFGE,                           1,              0,             Type.BRANCH),
    IFGT            (StackOpcodes.IFGT,                           1,              0,             Type.BRANCH),
    IFLE            (StackOpcodes.IFLE,                           1,              0,             Type.BRANCH),
    IF_ICMPEQ       (StackOpcodes.IF_ICMPEQ,                      2,              0,             Type.BRANCH),
    IF_ICMPNE       (StackOpcodes.IF_ICMPNE,                      2,              0,             Type.BRANCH),
    IF_ICMPLT       (StackOpcodes.IF_ICMPLT,                      2,              0,             Type.BRANCH),
    IF_ICMPGE       (StackOpcodes.IF_ICMPGE,                      2,              0,             Type.BRANCH),
    IF_ICMPGT       (StackOpcodes.IF_ICMPGT,                      2,              0,             Type.BRANCH),
    IF_ICMPLE       (StackOpcodes.IF_ICMPLE,                      2,              0,             Type.BRANCH),
    IF_ACMPEQ       (StackOpcodes.IF_ACMPEQ,                      2,              0,             Type.BRANCH),
    IF_ACMPNE       (StackOpcodes.IF_ACMPNE,                      2,              0,             Type.BRANCH),
    GOTO            (StackOpcodes.GOTO,                           0,              0,             Type.BRANCH),
    JSR             (StackOpcodes.JSR,                            1,              0,             Type.BRANCH),
    RET             (StackOpcodes.RET,                            0,              0,              Type.STORE),
    TABLESWITCH     (StackOpcodes.TABLESWITCH,                    1,              0,             Type.SWITCH),
    LOOKUPSWITCH    (StackOpcodes.LOOKUPSWITCH,                   1,              0,             Type.SWITCH),
    IRETURN         (StackOpcodes.IRETURN,                        1,              0,             Type.RETURN),
    LRETURN         (StackOpcodes.LRETURN,                        2,              0,             Type.RETURN),
    FRETURN         (StackOpcodes.FRETURN,                        1,              0,             Type.RETURN),
    DRETURN         (StackOpcodes.DRETURN,                        2,              0,             Type.RETURN),
    ARETURN         (StackOpcodes.ARETURN,                        1,              0,             Type.RETURN),
    RETURN          (StackOpcodes.RETURN,                         0,              0,             Type.RETURN),
    GETSTATIC       (StackOpcodes.GETSTATIC,                      0,              0,        Type.REFERENTIAL),
    PUTSTATIC       (StackOpcodes.PUTSTATIC,          UNPREDICTABLE,              0,        Type.REFERENTIAL),
    GETFIELD        (StackOpcodes.GETFIELD,                       1,  UNPREDICTABLE,        Type.REFERENTIAL),
    PUTFIELD        (StackOpcodes.PUTFIELD,           UNPREDICTABLE,              1,        Type.REFERENTIAL),
    INVOKEVIRTUAL   (StackOpcodes.INVOKEVIRTUAL,      UNPREDICTABLE,  UNPREDICTABLE,        Type.REFERENTIAL),
    INVOKESPECIAL   (StackOpcodes.INVOKESPECIAL,      UNPREDICTABLE,  UNPREDICTABLE,        Type.REFERENTIAL),
    INVOKESTATIC    (StackOpcodes.INVOKESTATIC,       UNPREDICTABLE,  UNPREDICTABLE,        Type.REFERENTIAL),
    INVOKEINTERFACE (StackOpcodes.INVOKEINTERFACE,    UNPREDICTABLE,  UNPREDICTABLE,        Type.REFERENTIAL),
    INVOKEDYNAMIC   (StackOpcodes.INVOKEDYNAMIC,          UNDEFINED,      UNDEFINED,        Type.REFERENTIAL),
    NEW             (StackOpcodes.NEW,                            0,              1,               Type.TYPE),
    NEWARRAY        (StackOpcodes.NEWARRAY,                       1,              1,             Type.NUMBER),
    ANEWARRAY       (StackOpcodes.ANEWARRAY,                      1,              1,               Type.TYPE),
    ARRAYLENGTH     (StackOpcodes.ARRAYLENGTH,                    1,              1,               Type.INSN),
    ATHROW          (StackOpcodes.ATHROW,                         1,              1,               Type.INSN),
    CHECKCAST       (StackOpcodes.CHECKCAST,                      1,              1,               Type.TYPE),
    INSTANCEOF      (StackOpcodes.INSTANCEOF,                     1,              1,               Type.TYPE),
    MONITORENTER    (StackOpcodes.MONITORENTER,                   1,              0,            Type.MONITOR),
    MONITOREXIT     (StackOpcodes.MONITOREXIT,                    1,              0,            Type.MONITOR),
    MULTIANEWARRAY  (StackOpcodes.MULTIANEWARRAY,     UNPREDICTABLE,              1,     Type.MULTIANEWARRAY),
    IFNULL          (StackOpcodes.IFNULL,                         1,              0,             Type.BRANCH),
    IFNONNULL       (StackOpcodes.IFNONNULL,                      1,              0,             Type.BRANCH);

    private final int opcode;
    private final int consuming;
    private final int producing;
    private final Type type;

    private StackWord(final int opcode, final int consuming, final int producing, final Type type) {
        this.opcode = opcode;
        this.consuming = consuming;
        this.producing = producing;
        this.type = type;
    }

    /**
     * @see org.objectweb.asm.Opcodes
     * @param opcode the instruction opcode
     * @return a StackWord for the specified opcode, or null
     */
    public static StackWord get(final int opcode) {
        for (final StackWord stackWord : StackWord.values()) {
            if (stackWord.opcode == opcode) {
                return stackWord;
            }
        }
        return null;
    }

    /**
     * @param ain the instruction
     * @return a StackWord for the specified instruction, or null
     */
    public static StackWord get(final AbstractInsnNode ain) {
        return get(ain.opcode());
    }

    /**
     * @return the opcode of this instruction
     */
    public int getOpcode() {
        return opcode;
    }

    /**
     * @return the consuming count of this instruction
     */
    public int getConsuming() {
        return consuming;
    }

    /**
     * @return true if this instruction consumes
     */
    public boolean consumes() {
        return consuming > 0;
    }

    /**
     * @return the producing count of this instruction
     */
    public int getProducing() {
        return producing;
    }

    /**
     * @return true if this instruction produces
     */
    public boolean produces() {
        return producing > 0;
    }

    /**
     * @return The {@link Type} of this StackWord
     */
    public Type getType() {
        return type;
    }

    /**
     * An enum of word types
     */
    public enum Type {

        /**
         * LDC, BIPUSH, SIPUSH
         */
        NUMBER,
        /**
         * ....A lot
         */
        ARITHMETIC,
        /**
         * ISTORE, LSTORE, FSTORE, DSTORE, ASTORE
         */
        STORE,
        /**
         * ILOAD, LLOAD, FLOAD, DLOAD, ALOAD
         */
        LOAD,
        /**
         * IALOAD, LALOAD, FALOAD, DALOAD, AALOAD, BALOAD, SALOAD, CALOAD
         */
        ARRAY_LOAD,
        /**
         * IASTORE, LASTORE, FASTORE, DASTORE, AASTORE, BASTORE, SASTORE, CASTORE
         */
        ARRAY_STORE,
        /**
         * GOTO,
         */
        BRANCH,
        TYPE,
        /**
         * GETFIELD, GETSTATIC, PUTFIELD, PUTSTATIC,
         * INVOKEVIRTUAL, INVOKESTATIC, INVOKEDYNAMIC, INVOKEINTERFACE, INVOKESPECIAL
         */
        REFERENTIAL,
        /**
         * IINC
         */
        INCREMENT,
        /**
         * LDC, LDC_W, LDC2_W
         */
        CONSTANT,
        /**
         * ....A lot
         */
        INSN,
        CONVERSION,
        SWITCH,
        RETURN,
        MONITOR,
        MULTIANEWARRAY
    }
}
