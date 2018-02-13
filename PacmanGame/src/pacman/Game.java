//Створення ігрового поля та самої гри

package pacman;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Game extends JPanel implements ActionListener {
	
	private Dimension field;
	private final Font scoreFont = new Font("Arial",Font.BOLD, 14);
	
	private final Color foodColor = new Color(255, 255, 0);
	private Color labyrinthColor;
	
	private boolean playingGame = false;
	private boolean dead = false;
	
	//константи 
	private static final int BLOCK_SIZE = 24;
	private static final int NUMBER_OF_BLOCKS = 19;
	static final int SCREEN_SIZE = NUMBER_OF_BLOCKS * BLOCK_SIZE;
	private static final int PACMAN_ANIMATION_DELAY = 2;
	private static final int GHOST_ANIMATION_DELAY = PACMAN_ANIMATION_DELAY *2;
	private static final int PACMAN_ANIMATION_COUNT = 4;
	private static final int MAXIMUM_NUMBER_OF_GHOSTS = 15;
	private static final int PACMAN_SPEED = 6;
	
	private int pacmanAnimationCount = PACMAN_ANIMATION_DELAY;
	private int pacmanAnimationDirection = 1;
	private int pacmanAnimationPosition = 0;
	
	private int ghostAnimationCount = GHOST_ANIMATION_DELAY;
	private int ghostAnimationDirection = 1;
	private int ghostAnimationPosition = 0;
	
	private int NUMBER_OF_GHOSTS = 6;
	private int hpLeft, totalScore;
	private int[] dx, dy;
	private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed, ghostType;
	
	private BufferedImage spriteSheet;
	
	private Image img;
	
	//картинки привидів
	private Image redGhost1up,    redGhost2up;
	private Image redGhost1left,  redGhost2left;
	private Image redGhost1down,  redGhost2down;
	private Image redGhost1right, redGhost2right;
	
	private Image pinkGhost1up,    pinkGhost2up;
	private Image pinkGhost1left,  pinkGhost2left;
	private Image pinkGhost1down,  pinkGhost2down;
	private Image pinkGhost1right, pinkGhost2right;
	
	private Image blueGhost1up,    blueGhost2up;
	private Image blueGhost1left,  blueGhost2left;
	private Image blueGhost1down,  blueGhost2down;
	private Image blueGhost1right, blueGhost2right;
	
	private Image orangeGhost1up,    orangeGhost2up;
	private Image orangeGhost1left,  orangeGhost2left;
	private Image orangeGhost1down,  orangeGhost2down;
	private Image orangeGhost1right, orangeGhost2right;
	
	//картинки пакмана
	private Image pacmanImage;
	private Image pacmanUp1, pacmanUp2, pacmanUp3;
	private Image pacmanLeft1, pacmanLeft2, pacmanLeft3;
	private Image pacmanDown1, pacmanDown2, pacmanDown3;
	private Image pacmanRight1, pacmanRight2, pacmanRight3;
	
	private int pacman_x, pacman_y, pacman_dx, pacman_dy;
	private int reqired_dx, reqired_dy, view_dx, view_dy;
	
	private final int validSpeeds[] = {2, 3, 3, 4, 6, 6, 7};
	
	private int currentSpeed = 3;
	private short[] screenData;
	private Timer timer;
	
	//масив рівнів 
	private final short level[] = {
	
		 0,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  8,  0, // 1
		 4, 19, 26, 26,  2, 26, 26, 26, 22,  5, 19, 26, 26, 26,  2, 26, 26, 22,  1, // 2
		 4,  5,  3,  6,  5, 11, 10, 14,  5, 13,  5, 11, 10, 14,  5,  3,  6,  5,  1, // 3
		 4,  5,  1,  4, 17, 26,  2, 26,  8, 26,  8, 26,  2, 26, 20,  1,  4,  5,  1, // 4
		 4,  5,  9, 12,  5,  7,  5, 11, 10,  2, 10, 14,  5,  7,  5,  9, 12,  5,  1, // 5
		 4, 17, 26, 26, 20,  5, 25, 26, 22,  5, 19, 26, 28,  5, 17, 26, 26, 20,  1, // 6
		 4,  5, 11,  6,  5,  1, 10, 14,  5, 13,  5, 11, 10,  4,  5,  3, 14,  5,  1, // 7
		 4, 17, 22, 13,  5,  5, 19, 26,  0,  2,  0, 26, 22,  5,  5, 13, 19, 20,  1, // 8
		 4, 17,  8, 26, 20, 13,  5,  3,  0,  0,  0,  6,  5, 13, 17, 26,  8, 20,  1, // 9
		 4,  5, 11, 14, 17, 26, 20,  9,  8,  8,  8, 12, 17, 26, 20, 11, 14,  5,  1, // 10
		 4, 17,  2, 26, 20,  7, 17, 26, 26, 26, 26, 26, 20,  7, 17, 26,  2, 20,  1, // 11
		 4, 17, 28,  7,  5,  5,  5, 11, 10,  2, 10, 14,  5,  5,  5,  7, 25, 20,  1, // 12
		 4,  5, 11, 12,  5, 13, 17, 26, 22,  5, 19, 26, 20, 13,  5,  9, 14,  5,  1, // 13
		 4, 17, 26, 26, 16, 26, 28,  7,  5, 13,  5,  7, 25, 26, 16, 26, 26, 20,  1, // 14
		 4,  5, 11,  6,  5, 11, 10, 12, 17,  2, 20,  9, 10, 14,  5,  3, 14,  5,  1, // 15
		 4, 17, 22,  5, 17, 26, 26, 26,  8,  8,  8, 26, 26, 26, 20,  5, 19, 20,  1, // 16
		 4, 17, 20, 13,  5, 11, 10, 10, 10, 10, 10, 10, 10, 14,  5, 13, 17, 20,  1, // 17
		 4, 25,  8, 26,  8, 26, 26, 26, 26, 26, 26, 26, 26, 26,  8, 26,  8, 28,  1, // 18
		 0,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  0, // 19
	};
	
	Game () {
		initImages();
		initVariables();
		initBoard();
	}
	
//Активує всі картинки
	private void initImages () {
		System.out.println("initImages()");
		
		int pacmanAnimCoordX = 0, pacmanAnimCoordY = 71;
		int redGhostAnimCoordX = 0, redGhostAnimCoordY = 143;
		int pinkGhostAnimCoordX = 0, pinkGhostAnimCoordY = 191;
		int blueGhostAnimCoordX = 192, blueGhostAnimCoordY = 191;
		int orangeGhostAnimCoordX = 0, orangeGhostAnimCoordY = 215;
		
		pacmanImage = getSprite(0,168);
		pacmanUp1 = getSprite(pacmanAnimCoordX+3*BLOCK_SIZE,pacmanAnimCoordY);
		pacmanUp2 = getSprite(pacmanAnimCoordX+BLOCK_SIZE,pacmanAnimCoordY);
		pacmanUp3 = getSprite(pacmanAnimCoordX+3*BLOCK_SIZE,pacmanAnimCoordY);
		pacmanDown1 = getSprite(pacmanAnimCoordX+7*BLOCK_SIZE,pacmanAnimCoordY);
		pacmanDown2 = getSprite(pacmanAnimCoordX+5*BLOCK_SIZE,pacmanAnimCoordY);
		pacmanDown3 = getSprite(pacmanAnimCoordX+7*BLOCK_SIZE,pacmanAnimCoordY);
		pacmanLeft1 = getSprite(pacmanAnimCoordX+2*BLOCK_SIZE,pacmanAnimCoordY);
		pacmanLeft2 = getSprite(pacmanAnimCoordX,pacmanAnimCoordY);
		pacmanLeft3 = getSprite(pacmanAnimCoordX+2*BLOCK_SIZE,pacmanAnimCoordY);
		pacmanRight1 = getSprite(pacmanAnimCoordX+6*BLOCK_SIZE,pacmanAnimCoordY);
		pacmanRight2 = getSprite(pacmanAnimCoordX+4*BLOCK_SIZE,pacmanAnimCoordY);
		pacmanRight3 = getSprite(pacmanAnimCoordX+6*BLOCK_SIZE,pacmanAnimCoordY);
		
		redGhost1right = getSprite(redGhostAnimCoordX,redGhostAnimCoordY);
		redGhost2right = getSprite(redGhostAnimCoordX+BLOCK_SIZE,redGhostAnimCoordY);
		redGhost1down = getSprite(redGhostAnimCoordX+2*BLOCK_SIZE,redGhostAnimCoordY);
		redGhost2down = getSprite(redGhostAnimCoordX+3*BLOCK_SIZE,redGhostAnimCoordY);
		redGhost1left = getSprite(redGhostAnimCoordX+4*BLOCK_SIZE,redGhostAnimCoordY);
		redGhost2left = getSprite(redGhostAnimCoordX+5*BLOCK_SIZE,redGhostAnimCoordY);
		redGhost1up = getSprite(redGhostAnimCoordX+6*BLOCK_SIZE,redGhostAnimCoordY);
		redGhost2up = getSprite(redGhostAnimCoordX+7*BLOCK_SIZE,redGhostAnimCoordY);
		
		pinkGhost1right = getSprite(pinkGhostAnimCoordX,pinkGhostAnimCoordY);
		pinkGhost2right = getSprite(pinkGhostAnimCoordX+BLOCK_SIZE,pinkGhostAnimCoordY);
		pinkGhost1down = getSprite(pinkGhostAnimCoordX+2*BLOCK_SIZE,pinkGhostAnimCoordY);
		pinkGhost2down = getSprite(pinkGhostAnimCoordX+3*BLOCK_SIZE,pinkGhostAnimCoordY);
		pinkGhost1left = getSprite(pinkGhostAnimCoordX+4*BLOCK_SIZE,pinkGhostAnimCoordY);
		pinkGhost2left = getSprite(pinkGhostAnimCoordX+5*BLOCK_SIZE,pinkGhostAnimCoordY);
		pinkGhost1up = getSprite(pinkGhostAnimCoordX+6*BLOCK_SIZE,pinkGhostAnimCoordY);
		pinkGhost2up = getSprite(pinkGhostAnimCoordX+7*BLOCK_SIZE,pinkGhostAnimCoordY);
		
		blueGhost1right = getSprite(blueGhostAnimCoordX,blueGhostAnimCoordY);
		blueGhost2right = getSprite(blueGhostAnimCoordX+BLOCK_SIZE,blueGhostAnimCoordY);
		blueGhost1down = getSprite(blueGhostAnimCoordX+2*BLOCK_SIZE,blueGhostAnimCoordY);
		blueGhost2down = getSprite(blueGhostAnimCoordX+3*BLOCK_SIZE,blueGhostAnimCoordY);
		blueGhost1left = getSprite(blueGhostAnimCoordX+4*BLOCK_SIZE,blueGhostAnimCoordY);
		blueGhost2left = getSprite(blueGhostAnimCoordX+5*BLOCK_SIZE,blueGhostAnimCoordY);
		blueGhost1up = getSprite(blueGhostAnimCoordX+6*BLOCK_SIZE,blueGhostAnimCoordY);
		blueGhost2up = getSprite(blueGhostAnimCoordX+7*BLOCK_SIZE,blueGhostAnimCoordY);
		
		orangeGhost1right = getSprite(orangeGhostAnimCoordX,orangeGhostAnimCoordY);
		orangeGhost2right = getSprite(orangeGhostAnimCoordX+BLOCK_SIZE,orangeGhostAnimCoordY);
		orangeGhost1down = getSprite(orangeGhostAnimCoordX+2*BLOCK_SIZE,orangeGhostAnimCoordY);
		orangeGhost2down = getSprite(orangeGhostAnimCoordX+3*BLOCK_SIZE,orangeGhostAnimCoordY);
		orangeGhost1left = getSprite(orangeGhostAnimCoordX+4*BLOCK_SIZE,orangeGhostAnimCoordY);
		orangeGhost2left = getSprite(orangeGhostAnimCoordX+5*BLOCK_SIZE,orangeGhostAnimCoordY);
		orangeGhost1up = getSprite(orangeGhostAnimCoordX+6*BLOCK_SIZE,orangeGhostAnimCoordY);
		orangeGhost2up = getSprite(orangeGhostAnimCoordX+7*BLOCK_SIZE,orangeGhostAnimCoordY);
	}
	
	
// Активую змінні 
	private void initVariables() {
		System.out.println("initVariables()");
		
		screenData = new short[NUMBER_OF_BLOCKS * NUMBER_OF_BLOCKS];
		labyrinthColor = new Color(0,0,255);
		field = new Dimension(400, 400);
		ghost_x = new int[MAXIMUM_NUMBER_OF_GHOSTS];
		ghost_dx = new int[MAXIMUM_NUMBER_OF_GHOSTS];
		ghost_y = new int[MAXIMUM_NUMBER_OF_GHOSTS];
		ghost_dy = new int[MAXIMUM_NUMBER_OF_GHOSTS];
		ghostSpeed = new int[MAXIMUM_NUMBER_OF_GHOSTS];
		ghostType = new int[MAXIMUM_NUMBER_OF_GHOSTS];
		dx = new int[4];
		dy = new int[4];
		
		timer = new Timer(40, this);
		timer.start();
	}
	
	// Активує лісенери
	private void initBoard() {
		System.out.println("initBoard()");
		
		addKeyListener(new TAdapter());
		setFocusable(true);
		setBackground(Color.black);
		setDoubleBuffered(true);
	}
	
	// Активує початок гри
	private void initGame() {
		System.out.println("initGame()");
		
		hpLeft = 3;
		totalScore = 0;
		initLevel();
		NUMBER_OF_GHOSTS = 6;
		currentSpeed = 3;
	}
//Активує рівень
	private void initLevel() {
		System.out.println("initLevel()");
		
		System.arraycopy(level, 0, screenData, 0, NUMBER_OF_BLOCKS * NUMBER_OF_BLOCKS);
		continueLevel();
	}
	
	//Активує наступний рівень
	private void continueLevel() {
		System.out.println("continueLevel()");
		
		Random rnd = new Random();
		short i;
		int dy = 1;
		int random;
		
		
		for (i = 0; i < NUMBER_OF_GHOSTS; i++) {
			
			ghost_x[i] = 9 * BLOCK_SIZE;
			ghost_y[i] = 8 * BLOCK_SIZE;
			ghost_dx[i] = 0;
			ghost_dy[i] = dy;
			ghostType[i] = rnd.nextInt(4)+1;
			dy = -dy;
			random = (int) (Math.random() * (currentSpeed + 1));
			
			if (random > currentSpeed) {
				random = currentSpeed;
			}
		
			
			ghostSpeed[i] =	validSpeeds[random];
		}
		
		pacman_x = 9 * BLOCK_SIZE;
		pacman_y = 15 * BLOCK_SIZE;
		pacman_dx = 0;
		pacman_dy = 0;
		reqired_dx = 0;
		reqired_dy = 0;
		view_dx = -1;
		view_dy = 0;
		dead = false;
	}
	
	@Override
	public void addNotify() {
		System.out.println("addNotify() - @override");
		
		super.addNotify();
		initGame();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		System.out.println("paintComponent() - @override");
		
		super.paintComponent(g);
		drawAll(g);
	}
	
	// Малює всі компоненти
	private void drawAll (Graphics g) {
		System.out.println("drawAll()");
		
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.black);
		g2d.fillRect(0, 0, field.width, field.height);
		
		drawField(g2d);
		drawScore(g2d);
		animate();
		
		if (playingGame)
			playGame(g2d);
		else
			showIntroScreen(g2d);
		
		g2d.drawImage(img,5,5,this);
		Toolkit.getDefaultToolkit().sync();
		g2d.dispose();
	}
	
	// Малює поле відносно рівня
	private void drawField (Graphics2D g2d) {
		System.out.println("drawField()");
		
		short i = 0;
		
		for (int y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
			for (int x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {
				
				g2d.setColor(labyrinthColor);
				g2d.setStroke(new BasicStroke(2));
				
				if ((screenData[i] & 1) != 0) {
					g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);
				}
				
				if ((screenData[i] & 2) != 0) {
					g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
				}
				
				if ((screenData[i] & 4) != 0) {
					g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1,
							y + BLOCK_SIZE - 1);
				}
				
				if ((screenData[i] & 8) != 0) {
					g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,
							y + BLOCK_SIZE - 1);
				}
				
				if ((screenData[i] & 16) != 0) {
					g2d.setColor(foodColor);
					g2d.fillRect(x + 11, y + 11, 2, 2);
				}
				
				i++;
			}
		}
	}
	
//Малює рахунок та здоровья
	private void drawScore(Graphics2D g2d) {
		System.out.println("drawScore()");
		
		g2d.setFont(scoreFont);
		g2d.setColor(new Color(96, 128, 255));
		
		String s = "Score: " + totalScore;
		g2d.drawString(s, SCREEN_SIZE / 2 + 76, SCREEN_SIZE + 4);
		
		for (int i = 0; i < hpLeft; i++)
			g2d.drawImage(pacmanLeft2, i * 28 + 6+ BLOCK_SIZE, SCREEN_SIZE - BLOCK_SIZE/2, this);
	}
	
	//Відповідає за анімацію 
	private void animate () {
		System.out.println("animate()");
		
		if (pacmanAnimationCount%2 == 0){
			ghostAnimationCount--;
			
			if (ghostAnimationCount <= 0){
				ghostAnimationCount = GHOST_ANIMATION_DELAY;
				ghostAnimationPosition = ghostAnimationPosition + ghostAnimationDirection;
				
				if(ghostAnimationPosition == 1 || ghostAnimationPosition == 0){
					ghostAnimationDirection = -ghostAnimationDirection;
				}
			}
		}
		pacmanAnimationCount--;
		
		
		if (pacmanAnimationCount <= 0) {
			pacmanAnimationCount = PACMAN_ANIMATION_DELAY;
			pacmanAnimationPosition = pacmanAnimationPosition + pacmanAnimationDirection;
			
			if (pacmanAnimationPosition == (PACMAN_ANIMATION_COUNT - 1) || pacmanAnimationPosition == 0) {
				pacmanAnimationDirection = -pacmanAnimationDirection;
			}
		}
	}
	
	// Відповідає за стан пакмана
	private void playGame(Graphics2D g2d) {
		System.out.println("playGame()");
		
		if (dead) {
			death(g2d);
		} else {
			movePacman();
			drawPacman(g2d);
			moveGhosts(g2d);
			checkField();
		}
	}
	
	// Показує меню запуску гри
	private void showIntroScreen(Graphics2D g2d) {
		System.out.println("showIntroScreen()");
		
		g2d.setColor(new Color(0, 32, 48));
		g2d.fillRect(50, SCREEN_SIZE / 2 - 30, SCREEN_SIZE - 100, 50);
		g2d.setColor(Color.yellow);
		g2d.drawRect(50, SCREEN_SIZE / 2 - 30, SCREEN_SIZE - 100, 50);
		
		String s = "Press S to start.";
		Font small = new Font("Helvetica", Font.BOLD, 14);
		FontMetrics metr = this.getFontMetrics(small);
		
		g2d.setColor(Color.yellow);
		g2d.setFont(small);
		g2d.drawString(s, (SCREEN_SIZE - metr.stringWidth(s)) / 2, SCREEN_SIZE / 2);
	}
	
// Перевіряє стан точок
	private void checkField () {
		System.out.println("checkField()");
		
		short i = 0;
		boolean finished = true;
		
		while (i < NUMBER_OF_BLOCKS*NUMBER_OF_BLOCKS && finished) {
			if ((screenData[i] & 48) != 0)
				finished = false;
			i++;
		}
		if (finished) {
			if (NUMBER_OF_GHOSTS < MAXIMUM_NUMBER_OF_GHOSTS)
				NUMBER_OF_GHOSTS++;
			int maximumSpeed = 6;
			if (currentSpeed < maximumSpeed)
				currentSpeed++;
			
			initLevel();
		}
	}
	
	
	private void death(Graphics2D g2d) {
		System.out.println("death()");
		
		hpLeft--;
		
		if (hpLeft == 0)
			playingGame = false;
		
		continueLevel();
	}
	
	private void moveGhosts(Graphics2D g2d) {
		System.out.println("moveGhosts()");
		
		int position;
		int counter;
		
		for (short i = 0; i < NUMBER_OF_GHOSTS; i++) {
			if (ghost_x[i] % BLOCK_SIZE == 0 && ghost_y[i] % BLOCK_SIZE == 0) {
				position = (ghost_x[i] / BLOCK_SIZE + NUMBER_OF_BLOCKS * (int)(ghost_y[i] / BLOCK_SIZE)); 
				
				counter = 0;
				
				if ((screenData[position] & 1) == 0 && ghost_dx[i] != 1) {
					dx[counter] = -1;
					dy[counter] = 0;
					counter++;
				}
				
				if ((screenData[position] & 2) == 0 && ghost_dy[i] != 1) {
					dx[counter] = 0;
					dy[counter] = -1;
					counter++;
				}
				
				if ((screenData[position] & 4) == 0 && ghost_dx[i] != -1) {
					dx[counter] = 1;
					dy[counter] = 0;
					counter++;
				}
				
				if ((screenData[position] & 8) == 0 && ghost_dy[i] != -1) {
					dx[counter] = 0;
					dy[counter] = 1;
					counter++;
				}
				
				if (counter == 0) {
					
					if ((screenData[position] & 15) == 15) {
						ghost_dx[i] = 0;
						ghost_dy[i] = 0;
					} else {
						ghost_dx[i] = -ghost_dx[i];
						ghost_dy[i] = -ghost_dy[i];
					}
					
				} else {
					counter = (int) (Math.random() * counter);
					
					if (counter > 3)
						counter = 3;
					
					ghost_dx[i] = dx[counter];
					ghost_dy[i] = dy[counter];
				}
				
			}
			
			ghost_x[i] = ghost_x[i] + (ghost_dx[i] * ghostSpeed[i]);
			ghost_y[i] = ghost_y[i] + (ghost_dy[i] * ghostSpeed[i]);
			drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1, ghostType[i], ghost_dx[i], ghost_dy[i]);
			
			if (pacman_x > (ghost_x[i] - 12) && pacman_x < (ghost_x[i] + 12)
					&& pacman_y > (ghost_y[i] - 12) && pacman_y < (ghost_y[i] + 12)
					&& playingGame) {
				
				dead = true;
			}
		}
	}
	
	private void drawGhost(Graphics2D g2d, int x, int y, int ghostType, int ghost_dx, int ghost_dy) {
		if(ghost_dx == 1)
			drawGhostRight(g2d,x,y,ghostType);
		else if (ghost_dx == -1)
			drawGhostLeft(g2d,x,y,ghostType);
		else if (ghost_dy == 1)
			drawGhostDown(g2d,x,y,ghostType);
		else
			drawGhostUp(g2d,x,y,ghostType);
	}
	
	private void drawGhostUp(Graphics2D g2d, int x, int y, int ghostType){
		if (ghostType == 1)
			switch (ghostAnimationPosition){
				case 0:
					g2d.drawImage(redGhost1up, x, y, this);
					break;
				default:
					g2d.drawImage(redGhost2up, x, y, this);
					break;
			}
		else if (ghostType == 2)
			switch (ghostAnimationPosition){
				case 0:
					g2d.drawImage(pinkGhost1up, x, y, this);
					break;
				default:
					g2d.drawImage(pinkGhost2up, x, y, this);
					break;
			}
		else if (ghostType == 3)
			switch (ghostAnimationPosition){
				case 0:
					g2d.drawImage(blueGhost1up, x, y, this);
					break;
				default:
					g2d.drawImage(blueGhost2up, x, y, this);
					break;
			}
		else
			switch (ghostAnimationPosition){
				case 0:
					g2d.drawImage(orangeGhost1up, x, y, this);
					break;
				default:
					g2d.drawImage(orangeGhost2up, x, y, this);
					break;
			}
	}
	
	private void drawGhostDown(Graphics2D g2d,int x, int y, int ghostType){
		if (ghostType == 1)
			switch (ghostAnimationPosition){
				case 0:
					g2d.drawImage(redGhost1down, x, y, this);
					break;
				default:
					g2d.drawImage(redGhost2down, x, y, this);
					break;
			}
		else if (ghostType == 2)
			switch (ghostAnimationPosition){
				case 0:
					g2d.drawImage(pinkGhost1down, x, y, this);
					break;
				default:
					g2d.drawImage(pinkGhost2down, x, y, this);
					break;
			}
		else if (ghostType == 3)
			switch (ghostAnimationPosition){
				case 0:
					g2d.drawImage(blueGhost1down, x, y, this);
					break;
				default:
					g2d.drawImage(blueGhost2down, x, y, this);
					break;
			}
		else
			switch (ghostAnimationPosition){
				case 0:
					g2d.drawImage(orangeGhost1down, x, y, this);
					break;
				default:
					g2d.drawImage(orangeGhost2down, x, y, this);
					break;
			}
	}
	
	private void drawGhostLeft(Graphics2D g2d, int x, int y, int ghostType){
		if (ghostType == 1)
			switch (ghostAnimationPosition){
				case 0:
					g2d.drawImage(redGhost1left, x, y, this);
					break;
				default:
					g2d.drawImage(redGhost2left, x, y, this);
					break;
			}
		else if (ghostType == 2)
			switch (ghostAnimationPosition){
				case 0:
					g2d.drawImage(pinkGhost1left, x, y, this);
					break;
				default:
					g2d.drawImage(pinkGhost2left, x, y, this);
					break;
			}
		else if (ghostType == 3)
			switch (ghostAnimationPosition){
				case 0:
					g2d.drawImage(blueGhost1left, x, y, this);
					break;
				default:
					g2d.drawImage(blueGhost2left, x, y, this);
					break;
			}
		else
			switch (ghostAnimationPosition){
				case 0:
					g2d.drawImage(orangeGhost1left, x, y, this);
					break;
				default:
					g2d.drawImage(orangeGhost2left, x, y, this);
					break;
			}
	}
	
	private void drawGhostRight(Graphics2D g2d, int x, int y, int ghostType){
		if (ghostType == 1)
			switch (ghostAnimationPosition){
				case 0:
					g2d.drawImage(redGhost1right, x, y, this);
					break;
				default:
					g2d.drawImage(redGhost2right, x, y, this);
					break;
			}
		else if (ghostType == 2)
			switch (ghostAnimationPosition){
				case 0:
					g2d.drawImage(pinkGhost1right, x, y, this);
					break;
				default:
					g2d.drawImage(pinkGhost2right, x, y, this);
					break;
			}
		else if (ghostType == 3)
			switch (ghostAnimationPosition){
				case 0:
					g2d.drawImage(blueGhost1right, x, y, this);
					break;
				default:
					g2d.drawImage(blueGhost2right, x, y, this);
					break;
			}
		else
			switch (ghostAnimationPosition){
				case 0:
					g2d.drawImage(orangeGhost1right, x, y, this);
					break;
				default:
					g2d.drawImage(orangeGhost2right, x, y, this);
					break;
			}
	}
	
	private void movePacman() {
		System.out.println("movePacman()");
		
		int position;
		short ch;
		
		if (reqired_dx == -pacman_dx && reqired_dy == -pacman_dy) {
			pacman_dx = reqired_dx;
			pacman_dy = reqired_dy;
			view_dx = pacman_dx;
			view_dy = pacman_dy;
		}
		
		if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
			position = (int)(pacman_x / BLOCK_SIZE + NUMBER_OF_BLOCKS * (pacman_y / BLOCK_SIZE)); // TODO (int)
			ch = screenData[position];
			
			if ((ch & 16) != 0) {
				screenData[position] = (short) (ch & 15);
				totalScore +=10;
			}
			
			if (reqired_dx != 0 || reqired_dy != 0) {
				if (!(     (reqired_dx == -1 && reqired_dy == 0  && (ch & 1) != 0)
						|| (reqired_dx == 1  && reqired_dy == 0  && (ch & 4) != 0)
						|| (reqired_dx == 0  && reqired_dy == -1 && (ch & 2) != 0)
						|| (reqired_dx == 0  && reqired_dy == 1  && (ch & 8) != 0))) {
					pacman_dx = reqired_dx;
					pacman_dy = reqired_dy;
					view_dx = pacman_dx;
					view_dy = pacman_dy;
				}
			}
			
			
			if ((pacman_dx == -1 && pacman_dy == 0 && (ch & 1) != 0)
					|| (pacman_dx == 1 && pacman_dy == 0 && (ch & 4) != 0)
					|| (pacman_dx == 0 && pacman_dy == -1 && (ch & 2) != 0)
					|| (pacman_dx == 0 && pacman_dy == 1 && (ch & 8) != 0)) {
				pacman_dx = 0;
				pacman_dy = 0;
			}
		}
		pacman_x = pacman_x + PACMAN_SPEED * pacman_dx;
		pacman_y = pacman_y + PACMAN_SPEED * pacman_dy;
	}
	
	private void drawPacman(Graphics2D g2d) {
		
		if (view_dy == 1) {
			drawPacmanDown(g2d);
		} else if (view_dx == -1) {
			drawPacmanLeft(g2d);
		} else if (view_dy == -1) {
			drawPacmanUp(g2d);
		} else {
			drawPacmanRight(g2d);
		}
	}
	
	private void drawPacmanUp(Graphics2D g2d) {
		
		switch (pacmanAnimationPosition) {
			case 1:
				g2d.drawImage(pacmanUp1, pacman_x + 1, pacman_y + 1, this);
				break;
			case 2:
				g2d.drawImage(pacmanUp2, pacman_x + 1, pacman_y + 1, this);
				break;
			case 3:
				g2d.drawImage(pacmanUp3, pacman_x + 1, pacman_y + 1, this);
				break;
			default:
				g2d.drawImage(pacmanImage, pacman_x + 1, pacman_y + 1, this);
				break;
		}
	}
	
	private void drawPacmanDown(Graphics2D g2d) {
		
		switch (pacmanAnimationPosition) {
			case 1:
				g2d.drawImage(pacmanDown1, pacman_x + 1, pacman_y + 1, this);
				break;
			case 2:
				g2d.drawImage(pacmanDown2, pacman_x + 1, pacman_y + 1, this);
				break;
			case 3:
				g2d.drawImage(pacmanDown3, pacman_x + 1, pacman_y + 1, this);
				break;
			default:
				g2d.drawImage(pacmanImage, pacman_x + 1, pacman_y + 1, this);
				break;
		}
	}
	
	private void drawPacmanLeft (Graphics2D g2d) {
		
		switch (pacmanAnimationPosition) {
			case 1:
				g2d.drawImage(pacmanLeft1, pacman_x + 1, pacman_y + 1, this);
				break;
			case 2:
				g2d.drawImage(pacmanLeft2, pacman_x + 1, pacman_y + 1, this);
				break;
			case 3:
				g2d.drawImage(pacmanLeft3, pacman_x + 1, pacman_y + 1, this);
				break;
			default:
				g2d.drawImage(pacmanImage, pacman_x + 1, pacman_y + 1, this);
				break;
		}
	}
	
	private void drawPacmanRight(Graphics2D g2d) {
		
		switch (pacmanAnimationPosition) {
			case 1:
				g2d.drawImage(pacmanRight1, pacman_x + 1, pacman_y + 1, this);
				break;
			case 2:
				g2d.drawImage(pacmanRight2, pacman_x + 1, pacman_y + 1, this);
				break;
			case 3:
				g2d.drawImage(pacmanRight3, pacman_x + 1, pacman_y + 1, this);
				break;
			default:
				g2d.drawImage(pacmanImage, pacman_x + 1, pacman_y + 1, this);
				break;
		}
	}
	
	private BufferedImage loadSpriteSheet(){
		BufferedImage spriteSheet = null;
		
		try {
			spriteSheet = ImageIO.read(new File("images/spritesheet.png"));
		} catch (IOException e){
			e.printStackTrace();
		}
		
		return spriteSheet;
	}
	
	private BufferedImage getSprite(int xGrid, int yGrid){
		
		if (spriteSheet == null)
			spriteSheet = loadSpriteSheet();
		
		return spriteSheet.getSubimage(xGrid,yGrid,BLOCK_SIZE,BLOCK_SIZE);
	}
	
	//Зчитує натиски клавіатури 
	class TAdapter extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {
			
			int key = e.getKeyCode();
			
			if (playingGame) {
				if (key == KeyEvent.VK_LEFT) {
					reqired_dx = -1;
					reqired_dy = 0;
				} else if (key == KeyEvent.VK_RIGHT) {
					reqired_dx = 1;
					reqired_dy = 0;
				} else if (key == KeyEvent.VK_UP) {
					reqired_dx = 0;
					reqired_dy = -1;
				} else if (key == KeyEvent.VK_DOWN) {
					reqired_dx = 0;
					reqired_dy = 1;
				} else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
					playingGame = false;
				} else if (key == KeyEvent.VK_P) {
					if (timer.isRunning()) {
						timer.stop();
					} else {
						timer.start();
					}
				}
			} else if (key == 's' || key == 'S'){
				playingGame = true;
				initGame();
			}
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			
			int key = e.getKeyCode();
			
			if (key == Event.LEFT || key == Event.RIGHT
					|| key == Event.UP || key == Event.DOWN) {
				reqired_dx = 0;
				reqired_dy = 0;
			}
		}
	}
	
	//Перемалювує поле
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("actionPerformed() — !!!!!");
		
		repaint();
	}
}
