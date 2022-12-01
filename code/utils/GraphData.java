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

    String filepath = new File("").getAbsolutePath() + "\\code\\data\\" + filename;

    // read data from json file with gson library
    this.vertices = readJsonData(filepath, "vertices",
        TypeToken.getParameterized(ArrayList.class, GraphVertex.class).getType());

    this.edges = readJsonData(filepath, "edges",
        TypeToken.getParameterized(ArrayList.class, GraphEdge.class).getType());

    this.directed_edges = readJsonData(filepath, "directed_edges",
        TypeToken.getParameterized(Boolean.class).getType());
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

  private <T> T readJsonData(String filepath, String key, Type type) throws FileNotFoundException {
    JsonElement json = gson.fromJson(new FileReader(filepath), JsonElement.class);
    JsonObject jsonObject = json.getAsJsonObject();
    return gson.fromJson(jsonObject.get(key), type);
  }

}
