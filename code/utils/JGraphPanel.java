package code.utils;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

public class JGraphPanel extends JPanel {

  private GraphData graph_data;

  @SuppressWarnings("deprecation")
  public JGraphPanel(Dimension size, GraphData graph_data) {

    this.graph_data = graph_data;

    // generate graph with directed or undrected edges
    SimpleDirectedWeightedGraph<String, WeightedEdgeLabel> graph = buildGraph();
    // initialize jgraphx adapter
    JGraphXAdapter<String, WeightedEdgeLabel> graphAdapter = new JGraphXAdapter<String, WeightedEdgeLabel>(graph);

    // initialize graph layout, execute graph on layout and mx graph component
    mxHierarchicalLayout layout = new mxHierarchicalLayout(graphAdapter);
    layout.execute(graphAdapter.getDefaultParent());
    mxGraphComponent component = new mxGraphComponent(graphAdapter);

    // ignore graph directions (arrows)
    if (!graph_data.isDirected()) {
      mxGraphModel graphModel = (mxGraphModel) component.getGraph().getModel();
      Collection<Object> cells = graphModel.getCells().values();
      mxUtils.setCellStyles(graphModel, cells.toArray(), mxConstants.STYLE_ENDARROW, mxConstants.NONE);
    }

    // TODO fix spacing or scale of graph

    // set size from constructor parameter
    component.setPreferredSize(size);

    // disable creating new edges
    component.setConnectable(false);

    // disable connecting edges to nothing
    component.getGraph().setAllowDanglingEdges(false);

    // set layout of jpanel
    setLayout(new BorderLayout());

    // set graph description
    String html_content = "<html><br>"
        + "Anzahl der Knoten: " + graph_data.getVerticesCount() + "<br>"
        + "Anzahl der Kanten: " + graph_data.getEdgesCount() + "<br>"
        + "Summe der Kantengewichte: " + graph_data.getEdgesWeight() + "<br>"
        + "</html>";

    JLabel description = new JLabel("<html>" + html_content + "</html>");

    add(description, BorderLayout.NORTH);
    add(component, BorderLayout.CENTER);
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
