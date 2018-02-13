
//Створення панелі меню

package pacman;

import java.awt.*;

import javax.swing.*;

class MenuPanel extends JPanel {
	JButton exit = new JButton("Quit");
	JButton play = new JButton("Start");
	
	MenuPanel(){
		super();
		initPanel();
	}
	
	private void initPanel(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.BLUE);
		
		Font myChoice = new Font("Gulim",Font.BOLD,14);
		play.setFont(myChoice);
		exit.setFont(myChoice);
		add(Box.createRigidArea(new Dimension(0, 175)));
		
		JLabel welcome = new JLabel("PACMAN");
		welcome.setFont(myChoice);
		welcome.setAlignmentX(CENTER_ALIGNMENT);
		welcome.setForeground(new Color(255, 255, 255));
		add(welcome);
		
		play.setAlignmentX(CENTER_ALIGNMENT);
		add(Box.createRigidArea(new Dimension(0, 25)));
		add(play);
		
		exit.setAlignmentX(CENTER_ALIGNMENT);
		add(Box.createRigidArea(new Dimension(0, 25)));
		add(exit);
	}
}
