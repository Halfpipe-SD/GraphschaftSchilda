package code.utils;

public class GraphVertex {
  private String label;
  private int value;

  private GraphVertex predecessor;
  private GraphVertex next;

  public GraphVertex(int value) {
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

  public int getValue() {
    return value;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public String toString() {
    return label + "(" + value + ")";
  }

}
