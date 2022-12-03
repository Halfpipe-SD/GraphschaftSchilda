package code.utils;

public class GraphEdge {
  private String source;
  private String target;
  private double weight;

  public GraphEdge(String source, String target, double weight) {
    this.source = source;
    this.target = target;
    this.weight = weight;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public String getSource() {
    return source;
  }

  public String getTarget() {
    return target;
  }

  public double getWeight() {
    return weight;
  }

  @Override
  public String toString() {
    return "Edge [source=" + source + ", target=" + target + ", weight=" + weight + "]";
  }
}
