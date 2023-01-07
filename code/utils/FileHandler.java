package code.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FileHandler {

  private int[][] matrix;
  private char[] vertex_letters;

  private final static String absolute_path = new File("").getAbsolutePath();
  private final static String path_input = absolute_path + "/code/data/";
  private final static String path_output = absolute_path + "/code/output/";

  public FileHandler(String filename) throws FileNotFoundException, IOException {
    initialize(filename);
  }

  /**
   * Laufzeit O(n) mit n = Anzahl der Zeichen der Datei
   */
  private void initialize(String filename) throws FileNotFoundException, IOException {

    // read data from txt file with filereader
    FileReader fr = new FileReader(path_input + filename);
    BufferedReader br = new BufferedReader(fr);
    try {
      // get number of vertices in first line
      String firstline = br.readLine().trim();
      int vertices = firstline.split(" ").length;

      // put vertex letters in char array
      vertex_letters = Arrays.stream(firstline.split(" "))
          .collect(Collectors.joining())
          .toCharArray();

      System.out.println("Read file: " + filename);

      matrix = new int[vertices][vertices];

      String line;
      int linecount = 0;
      while ((line = br.readLine()) != null && line.length() > 0) {
        // skip first line
        if (line.charAt(0) == ' ' && line.charAt(1) == ' ')
          continue;

        // split line into values
        String[] values = line.substring(2, line.length()).split(" ");

        // fill matrix
        for (int i = 0; i < values.length; i++)
          matrix[linecount][i] = Integer.parseInt(values[i]);

        linecount++;
      }
    } finally {
      fr.close();
    }
  }

  /**
   * Laufzeit O(n^2) mit n = Anzahl der Knoten des Graphen
   */
  public static void writeOutputToFile(String filename, int[][] m, char[] l, boolean isDirected) throws IOException {
    File file = new File(path_output + filename + ".txt");
    FileWriter writer = new FileWriter(file);
    BufferedWriter bw = new BufferedWriter(writer);

    // write first line with vertex letters
    if (l != null) {
      bw.write("  ");

      for (int i = 0; i < l.length; i++)
        bw.write(l[i] + " ");

      bw.newLine();
    }
    // other lines
    for (int i = 0; i < m.length; i++) {
      if (l != null)
        bw.write(l[i] + " ");

      for (int j = 0; j < m[i].length; j++) {
        // skip values on the right side of the diagonal if the matrix is not directed
        if (!isDirected && j > i)
          break;

        bw.write(m[i][j] + " ");
      }
      bw.newLine();
    }

    bw.close();
    writer.close();

    System.out.println("\nNew file created or overridden '" + filename + ".txt' in directory '" + path_output + "'");
  }

  public int[][] getMatrix() {
    return this.matrix;
  }

  public char[] getVertexLetters() {
    return this.vertex_letters;
  }
}
