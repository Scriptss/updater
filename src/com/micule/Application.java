package com.micule;

import com.micule.analysis.Mul;
import com.micule.analysis.MultiplierAnalyser;
import com.micule.handler.AnalysisHandler;
import com.micule.hook.HookContainer;
import com.micule.util.DummyUtil;
import org.objectweb.asm.commons.cfg.CallVisitor;
import org.objectweb.asm.commons.cfg.FlowSortVisitor;
import org.objectweb.asm.commons.util.JarArchive;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.parabot.XMLHookGenerator;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author System wrote by Parnassian and JKetelaar
 * @author Patterns made by JKetelaar, Empathy and Fryslan
 */
public class Application {

    public static final String CACHE =
//            "clients/runique/4.jar";
//	          "clients/grinderscape/1.jar";
//            "clients/liquid/1.jar";
            "clients/spk/client.jar";
//            "clients/clean/1.jar";
//            "clients/pkhonor/3.jar";
//            "clients/ikov/6.jar";
//            "clients/nearreality/1.jar";

    private static final boolean ENABLE_CALL_GRAPH = false;
    
    public static Map<String, ClassNode> CLASS_NODE_MAP;

    public static void main(String... args) {
    	System.out.println("[- RSPS Updater -]");
    	System.out.println("--> By Paradox, Parnassian, Empathy and Fryslan");
    	System.out.println();
    	
    	File jarFile = new File(CACHE);
    	
    	long startTime = System.currentTimeMillis();
    	System.out.println("-> Parsing " + jarFile.getName());
    	JarArchive archive = new JarArchive(jarFile);
    	CLASS_NODE_MAP = archive.classes();
    	System.out.println("-> Done. Parsed " + CLASS_NODE_MAP.size() + " classes. [Took " + took(startTime) + "]");
    	System.out.println();

        if (ENABLE_CALL_GRAPH) {
            System.out.println("-> Generating call graph...");
            startTime = System.currentTimeMillis();
            int count = 0;
            CallVisitor visitor = new CallVisitor();
            for(ClassNode cn : Application.CLASS_NODE_MAP.values()) {
                for(MethodNode mn : cn.methods) {
                    visitor.visit(mn);
                    count++;
                }
            }

            ArrayList<MethodNode> toRemove = new ArrayList<MethodNode>();
            for(ClassNode cn : Application.CLASS_NODE_MAP.values()) {
                for(MethodNode mn : cn.methods) {
                    if(!visitor.graph.containsVertex(mn.handle)) {
                        if(!DummyUtil.isInvoked(cn, mn, visitor.graph)) {
                            toRemove.add(mn);
                        }
                    }
                }
                cn.methods.removeAll(toRemove);
            }
            System.out.println("-> Done. Removed " + toRemove.size() + "/" + count + " methods. [Took " + took(startTime) + "]");
            System.out.println();
        }

    	if("nope".isEmpty()) {
    		for(ClassNode cn : Application.CLASS_NODE_MAP.values()) {
        		for(MethodNode mn : cn.methods) {
        			if(mn.owner.name.equals("fk") && mn.name.startsWith("bo")) {
        				FlowSortVisitor v = new FlowSortVisitor();
        				v.accept(mn);
        				v.showFrame();
        				return;
        			}
        		}
    		}
    	}
     	
    	System.out.println("-> Analyzing multipliers...");
        MultiplierAnalyser.findMultipliers(new ArrayList<ClassNode>(CLASS_NODE_MAP.values()));
        Mul.decideMultipliers();
        System.out.println("-> Done. Resolved " + Mul.multipliers.size() + " multipliers. [Took " + (System.currentTimeMillis() - startTime) + "ms]");
        System.out.println();
        
        startTime = System.currentTimeMillis();
        
        System.out.println("-> Finding hooks...");
        System.out.println();
        
        new AnalysisHandler().run();
        
        long endTime = System.currentTimeMillis();

        double classes = 0, fields = 0, methods = 0;
        double foundClasses = 0, foundFields = 0, foundMethods = 0;
        for (HookContainer hc : HookContainer.values()) {
            classes++;
            if(hc.getInternalName() != null) {
            	foundClasses++;
            }
            foundFields += hc.fieldFoundCount();
            foundMethods += hc.methodFoundCount();
            fields += hc.fieldCount();
            methods += hc.methodCount();
            System.out.println(hc.toString());
        }
        
        DecimalFormat f = new DecimalFormat("##.00");
        
        System.out.println("--> Done. [Took " + (endTime - startTime) + "ms]");
        System.out.println();
        System.out.println(String.format("%s %-10s %-10s", "--> Classes :", (int)foundClasses + "/" + (int)classes, f.format((foundClasses / classes) * 100.0D) + "%"));
        System.out.println(String.format("%s %-10s %-10s", "-> Fields   :", (int)foundFields + "/" + (int)fields, f.format((foundFields / fields) * 100.0D) + "%"));
        System.out.println(String.format("%s %-10s %-10s", "~> Methods  :", (int)foundMethods + "/" + (int)methods, f.format((foundMethods / methods) * 100.0D) + "%"));
    
        System.out.println();
        System.out.println("-> Writing " + jarFile.getParentFile().getName() + ".xml...");
        startTime = System.currentTimeMillis();
        new XMLHookGenerator().save(new File("hooks/" + jarFile.getParentFile().getName() + ".xml"));
        endTime = System.currentTimeMillis();
        System.out.println("--> Done. [Took " + (endTime - startTime) + "ms]");
        
        archive.write(new File("out.jar"));
    }
    
    public static String took(long startTime) {
    	long endTime = System.currentTimeMillis();
    	return endTime - startTime + "ms";
    }
}
