//Створення інтерфейсу користувача

package pacman;

import javax.swing.WindowConstants;
import javax.swing.JFrame;

class Pacman extends JFrame {
	private final MenuPanel comp = new MenuPanel();
	
	Pacman() {
		super();
		initPanel();
	}
	//Створює всі компоненти панелі
	private void initPanel(){
		setTitle("Pacman");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		int SIZE = Game.SCREEN_SIZE;
		setSize(SIZE + 15, SIZE + 64);
		setLocationRelativeTo(null);
		setFocusable(false);
		add(comp);
		comp.play.setFocusable(false);
		comp.play.addActionListener(e -> Pacman.this.initUI());
		comp.exit.setFocusable(false);
		comp.exit.addActionListener(e -> System.exit(0));
		add(comp);
	}
	
	//Створює весь інтерфейс користувача
	private void initUI() {
		remove(comp);
		Game game = new Game();
		add(game);
		game.setFocusable(true);
		repaint();
		validate();
		game.requestFocus();
	}
	
}