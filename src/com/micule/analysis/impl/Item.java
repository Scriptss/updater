package com.micule.analysis.impl;

import com.micule.analysis.Analyser;
import com.micule.hook.HookContainer;

import org.objectweb.asm.commons.cfg.BasicBlock;
import org.objectweb.asm.commons.cfg.BlockVisitor;
import org.objectweb.asm.commons.cfg.tree.NodeVisitor;
import org.objectweb.asm.commons.cfg.tree.node.FieldMemberNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Modifier;

/**
 * @author Empathy
 * Item
 */
public class Item extends Analyser {
	@Override
	public ClassSpec specify(ClassNode cn) {
		if (!Modifier.isAbstract(cn.access) && cn.superName.equals(HookContainer.ANIMABLE.getInternalName()) && cn.fields.size() < 6
				&& cn.methods.size() <= 2) {
			return new ClassSpec(HookContainer.ITEM, cn);
		}
		return null;
	}

	@Override
	public void evaluate(ClassNode cn) {
		for (MethodNode mn : cn.methods) {
			if (mn.desc.startsWith("()L")) {
				ItemIdVisitor itemIdVisitor = new ItemIdVisitor();
				for (BasicBlock b : mn.graph()) {
					b.accept(itemIdVisitor);
				}
			}
		}
	}
	
	private class ItemIdVisitor extends BlockVisitor {
		@Override
		public boolean validate() {
			return true;
		}

		@Override
		public void visit(BasicBlock block) {
			block.tree().accept(new NodeVisitor() {
				@Override
				public void visitField(FieldMemberNode fmn) {
					if (fmn.desc().equals("I") && fmn.previous() == null) {
						HookContainer.ITEM.addHook(fmn, "getId");
					}
				}
			});			
		}		
	}
	
}