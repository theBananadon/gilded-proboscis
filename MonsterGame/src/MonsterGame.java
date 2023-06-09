import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MonsterGame extends JPanel implements ActionListener {
    private Timer timer;
    private Noctis noctis;
    private Boxis boxis;
    private Player player;
    private boolean isGameRunning;

    public MonsterGame() {
        setPreferredSize(new Dimension(800, 600));
        noctis = new Noctis(10, 1, 1000, 100, 100, 50, "Super_Munci.png");
        boxis = new Boxis(15, 1, 5000, 500, 500, 100, "boxis.png");
        player = new Player(400, 300);
        isGameRunning = true;
        timer = new Timer(10, this);
        timer.start();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        noctis.draw(g);
        boxis.draw(g);
        player.draw(g);
    }

    public void actionPerformed(ActionEvent e) {
        if (isGameRunning) {
            noctis.update(player);
            boxis.update(player);
            player.move();
            checkInteractions();
            repaint();
        }
    }

    private void checkInteractions() {
        if (noctis.isNearPlayer(player)) {
            System.out.println("Noctis is near the player!");
            // Implement interaction logic when Noctis is near the player
        }
        if (boxis.isNearPlayer(player)) {
            System.out.println("Boxis is near the player!");
            // Implement interaction logic when Boxis is near the player
        }
    }


}

abstract class Monster {
    protected int visionRange;
    protected int speed;
    protected int reactionTime;
    protected int respawnX;
    protected int respawnY;
    protected int respawnRange;

    protected int x;
    protected int y;
    protected boolean isChasing;
    protected boolean isVisible;

    public Monster(int visionRange, int speed, int reactionTime, int respawnX, int respawnY, int respawnRange) {
        this.visionRange = visionRange;
        this.speed = speed;
        this.reactionTime = reactionTime;
        this.respawnX = respawnX;
        this.respawnY = respawnY;
        this.respawnRange = respawnRange;

        this.x = respawnX;
        this.y = respawnY;
        this.isChasing = false;
        this.isVisible = true;
    }

    public void update(Player player) {
        if (!isChasing) {
            if (isVisible) {
                if (canSeePlayer(player)) {
                    isChasing = true;
                    System.out.println("Chasing player!");
                } else {
                    move();
                }
            } else {
                respawn();
            }
        } else {
            if (player.isFlashingLight()) {
                isVisible = false;
                System.out.println("Monster disappeared!");
            } else {
                chase(player);
            }
        }
    }

    public boolean canSeePlayer(Player player) {
        int playerX = player.getX();
        int playerY = player.getY();

        int distance = (int) Math.sqrt((playerX - x) * (playerX - x) + (playerY - y) * (playerY - y));

        return distance <= visionRange && player.isVisible();
    }

    public abstract void move();

    public void chase(Player player) {
        int playerX = player.getX();
        int playerY = player.getY();

        if (x < playerX) {
            x += speed;
        } else if (x > playerX) {
            x -= speed;
        }

        if (y < playerY) {
            y += speed;
        } else if (y > playerY) {
            y -= speed;
        }
    }

    public void respawn() {
        Random random = new Random();
        x = respawnX + random.nextInt(respawnRange) - respawnRange / 2;
        y = respawnY + random.nextInt(respawnRange) - respawnRange / 2;
        isVisible = true;
        System.out.println("Monster respawned at (" + x + ", " + y + ")");
    }

    public abstract void draw(Graphics g);

    public boolean isNearPlayer(Player player) {
        int playerX = player.getX();
        int playerY = player.getY();

        int distance = (int) Math.sqrt((playerX - x) * (playerX - x) + (playerY - y) * (playerY - y));

        return distance == 0;
    }
}

class Noctis extends Monster {
    public Noctis(int visionRange, int speed, int reactionTime, int respawnX, int respawnY, int respawnRange, String s) {
        super(visionRange, speed, reactionTime, respawnX, respawnY, respawnRange);
    }

    @Override
    public void move() {
        // Implement pathfinding logic for Noctis
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x, y, 20, 20);
    }
}

class Boxis extends Monster {
    public Boxis(int visionRange, int speed, int reactionTime, int respawnX, int respawnY, int respawnRange, String s) {
        super(visionRange, speed, reactionTime, respawnX, respawnY, respawnRange);
    }

    @Override
    public void move() {
        // Implement pathfinding logic for Boxis
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillOval(x, y, 20, 20);
    }
}

class Player {
    private int x;
    private int y;
    private boolean isFlashingLight;
    private boolean isVisible;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.isFlashingLight = false;
        this.isVisible = true;
    }

    public void move() {
        // Implement player movement logic
    }

    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillOval(x, y, 20, 20);
    }

    public boolean isFlashingLight() {
        return isFlashingLight;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
