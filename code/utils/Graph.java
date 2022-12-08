package code.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class Graph {

  private Gson gson = new GsonBuilder().setPrettyPrinting().create();

  // graph properties
  private ArrayList<Vertex> vertices;
  private ArrayList<Edge> edges;
  private boolean directed_edges;

  private double maximum_flow = 0;

  public Graph(ArrayList<Vertex> vertices, ArrayList<Edge> edges, boolean directed_edges) {
    this.vertices = vertices;
    this.edges = edges;
    this.directed_edges = directed_edges;
  }

  public Graph(String filename) throws FileNotFoundException {

    // filepath is apparently compatible with both Windows and Unix systems
    String filepath = new File("").getAbsolutePath() + "/code/data/" + filename;

    // read data from json file with gson library
    this.vertices = readJsonData(filepath, "vertices",
        TypeToken.getParameterized(ArrayList.class, Vertex.class).getType());

    this.edges = readJsonData(filepath, "edges",
        TypeToken.getParameterized(ArrayList.class, Edge.class).getType());

    this.directed_edges = readJsonData(filepath, "directed_edges",
        TypeToken.getParameterized(Boolean.class).getType());
  }

  private <T> T readJsonData(String filepath, String key, Type type) throws FileNotFoundException {
    JsonElement json = gson.fromJson(new FileReader(filepath), JsonElement.class);
    JsonObject jsonObject = json.getAsJsonObject();
    return gson.fromJson(jsonObject.get(key), type);
  }

  // static helper functions

  public static ArrayList<Vertex> getNeighbors(
      Vertex vertex,
      ArrayList<Vertex> vertices,
      ArrayList<Edge> edges) {

    // neue Liste für nachbarn
    ArrayList<Vertex> neighbors = new ArrayList<Vertex>();

    for (Edge e : edges) {
      // Überspringe Kanten, die nicht mit dem Knoten verbunden sind
      if (!e.getSource().equals(vertex.getLabel()) && !e.getTarget().equals(vertex.getLabel()))
        continue;

      // Finde vertex v anhand der source/target, der mit u verbunden ist
      for (Vertex v : vertices) {
        if ((v.getLabel().equals(e.getTarget()) || v.getLabel().equals(e.getSource())) && !v.equals(vertex))
          neighbors.add(v);
      }
    }
    return neighbors;
  }

  public static Edge getEdgeBetweenTwoVertices(
      Vertex vertex1,
      Vertex vertex2,
      ArrayList<Edge> edges) {

    for (Edge e : edges) {
      // Prüfe ob die Kante zwischen den beiden Knoten liegt
      if ((e.getSource().equals(vertex1.getLabel()) &&
          e.getTarget().equals(vertex2.getLabel()))
          || (e.getSource().equals(vertex2.getLabel()) &&
              e.getTarget().equals(vertex1.getLabel())))
        return e;
    }
    return null;
  }

  public static ArrayList<Edge> getEdgesFromVertices(ArrayList<Vertex> vertices) {
    ArrayList<Edge> output = new ArrayList<Edge>();

    for (Vertex v : vertices) {
      // Überspringe root vertex, da kein Vorgänger vorhanden ist
      if (v.getPredecessor() == null)
        continue;

      // Erstelle Kante aus dem Vorgänger und dem aktuellen Knoten
      Edge edge = new Edge(v.getPredecessor().getLabel(), v.getLabel(), v.getValue());

      // eliminiere doppelte kanten
      for (Edge e : output) {
        if (e.getSource().equals(edge.getTarget()) && e.getTarget().equals(edge.getSource())) {
          // entferne die doppelte kante
          output.remove(e);
        }
      }
      output.add(edge);
    }
    return output;
  }

  public static ArrayList<Vertex> getAllPredeseccorsForVertex(Vertex vertex) {
    ArrayList<Vertex> output = new ArrayList<Vertex>();

    Vertex current = vertex;
    while (current.getPredecessor() != null) {
      output.add(current.getPredecessor());
      current = current.getPredecessor();
    }
    return output;
  }

  public static Vertex getSourceVertexFromEdge(Edge edge, ArrayList<Vertex> vertices) {
    for (Vertex v : vertices) {
      if (v.getLabel().equals(edge.getSource()))
        return v;
    }
    return null;
  }

  public static Vertex getTargetVertexFromEdge(Edge edge, ArrayList<Vertex> vertices) {
    for (Vertex v : vertices) {
      if (v.getLabel().equals(edge.getTarget()))
        return v;
    }
    return null;
  }

  public static ArrayList<Edge> getAdjacentEdges(String vertex, ArrayList<Edge> edges) {
    ArrayList<Edge> output = new ArrayList<Edge>();

    for (Edge e : edges) {
      if (e.getSource().equals(vertex) || e.getTarget().equals(vertex))
        output.add(e);
    }
    return output;
  }

  public static double getWeightSum(Vertex u, Vertex v, ArrayList<Edge> edges) {
    double sum = 0;
    for (Edge e : edges) {
      if ((e.getSource().equals(u.getLabel()) && e.getTarget().equals(v.getLabel()))
          || (e.getSource().equals(v.getLabel()) && e.getTarget().equals(u.getLabel()))) {
        sum += e.getWeight();
      }
    }
    return sum;
  }

  public static Vertex getVertexByLabel(ArrayList<Vertex> vertices, String target) {
    for (Vertex vertex : vertices) {
      if (vertex.getLabel().equals(target))
        return vertex;
    }
    return null;
  }

  public static ArrayList<Edge> getEdgesOfVertex(ArrayList<Edge> edges, Vertex current_vertex) {
    ArrayList<Edge> current_edges = new ArrayList<Edge>();
    for (Edge edge : edges)
      if (edge.getSource().equals(current_vertex.getLabel())) {
        current_edges.add(edge);
      }
    return current_edges;
  }

  public ArrayList<Vertex> getVertices() {
    return vertices;
  }

  public ArrayList<Edge> getEdges() {
    return edges;
  }

  public boolean isDirected() {
    return directed_edges;
  }

  public void setDirected(boolean directed) {
    this.directed_edges = directed;
  }

  public void setEdges(ArrayList<Edge> edges) {
    this.edges = edges;
  }

  public void setVertices(ArrayList<Vertex> vertices) {
    this.vertices = vertices;
  }

  public void setMaximumFlow(double maximumFlow) {
    this.maximum_flow = maximumFlow;
  }

  public String getVerticesCount() {
    return String.valueOf(vertices.size());
  }

  public String getEdgesCount() {
    return String.valueOf(edges.size());
  }

  public double getVerticesValue() {
    return vertices.stream().mapToDouble(Vertex::getValue).sum();
  }

  public double getEdgesWeight() {
    return edges.stream().mapToDouble(Edge::getWeight).sum();
  }

  public double getMaximumFlow() {
    return maximum_flow;
  }

  public Graph deepCopy() {
    ArrayList<Vertex> vertices = new ArrayList<Vertex>();
    ArrayList<Edge> edges = new ArrayList<Edge>();

    for (Vertex v : this.vertices) {
      vertices.add(new Vertex(v.getLabel(), v.getValue()));
    }

    for (Edge e : this.edges) {
      edges.add(new Edge(e.getSource(), e.getTarget(), e.getFlow(), e.getCapacity()));
    }

    return new Graph(vertices, edges, this.directed_edges);
  }

  public void addVertex(Vertex vertex) {
    this.vertices.add(vertex);
  }

  public void addEdge(Edge edge) {
    this.edges.add(edge);
  }
}
