package com.micule.analysis.impl;

import com.micule.analysis.Analyser;
import com.micule.hook.HookContainer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

public class Player extends Analyser {
    @Override
    public ClassSpec specify(ClassNode cn) {
        if (cn.superName.equals(HookContainer.CHARACTER.getInternalName()) && cn.fieldCount() > 10){
            return new ClassSpec(HookContainer.PLAYER, cn);
        }
        return null;
    }

    @Override
    public void evaluate(ClassNode cn) {
        int i = 0;
        for (FieldNode fn : cn.fields) {
            i++;
            if (fn.desc.equals("Ljava/lang/String;")) {
                HookContainer.PLAYER.addHook(fn, "getName");
            } else if (fn.desc.contains("L") && !fn.desc.equals("L" + HookContainer.NPC_DEF + ";")) {
                FieldNode nf = null;
                for (int x = i; x < cn.fields.size(); x++) {
                    nf = cn.fields.get(x);
                    if (nf.desc.contains("[I")) {
                        HookContainer.PLAYER.addHook(nf, "getEquipment");
                        break;
                    }
                }
            }
        }
    }
}
