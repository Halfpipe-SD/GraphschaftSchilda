package code.problem1;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;

import code.utils.BasicWindow;
import code.utils.GraphData;
import code.utils.JGraphPanel;

public class Problem1 extends BasicWindow {

  public Problem1(String title) throws FileNotFoundException {
    super(title);

    setSize(new Dimension(510, 600));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    Dimension size = new Dimension(getWidth() / 2, getHeight());

    GraphData graph_input = new GraphData("town_streets.json");
    graph_input.setDirected(false);

    // initialize algorithm instance
    Alrogithm algorithm = new Alrogithm(graph_input);

    // use the algorithm to get the minimum spanning tree
    GraphData graph_output = algorithm.get_output_graph();

    JGraphPanel p1 = new JGraphPanel("Rohdaten", size, graph_input);
    JGraphPanel p2 = new JGraphPanel("Minimum Spanning Tree (Prim)", size, graph_output);

    add(p1);
    add(p2);
  }

}