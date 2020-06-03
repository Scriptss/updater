package org.parabot;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.micule.hook.HookContainer;
import com.micule.hook.MemberHook;
import com.micule.hook.MemberHookSpecification;
import com.micule.util.Utils;

public class XMLHookGenerator {
	private Document doc;
	private Element accessors;
	private Element getters;
	private Element setters;
	private Element callbacks;
	private Element invokers;
	
	private HashMap<String, String> accessorMap;
	
	public XMLHookGenerator() {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	 
			doc = docBuilder.newDocument();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		this.accessorMap = new HashMap<String, String>();
		
		Element root = doc.createElement("injector");
		doc.appendChild(root);
		
		accessors = doc.createElement("interfaces");
		root.appendChild(accessors);
		
		getters = doc.createElement("getters");
		root.appendChild(getters);
		
		setters = doc.createElement("setters");
		root.appendChild(setters);
		
		callbacks = doc.createElement("callbacks");
		root.appendChild(callbacks);
		
		invokers = doc.createElement("invokers");
		root.appendChild(invokers);
		
		for(HookContainer hc : HookContainer.values()) {
			if(hc.getInternalName() != null) {
				accessor(hc);
				accessorMap.put(hc.getInternalName(), hc.getDefinedName());
			}
		}
		
		for (HookContainer hc : HookContainer.values()) {
			if (hc.getInternalName() != null) {
				for (final Entry<String, MemberHookSpecification> entry : hc.getHooks().entrySet()) {
					String name = entry.getKey();
					MemberHookSpecification spec = entry.getValue();
					if(spec == null) {
						continue;
					}
					if (spec.isGetter()) {
						getter(hc, name, spec);
					}
					if (spec.isSetter()) {
						setter(hc, name, spec);
					}
					if (spec.isCallback()) {
						callback(hc, name, spec);
					}
					if (spec.isInvoker()) {
						invoker(hc, name, spec);
					}
				}
			}
		}
	
	}
	
	public void invoker(HookContainer hc, String name, MemberHookSpecification spec) {
		Element root = doc.createElement("add");
		
		MethodNode mn = Utils.method(spec);
		
		if(mn == null) {
			System.err.println("[Invoker] XMLHookGenerator: method node is null: " + name + " " + spec.getOwner() + "." + spec.getName() + "  " + spec.getDescriptor());
			return;
		}
		
		if(hc.getInternalName().equals(spec.getOwner()) && !Modifier.isStatic(mn.access)) {
			// write invoker in this class if not static
			Element accessor = doc.createElement("accessor");
			accessor.appendChild(doc.createTextNode(accessorMap.get(spec.getOwner())));
			root.appendChild(accessor);
		} else if(Modifier.isStatic(mn.access)) {
			// write invoker in client class if method is static
			Element className = doc.createElement("classname");
			className.appendChild(doc.createTextNode(spec.getOwner()));
			root.appendChild(className);
			
			Element into = doc.createElement("into");
			into.appendChild(doc.createTextNode(clientClass()));
			root.appendChild(into);
		} else {
			System.err.println("[Invoker] XMLHookGenerator: could not write invoker: " + name + " " + spec.getOwner() + "." + spec.getName() + "  " + spec.getDescriptor());
			return;
		}
		
		Element invokerName = doc.createElement("methodname");
		invokerName.appendChild(doc.createTextNode(spec.getData("inv-methodname")));
		root.appendChild(invokerName);
		

		Type t = Type.getMethodType(mn.desc);
		
		Element invokeMethod = doc.createElement("invokemethod");
		invokeMethod.appendChild(doc.createTextNode(mn.name));
		root.appendChild(invokeMethod);
		
		if(spec.getData("desc") == null) {
			Element desc = doc.createElement("desc");
			desc.appendChild(doc.createTextNode(t.getReturnType().getDescriptor()));
			root.appendChild(desc);
		} else {
			Element desc = doc.createElement("desc");
			desc.appendChild(doc.createTextNode(spec.getData("desc")));
			root.appendChild(desc);
		}
		
		StringBuilder builder = new StringBuilder().append('(');
		for(Type type : t.getArgumentTypes()) {
			builder.append(type.getDescriptor());
		}
		builder.append(')');
		
		Element argDesc = doc.createElement("argsdesc");
		argDesc.appendChild(doc.createTextNode(builder.toString()));
		root.appendChild(argDesc);
		
		
		invokers.appendChild(root);
	}
	
	public void callback(HookContainer hc, String name, MemberHookSpecification spec) {
		Element root = doc.createElement("add");
		
		MethodNode mn = Utils.method(spec);
		
		if(mn == null) {
			System.err.println("[Callback] XMLHookGenerator: method node is null: " + name + " " + spec.getOwner() + "." + spec.getName() + "  " + spec.getDescriptor());
			return;
		}
		
		if(hc.getInternalName().equals(spec.getOwner())) {
			// use accessor
			Element accessor = doc.createElement("accessor");
			accessor.appendChild(doc.createTextNode(accessorMap.get(spec.getOwner())));
			root.appendChild(accessor);
		} else {
			// use classname
			Element className = doc.createElement("classname");
			className.appendChild(doc.createTextNode(spec.getOwner()));
			root.appendChild(className);
		}
		
		
		Element methodName = doc.createElement("methodname");
		methodName.appendChild(doc.createTextNode(spec.getName()));
		root.appendChild(methodName);

		Element desc = doc.createElement("desc");
		desc.appendChild(doc.createTextNode(spec.getDescriptor()));
		root.appendChild(desc);

		Element callClass = doc.createElement("callclass");
		callClass.appendChild(doc.createTextNode(spec.getData("callclass")));
		root.appendChild(callClass);

		Element callMethod = doc.createElement("callmethod");
		callMethod.appendChild(doc.createTextNode(spec.getData("callmethod")));
		root.appendChild(callMethod);

		Element callDesc = doc.createElement("calldesc");
		callDesc.appendChild(doc.createTextNode(spec.getData("calldesc")));
		root.appendChild(callDesc);

		Element callArgs = doc.createElement("callargs");
		callArgs.appendChild(doc.createTextNode(spec.getData("callargs")));
		root.appendChild(callArgs);

		callbacks.appendChild(root);
	}
	
	public void setter(HookContainer hc, String name, MemberHookSpecification spec) {
		Element root = doc.createElement("add");
		
		FieldNode fn = Utils.fieldNode(spec);
		
		if(fn == null) {
			System.err.println("[Setter] XMLHookGenerator: field node is null: " + name + " " + spec.getOwner() + "." + spec.getName() + "  " + spec.getDescriptor());
			return;
		}
		
		if(hc.getInternalName().equals(spec.getOwner()) && !Modifier.isStatic(fn.access)) {
			// write setter in this class if not static
			Element accessor = doc.createElement("accessor");
			accessor.appendChild(doc.createTextNode(accessorMap.get(spec.getOwner())));
			root.appendChild(accessor);
		} else if(Modifier.isStatic(fn.access)) {
			// write setter in client class if field is static
			Element className = doc.createElement("classname");
			className.appendChild(doc.createTextNode(spec.getOwner()));
			root.appendChild(className);
			
			Element into = doc.createElement("into");
			into.appendChild(doc.createTextNode(clientClass()));
			root.appendChild(into);
		} else {
			System.err.println("[Setter] XMLHookGenerator: could not write setter: " + name + " " + spec.getOwner() + "." + spec.getName() + "  " + spec.getDescriptor());
			return;
		}

		Element field = doc.createElement("field");
		field.appendChild(doc.createTextNode(spec.getName()));
		root.appendChild(field);

		Element methodName = doc.createElement("methodname");
		methodName.appendChild(doc.createTextNode(name));
		root.appendChild(methodName);

		Element descField = doc.createElement("descfield");
		descField.appendChild(doc.createTextNode(spec.getDescriptor()));
		root.appendChild(descField);

		setters.appendChild(root);
	}

	public void getter(HookContainer hc, String name, MemberHookSpecification spec) {
		Element root = doc.createElement("add");
		
		FieldNode fn = Utils.fieldNode(spec);
		if(fn == null) {
			System.err.println("[Getter] XMLHookGenerator: field node is null: " + name + " " + spec.getOwner() + "." + spec.getName() + "  " + spec.getDescriptor());
			return;
		}
		
		if(hc.getInternalName().equals(spec.getOwner()) && !Modifier.isStatic(fn.access)) {
			// write getter in this class if not static
			Element accessor = doc.createElement("accessor");
			accessor.appendChild(doc.createTextNode(accessorMap.get(spec.getOwner())));
			root.appendChild(accessor);
		} else if(Modifier.isStatic(fn.access)) {
			// write getter in client class if field is static
			Element className = doc.createElement("classname");
			className.appendChild(doc.createTextNode(spec.getOwner()));
			root.appendChild(className);
			
			Element into = doc.createElement("into");
			into.appendChild(doc.createTextNode(clientClass()));
			root.appendChild(into);
		} else {
			System.err.println("[Getter] XMLHookGenerator: could not write getter: " + name + " " + spec.getOwner() + "." + spec.getName() + "  " + spec.getDescriptor());
			return;
		}

		Element field = doc.createElement("field");
		field.appendChild(doc.createTextNode(spec.getName()));
		root.appendChild(field);

		Element methodName = doc.createElement("methodname");
		methodName.appendChild(doc.createTextNode(name));
		root.appendChild(methodName);

		if(spec.getData("checkcast") != null) {
			Element desc = doc.createElement("desc");
			desc.appendChild(doc.createTextNode(spec.getData("checkcast")));
			
			root.appendChild(desc);
		} else if(Type.getType(spec.getDescriptor()).getSort() > 8) {
			Element desc = doc.createElement("desc");

			Type realType = Type.getType(spec.getDescriptor());
			String className = className(realType);
			if (accessorMap.containsKey(className)) {
				desc.appendChild(doc.createTextNode(accessor(realType, accessorMap.get(className))));

				root.appendChild(desc);
			}
		}

		Element descField = doc.createElement("descfield");
		descField.appendChild(doc.createTextNode(spec.getDescriptor()));
		root.appendChild(descField);
		
		if(spec.getDescriptor().equals("I") && ((MemberHook)spec).getMultiplier() != 1) {
			Element multiplier = doc.createElement("multiplier");
			multiplier.appendChild(doc.createTextNode(((MemberHook)spec).getMultiplier() + ""));
			root.appendChild(multiplier);
		}

		getters.appendChild(root);
	}
	
	public void accessor(HookContainer container) {
		Element root = doc.createElement("add");
		Element className = doc.createElement("classname");
		className.appendChild(doc.createTextNode(container.getInternalName()));
		Element accessor = doc.createElement("interface");
		accessor.appendChild(doc.createTextNode(container.getDefinedName()));
		root.appendChild(className);
		root.appendChild(accessor);
		accessors.appendChild(root);
	}
	
	public void save(File file) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(file);
	 
			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	
	private String clientClass() {
		for(Entry<String, String> entry : this.accessorMap.entrySet()) {
			if(entry.getValue().equals("Client")) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	public boolean isValidAccessor(String className) {
		if(this.accessorMap.containsKey(className)) {
			String accessor = this.accessorMap.get(className);
			int count = 0;
			for(String a : this.accessorMap.values()) {
				if(a.equals(accessor)) {
					count++;
				}
			}
			return count < 2;
		}
		return false;
	}
	
	public static String className(Type type) {
		String className = type.getClassName().replace("[]", "").replace('.', '/');
		return className;
	}
	
	public static String accessor(Type type, String accessorName) {
		String internalName = type.getInternalName();
		int count = internalName.length() - internalName.replace("[", "").length();
		String desc = "";
		if(count > 0) {
			desc += internalName.substring(0, count);
		}
		return desc + "%s" + accessorName;
	}

}
