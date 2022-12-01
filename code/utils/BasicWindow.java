package code.utils;

import javax.swing.JFrame;

public class BasicWindow extends JFrame {

  public BasicWindow(String title) {

    /// initialize window
    super(title);
    setResizable(true);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setVisible(true);
  }

}
