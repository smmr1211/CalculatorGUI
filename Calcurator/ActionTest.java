package Calcurator;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ActionTest extends JFrame {
	public ActionTest() {
		JPanel pane = (JPanel) getContentPane();
		Actions action = new Actions();
		pane.add(new JButton(action), BorderLayout.SOUTH);
	}
	public static void main(String[] args) {
		ActionTest at = new ActionTest();
		at.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		at.setSize(200, 200);
		at.setVisible(true);
	}
}