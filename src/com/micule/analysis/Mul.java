package com.micule.analysis;

import java.util.HashMap;
import java.util.Map;
 
public class Mul {
 
    public static Map<Integer, Map<Integer, Multiplier>> potentialMultipliers = new HashMap<Integer, Map<Integer, Multiplier>>();
    public static Map<Integer, Multiplier> multipliers;
 
    public static void put(int refHash, int multiplier) {
        Map<Integer, Multiplier> potentialMultiplierMap = potentialMultipliers.get(refHash);
        if (potentialMultiplierMap == null) {
            potentialMultiplierMap = new HashMap<Integer, Multiplier>();
            potentialMultiplierMap.put(multiplier, new Multiplier(refHash, 1, multiplier));
            potentialMultipliers.put(refHash, potentialMultiplierMap);
        } else {
            Multiplier inst = potentialMultiplierMap.get(multiplier);
            if (inst == null) {
                potentialMultiplierMap.put(multiplier, new Multiplier(refHash, 1, multiplier));
            } else {
                inst.increment();
            }
        }
    }
 
    public static void decideMultipliers() {
        multipliers = new HashMap<Integer, Multiplier>();
        for (Map<Integer, Multiplier> potential : potentialMultipliers.values()) {
            Multiplier max = null;
            for (Multiplier m : potential.values()) {
                if (max == null || m.count > max.count) {
                    max = m;
                }
            }
            multipliers.put(max.ref, max);
        }
        potentialMultipliers = null;
    }
 
    public static int get(String owner, String name) {
        try {
            return get(getHash(owner, name));
        } catch (Exception e) {
            return 1;
        }
    }
 
    public static int get(int refHash) {
        try {
            return multipliers.get(refHash).value;
        } catch (Exception e) {
            return 1;
        }
    }
 
    public static int getHash(String owner, String name) {
        return (owner + "." + name).hashCode();
    }
 
    public static class Multiplier {
        public int ref;
        public int count;
        public int value;
 
        public Multiplier(int ref, int count, int value) {
            this.ref = ref;
            this.count = count;
            this.value = value;
        }
 
        public void increment() {
            count++;
        }
    }
}