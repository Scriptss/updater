package com.micule.hook;

import java.util.LinkedHashMap;
import java.util.Map;

import org.objectweb.asm.commons.cfg.tree.node.FieldMemberNode;
import org.objectweb.asm.commons.cfg.tree.node.MethodMemberNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.micule.util.StringUtil;

public enum HookContainer {

    ANIMABLE("Animable", null),
    NODE("Node", null, "getNext"),
    NODE_SUB("NodeSub", null),
    COLLISION_MAP("CollisionMap", null, "getFlags"),
    INTERFACE("Interface", NODE, "getItems", "getStackSizes", "getMessage"),
    NPC("Npc", null, "getDef"),
    CHARACTER("Character", null, "getAnimation", "getCurrentHealth", "getInteractingEntity", "getLoopCycleStatus", "getMaxHealth", "getX", "getY"),
    DEQUE("Deque", null, "getCurrent", "getHead"),
    OBJECT1("Object1", null),
    OBJECT2("Object2", null),
    OBJECT3("Object3", null),
    OBJECT4("Object4", null),
    OBJECT5("Object5", null),
    GROUND("Ground", null, "getGroundItem", "getGroundDecoration", "getInteractiveObjects", "getWallDecorations", "getWallObject"),
    ITEM("Item", null, "getId"),
    PLAYER("Player", null, "getName", "getEquipment"),
    SCENE("Scene", null, "getGroundArray"),
    CLIENT("Client", null, "getBackDialogId", "getBaseX", "getBaseY", "getCollisionMap", "getCurrentExp", "getGroundItems", "isLoggedIn", "getLoopCycle", "getMyPlayer", "getNpcs", "getOpenInterfaceId", "getPlane", "getPlayers", "getScene", "getMenuAction1", "getMenuAction2", "getMenuAction3", "getMenuAction4", "getMenuActionId", "getCurrentStats", "getSettings", "setInterface", "messageListener", "intercept", "messageListenerHook", "walkTo()", "processMenuAction()"),
    NPC_DEF("NpcDef", null, "getId", "getNpcName"),
    ;

    private final Map<String, MemberHookSpecification> hooks;
    private final String definedName;
    private final HookContainer superType;
    private String internalName;
 
    private HookContainer(final String definedName, final HookContainer superType, final String... keys) {
        this.definedName = definedName;
        this.superType = superType;
        this.hooks = new LinkedHashMap<String, MemberHookSpecification>();
        for (final String key : keys) {
            hooks.put(key, null);
        }
    }
 
    public void addHook(final MemberHookSpecification spec) {
        this.hooks.put(spec.getDefinedName(), spec);
    }
 
    public void addHook(final MethodMemberNode ref, final String definedName) {
        this.hooks.put(definedName, new MemberHook(ref, definedName));
    }
    
    public void addHook(final FieldMemberNode ref, final String definedName) {
        this.hooks.put(definedName, new MemberHook(ref, definedName));
    }
 
    public void addHook(final FieldNode fn, final String definedName) {
        this.hooks.put(definedName, new MemberHook(fn, definedName));
    }
    
 
    public void addHook(final MethodNode mn, final String definedName) {
        this.hooks.put(definedName, new MemberHook(mn, definedName));
    }
 
    public void addHook(final FieldInsnNode fn, final String definedName) {
        this.hooks.put(definedName, new MemberHook(fn, definedName));
    }
 
    public void addHook(final MethodInsnNode mn, final String definedName) {
        this.hooks.put(definedName, new MemberHook(mn, definedName));
    }
 
    public String getDefinedName() {
        return definedName;
    }
 
    public HookContainer getSuperType() {
        return superType;
    }
 
    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(final String internalName) {
        this.internalName = internalName;
    }

    public int size() {
        return hooks.size();
    }
    
    public Map<String, MemberHookSpecification> getHooks() {
    	return this.hooks;
    }
    
    public int fieldCount() {
    	int fields = 0;
    	for(String name : hooks.keySet()) {
    		if(!name.contains("()")) {
    			fields++;
    		}
    	}
    	return fields;
    }
    
    public int fieldFoundCount() {
    	int fields = 0;
    	for(final Map.Entry<String, MemberHookSpecification> spec : hooks.entrySet()) {
    		if(!spec.getKey().contains("()") && spec.getValue() != null) {
    			fields++;
    		}
    	}
    	return fields;
    }
    
    public int methodFoundCount() {
    	int methods = 0;
    	for(final Map.Entry<String, MemberHookSpecification> spec : hooks.entrySet()) {
    		if(spec.getKey().contains("()") && spec.getValue() != null) {
    			methods++;
    		}
    	}
    	return methods;
    }
    
    public int methodCount() {
    	int methods = 0;
    	for(String name : hooks.keySet()) {
    		if(name.contains("()")) {
    			methods++;
    		}
    	}
    	return methods;
    }
   

    @Override
    public String toString() {
    	if(internalName == null) {
    		return "--> " + String.format("%s - %s", definedName, "BROKEN\n");
    	}
    	final StringBuilder sb = new StringBuilder();
    	sb.append("--> ").append(definedName).append(" [").append(internalName).append((superType != null ? " extends " + superType.definedName : "" )).append("]");
    	sb.append('\n');
    	for (final Map.Entry<String, MemberHookSpecification> spec : hooks.entrySet()) {
    		if(spec.getValue() != null) {
    			if(spec.getKey().contains("()")) {
    				sb.append(String.format("~>    %-20s %-20s", spec.getKey(), spec.getValue().getOwner() + "." + spec.getValue().getName() + StringUtil.getMethodDesc(spec.getValue().getDescriptor())));
    			} else {
    				sb.append(String.format("->    %-20s %-15s %-10s %-10s", spec.getKey(), spec.getValue().getOwner() + "." + spec.getValue().getName(), StringUtil.getTypeDesc(spec.getValue().getDescriptor()), getMultiplier(spec.getValue())));
    			}
    			sb.append('\n');
    		} else {
    			sb.append(String.format("=>   %-20s %-15s", spec.getKey(), "BROKEN")).append('\n');
    		}
    	}
    	return sb.toString();
    }
    
    private String getMultiplier(MemberHookSpecification spec) {
    	if(spec.getDescriptor().equals("I") && ((MemberHook)spec).getMultiplier() != 1) {
    		return "* " + ((MemberHook)spec).getMultiplier();
    	}
    	return "";
    }
 
    public MemberHookSpecification getHook(final String key) {
        return hooks.get(key);
    }
}