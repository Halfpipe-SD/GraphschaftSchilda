package code.problem6;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import code.utils.BasicWindow;
import code.utils.GraphData;
import code.utils.GraphEdge;
import code.utils.GraphVertex;
import code.utils.JGraphPanel;

public class Problem6 extends BasicWindow {

  public Problem6(String title) throws FileNotFoundException {
    super(title);

    setSize(new Dimension(500, 520));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    GraphData graph_input = new GraphData("town_traffic.json");
    GraphData graph_output = graph_input.deepCopy();

    double maximum_flow = fordFulkersonMaxFlow(graph_output);
    System.out.println("Maximum flow: " + maximum_flow);

    // entferne alle inversen kanten für den output des graphen
    ArrayList<GraphEdge> non_inverse = graph_output.getEdges();
    non_inverse.removeIf(e -> e.getCapacity() == 0);
    graph_output.setEdges(non_inverse);

    JGraphPanel p1 = new JGraphPanel("Rohdaten", graph_input, "hierarchical");
    JGraphPanel p2 = new JGraphPanel("Flussnetzwerk mit Ford-Fulkerson", graph_output, "hierarchical");

    add(p1);
    add(p2);
  }

  private double fordFulkersonMaxFlow(GraphData input) {

    // initialize the residual graph to the original graph
    GraphData residual_graph = new GraphData(input.getVertices(), input.getEdges(), true);

    // initialize the flow to 0
    double flow = 0;

    // while there is an augmenting path
    while (true) {

      // find an augmenting path
      ArrayList<GraphEdge> augmenting_path = findAugmentingPaths(residual_graph);

      // if there is no augmenting path, the algorithm terminates
      if (augmenting_path == null)
        break;

      // find the bottleneck of the augmenting path
      double bottleneck = findBottleneck(residual_graph, augmenting_path);
      System.out.println("Bottleneck: " + bottleneck);
      // update the residual graph
      residual_graph = updateResidualGraph(residual_graph, augmenting_path, bottleneck, input.getVertices());

      // print residual path
      System.out.println("Residual path: ");
      for (GraphEdge e : residual_graph.getEdges()) {
        System.out.println(
            "-> " + e.getFlow() + "/" + e.getCapacity() + "\t (" + e.getSource() + " -> "
                + e.getTarget() + ") ");
      }

      // update the flow
      flow += bottleneck;
    }

    // set maximum flow to graph data
    input.setMaximumFlow(flow);

    return flow;
  }

  private GraphData updateResidualGraph(GraphData residual_graph, ArrayList<GraphEdge> augmenting_path,
      double bottleneck, ArrayList<GraphVertex> vertices) {

    // iterate over the edges of the augmenting path
    for (GraphEdge edge : augmenting_path) {
      // get the source and target vertex of the edge
      GraphVertex source = GraphData.getVertexByLabel(vertices, edge.getSource());
      GraphVertex target = GraphData.getVertexByLabel(vertices, edge.getTarget());

      // update the flow of the edge
      edge.setFlow(edge.getFlow() + bottleneck);

      // check if the reverse edge is already in the residual graph
      // and update the flow of the reverse edge if yes
      boolean is_reverse_edge_in_graph = false;
      for (GraphEdge reverse_edge : GraphData.getEdgesOfVertex(residual_graph.getEdges(), target)) {
        if (!reverse_edge.getTarget().equals(source.getLabel()))
          break;
        reverse_edge.setFlow(reverse_edge.getFlow() - bottleneck);
        is_reverse_edge_in_graph = true;
      }

      // add the reverse edge to the residual graph
      if (!is_reverse_edge_in_graph)
        residual_graph.addEdge(new GraphEdge(target.getLabel(), source.getLabel(), -edge.getFlow(), 0));
    }

    return residual_graph;
  }

  private double findBottleneck(GraphData residual_graph, ArrayList<GraphEdge> augmenting_path) {

    // initialize the bottleneck
    double bottleneck = Double.MAX_VALUE;

    // iterate over the edges of the augmenting path
    for (GraphEdge edge : augmenting_path) {
      // if the edge is in the residual graph and update the bottleneck
      if (residual_graph.getEdges().contains(edge))
        bottleneck = Math.min(bottleneck, edge.getCapacity() - edge.getFlow());
    }

    return bottleneck;
  }

  private ArrayList<GraphEdge> findAugmentingPaths(GraphData residual_graph) {

    ArrayList<GraphEdge> path = new ArrayList<GraphEdge>();
    ArrayList<GraphEdge> augmenting_path = new ArrayList<GraphEdge>();

    // initialize the source and sink vertex
    GraphVertex source = GraphData.getVertexByLabel(residual_graph.getVertices(), "Source");
    GraphVertex sink = GraphData.getVertexByLabel(residual_graph.getVertices(), "Sink");

    // initialize the stack
    Stack<GraphVertex> stack = new Stack<GraphVertex>();

    // Push root in our stack
    stack.push(source);
    // set all other vertices as not visited
    for (GraphVertex vertex : residual_graph.getVertices()) {
      if (vertex != source)
        vertex.setVisited(false);
    }

    // While stack is not empty
    while (!stack.isEmpty()) {
      // Pop current node
      GraphVertex current_vertex = stack.pop();

      // Push right child, then left child to stack
      for (GraphEdge edge : GraphData.getEdgesOfVertex(residual_graph.getEdges(), current_vertex)) {
        GraphVertex target_vertex = GraphData.getVertexByLabel(residual_graph.getVertices(), edge.getTarget());

        if (!target_vertex.isVisited() && edge.getCapacity() - edge.getFlow() > 0) {
          target_vertex.setVisited(true);

          // only add edge to path if it is not a reverse edge with target source
          if (!edge.getTarget().equals(source.getLabel()))
            path.add(edge);

          if (target_vertex.equals(sink))
            augmenting_path.addAll(path);

          stack.push(target_vertex);
        }
      }
    }

    // generate new path from sink to source
    ArrayList<GraphEdge> new_path = new ArrayList<GraphEdge>();

    if (augmenting_path.size() == 0)
      return null;

    // add edge to sink from augmenting_path to new path
    GraphEdge edge_to_sink = null;

    for (GraphEdge edge : augmenting_path) {
      if (edge.getTarget().equals(sink.getLabel()))
        edge_to_sink = edge;
    }
    new_path.add(edge_to_sink);

    GraphEdge last_added = edge_to_sink;
    boolean keep_going = true;

    while (keep_going) {
      for (GraphEdge edge : augmenting_path) {
        if (edge.getTarget().equals(last_added.getSource())) {
          new_path.add(edge);
          last_added = edge;
          if (last_added.getSource().equals(source.getLabel()))
            keep_going = false;
        }
      }
    }

    Collections.reverse(new_path);

    return new_path;
  }
}