package org.objectweb.asm.dogbytes;

/**
 * Project: AST
 * Created by Dogerina.
 * Copyright under GPL license by Dogerina.
 * Represents a primitive type
 */
public enum Primitive {

    /*         D   T              W */
    BOOLEAN  ("Z", boolean.class, 1),
    BYTE     ("B", byte.class,    1),
    CHAR     ("C", char.class,    1),
    SHORT    ("S", short.class,   1),
    INT      ("I", int.class,     1),
    FLOAT    ("F", float.class,   1),
    LONG     ("J", long.class,    2),
    DOUBLE   ("D", double.class,  2);

    private final String descriptor;
    private final Class type;
    private final int words;

    private Primitive(final String descriptor, final Class type, final int words) {
        this.descriptor = descriptor;
        this.type = type;
        this.words = words;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public Class getType() {
        return type;
    }

    public int getWords() {
        return words;
    }
}
