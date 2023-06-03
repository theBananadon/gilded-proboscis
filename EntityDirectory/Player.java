

public class Player extends Entity {
    double xAngle = 0;

    double[] xAxis = new double[]{Math.cos(xAngle), 0, Math.sin(xAngle)};
    double[] yAxis = new double[]{0,1,0};
    double[] normal = new double[]{Math.cos(xAngle + Math.PI / 2), 0, Math.sin(xAngle + Math.PI / 2)};
    boolean right = false, left = false, forward = false, backward = false;
    boolean turnRight = false, turnLeft = false, turnUp = false, turnDown = false;

    public Player(GamePanel gp, double x, double y, double z) {
        super(gp, x, y, z);

    }

    public void updatePlayer(){
        if(gp.playState) {
            updateAxis();
            if (forward) {
                z += 5 * normal[2];
                x += 5 * normal[0];
            }
            if (backward) {
                z -= 5 * normal[2];
                x -= 5 * normal[0];
            }
            if (right) {
                x += 5 * normal[2];
                z -= 5 * normal[0];

            }
            if (left) {
                x -= 5 * normal[2];
                z += 5 * normal[0];

            }
            if (turnRight) {
                xAngle = (xAngle - Math.PI / 50) % (2 * Math.PI);
            }
            if (turnLeft) {
                xAngle = (xAngle + Math.PI / 50) % (2 * Math.PI);
            }
        }
    }

    public void updateAxis(){
        xAxis = new double[]{Math.cos(xAngle), 0 ,Math.sin(xAngle)};
        yAxis = new double[]{0,1,0};
        normal = new double[]{Math.cos(xAngle + Math.PI / 2), 0, Math.sin(xAngle + Math.PI / 2)};
    }
}
