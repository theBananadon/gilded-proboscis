import java.awt.*;
import java.awt.image.BufferedImage;

public class TaskObject extends Entity{
    double imageTick = 0;

    public TaskObject(GamePanel gp, double x, double y, double z, BufferedImage taskSprite) {
        super(gp, x, y, z);
        super.defaultImage = taskSprite;
        points = new Point[]{new Point((int) (x - 3), 2), new Point((int) (x + 3), 2), new Point((int) (x + 3), -2), new Point((int) (x - 3), -2)};
        distance = new int[]{(int) z, (int) z, (int) z, (int) z};
        tileX = (int) (x / gp.TILE_SIZE);
        tileZ = (int) (z / gp.TILE_SIZE);
    }

    public void updateImage(){

        points[0].x = (int) (x - 3 * Math.cos(imageTick));
        points[1].x = (int) (x + 3 * Math.cos(imageTick));
        points[2].x = (int) (x + 3 * Math.cos(imageTick));
        points[3].x = (int) (x - 3 * Math.cos(imageTick));

        distance[0] = (int) (z - 3 * Math.sin(imageTick));
        distance[1] = (int) (z + 3 * Math.sin(imageTick));
        distance[2] = (int) (z + 3 * Math.sin(imageTick));
        distance[3] = (int) (z - 3 * Math.sin(imageTick));

        imageTick += 0.1;

    }



}