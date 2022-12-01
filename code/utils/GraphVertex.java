package code.utils;

public class GraphVertex {
  private String label;
  private int key;

  private GraphVertex predecessor;
  private GraphVertex next;

  public GraphVertex(int key) {
    this.key = key;
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

  public void setValue(int key) {
    this.key = key;
  }

  public int getValue() {
    return key;
  }

  public String getLabel() {
    return label;
  }

  @Override
  public String toString() {
    return label + "(" + key + ")";
  }

}
