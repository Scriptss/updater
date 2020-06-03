package com.micule.analysis;

import org.objectweb.asm.commons.cfg.BasicBlock;
import org.objectweb.asm.commons.cfg.tree.NodeVisitor;
import org.objectweb.asm.commons.cfg.tree.node.*;

public abstract class TreeVisitor extends NodeVisitor {
 
    protected final BasicBlock block;
 
    public TreeVisitor(final BasicBlock block) {
        this.block = block;
    }

    public abstract boolean validateBlock(final BasicBlock block);
 
    @Override
    public final boolean validate() {
        return validateBlock(block);
    }
 
    public void visitAny(AbstractNode n) {
        super.visitAny(n);
    }
 
    public void visit(AbstractNode n) {
        super.visit(n);
    }
 
    public void visitCode() {
        super.visitCode();
    }
 
    public void visitEnd() {
        super.visitEnd();
    }
 
    public void visitField(FieldMemberNode fmn) {
        super.visitField(fmn);
    }
 
    public void visitFrame(AbstractNode n) {
        super.visitFrame(n);
    }
 
    public void visitIinc(IincNode in) {
        super.visitIinc(in);
    }
 
    public void visitJump(JumpNode jn) {
        super.visitJump(jn);
    }
 
    public void visitLabel(AbstractNode n) {
        super.visitLabel(n);
    }
 
    public void visitConversion(ConversionNode cn) {
        super.visitConversion(cn);
    }
 
    public void visitConstant(ConstantNode cn) {
        super.visitConstant(cn);
    }
 
    public void visitNumber(NumberNode nn) {
        super.visitNumber(nn);
    }
 
    public void visitOperation(ArithmeticNode an) {
        super.visitOperation(an);
    }
 
    public void visitVariable(VariableNode vn) {
        super.visitVariable(vn);
    }
 
    public void visitLine(AbstractNode n) {
        super.visitLine(n);
    }
 
    public void visitLookupSwitch(AbstractNode n) {
        super.visitLookupSwitch(n);
    }
 
    public void visitMethod(MethodMemberNode mmn) {
        super.visitMethod(mmn);
    }
 
    public void visitMultiANewArray(AbstractNode n) {
        super.visitMultiANewArray(n);
    }
 
    public void visitTableSwitch(AbstractNode n) {
        super.visitTableSwitch(n);
    }
 
    public void visitType(TypeNode tn) {
        super.visitType(tn);
    }
}