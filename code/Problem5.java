package code;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import java.util.PriorityQueue;

import code.utils.AdjazenzMatrix;
import code.utils.BasicWindow;
import code.utils.Edge;
import code.utils.FileHandler;
import code.utils.JGraphPanel;
import code.utils.Vertex;

public class Problem5 extends BasicWindow {

  public Problem5(String title) throws FileNotFoundException, IOException {
    super(title);

    setSize(new Dimension(510, 650));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);


    // Initializiere FileHandler und lese die Daten aus der Datei
    FileHandler fh = new FileHandler("problem1.txt");

    // Erstelle die Adjazenzmatrix und gebe sie in der Konsole aus
    AdjazenzMatrix am_input = new AdjazenzMatrix("Input", fh.getMatrix(), fh.getVertexLetters(), false);
    am_input.printMatrix();

    // Erstelle die Ausgabe-Adjazenzmatrix mit dem Prim Algorithmus
    int[][] matrix_output = hierholzer(am_input);

    // Erstelle die Ausgabe-Adjazenzmatrix, gebe sie in der Konsole aus und schreibe
    // sie in eine Datei
    AdjazenzMatrix am_output = new AdjazenzMatrix("Output", matrix_output, fh.getVertexLetters(), false);
    am_output.printAndWriteMatrix(title);

    // Erstelle die Graphen und füge sie dem Fenster hinzu
    JGraphPanel p1 = new JGraphPanel("Rohdaten", am_input, "hierarchical");
    JGraphPanel p2 = new JGraphPanel("Minimum Spanning Tree (Prim)", am_output,
        "circle");

    add(p1);
    add(p2);
  }

  private int[][] hierholzer(AdjazenzMatrix input) {
    int[][] matrix = input.getMatrixCopy();
    char[] vertexLetters = input.getVertexLetters();

    ArrayList<Vertex> vertices = new ArrayList<>();
    ArrayList<Edge> edges = getEdges(matrix, vertexLetters);

    // Generiere eine Liste aller Knoten mit dem Wert unendlich und ohne Vorgänger
    for (int i = 0; i < matrix.length; i++)
      vertices.add(new Vertex(vertexLetters[i], Integer.MAX_VALUE, null));

    // Starte mit beliebigem Startknoten, Startknoten bekommt den Wert 0
    vertices.get(0).setKey(0);

    // Speichere alle Knoten in einer geeigneten Datenstruktur Q
    // -> Prioritätswarteschlange
    PriorityQueue<Vertex> q = new PriorityQueue<>(Comparator.comparingInt(Vertex::getKey));
    q.addAll(vertices);

    // Solange es noch Knoten in Q gibt...
    while (!q.isEmpty()) {
      // Entnehme den Knoten mit dem kleinsten Wert
      Vertex u = q.poll();

      // Für jeden Nachbarn n von u
      for (Vertex n : getNeighbors(u, vertices, edges)) {
        // Finde die Kante (u, n)
        Edge e = null;
        for (Edge edge : edges)
          if ((edge.getSource() == u.getLetter() && edge.getTarget() == n.getLetter())
              || (edge.getSource() == n.getLetter() && edge.getTarget() == u.getLetter()))
            e = edge;
        System.out.println(e);

        // // hierholzers algorithmus with edge e

        // // Maintain a stack to keep vertices
        // Stack<Integer> curr_path = new Stack<>();

        // // vector to store final circuit
        // List<Integer> circuit = new ArrayList<>();

        // // start from any vertex
        // curr_path.push(0);
        // int curr_v = 0; // Current vertex
        // while (curr_path.size() != 0) {
        // // If there's remaining edge
        // if (edge_count.get(curr_v) != 0) {
        // // Push the vertex
        // curr_path.push(curr_v);

        // // Find the next vertex using an edge
        // int next_v = e.get(curr_v).get(e.get(curr_v).size() - 1);

        // // and remove that edge
        // edge_count.put(curr_v,
        // edge_count.get(curr_v) - 1);
        // e.get(curr_v).remove(
        // e.get(curr_v).size() - 1);

        // // Move to next vertex
        // curr_v = next_v;
        // }
        // // back-track to find remaining circuit
        // else {
        // circuit.add(curr_v);

        // // Back-tracking
        // curr_v = curr_path.pop();
        // }
        // }

        // // we've got the circuit, now print it in reverse
        // for (int i = circuit.size() - 1; i >= 0; i--) {
        // System.out.print(circuit.get(i));
        // if (i != 0)
        // System.out.print(" -> ");
        // }
        // }

        // Erstelle die Adjazenzmatrix für den Minimum Spanning Tree
        int[][] matrix_output = new int[matrix.length][matrix.length];
        for (Vertex v : vertices) {
          if (v.getPredecessor() == null)
            continue;

          int i = v.getLetter() - 'A';
          int j = v.getPredecessor().getLetter() - 'A';
          matrix_output[i][j] = matrix[i][j];
          matrix_output[j][i] = matrix[j][i];
        }
        return matrix_output;
      }
    }
    return matrix;
  }

  private ArrayList<Vertex> getNeighbors(Vertex u, ArrayList<Vertex> vertices, ArrayList<Edge> edges) {
    // neue Liste für nachbarn
    ArrayList<Vertex> neighbors = new ArrayList<Vertex>();

    for (Edge e : edges) {
      // Überspringe Kanten, die nicht mit dem Knoten verbunden sind
      if (e.getSource() != u.getLetter() && e.getTarget() != u.getLetter())
        continue;
      // Finde vertex v anhand der source/target, der mit u verbunden ist und füge ihn
      // der Liste hinzu
      for (Vertex v : vertices)
        if ((v.getLetter() == e.getSource() || v.getLetter() == e.getTarget()) && v != u)
          neighbors.add(v);
    }
    return neighbors;
  }

  private ArrayList<Edge> getEdges(int[][] matrix, char[] vertexLetters) {
    ArrayList<Edge> edges = new ArrayList<>();

    // Iteriere über die Adjazenzmatrix und füge alle Kanten der Liste hinzu
    for (int i = 0; i < matrix.length; i++)
      for (int j = 0; j < matrix.length; j++)
        if (matrix[i][j] != 0)
          edges.add(new Edge(vertexLetters[i], vertexLetters[j], matrix[i][j]));

    return edges;
  }

}