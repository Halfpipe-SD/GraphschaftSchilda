package code;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JLabel;

import code.utils.AdjazenzMatrix;
import code.utils.BasicWindow;
import code.utils.FileHandler;
import code.utils.JGraphPanel;

public class Problem5 extends BasicWindow {

  public Problem5(String title) throws FileNotFoundException, IOException {
    super(title);

    setSize(new Dimension(600, 400));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    // Initializiere FileHandler und lese die Daten aus der Datei
    FileHandler fh = new FileHandler("problem5.txt");

    // Erstelle die Adjazenzmatrix und gebe sie in der Konsole aus
    AdjazenzMatrix am_input = new AdjazenzMatrix("Input", fh.getMatrix(), fh.getVertexLetters(), false);
    am_input.printMatrix();

    int[][] mirrored = mirrorMatrix(am_input.getMatrix());

    boolean eulerGraph = isEulerGraph(mirrored);

    if (!eulerGraph) {
      System.out.println("Graph ist kein Eulergraph");
      return;
    } else {
      System.out.println("Graph ist ein Eulergraph");

      // Erstelle die Ausgabe-Adjazenzmatrix mit dem Hierholzer-Algorithmus
      ArrayList<Character> tour = hierholzerEulerTour(mirrored, am_input.getVertexLetters());

      System.out.println("\nEulerkreis: ");
      System.out.println(tour);

      // Schreibe tour in Datei
      fh.writeCustomToFile(title, tour.toString());

      // Erstelle den Graphen und füge ihn dem Fenster hinzu
      JGraphPanel p1 = new JGraphPanel("Rohdaten", am_input, "circle");

      add(p1);

      String info = "<br>"
          + "<h3>Weg des Postboten:" + "<br><br>";

      for (int i = 0; i < tour.size(); i++) {
        info += tour.get(i);
        if (i < tour.size() - 1)
          info += " ➡️ ";
      }
      info += "</h3>";

      add(new JLabel("<html>" + info + "</html>"));
    }
  }

  /**
   * Laufzeit: O(V^2)
   * 
   * @param matrix
   * @param vertexLetters
   * @return
   */

  // Wenn alle Knotengrade gerade sind, dann ist der Graph ein Eulergraph
  private boolean isEulerGraph(int[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
      int degree = 0;
      for (int j = 0; j < matrix.length; j++) {
        if (matrix[i][j] == 1)
          degree++;
      }
      if (degree % 2 != 0)
        return false;
    }
    return true;
  }

  private ArrayList<Character> hierholzerEulerTour(int[][] matrix, char[] vertexLetters) {
    // ArrayList, um den Pfad zu speichern
    ArrayList<Character> path = new ArrayList<>();
    // Stack, um die nächsten Knoten zu speichern
    Stack<Character> stack = new Stack<>();

    // Erster Knoten als Startpunkt
    char start = vertexLetters[0];
    stack.push(start);

    // Solange der Stack nicht leer ist
    while (!stack.isEmpty()) {
      // Hole aktuellen Knoten aus dem stack
      char current = stack.peek();

      // Gehe alle Nachbarn durch
      boolean foundNext = false;
      for (int i = 0; i < matrix.length; i++) {
        // Index des aktuellen Knotens
        int index = current - vertexLetters[0];

        // Wenn der Knoten keinen Nachbarn hat, überspringe ihn
        if (matrix[index][i] != 1)
          continue;

        // füge den Nachbarn dem Pfad/Stack hinzu
        stack.push(vertexLetters[i]);

        // Entferne die Kante zwischen dem aktuellen und nächsten Knoten
        matrix[index][i] = 0;
        matrix[i][index] = 0;
        foundNext = true;
        break;
      }
      // Wenn kein weiterer Nachbar gefunden wurde, entferne den aktuellen Knoten vom
      // Stack und füge ihn dem Pfad hinzu
      if (!foundNext)
        path.add(0, stack.pop());
    }
    return path;
  }

  private int[][] mirrorMatrix(int[][] matrix) {
    int[][] mirrored = new int[matrix.length][matrix.length];
    for (int i = 0; i < matrix.length; i++)
      for (int j = 0; j < matrix.length; j++)
        if (i < j)
          mirrored[i][j] = matrix[j][i];
        else
          mirrored[i][j] = matrix[i][j];
    return mirrored;
  }
}