import java.awt.*;

public class Floor extends Entity{
    int colorIdentity;

    public Floor(GamePanel gp, Point[] points, int[] distance, int colorIdentity) {
        super(gp, points, distance);
        this.colorIdentity = colorIdentity;

    }
}
