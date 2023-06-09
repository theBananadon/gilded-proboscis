import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

abstract class Monster extends Entity{
    protected int visionRange;
    protected double speed;
    protected int reactionTime;
    protected int respawnX;
    protected int respawnY;
    protected int respawnRange;

    protected double x;
    protected double y;
    protected double z;
    protected boolean isChasing;
    protected boolean isVisible;

    public Monster(int visionRange, double speed, int reactionTime, int respawnX, int respawnY, int respawnRange, GamePanel gp) {
        super(gp, null, null);
        this.visionRange = visionRange;
        this.speed = speed;
        this.reactionTime = reactionTime;
        this.respawnX = respawnX;
        this.respawnY = respawnY;
        this.respawnRange = respawnRange;

        this.x = respawnX;
        this.y = -3;
        this.z = respawnY;
        this.isChasing = false;
        this.isVisible = true;
        points = new Point[]{new Point((int) (x - 5), -8), new Point((int) (x + 5), -8), new Point((int) (x + 5), 0), new Point((int) (x - 5), 0)};
        distance = new int[]{(int) z, (int) z, (int) z, (int) z};
    }

    public void update(Player player) {
        move();
        if (!isChasing) {
            if (isVisible) {
                if (canSeePlayer()) {
                    isChasing = true;
                } else {
                    move();
                }
            } else {
                respawn();
            }
        } else {
            if (gp.isFlashLightOn) {
                isVisible = false;
                respawn();
                isVisible = true;
            } else {
                chase(player);
            }
        }
    }

    public boolean canSeePlayer() {
        double playerX = gp.player.x;
        double playerZ = gp.player.z;

        int distance = (int) Math.sqrt((playerX - x) * (playerX - x) + (playerZ - z) * (playerZ - z));

        return distance <= visionRange && !gp.isFlashLightOn;
    }

    public abstract void move();

    public void chase(Player player) {
        double playerX = gp.player.x;
        double playerZ = gp.player.z;

        if (x < playerX) {
            x += speed;
        } else if (x > playerX) {
            x -= speed;
        }

        if (z < playerZ) {
            z += speed;
        } else if (z > playerZ) {
            z -= speed;
        }
    }

    public void respawn() {
        x = (int) (respawnX + Math.random() * respawnRange - respawnRange / 2);
        z = (int) (respawnY + Math.random() * respawnRange - respawnRange / 2);
        isVisible = true;

    }


    public boolean isNearPlayer(Player player) {
        double playerX = gp.player.x;
        double playerZ = gp.player.z;

        int distance = (int) Math.sqrt((playerX - x) * (playerX - x) + (playerZ - z) * (playerZ - z));

        return distance == 0;
    }
}

class Noctis extends Monster {

    public Noctis(int visionRange, double speed, int reactionTime, int respawnX, int respawnY, int respawnRange, GamePanel gp, String s) {
        super(visionRange, speed, reactionTime, respawnX, respawnY, respawnRange, gp);
        try {
            defaultImage = ImageIO.read(new File("images\\Super_Munci.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void move() {
        double distanced = Math.sqrt((x - gp.player.x) * (x - gp.player.x) + (z - gp.player.z) * (z - gp.player.z));
        double xChange = (gp.player.x - x) / distanced;
        double zChange = (gp.player.z - z) / distanced;

        x += speed * xChange * distanced / 100;
        z += speed * zChange * distanced / 100;

        points = new Point[]{new Point((int) (x - 2 * Math.cos(gp.player.xAngle)), -3), new Point((int) (x + 2 * Math.cos(gp.player.xAngle)), -3), new Point((int) (x + 2 * Math.cos(gp.player.xAngle)), 3), new Point((int) (x - 2 * Math.cos(gp.player.xAngle)), 3)};
        distance = new int[]{(int) (z - 2 * Math.sin(gp.player.xAngle)), (int) (z + 2 * Math.sin(gp.player.xAngle)), (int) (z + 2 * Math.sin(gp.player.xAngle)), (int) (z - 2 * Math.sin(gp.player.xAngle))};


    }

}

class Boxis extends Monster {
    public Boxis(int visionRange, int speed, int reactionTime, int respawnX, int respawnY, int respawnRange, GamePanel gp, String s) {
        super(visionRange, speed, reactionTime, respawnX, respawnY, respawnRange, gp);
    }

    @Override
    public void move() {
        // Implement pathfinding logic for Boxis
    }

}