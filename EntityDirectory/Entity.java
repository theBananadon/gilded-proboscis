import java.awt.*;
import java.awt.image.BufferedImage;

public class Entity {
    public Point[] points;
    public int[] distance;
    public GamePanel gp;
    public double x;
    public double y;
    public double z;
    int tileX = 0; int tileZ = 0;
    public BufferedImage defaultImage;
    int colorIdentity;


    public Entity(GamePanel gp, double x, double y, double z){
        this.gp = gp;
        this.x = x;
        this.y = y;
        this.z = z;
        this.tileX = (int) (x / gp.TILE_SIZE);
        this.tileZ = (int) (x / gp.TILE_SIZE);
    }

    public Entity(GamePanel gp, Point[] points, int[] distance){
        this.gp = gp;
        this.points = points;
        this.distance = distance;

    }

    public Point calculateCentre(Player player){
        double xAvg = 0, yAvg = 0, zAvg = 0;

        int num = points.length;
        for (int i = 0; i < num; i++) {
            xAvg += Math.abs((player.x - points[i].x) / num);
            yAvg += Math.abs((player.y - points[i].y) / num);
            zAvg += Math.abs((player.z - distance[i]) / num);
        }

        return new Point((int) xAvg, (int) zAvg);
    }

    public Point calculateCentre2(Player player){
        return new Point((int) (gp.TILE_SIZE * x - player.x), (int) (gp.TILE_SIZE * z - player.z));
    }


}