package com.micule.hook;

import java.util.ArrayList;
import java.util.HashMap;

import org.objectweb.asm.commons.cfg.tree.node.FieldMemberNode;
import org.objectweb.asm.commons.cfg.tree.node.MethodMemberNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.micule.analysis.Mul;

public final class MemberHook implements MemberHookSpecification, Comparable<MemberHook> {
	public static final int TYPE_GETTER = 0;
	public static final int TYPE_SETTER = 1;
	public static final int TYPE_INVOKER = 2;
	public static final int TYPE_CALLBACK = 3;
	
	private ArrayList<Integer> types;
	private HashMap<String, String> data;
    private final String owner, name, desc, definedName;
 
    public MemberHook(final String owner, final String name, final String desc, final String definedName, int... types) {
    	this.types = new ArrayList<>();
    	this.data = new HashMap<>();
        this.owner = owner;
        this.name = name;
        this.desc = desc;
        this.definedName = definedName;
        
        for(int type : types) {
        	this.types.add(type);
        }
    }
 
    public MemberHook(final FieldNode fn, final String definedName) {
        this(fn.owner.name, fn.name, fn.desc, definedName, TYPE_GETTER);
    }
 
    public MemberHook(final FieldMemberNode fn, final String definedName) {
        this(fn.owner(), fn.name(), fn.desc(), definedName, TYPE_GETTER);
    }
    
    public MemberHook(final MethodMemberNode mn, final String definedName) {
        this(mn.owner(), mn.name(), mn.desc(), definedName);
    }
  
    public MemberHook(final FieldInsnNode fin, final String definedName) {
        this(fin.owner, fin.name, fin.desc, definedName, TYPE_GETTER);
    }
 
    public MemberHook(final MethodNode fn, final String definedName) {
        this(fn.owner.name, fn.name, fn.desc, definedName);
    }
 
    public MemberHook(final MethodInsnNode fin, final String definedName) {
        this(fin.owner, fin.name, fin.desc, definedName);
    }
    
    public void addData(String key, String value) {
    	data.put(key, value);
    }
    
    public String getData(String key) {
    	return data.get(key);
    }
    
    @Override
    public void addType(int type) {
    	types.add(type);
    }
    
    public int getMultiplier() {
    	int multiplier = Mul.get(this.owner, this.name);
        return multiplier;
    }
 
    @Override
    public String getOwner() {
        return owner;
    }
 
    @Override
    public String getName() {
        return name;
    }
 
    @Override
    public String getDefinedName() {
        return definedName;
    }
 
    @Override
    public String getDescriptor() {
        return desc;
    }
 
    @Override
    public boolean equals(final Object ob) {
        if (ob instanceof MemberHook) {
            final MemberHook o = (MemberHook) ob;
            return o.definedName.equals(definedName) && o.desc.equals(desc) && o.owner.equals(owner) && o.name.equals(name);
        }
        return false;
    }
 
    @Override
    public int compareTo(MemberHook o) {
        return o.name.compareTo(name) + o.definedName.compareTo(definedName) + o.desc.compareTo(desc) + o.owner.compareTo(owner);
    }

	@Override
	public boolean isGetter() {
		return types.contains(TYPE_GETTER);
	}

	@Override
	public boolean isSetter() {
		return types.contains(TYPE_SETTER);
	}

	@Override
	public boolean isInvoker() {
		return types.contains(TYPE_INVOKER);
	}

	@Override
	public boolean isCallback() {
		return types.contains(TYPE_CALLBACK);
	}

}