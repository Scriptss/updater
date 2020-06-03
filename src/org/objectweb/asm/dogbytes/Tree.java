package org.objectweb.asm.dogbytes;

import org.objectweb.asm.dogbytes.expression.Expression;

import java.util.Iterator;

/**
 * Project: AST
 * Created by Dogerina.
 * Copyright under GPL license by Dogerina.
 */
public class Tree implements Iterable<Expression> {

    @Override
    public Iterator<Expression> iterator() {
        return null;
    }

    public static final class Size {

        private final int consuming, producing;

        public Size(final int consuming, final int producing) {
            this.consuming = consuming;
            this.producing = producing;
        }

        public int getConsuming() {
            return consuming;
        }

        public int getProducing() {
            return producing;
        }
    }
}
