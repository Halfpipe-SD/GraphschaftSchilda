package code;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;

import code.utils.BasicWindow;
import code.utils.Graph;
import code.utils.JGraphPanel;

public class Problem5 extends BasicWindow {

  public Problem5(String title) throws FileNotFoundException {
    super(title);

    setSize(new Dimension(510, 600));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    Graph graph_input = new Graph("town_streets.json");
    Graph graph_output = graph_input.deepCopy();

    JGraphPanel p1 = new JGraphPanel("Rohdaten", graph_input, "hierarchical");
    JGraphPanel p2 = new JGraphPanel("Output", graph_output, "hierarchical");

    add(p1);
    add(p2);
  }

}