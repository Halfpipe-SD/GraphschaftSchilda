package code;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

import code.utils.AdjazenzMatrix;
import code.utils.BasicWindow;
import code.utils.FileHandler;
import code.utils.JGraphPanel;

public class Problem5 extends BasicWindow {

  // private int max_flow = 0;
  // private int num_vertices = 0;

  public Problem5(String title) throws FileNotFoundException, IOException {
    super(title);

    // layout settings
    setSize(new Dimension(510, 460));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    // Initializiere FileHandler und lese die Daten aus der Datei
    FileHandler fh = new FileHandler("problem5.txt");

    // Erstelle die Adjazenzmatrix und gebe sie in der Konsole aus
    AdjazenzMatrix am_input = new AdjazenzMatrix("Input", fh.getMatrix(), fh.getVertexLetters(), true);
    am_input.printMatrix();

    // Erstelle die Ausgabe-Adjazenzmatrix mit dem Prim Algorithmus
    int[][] matrix_output = null;// fordFulkerson(am_input.getMatrixCopy());

    // Erstelle die Ausgabe-Adjazenzmatrix, gebe sie in der Konsole aus und schreibe
    // sie in eine Datei
    AdjazenzMatrix am_output = new AdjazenzMatrix("Output", matrix_output, fh.getVertexLetters(), true);
    am_output.printAndWriteMatrix(title);

    // Erstelle die Graphen und füge sie dem Fenster hinzu
    JGraphPanel p1 = new JGraphPanel("Rohdaten", am_input, "hierarchical");
    JGraphPanel p2 = new JGraphPanel("Ausgabe", am_output,
        "hierarchical");

    add(p1);
    add(p2);
  }

}