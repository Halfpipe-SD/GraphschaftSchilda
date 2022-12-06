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

public class GraphData {

  private Gson gson = new GsonBuilder().setPrettyPrinting().create();

  // graph properties
  private ArrayList<GraphVertex> vertices;
  private ArrayList<GraphEdge> edges;
  private boolean directed_edges;

  public GraphData(ArrayList<GraphVertex> vertices, ArrayList<GraphEdge> edges, boolean directed_edges) {
    this.vertices = vertices;
    this.edges = edges;
    this.directed_edges = directed_edges;
  }

  public GraphData(String filename) throws FileNotFoundException {

    // filepath is apparently compatible with both Windows and Unix systems
    String filepath = new File("").getAbsolutePath() + "/code/data/" + filename;

    // read data from json file with gson library
    this.vertices = readJsonData(filepath, "vertices",
        TypeToken.getParameterized(ArrayList.class, GraphVertex.class).getType());

    this.edges = readJsonData(filepath, "edges",
        TypeToken.getParameterized(ArrayList.class, GraphEdge.class).getType());

    this.directed_edges = readJsonData(filepath, "directed_edges",
        TypeToken.getParameterized(Boolean.class).getType());
  }

  private <T> T readJsonData(String filepath, String key, Type type) throws FileNotFoundException {
    JsonElement json = gson.fromJson(new FileReader(filepath), JsonElement.class);
    JsonObject jsonObject = json.getAsJsonObject();
    return gson.fromJson(jsonObject.get(key), type);
  }

  // static helper functions

  public static ArrayList<GraphVertex> getNeighbors(
      GraphVertex vertex,
      ArrayList<GraphVertex> vertices,
      ArrayList<GraphEdge> edges) {

    // neue Liste für nachbarn
    ArrayList<GraphVertex> neighbors = new ArrayList<GraphVertex>();

    for (GraphEdge e : edges) {
      // Überspringe Kanten, die nicht mit dem Knoten verbunden sind
      if (!e.getSource().equals(vertex.getLabel()) && !e.getTarget().equals(vertex.getLabel()))
        continue;

      // Finde vertex v anhand der source/target, der mit u verbunden ist
      for (GraphVertex v : vertices) {
        if ((v.getLabel().equals(e.getTarget()) || v.getLabel().equals(e.getSource())) && !v.equals(vertex))
          neighbors.add(v);
      }
    }
    return neighbors;
  }

  public static GraphEdge getEdgeBetweenTwoVertices(
      GraphVertex vertex1,
      GraphVertex vertex2,
      ArrayList<GraphEdge> edges) {

    for (GraphEdge e : edges) {
      // Prüfe ob die Kante zwischen den beiden Knoten liegt
      if ((e.getSource().equals(vertex1.getLabel()) &&
          e.getTarget().equals(vertex2.getLabel()))
          || (e.getSource().equals(vertex2.getLabel()) &&
              e.getTarget().equals(vertex1.getLabel())))
        return e;
    }
    return null;
  }

  public static ArrayList<GraphEdge> getEdgesFromVertices(ArrayList<GraphVertex> vertices) {
    ArrayList<GraphEdge> output = new ArrayList<GraphEdge>();

    for (GraphVertex v : vertices) {
      // Überspringe root vertex, da kein Vorgänger vorhanden ist
      if (v.getPredecessor() == null)
        continue;

      // Erstelle Kante aus dem Vorgänger und dem aktuellen Knoten
      GraphEdge edge = new GraphEdge(v.getPredecessor().getLabel(), v.getLabel(), v.getValue());

      // eliminiere doppelte kanten
      for (GraphEdge e : output) {
        if (e.getSource().equals(edge.getTarget()) && e.getTarget().equals(edge.getSource())) {
          // entferne die doppelte kante
          output.remove(e);
        }
      }
      output.add(edge);
    }
    return output;
  }

  public static ArrayList<GraphVertex> getAllPredeseccorsForVertex(GraphVertex vertex) {
    ArrayList<GraphVertex> output = new ArrayList<GraphVertex>();

    GraphVertex current = vertex;
    while (current.getPredecessor() != null) {
      output.add(current.getPredecessor());
      current = current.getPredecessor();
    }
    return output;
  }

  public static GraphVertex getSourceVertexFromEdge(GraphEdge edge, ArrayList<GraphVertex> vertices) {
    for (GraphVertex v : vertices) {
      if (v.getLabel().equals(edge.getSource()))
        return v;
    }
    return null;
  }

  public static GraphVertex getTargetVertexFromEdge(GraphEdge edge, ArrayList<GraphVertex> vertices) {
    for (GraphVertex v : vertices) {
      if (v.getLabel().equals(edge.getTarget()))
        return v;
    }
    return null;
  }

  public static ArrayList<GraphEdge> getAdjacentEdges(String vertex, ArrayList<GraphEdge> edges) {
    ArrayList<GraphEdge> output = new ArrayList<GraphEdge>();

    for (GraphEdge e : edges) {
      if (e.getSource().equals(vertex) || e.getTarget().equals(vertex))
        output.add(e);
    }
    return output;
  }

  public static double getWeightSum(GraphVertex u, GraphVertex v, ArrayList<GraphEdge> edges) {
    double sum = 0;
    for (GraphEdge e : edges) {
      if ((e.getSource().equals(u.getLabel()) && e.getTarget().equals(v.getLabel()))
          || (e.getSource().equals(v.getLabel()) && e.getTarget().equals(u.getLabel()))) {
        sum += e.getWeight();
      }
    }
    return sum;
  }

  public ArrayList<GraphVertex> getVertices() {
    return vertices;
  }

  public ArrayList<GraphEdge> getEdges() {
    return edges;
  }

  public boolean isDirected() {
    return directed_edges;
  }

  public void setDirected(boolean directed) {
    this.directed_edges = directed;
  }

  public void setEdges(ArrayList<GraphEdge> edges) {
    this.edges = edges;
  }

  public void setVertices(ArrayList<GraphVertex> vertices) {
    this.vertices = vertices;
  }

  public String getVerticesCount() {
    return String.valueOf(vertices.size());
  }

  public String getEdgesCount() {
    return String.valueOf(edges.size());
  }

  public double getVerticesValue() {
    return vertices.stream().mapToDouble(GraphVertex::getValue).sum();
  }

  public double getEdgesWeight() {
    return edges.stream().mapToDouble(GraphEdge::getWeight).sum();
  }
}
