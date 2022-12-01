package code.utils;

import java.awt.Dimension;

import javax.swing.JPanel;

import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;

public class JGraphPanel extends JPanel {

  private GraphData graph_data;

  public JGraphPanel(Dimension size, GraphData graph_data) {

    this.graph_data = graph_data;

    SimpleDirectedWeightedGraph<String, WeightedEdgeLabel> g = buildGraph();
    JGraphXAdapter<String, WeightedEdgeLabel> graphAdapter = new JGraphXAdapter<String, WeightedEdgeLabel>(g);

    mxHierarchicalLayout layout = new mxHierarchicalLayout(graphAdapter);

    layout.execute(graphAdapter.getDefaultParent());

    mxGraphComponent component = new mxGraphComponent(graphAdapter);

    // set scale of graph
    component.zoomAndCenter();

    // set size from constructor parameter
    component.setPreferredSize(size);

    // disable creating new edges
    component.setConnectable(false);

    // disable connecting edges to nothing
    component.getGraph().setAllowDanglingEdges(false);

    add(component);
  }

  public SimpleDirectedWeightedGraph<String, WeightedEdgeLabel> buildGraph() {
    SimpleDirectedWeightedGraph<String, WeightedEdgeLabel> g = new SimpleDirectedWeightedGraph<String, WeightedEdgeLabel>(
        WeightedEdgeLabel.class);

    // add vertices from graph data
    for (GraphVertex v : graph_data.getVertices()) {
      g.addVertex(v.getLabel());
    }

    // add edges from graph data
    for (GraphEdge e : graph_data.getEdges()) {
      WeightedEdgeLabel edge = g.addEdge(e.getSource(), e.getTarget());
      g.setEdgeWeight(edge, e.getWeight());
    }

    return g;
  }

  public static class WeightedEdgeLabel extends DefaultWeightedEdge {
    @Override
    public String toString() {
      return String.valueOf(getWeight());
    }
  }

}
