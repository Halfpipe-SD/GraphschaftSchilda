/*
 * Important documentation for the jgrapht lib
 * https://jgrapht.org/guide/UserOverview
 */

package code;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFrame;

import code.problem1.Problem1;
import code.problem3.Problem3;
import code.problem4.Problem4;
import code.problem6.Problem6;
import code.test.Template;

public class Main extends JFrame {

  private final String[] button_labels = {
      "1 Straßenplaner",
      "2 Wasserversorgungsplaner",
      "3 Stromversorgungsplaner",
      "4 Feuerwerksplaner",
      "5 Festhochzeitsplaner",
      "6 Verkehrsplaner",
      "7 Aufgabenplaner",
      "8 Test" };

  /*
   * #### PROGRAM START ####
   * 
   * This starts the program and creates the main window.
   */
  public static void main(String[] args) {

    new Main();
  }

  public Main() {

    // initialize window
    super("Die Graphschaft Schilda - Planungstool");
    setResizable(true);
    setSize(new Dimension(400, 300));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new GridLayout(this.button_labels.length, 1));

    // add buttons
    for (String value : this.button_labels) {
      JButton button = new JButton(value);
      button.addActionListener(e -> this.button_pressed(value));
      add(button);
    }
    setVisible(true);
  }

  private void button_pressed(String label) {

    // get problem number from button label
    int problem = Integer.parseInt(label.substring(0, 1));
    System.out.println("Chose menu option: " + label);

    try {
      // open problem window
      switch (problem) {
        case 1:
          new Problem1(label);
          break;
        case 2:
          // new Problem2();
          break;
        case 3:
          new Problem3(label);
          break;
        case 4:
          new Problem4(label);
          break;
        case 5:
          // new Problem5();
          break;
        case 6:
          new Problem6(label);
          break;
        case 7:
          // new Problem7();
          break;
        case 8:
          new Template(label);
          break;
      }
    } catch (FileNotFoundException enf) {
      System.out.println("File not found " + enf.getMessage());
      System.exit(1);
    }
  }

}