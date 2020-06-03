package org.objectweb.asm.dogbytes.statement;

import org.objectweb.asm.dogbytes.expression.Expression;

/**
 * Project: AST
 * Created by Dogerina.
 * Copyright under GPL license by Dogerina.
 */
public class ForLoopStatement extends Statement {

    private Expression initializer;
    private Expression condition;
    private Expression change;
}
