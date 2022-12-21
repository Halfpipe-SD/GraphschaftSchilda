package code.utils;

public class AdjazenzMatrix {

  private int[][] matrix;
  private String label;
  private boolean isDirected;

  public AdjazenzMatrix(String label, int[][] matrix, boolean isDirected) {
    this.matrix = matrix;
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

    for (int i = 0; i < this.matrix.length; i++) {
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
}
