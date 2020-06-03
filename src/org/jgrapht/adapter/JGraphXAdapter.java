package org.jgrapht.adapter;

import java.util.HashMap;

import org.jgrapht.Graph;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.view.mxGraph;

public class JGraphXAdapter<V, E> extends mxGraph {

    private Graph<V, E> graphT;

    private HashMap<V, mxCell> vertexToCellMap = new HashMap<V, mxCell>();

    private HashMap<E, mxCell> edgeToCellMap = new HashMap<E, mxCell>();

    private HashMap<mxCell, V> cellToVertexMap = new HashMap<mxCell, V>();

    private HashMap<mxCell, E> cellToEdgeMap = new HashMap<mxCell, E>();

    /*
     * CONSTRUCTOR
     */

    public JGraphXAdapter(final Graph<V, E> graphT) {
        super();
        this.graphT = graphT;
        insertJGraphT(graphT);
    }

    /*
     * METHODS
     */

    public void addJGraphTVertex(V vertex) {

        getModel().beginUpdate();

        try {
            mxCell cell = new mxCell(vertex);
            cell.setVertex(true);
            cell.setId(null);
            addCell(cell, defaultParent);
            vertexToCellMap.put(vertex, cell);
            cellToVertexMap.put(cell, vertex);
        } finally {
            getModel().endUpdate();
        }
    }

    public void addJGraphTEdge(E edge) {

        getModel().beginUpdate();

        try {
            V source = graphT.getEdgeSource(edge);
            V target = graphT.getEdgeTarget(edge);
            mxCell cell = new mxCell(edge);
            cell.setEdge(true);
            cell.setId(null);
            cell.setGeometry(new mxGeometry());
            cell.getGeometry().setRelative(true);
            addEdge(cell, defaultParent, vertexToCellMap.get(source), vertexToCellMap.get(target), null);
            edgeToCellMap.put(edge, cell);
            cellToEdgeMap.put(cell, edge);
        } finally {
            getModel().endUpdate();
        }
    }

    public HashMap<V, mxCell> getVertexToCellMap() {
        return vertexToCellMap;
    }

    public HashMap<E, mxCell> getEdgeToCellMap() {
        return edgeToCellMap;
    }

    public HashMap<mxCell, E> getCellToEdgeMap() {
        return cellToEdgeMap;
    }

    public HashMap<mxCell, V> getCellToVertexMap() {
        return cellToVertexMap;
    }

    /*
     * GRAPH LISTENER
     */

    /*
     * PRIVATE METHODS
     */

    private void insertJGraphT(Graph<V, E> graphT) {
        getModel().beginUpdate();
        try {
            for (V vertex : graphT.vertexSet()) {
                addJGraphTVertex(vertex);
            }
            for (E edge : graphT.edgeSet()) {
                addJGraphTEdge(edge);
            }
        } finally {
            getModel().endUpdate();
        }

    }
}