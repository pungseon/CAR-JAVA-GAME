package Cars;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import static java.awt.image.ImageObserver.ABORT;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Balloon
 */
public class work extends JPanel implements ActionListener, KeyListener {

    private int space;
    private int width = 80;
    private int height = 70;
    private int speed = 1;
    private int WIDTH = 500;
    private int HEIGHT = 700;
    private int move = 20;
    private int count = 1;
    private ArrayList<Rectangle> ocars;
    private ArrayList<Rectangle> line;
    private Rectangle car;
    private Random rand;
    private Timer gameTimer;
    private int seconds = 0;
    private int minutes = 0;
    int score;
    int time;
    int highScore;
    boolean isFinished;
    Boolean linef = true;
    BufferedImage user;
    BufferedImage op1;
    BufferedImage op2;
    BufferedImage bg;
    BufferedImage map;
    Timer t;

    public work() throws IOException {
        bg = ImageIO.read(new File("pic\\bg.png"));
        map = ImageIO.read(new File("pic\\map.png"));
        user = ImageIO.read(new File("pic\\user.png"));
        op1 = ImageIO.read(new File("pic\\car 2.png"));
        op2 = ImageIO.read(new File("pic\\car 3.png"));
        t = new Timer(20, this);
        rand = new Random();
        ocars = new ArrayList<Rectangle>();
        line = new ArrayList<Rectangle>();
        car = new Rectangle(WIDTH / 2 - 80, HEIGHT - 100, width - 20, height);
        space = 300;
        speed = 10;
        isFinished = false; //when false, game is running, when true, game has ended
        score = highScore = 0;  //initialling setting the current score and the highscore to zero
        addKeyListener(this);
        setFocusable(true);
        addocars(true);
        addocars(true);
        addocars(true);
        addocars(true);
        addline(true);
        addline(true);
        addline(true);
        addline(true);
        addline(true);
        addline(true);
        addline(true);
        t.start();
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                seconds++;
                if (seconds >= 60) {
                    seconds = 0;
                    minutes++;
                }
            }
        });
        gameTimer.start();
    }

    public void addline(Boolean first) {
        int x = WIDTH / 2 - 2;
        int y = 700;
        int width = 4;
        int height = 100;
        int sp = 130;
        if (linef) {
            line.add(new Rectangle(x, y - (line.size() * sp), width, height));
        } else {
            line.add(new Rectangle(x, line.get(line.size() - 1).y - sp, width, height));
        }
    }

    public void addocars(boolean first) {
        int positionx = rand.nextInt() % 2;
        int x = 0;
        int y = 0;
        int Width = width - 20;
        int Height = height;
        if (positionx == 0) {
            x = WIDTH / 2 - 90;
        } else {
            x = WIDTH / 2 + 20;
        }

        if (first || ocars.isEmpty()) {
            ocars.add(new Rectangle(x, y - 100 - (ocars.size() * space), Width, Height));
        } else {
            ocars.add(new Rectangle(x, ocars.get(ocars.size() - 1).y - 400, Width, Height));
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bg, 0, 0, null);
        g.drawImage(map, WIDTH / 2 - 135, 0, null);
        g.setColor(Color.WHITE);
        for (Rectangle rect : line) {
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
        }
        g.drawImage(user, car.x, car.y, null);
        g.setColor(Color.PINK);
        for (Rectangle rect : ocars) {
            g.drawImage(op1, rect.x, rect.y, null);
        }
        g.setColor(Color.RED);
        g.fillRect(10, 10, 100, 40);
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 20, 30);
        g.setColor(Color.BLUE);
        g.fillRect(10, 60, 100, 40);
        g.setColor(Color.WHITE);
        g.drawString("Time: " + minutes + " : " + seconds, 20, 80);
    }

    public void actionPerformed(ActionEvent e) {
        Rectangle rect;
        int speedIncrement = 0;
        for (int i = 0; i < ocars.size(); i++) {
            rect = ocars.get(i);
            rect.y += speed;
            if (rect.y + rect.height > HEIGHT) {
                score += 10;
            }
            if (ocars.get(i).y > HEIGHT) {
                ocars.remove(i);
                addocars(false);
                score += 10;
                time++;
                if (time >= 20) {
                    time = 0;
                    speedIncrement++;
                    if (speedIncrement % 20 == 0) {
                        speed++;
                    }
                }
            }
        }
        if (score > highScore) {
            highScore = score;
        }
        for (Rectangle r : ocars) {
            if (r.intersects(car)) {
                isFinished = true;
                t.stop();
                JOptionPane.showMessageDialog(this, "Game Over");
                int result = JOptionPane.showConfirmDialog(this, "Game Over! Do you want to play again?");
                if (result == JOptionPane.OK_OPTION) {
                    resetGame();
                } else if (result == JOptionPane.CANCEL_OPTION) {
                    JOptionPane.showMessageDialog(this, "Your High Score: " + highScore);
                    System.exit(0);
                }
            }
        }
        for (int i = 0; i < ocars.size(); i++) {
            rect = ocars.get(i);
            if (rect.y + rect.height > HEIGHT) {
                ocars.remove(rect);
                addocars(false);
            }
        }
        for (int i = 0; i < line.size(); i++) {
            rect = line.get(i);
            if (count % 1000 == 0) {
                speed++;
            }
            rect.y += speed;
        }
        for (int i = 0; i < line.size(); i++) {
            rect = line.get(i);
            if (rect.y > HEIGHT) {
                line.remove(rect);
                addline(false);
            }
        }
        repaint();
    }

    public void resetGame() {
        ocars.clear();
        line.clear();
        car = new Rectangle(WIDTH / 2 - 80, HEIGHT - 100, width - 20, height);
        isFinished = false;
        score = 0;
        seconds = 0;
        minutes = 0;
        t.start();
        addocars(true);
        addocars(true);
        addocars(true);
        addline(true);
        addline(true);
        addline(true);
        addline(true);
        addline(true);
        addline(true);
        addline(true);
    }

    public void moveup() {
        if (car.y - move < 0) {
            System.out.println("\b");
        } else {
            car.y -= move;
        }
    }

    public void movedown() {
        if (car.y + move + car.height > HEIGHT - 1) {
            System.out.println("\b");
        } else {
            car.y += move;
        }
    }

    public void moveleft() {
        if (car.x - move < WIDTH / 2 - 90) {
            System.out.println("\b");
        } else {
            car.x -= move;
        }
    }

    public void moveright() {
        if (car.x + move > WIDTH / 2 + 10) {
            System.out.println("\b");
        } else {
            car.x += move;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                moveup();
                break;
            case KeyEvent.VK_DOWN:
                movedown();
                break;
            case KeyEvent.VK_LEFT:
                moveleft();
                break;
            case KeyEvent.VK_RIGHT:
                moveright();
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                moveup();
                break;
            case KeyEvent.VK_DOWN:
                movedown();
                break;
            case KeyEvent.VK_LEFT:
                moveleft();
                break;
            case KeyEvent.VK_RIGHT:
                moveright();
                break;
            default:
                break;
        }
    }
}
