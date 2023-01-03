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

public class JGraphPanel extends JPanel {

  private AdjazenzMatrix ad_matrix;

  @SuppressWarnings("deprecation")
  public JGraphPanel(String title, AdjazenzMatrix ad_matrix, String layout) {

    this.ad_matrix = ad_matrix;

    // determine edge type based on values set in graph data
    String edge_type;
    if (ad_matrix.getMaxFlow() != 0)
      edge_type = "flow";
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
    if (!ad_matrix.isDirected())
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
        + "Anzahl der Knoten: " + ad_matrix.getVerticesCount() + "<br>"
        + "Anzahl der Kanten: " + ad_matrix.getEdgesCount() + "<br>";

    switch (edge_type) {
      case "flow":
        html_content += "Maximaler Durchfluss: " + ad_matrix.getMaxFlow() + "<br>";
        break;
      case "weight":
        html_content += "Summe der Kantengewichte: " + ad_matrix.getEdgesWeight() + "<br>";
        break;
    }

    JLabel description = new JLabel("<html>" + html_content + "</html>");

    add(description, BorderLayout.NORTH);
    add(component, BorderLayout.CENTER);
  }

  public SimpleDirectedWeightedGraph<String, CustomEdge> buildGraph() {
    SimpleDirectedWeightedGraph<String, CustomEdge> g = new SimpleDirectedWeightedGraph<String, CustomEdge>(
        CustomEdge.class);

    char[] vertex_letters = ad_matrix.getVertexLetters();

    // add vertices
    for (char c : vertex_letters)
      g.addVertex("  " + c + "  ");

    // add edges
    int[][] matrix = ad_matrix.getMatrix();

    for (int i = 0; i < matrix.length; i++) {
      for (int j = 0; j < matrix[i].length; j++) {
        if (matrix[i][j] == 0)
          continue;

        String source = "  " + vertex_letters[j] + "  ";
        String target = "  " + vertex_letters[i] + "  ";

        // add edge with weight or flow
        g.addEdge(target, source, new CustomEdge("  " + matrix[i][j] + "  "));
      }
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
