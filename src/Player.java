public class Player {
    public int x;
    public int y;
    public int z;
    double xAngle = 0;

    int n = 20;
    double[] xAxis = new double[]{Math.cos(xAngle), 0, Math.sin(xAngle)};
    double[] yAxis = new double[]{0,1,0};
    double[] normal = new double[]{Math.cos(xAngle + Math.PI / 2), 0, Math.sin(xAngle + Math.PI / 2)};
    boolean right = false, left = false, forward = false, backward = false;
    boolean turnRight = false, turnLeft = false, turnUp = false, turnDown = false;
}
