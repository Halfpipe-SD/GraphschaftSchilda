package code.utils;

import java.io.IOException;

public class AdjazenzMatrix {

  private String label;

  private int[][] matrix;
  private char[] vertex_letters;

  private boolean isDirected;
  private int max_flow;

  public AdjazenzMatrix(String label, int[][] matrix, boolean isDirected) {
    this.matrix = matrix;
    this.label = label;
    this.isDirected = isDirected;
  }

  public AdjazenzMatrix(String label, int[][] matrix, char[] vertex_letters, boolean isDirected) {
    this.matrix = matrix;
    this.label = label;
    this.vertex_letters = vertex_letters;
    this.isDirected = isDirected;
  }

  // constructor for ProblemTest2
  public AdjazenzMatrix(String label, int max_flow, boolean isDirected) {
    this.max_flow = max_flow;
    this.label = label;
    this.isDirected = isDirected;
  }

  public void printMatrix() throws IOException {
    handleMatrix(false, this.label);
  }

  public void printAndWriteMatrix(String filename) throws IOException {
    handleMatrix(true, filename);
  }

  private void handleMatrix(boolean write_to_file, String filename) throws IOException {
    if (this.isDirected)
      System.out.println("\nGerichtete Adjazenzmatrix '" + this.label + "':");
    else
      System.out.println("\nUngerichtete Adjazenzmatrix '" + this.label + "':");

    // first line with vertex letters
    if (this.vertex_letters != null) {
      System.out.print("  ");
      for (int i = 0; i < this.vertex_letters.length; i++)
        System.out.print(this.vertex_letters[i] + " ");
      System.out.println();
    }
    // other lines
    for (int i = 0; i < this.matrix.length; i++) {
      if (this.vertex_letters != null)
        System.out.print(this.vertex_letters[i] + " ");

      // print only half of the matrix
      for (int j = 0; j < this.matrix[i].length; j++) {
        // skip values on the right side of the diagonal if the matrix is not directed
        if (!isDirected && j > i)
          break;
        System.out.print(this.matrix[i][j] + " ");
      }
      System.out.println();
    }

    if (!write_to_file)
      return;

    // write matrix to file
    FileHandler.writeOutputToFile(filename, matrix, vertex_letters, isDirected);
  }

  public int[][] getMatrix() {
    return this.matrix;
  }

  public String getLabel() {
    return this.label;
  }

  public boolean isDirected() {
    return this.isDirected;
  }

  // getter for ProblemTest2
  public int getMaxFlow() {
    return this.max_flow;
  }
}
