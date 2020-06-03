package com.micule.hook;

public interface MemberHookSpecification extends HookSpecification {
    String getOwner();
    
    String getDescriptor();
    
    String getData(String key);
    
    void addData(String key, String value);
    
    void addType(int type);
    
    boolean isGetter();
    
    boolean isSetter();
    
    boolean isInvoker();
    
    boolean isCallback();
    
    
}