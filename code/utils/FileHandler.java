package code.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileHandler {

  private int[][] matrix;

  public FileHandler(String filename) throws FileNotFoundException, IOException {

    String filepath = new File("").getAbsolutePath() + "/code/data/" + filename;

    // read data from txt file with filereader
    FileReader fr = new FileReader(filepath);
    BufferedReader br = new BufferedReader(fr);
    try {
      // get number of vertices in first line
      String firstline = br.readLine().trim();
      int vertices = firstline.split(" ").length;

      System.out.println("Read file: " + filename);
      System.out.println("Number of vertices: " + vertices);

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

  public int[][] getMatrix() {
    return this.matrix;
  }

}
