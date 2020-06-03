package com.micule.analysis.impl;

import java.lang.reflect.Modifier;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.cfg.BasicBlock;
import org.objectweb.asm.commons.cfg.BlockVisitor;
import org.objectweb.asm.commons.cfg.tree.NodeVisitor;
import org.objectweb.asm.commons.cfg.tree.node.FieldMemberNode;
import org.objectweb.asm.commons.cfg.tree.node.IincNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.micule.analysis.Analyser;
import com.micule.hook.HookContainer;
import com.micule.hook.MemberHook;
import com.micule.hook.MemberHookSpecification;
import com.micule.util.DummyUtil;
import com.micule.util.TypeUtil;

public class Client extends Analyser {

    @Override
    public ClassSpec specify(ClassNode cn) {
        if (!Modifier.isAbstract(cn.access) && cn.fields.size() > 200) {
            return new ClassSpec(HookContainer.CLIENT, cn);
        }
        return null;
    }

    @Override
    public void evaluate(ClassNode cn) {

        for (MethodNode mn : cn.methods) {
            for (BasicBlock block : mn.graph()) {
                block.accept(new LoopCycleVisitor());
            }
            if (!Modifier.isStatic(mn.access)) {
                if ((mn.desc.startsWith("(IIIIIII") || mn.desc.startsWith("(ZZIIIII")) && mn.desc.endsWith(")Z")) {
                    HookContainer.CLIENT.addHook(mn, "walkTo()");

                    MemberHookSpecification spec = HookContainer.CLIENT.getHook("walkTo()");
                    spec.addType(MemberHook.TYPE_INVOKER);
                    spec.addData("invokemethod", mn.name);
                    spec.addData("inv-methodname", "walkTo");
                    BaseXYVisitor bxy = new BaseXYVisitor();
                    for (BasicBlock b : mn.graph()) {
                    	b.accept(bxy);
                    }
                }
            }
            Type[] types = Type.getArgumentTypes(mn.desc);
            if (TypeUtil.getCount(types, Type.INT_TYPE) == 1) {
                for (BasicBlock block : mn.graph()) {
                    block.accept(new ProcessMenuActionVisitor());
                }
            }

            if (!Modifier.isStatic(mn.access)) {
                if (mn.desc.startsWith("(Ljava/lang/String;ILjava/lang/String;") && mn.desc.endsWith(")V")) {
                    HookContainer.CLIENT.addHook(mn, "messageListener");
                }
            }

            if (mn.desc.equals("(L" + HookContainer.INTERFACE.getInternalName() + ";I)I")
                    || mn.desc.equals("(L" + HookContainer.INTERFACE.getInternalName() + ";I)J")) {

                StatsVisitor statsVisitor = new StatsVisitor();
                for (BasicBlock block : mn.graph()) {
                    block.accept(statsVisitor);
                }
            }

            if (mn.desc.startsWith("(Ljava/lang/String;Ljava/lang/String;Z") && mn.desc.endsWith(")V")) {
                LoggedInVisitor loggedInVisitor = new LoggedInVisitor();
                for (BasicBlock b : mn.graph()) {
                    b.accept(loggedInVisitor);
                }
            }
        }


        for (FieldNode fn : cn.fields) {
            if (fn.desc.equals("[L" + HookContainer.NPC.getInternalName() + ";")) {
                HookContainer.CLIENT.addHook(fn, "getNpcs");
            }

            if (fn.isStatic() && fn.desc.equals("L" + HookContainer.PLAYER.getInternalName() + ";")) {
                HookContainer.CLIENT.addHook(fn, "getMyPlayer");
            }

            if (fn.desc.equals("[[[L" + HookContainer.DEQUE.getInternalName() + ";")) {
                HookContainer.CLIENT.addHook(fn, "getGroundItems");
            }

            if (fn.desc.equals("L" + HookContainer.SCENE.getInternalName() + ";")) {
                HookContainer.CLIENT.addHook(fn, "getScene");
            }
            if (fn.desc.equals("[L" + HookContainer.PLAYER.getInternalName() + ";")) {
                HookContainer.CLIENT.addHook(fn, "getPlayers");
            }

            if (fn.desc.equals("[L" + HookContainer.COLLISION_MAP.getInternalName() + ";")) {
                HookContainer.CLIENT.addHook(fn, "getCollisionMap");
            }
        }
    }

    private class LoopCycleVisitor extends BlockVisitor {

        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public void visit(BasicBlock block) {
            block.tree().accept(new NodeVisitor() {
                @Override
                public void visitField(FieldMemberNode fmn) {
                    if (fmn.isStatic() && fmn.owner().equals(HookContainer.CLIENT.getInternalName()) && fmn.desc().equals("I")) {
                        if (fmn.method().desc.equals("(L" + HookContainer.CHARACTER.getInternalName() + ";)V")) {
                            HookContainer.CLIENT.addHook(fmn, "getLoopCycle");
                        }
                    }
                }
            });
        }
    }

    private class ProcessMenuActionVisitor extends BlockVisitor {

        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public void visit(BasicBlock block) {
            block.tree().accept(new NodeVisitor() {

                @Override
                public void visitIinc(IincNode nn) {
                    if (nn.increment() == -2000) {
                        boolean isInvoked = DummyUtil.isInvoked(nn.method());
                        if (isInvoked) {
                            HookContainer.CLIENT.addHook(block.owner, "processMenuAction()");

                            MemberHookSpecification spec = HookContainer.CLIENT.getHook("processMenuAction()");
                            spec.addType(MemberHook.TYPE_CALLBACK);
                            spec.addData("callclass", "org/rev317/min/callback/MenuAction");
                            spec.addData("callmethod", "intercept");
                            spec.addData("calldesc", nn.method().desc);
                            StringBuilder callArgs = new StringBuilder();
                            for (int i = 0; i < Type.getArgumentTypes(nn.method().desc).length; i++) {
                                callArgs.append(i).append(",");
                            }
                            callArgs.setLength(callArgs.length() - 1);
                            spec.addData("callargs", callArgs.toString());

                            spec.addType(MemberHook.TYPE_INVOKER);
                            spec.addData("inv-methodname", "doAction");

                            MenuActionVisitor mav = new MenuActionVisitor();
                            for (BasicBlock b : block.owner.graph()) {
                                b.accept(mav);
                            }
                        }
                    }
                }
            });
        }

    }

    private class MenuActionVisitor extends BlockVisitor {
        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public void visit(BasicBlock block) {
            block.tree().accept(new NodeVisitor() {
                @Override
                public void visitField(FieldMemberNode fmn) {
                    if (fmn.desc().equals("[I") && fmn.next() != null && fmn.next().opcode() == 21) {
                        //add menu actions here
                    }

                    if (fmn.desc().equals("I") && fmn.next() != null && fmn.opcode() == 180 && fmn.next().opcode() == 2) {
                        HookContainer.CLIENT.addHook(fmn, "getOpenInterfaceId");
                    }
                }
            });
        }
    }

    private class StatsVisitor extends BlockVisitor {

        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public void visit(BasicBlock block) {
            block.tree().accept(new NodeVisitor() {
                @Override
                public void visitField(FieldMemberNode fmn) {
                    if (fmn.desc().equals("[I") && fmn.next() != null && fmn.next().opcode() == 46 && fmn.next().index() == 71) {
                        HookContainer.CLIENT.addHook(fmn, "getCurrentExp");
                    } else if (fmn.desc().equals("[I") && fmn.opcode() == 180 && fmn.next() != null && fmn.next().opcode() == 46 && fmn.next().index() == 47) {
                        HookContainer.CLIENT.addHook(fmn, "getCurrentStats");
                    } else if (fmn.desc().equals("[I") && fmn.opcode() == 180 && fmn.next() != null && fmn.next().index() > 160 && fmn.next().index() < 170) {
                        HookContainer.CLIENT.addHook(fmn, "getSettings");
                    }
                }
            });
        }
    }

    private class BaseXYVisitor extends BlockVisitor {

        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public void visit(BasicBlock block) {
            block.tree().accept(new NodeVisitor() {
                @Override
                public void visitField(FieldMemberNode fmn) {
                	if (fmn.desc().equals("I") && fmn.previous() != null) {
                		if (fmn.previous().opcode() == 21) {
                			if (HookContainer.CLIENT.getHook("getBaseY") == null) {
                				HookContainer.CLIENT.addHook(fmn, "getBaseY");
                			} else {
                				HookContainer.CLIENT.addHook(fmn, "getBaseX");
                			}
                		}
                	}
                }
            });
        }
    }
    
    private class LoggedInVisitor extends BlockVisitor {

        @Override
        public boolean validate() {
            return true;
        }

        @Override
        public void visit(BasicBlock block) {
            block.tree().accept(new NodeVisitor() {
                @Override
                public void visitField(FieldMemberNode fmn) {

                    if (fmn.desc().equals("Z") && fmn.next() != null && fmn.previous() != null && fmn.next().opcode() == 181 && fmn.previous().opcode() == -1) {
                        HookContainer.CLIENT.addHook(fmn, "isLoggedIn");
                    }
                }
            });
        }
    }
}