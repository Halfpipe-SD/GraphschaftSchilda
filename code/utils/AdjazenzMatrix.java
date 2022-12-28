package code.utils;

public class AdjazenzMatrix {

  private int[][] matrix;
  private String label;
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

  public void printMatrix() {
    String title = "Adjazenzmatrix '" + this.label + "':";

    if (this.isDirected)
      title = "\nGerichtete " + title;
    else
      title = "\nUngerichtete " + title;

    System.out.println(title);

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
      for (int j = 0; j < this.matrix[i].length; j++)
        System.out.print(this.matrix[i][j] + " ");
      System.out.println();
    }
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
