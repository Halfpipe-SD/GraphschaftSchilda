package code.problem1;

import java.util.ArrayList;
import code.utils.GraphData;
import code.utils.GraphVertex;
import code.utils.GraphEdge;

public class Alrogithm {

  private ArrayList<GraphVertex> vertices;
  private ArrayList<GraphEdge> edges;

  // initialize vertices and edges from graph data
  public Alrogithm(GraphData data) {
    this.vertices = data.getVertices();
    this.edges = data.getEdges();
  }

  public GraphData get_output_graph() {

    return new GraphData(this.vertices, this.edges, false);
  }
}
