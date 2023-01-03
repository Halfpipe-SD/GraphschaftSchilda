package code.utils;

public class Vertex {

  private char letter;
  private int key;
  private Vertex predecessor;
  private boolean visited;

  public Vertex(char letter, int value, Vertex predecessor) {
    this.letter = letter;
    this.key = value;
    this.predecessor = predecessor;
  }

  public Vertex(char letter, int value) {
    this(letter, value, null);
  }

  public char getLetter() {
    return this.letter;
  }

  public int getKey() {
    return this.key;
  }

  public void setKey(int value) {
    this.key = value;
  }

  public Vertex getPredecessor() {
    return this.predecessor;
  }

  public void setPredecessor(Vertex predecessor) {
    this.predecessor = predecessor;
  }

  public boolean isVisited() {
    return this.visited;
  }

  public void setVisited(boolean visited) {
    this.visited = visited;
  }

  @Override
  public String toString() {
    return "Vertex [letter=" + this.letter + ", key=" + this.key + ", predecessor=" + this.predecessor + "]";
  }
}
