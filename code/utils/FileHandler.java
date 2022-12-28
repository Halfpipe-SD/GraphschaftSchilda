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

  private String absolute_path = new File("").getAbsolutePath();
  private final String path_input = absolute_path + "/code/data/";
  private final String path_output = absolute_path + "/code/output/";

  public FileHandler(String filename) throws FileNotFoundException, IOException {

    // read data from txt file with filereader
    FileReader fr = new FileReader(path_input + filename);
    BufferedReader br = new BufferedReader(fr);
    try {
      // get number of vertices in first line
      String firstline = br.readLine().trim();
      int vertices = firstline.split(" ").length;
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

  public void writeOutputToFile(String filename) throws IOException {
    File file = new File(path_output + filename + ".txt");
    FileWriter writer = new FileWriter(file);
    BufferedWriter bw = new BufferedWriter(writer);

    // write matrix to file
    if (this.vertex_letters != null) {
      bw.write("  ");
      for (int i = 0; i < this.vertex_letters.length; i++)
        bw.write(this.vertex_letters[i] + " ");
      bw.newLine();
    }
    // other lines
    for (int i = 0; i < this.matrix.length; i++) {
      if (this.vertex_letters != null)
        bw.write(this.vertex_letters[i] + " ");
      for (int j = 0; j < this.matrix[i].length; j++)
        bw.write(this.matrix[i][j] + " ");
      bw.newLine();
    }

    bw.close();
    writer.close();

    System.out.println("New file created: " + filename + ".txt in directory " + path_output);
  }

  public int[][] getMatrix() {
    return this.matrix;
  }

  public char[] getVertexLetters() {
    return this.vertex_letters;
  }

}
