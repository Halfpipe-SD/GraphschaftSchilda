package code.utils;

public class GraphEdge {
  private String source;
  private String target;
  private double weight;

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
