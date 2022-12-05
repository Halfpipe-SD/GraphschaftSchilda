package code.utils;

public class GraphVertex {
  private String label;
  private int value;

  private GraphVertex predecessor;
  private GraphVertex next;

  private boolean visited;

  public GraphVertex(int value) {
    this.value = value;
  }

  public GraphVertex(String label) {
    this.label = label;
  }

  public GraphVertex(String label, int value) {
    this.label = label;
    this.value = value;
  }

  public void setNext(GraphVertex next) {
    this.next = next;
  }

  public GraphVertex getNext() {
    return next;
  }

  public void setPredecessor(GraphVertex predecessor) {
    this.predecessor = predecessor;
  }

  public GraphVertex getPredecessor() {
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
