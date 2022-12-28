package code.utils;

import java.awt.BorderLayout;
import java.util.Collection;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

import code.old.Edge;
import code.old.Graph;
import code.old.Vertex;

public class JGraphPanel extends JPanel {

  private Graph graph_data;

  private String edge_type;

  @SuppressWarnings("deprecation")
  public JGraphPanel(String title, Graph graph_data, String layout) {

    this.graph_data = graph_data;

    // determine edge type based on values set in graph data
    if (graph_data.getEdges().get(0).getFlow() != 0)
      edge_type = "flow";
    else if (graph_data.getEdges().get(0).getCapacity() != 0)
      edge_type = "capacity";
    else
      edge_type = "weight";

    // generate graph with directed or undrected edges
    SimpleDirectedWeightedGraph<String, CustomEdge> graph = buildGraph();
    // initialize jgraphx adapter
    JGraphXAdapter<String, CustomEdge> graphAdapter = new JGraphXAdapter<String, CustomEdge>(graph);

    mxGraphLayout graph_layout = null;

    switch (layout) {
      case "hierarchical":
        graph_layout = new mxHierarchicalLayout(graphAdapter);
        break;
      case "circle":
        graph_layout = new mxCircleLayout(graphAdapter);
        break;
      default:
        graph_layout = new mxHierarchicalLayout(graphAdapter);
        break;
    }

    // initialize graph layout, execute graph on layout and mx graph component
    graph_layout.execute(graphAdapter.getDefaultParent());
    mxGraphComponent component = new mxGraphComponent(graphAdapter);

    // adjust graph style
    mxGraphModel graphModel = (mxGraphModel) component.getGraph().getModel();
    Collection<Object> cells = graphModel.getCells().values();

    mxUtils.setCellStyles(graphModel, cells.toArray(), mxConstants.STYLE_FONTCOLOR, "#000000");
    mxUtils.setCellStyles(graphModel, cells.toArray(), mxConstants.STYLE_FONTSTYLE, "1");

    // ignore graph directions (arrows)
    if (!graph_data.isDirected())
      mxUtils.setCellStyles(graphModel, cells.toArray(), mxConstants.STYLE_ENDARROW, mxConstants.NONE);

    // set new geometry
    component.getGraph().getModel().setGeometry(component.getGraph().getModel().getRoot(),
        new mxGeometry(10, 10, 0, 0));

    // disable creating new edges
    component.setConnectable(false);

    // disable connecting edges to nothing
    component.getGraph().setAllowDanglingEdges(false);

    // set layout of jpanel
    setLayout(new BorderLayout());

    // set graph description
    String html_content = "<h3>" + title + "</h3>"
        + "Anzahl der Knoten: " + graph_data.getVerticesCount() + "<br>"
        + "Anzahl der Kanten: " + graph_data.getEdgesCount() + "<br>";

    switch (edge_type) {
      case "flow":
      case "capacity":
        html_content += "Maximaler Durchfluss: " + graph_data.getMaximumFlow() + "<br>";
        break;
      case "weight":
        html_content += "Summe der Kantengewichte: " + graph_data.getEdgesWeight() + "<br>";
        break;
    }

    JLabel description = new JLabel("<html>" + html_content + "</html>");

    add(description, BorderLayout.NORTH);
    add(component, BorderLayout.CENTER);
  }

  public SimpleDirectedWeightedGraph<String, CustomEdge> buildGraph() {
    SimpleDirectedWeightedGraph<String, CustomEdge> g = new SimpleDirectedWeightedGraph<String, CustomEdge>(
        CustomEdge.class);

    // add vertices from graph data
    for (Vertex v : graph_data.getVertices()) {
      g.addVertex(v.getLabel());
    }

    // add edges from graph data
    for (Edge e : graph_data.getEdges()) {

      String edge_label = "";

      switch (edge_type) {
        case "flow":
        case "capacity":
          edge_label = Math.round(e.getFlow()) + "/" + Math.round(e.getCapacity());
          break;
        case "weight":
          edge_label = Double.toString(e.getWeight());
          break;
      }

      g.addEdge(e.getSource(), e.getTarget(), new CustomEdge(edge_label));
    }

    return g;
  }

  class CustomEdge extends DefaultEdge {

    private String label;

    public CustomEdge(String label) {

      this.label = label;
    }

    public String getLabel() {
      return label;
    }

    @Override
    public String toString() {
      return label;
    }
  }

}
