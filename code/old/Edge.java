package code.old;

public class Edge {
  private String source;
  private String target;
  private double weight;

  private double flow;
  private double capacity;

  private boolean isVisited;

  public Edge(String source, String target, double weight) {
    this.source = source;
    this.target = target;
    this.weight = weight;
  }

  public Edge(String source, String target, double flow, double capacity) {
    this.source = source;
    this.target = target;
    this.flow = flow;
    this.capacity = capacity;
  }

  public void setFlow(double flow) {
    this.flow = flow;
  }

  public void setCapacity(double capacity) {
    this.capacity = capacity;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public void setVisited(boolean isVisited) {
    this.isVisited = isVisited;
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

  public double getFlow() {
    return flow;
  }

  public double getCapacity() {
    return capacity;
  }

  public boolean isVisited() {
    return isVisited;
  }

  @Override
  public String toString() {
    return "Edge [source=" + source + ", target=" + target + ", weight=" + weight + "]";
  }
}
