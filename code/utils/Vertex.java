package code.utils;

public class Vertex {
  private String label;
  private int value;

  private Vertex predecessor;
  private Vertex next;

  private boolean visited;

  public Vertex(int value) {
    this.value = value;
  }

  public Vertex(String label) {
    this.label = label;
  }

  public Vertex(String label, int value) {
    this.label = label;
    this.value = value;
  }

  public void setNext(Vertex next) {
    this.next = next;
  }

  public Vertex getNext() {
    return next;
  }

  public void setPredecessor(Vertex predecessor) {
    this.predecessor = predecessor;
  }

  public Vertex getPredecessor() {
    return predecessor;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public void setVisited(boolean visited) {
    this.visited = visited;
  }

  public int getValue() {
    return value;
  }

  public String getLabel() {
    return label;
  }

  public boolean isVisited() {
    return visited;
  }

  @Override
  public String toString() {
    return label + "(" + value + ")";
  }

}
