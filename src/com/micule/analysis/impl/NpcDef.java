package com.micule.analysis.impl;

import com.micule.analysis.Analyser;
import com.micule.hook.HookContainer;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.cfg.BasicBlock;
import org.objectweb.asm.commons.cfg.BlockVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Objects;

public class NpcDef extends Analyser {
	@Override
	public ClassSpec specify(ClassNode cn) {
		if (HookContainer.NPC.getHook("getDef") != null) {
			if (cn.name.equals(HookContainer.NPC.getHook("getDef").getDescriptor().replace("L", "").replace(";", ""))) {
				return new ClassSpec(HookContainer.NPC_DEF, cn);
			}
		}
		return null;
	}

	@Override
	public void evaluate(ClassNode cn) {
		for (FieldNode fn : cn.fields) {
			if (fn.desc.equals("Ljava/lang/String;")) {
				HookContainer.NPC_DEF.addHook(fn, "getNpcName");
			}

			for (MethodNode methodNode : cn.methods) {
				if (methodNode.desc.equals("(I)L" + HookContainer.NPC_DEF.getInternalName() + ";")) {
					System.out.println(methodNode.desc);
					for (BasicBlock block : methodNode.graph()) {
						block.accept(new NpcIdVisitor());
					}
				}
			}
		}
	}

	private class NpcIdVisitor extends BlockVisitor{

		@Override
		public boolean validate() {
			return true;
		}

		@Override
		public void visit(BasicBlock block) {
			block.accept(new MethodVisitor() {
				@Override
				public void visitFieldInsn(FieldInsnNode fin) {
					if (HookContainer.NPC_DEF.getHook("getId") == null && fin.owner.equals(HookContainer.NPC_DEF.getInternalName())) {
						if ((fin.desc.equals("I") || fin.desc.equals("J")) && fin.opcode() == 181 && fin.previous() != null && fin.previous().opcode() == 133) {
							HookContainer.NPC_DEF.addHook(fin, "getId");
							HookContainer.NPC_DEF.getHook("getId").addData("checkcast", "I");
						}
					}
				}
			});
		}
	}

}