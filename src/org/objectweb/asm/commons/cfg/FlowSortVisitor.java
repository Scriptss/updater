package org.objectweb.asm.commons.cfg;

import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.DRETURN;
import static org.objectweb.asm.Opcodes.FRETURN;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LRETURN;
import static org.objectweb.asm.Opcodes.RETURN;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.jgrapht.DirectedGraph;
import org.jgrapht.adapter.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.cfg.tree.NodeTree;
import org.objectweb.asm.commons.cfg.tree.util.TreeBuilder;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.IincInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LookupSwitchInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.MultiANewArrayInsnNode;
import org.objectweb.asm.tree.TableSwitchInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.micule.util.StringUtil;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;

/**
 * @author Tyler Sedlar
 */
public class FlowSortVisitor extends MethodVisitor {

    private MethodNode mn;
    private BasicBlock current = new BasicBlock(new Label());
    public List<BasicBlock> blocks = new ArrayList<>();
    public DirectedGraph<BasicBlock, DefaultEdge> graph = null;
    
    public List<AbstractInsnNode> instructions = new ArrayList<>();

    public void accept(MethodNode mn) {
        current = new BasicBlock(new Label());
        blocks.clear();
        blocks.add(current);
        (this.mn = mn).accept(this);
    }

    public FlowSortVisitor() {

    }

    public FlowSortVisitor(final MethodNode mn) {
        current = new BasicBlock(new Label());
        blocks.clear();
        blocks.add(current);
        (this.mn = mn).accept(this);
    }

    /**
     * Constructs blocks for all given labels.
     *
     * @param labels The labels in which to construct blocks for.
     * @return The constructed blocks.
     */
    private BasicBlock[] constructAll(List<LabelNode> labels) {
        BasicBlock[] blocks = new BasicBlock[labels.size()];
        for (int i = 0; i < blocks.length; i++)
            blocks[i] = construct(labels.get(i));
        return blocks;
    }

    /**
     * Constructs a block for the given label.
     *
     * @param ln The label to get a block from.
     * @return The given label's block.
     */
    private BasicBlock construct(LabelNode ln) {
        return construct(ln, true);
    }

    /**
     * Constructs a block for the given label.
     *
     * @param ln The label to get a block from.
     * @param add <t>true</t> to add the block to the next preds, otherwise <t>false.</t>
     * @return A block for the given label.
     */
    private BasicBlock construct(LabelNode ln, boolean add) {
        Label label = ln.getLabel();
        if (!(label.info instanceof BasicBlock)) {
            label.info = new BasicBlock(label);
            if (add) {
                current.next = ((BasicBlock) label.info);
                current.next.preds.add(current.next);
            }
            blocks.add((BasicBlock) label.info);
        }
        return (BasicBlock) label.info;
    }

    @Override
    public void visitInsn(InsnNode in) {
        current.instructions.add(in);
        switch (in.opcode()) {
            case RETURN:
            case IRETURN:
            case ARETURN:
            case FRETURN:
            case DRETURN:
            case LRETURN:
            case ATHROW: {
                current = construct(new LabelNode(new Label()), false);
                break;
            }
        }
    }

    @Override
    public void visitIntInsn(IntInsnNode iin) {
        current.instructions.add(iin);
    }

    @Override
    public void visitVarInsn(VarInsnNode vin) {
        current.instructions.add(vin);
    }

    @Override
    public void visitTypeInsn(TypeInsnNode tin) {
        current.instructions.add(tin);
    }

    @Override
    public void visitFieldInsn(FieldInsnNode fin) {
        current.instructions.add(fin);
    }

    @Override
    public void visitMethodInsn(MethodInsnNode min) {
        current.instructions.add(min);
    }

    @Override
    public void visitInvokeDynamicInsn(InvokeDynamicInsnNode idin) {
        current.instructions.add(idin);
    }

    @Override
    public void visitJumpInsn(JumpInsnNode jin) {
        int opcode = jin.opcode();      
        current.target = construct(jin.label);
        current.target.preds.add(current.target);
        //if (opcode != GOTO)
            current.instructions.add(jin);
       
        Stack<AbstractInsnNode> stack = current.stack;
        current = construct(new LabelNode(new Label()), opcode != GOTO);
        current.stack = stack;
    }


    @Override
    public void visitLabel(Label label) {
        //if (label == null || label.info == null) return;
        Stack<AbstractInsnNode> stack = current == null ? new Stack<AbstractInsnNode>() : current.stack;
        current = construct(new LabelNode(label));
        current.stack = stack;
    }

    @Override
    public void visitLdcInsn(LdcInsnNode ldc) {
        current.instructions.add(ldc);
    }

    @Override
    public void visitIincInsn(IincInsnNode iin) {
        current.instructions.add(iin);
    }

    @Override
    public void visitTableSwitchInsn(TableSwitchInsnNode tsin) {
        construct(tsin.dflt);
        constructAll(tsin.labels);
        current.instructions.add(tsin);
    }

    @Override
    public void visitLookupSwitchInsn(LookupSwitchInsnNode lsin) {
        construct(lsin.dflt);
        constructAll(lsin.labels);
        current.instructions.add(lsin);
    }

    @Override
    public void visitMultiANewArrayInsn(MultiANewArrayInsnNode manain) {
        current.instructions.add(manain);
    }

    @Override
    public void visitEnd() {
        List<BasicBlock> empty = new ArrayList<>();
        for (BasicBlock block : blocks) {
            block.owner = mn;
            if (block.isEmpty())
                empty.add(block);
        }
        blocks.removeAll(empty);
        Collections.sort(blocks, new Comparator<BasicBlock>() {
            public int compare(BasicBlock b1, BasicBlock b2) {
                return mn.instructions.indexOf(new LabelNode(b1.label)) - mn.instructions.indexOf(new LabelNode(b2.label));
            }
        });
        
        for (BasicBlock block : blocks) {
            block.setIndex(blocks.indexOf(block));
        }
        
        // SORT GOTOS
    	/*ArrayList<BasicBlock> ordered = new ArrayList<BasicBlock>();
    	
		for (int i = 0; i < blocks.size(); i++) {
			BasicBlock block = blocks.get(i);
			AbstractInsnNode last = block.last();
			if (last.opcode() == Opcodes.GOTO) {
				i = block.target.getIndex() - 1;
				block.instructions.remove(last);
			}
			ordered.add(block);
			if (ordered.size() == blocks.size()) {
				blocks = ordered;
				break;
			}

		}*/
		
		for (BasicBlock block : blocks) {
            block.setIndex(blocks.indexOf(block));
		}
		
		
		/*if(mn.owner.name.equals("f") && mn.name.startsWith("<")) {
			for(BasicBlock block : blocks) {
				System.out.print("BLOCK " + block.getIndex());
				if(block.target != null) {
					System.out.print(" -> " + block.target.getIndex());
				}
				System.out.println();
				
				for(AbstractInsnNode node : block.instructions) {
					StringUtil.print(node);
				}
				
				System.out.println();
			}
			
		}*/
		
        // END SORT
		graph = new SimpleDirectedGraph<BasicBlock, DefaultEdge>(DefaultEdge.class);
		
        for (BasicBlock block : blocks) {
            block.setIndex(blocks.indexOf(block));
            if (!graph.containsVertex(block))
                graph.addVertex(block);
            if (block.target != null && block.target != block) {
                if (!graph.containsVertex(block.target))
                    graph.addVertex(block.target);
                graph.addEdge(block, block.target);
            }
            if (block.next != null) {
                if (!graph.containsVertex(block.next))
                    graph.addVertex(block.next);
                graph.addEdge(block, block.next);
            }
        }
        
        /*for (BasicBlock block : graph) {
			instructions.addAll(block.instructions);
		}*/
    }
    
    public NodeTree tree() {
    	return TreeBuilder.build(mn, instructions);
    }
    
    public void showFrame(){
        JGraphXAdapter<BasicBlock, DefaultEdge> graph = new JGraphXAdapter<BasicBlock, DefaultEdge>(this.graph);
        graph.getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_NOLABEL, "1");
        JFrame frame = new JFrame();
        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        frame.getContentPane().add(graphComponent);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 320);
        frame.setVisible(true);
        graph.setAutoSizeCells(true);
        for (mxCell cell : graph.getVertexToCellMap().values()) {
            graph.getModel().setGeometry(cell, new mxGeometry(0, 0, 20, 20));
            graph.updateCellSize(cell);
        }
        graph.getModel().beginUpdate();
        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
        layout.execute(graph.getDefaultParent());
        graph.getModel().endUpdate();
        saveToPng(graphComponent);
    }
    
    private static void saveToPng(mxGraphComponent graphComponent) {
        try {
            Color bg = Color.white;
            BufferedImage image = mxCellRenderer.createBufferedImage(graphComponent.getGraph(), null, 1, bg, graphComponent.isAntiAlias(), null, graphComponent.getCanvas());
            ImageIO.write(image, "png", new File("graph_.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}