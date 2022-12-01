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

    setSize(new Dimension(450, 350));
    setLayout(new GridLayout(1, 2));
    setLocationRelativeTo(null);

    Dimension size = new Dimension(getWidth() / 2, getHeight());

    GraphData graph_data = new GraphData("town_water.json");

    JGraphPanel p1 = new JGraphPanel(size, graph_data);
    JGraphPanel p2 = new JGraphPanel(size, graph_data);

    add(p1);
    add(p2);
  }

}