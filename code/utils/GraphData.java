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

  private ArrayList<Vertex> vertices;
  private ArrayList<Edge> edges;
  private boolean directed_edges;

  public GraphData(ArrayList<Vertex> vertices, ArrayList<Edge> edges, boolean directed_edges) {
    this.vertices = vertices;
    this.edges = edges;
    this.directed_edges = directed_edges;
  }

  public GraphData(String filename) throws FileNotFoundException {

    String filepath = new File("").getAbsolutePath() + "\\code\\data\\" + filename;

    // read data from json file with gson library
    this.vertices = readVertices(filepath);
    this.edges = readEdges(filepath);
    this.directed_edges = readDirected(filepath);

    System.out.println(this.vertices);
    System.out.println(this.edges);
    System.out.println(this.directed_edges);
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

  private ArrayList<Vertex> readVertices(String filepath) throws FileNotFoundException {
    FileReader fr = new FileReader(filepath);
    JsonObject json = gson.fromJson(fr, JsonObject.class);
    JsonElement json_element = json.get("vertices");

    // Define the type of the object to be read from the json file
    Type t = new TypeToken<ArrayList<Vertex>>() {
    }.getType();

    return gson.fromJson(json_element, t);
  }

  private ArrayList<Edge> readEdges(String filepath) throws FileNotFoundException {
    FileReader fr = new FileReader(filepath);
    JsonObject json = gson.fromJson(fr, JsonObject.class);
    JsonElement json_element = json.get("edges");

    // Define the type of the object to be read from the json file
    Type t = new TypeToken<ArrayList<Edge>>() {
    }.getType();

    return gson.fromJson(json_element, t);
  }

  private boolean readDirected(String filepath) throws FileNotFoundException {
    FileReader fr = new FileReader(filepath);
    JsonObject json = gson.fromJson(fr, JsonObject.class);
    JsonElement json_element = json.get("directed_edges");

    // Define the type of the object to be read from the json file
    Type t = new TypeToken<Boolean>() {
    }.getType();

    return gson.fromJson(json_element, t);
  }

}
