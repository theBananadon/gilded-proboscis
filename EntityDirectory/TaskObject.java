import java.awt.image.BufferedImage;

public class TaskObject extends Entity{

    public TaskObject(GamePanel gp, double x, double y, double z, BufferedImage taskSprite) {
        super(gp, x, y, z);
        super.defaultImage = taskSprite;

    }



}