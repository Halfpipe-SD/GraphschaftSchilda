package code.utils;

public class Edge {

  private char source;
  private char target;
  private int weight;

  public Edge(char source, char target, int weight) {
    this.source = source;
    this.target = target;
    this.weight = weight;
  }

  public char getSource() {
    return this.source;
  }

  public void setSource(char source) {
    this.source = source;
  }

  public char getTarget() {
    return this.target;
  }

  public void setTarget(char target) {
    this.target = target;
  }

  public int getWeight() {
    return this.weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  @Override
  public String toString() {
    return "Edge [source=" + this.source + ", target=" + this.target + ", weight=" + this.weight + "]";
  }

}
