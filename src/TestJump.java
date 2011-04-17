import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
 
import javax.swing.JFrame;
 
 
public class TestJump extends Applet implements Runnable, KeyListener {
 
	private static final long serialVersionUID = 1L;
	private static final int G_WIDTH = 640;
	private static final int G_HEIGHT = 480;

	private BufferedImage backbuffer;
	private Graphics2D g2d;

	private boolean running = false;
	private double x = 200;
	private double y = 200;
	private double xa = 0;
	private double ya = 5;

	private boolean keyJump;
	private boolean keyLeft;
	private boolean keyRight;
	private boolean onGround = false;

	public TestJump() {
		setPreferredSize(new Dimension(G_WIDTH, G_HEIGHT));
		backbuffer = new BufferedImage(G_WIDTH, G_HEIGHT,BufferedImage.TYPE_INT_RGB);
		g2d = backbuffer.createGraphics();
		addKeyListener(this);
	}

	public void start() {
		running = true;
		new Thread(this).start();
	}

	public void stop() {
		running = false;
	}

	@Override
	public void run() {
		requestFocus();
		while (running) {
			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
			}

			if (y + 20 >= G_HEIGHT) {
				onGround = true;
				y = G_HEIGHT - 20;
			} else {
				onGround = false;
			}
			
			if(y <= 0) {
				y = 0;
			}
			
			if (onGround) {
				ya = 0;
			} else {
				ya += 0.99;
			}
			if (keyJump) {
				if(y > 0) {
					if(onGround) {
						ya -= 10 + Math.abs(xa) * 0.5;
					}
					keyJump = false;
				}
			}

			y += ya;
			x += xa;
			repaint();
		}

	}

	public void update(Graphics g) {
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, G_WIDTH, G_HEIGHT);

		g2d.setColor(Color.WHITE);
		g2d.fillRect((int) x, (int) y, 20, 20);

		g2d.setColor(Color.WHITE);
		g2d.drawString("x: " + x, 10, 10);
		g2d.drawString("y: " + y, 10, 20);
		g2d.drawString("xa: " + xa, 10, 30);
		g2d.drawString("ya: " + ya, 10, 40);
		g2d.drawString("OnGround: " + onGround, 10, 50);
		paint(g);
	}

	public void paint(Graphics g) {
		g.drawImage(backbuffer, 0, 0, null);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			keyJump = true;
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			keyLeft = true;
			xa = -5;
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			keyRight = true;
			xa = 5;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			keyJump = false;
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			keyLeft = false;
			if(xa < 0) {
				xa = 0;
			}
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			keyRight = false;
			if(xa > 0) {
				xa = 0;
			}
			
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		TestJump tj = new TestJump();
		frame.setLayout(new BorderLayout());
		frame.add(tj, BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setVisible(true);

		tj.start();

	}
}