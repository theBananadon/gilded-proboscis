import java.awt.*;

public class ObjectPrinter {

    GamePanel gp;
    static Player player;

    public ObjectPrinter(GamePanel gp, Player player){
        this.gp = gp;
        ObjectPrinter.player = player;
    }

    public static Point calculateCentre(Point[] points, int[] distance){
        double xAvg = 0, yAvg = 0, zAvg = 0;

        int num = points.length;
        for (int i = 0; i < num; i++) {
            xAvg += Math.abs((player.x - points[i].x) / num);
            yAvg += Math.abs((player.y - points[i].y) / num);
            zAvg += Math.abs((player.z - distance[i]) / num);
        }

        return new Point((int) xAvg, (int) zAvg);
    }

    public static Point[][] convertPoints(Point[] points, int[] distance){
        Point[][] newPoints = new Point[4][2];
        double[] n = player.normal;
        double[] xAxis = player.xAxis;
        double[] yAxis = player.yAxis;
        for(int i = 0; i < points.length; i++){
            double[] r_P = new double[]{points[i].x - player.x, points[i].y - player.y, distance[i] - player.z};
            double s = n[0] * r_P[0] + n[2] * r_P[2];
            double x2 = 75 * ((xAxis[0] * r_P[0] + xAxis[2] * r_P[2]) / (1 + s / 20)) + 300;
            double y2 = 75 * ((r_P[1]) / (1 + s / 20)) + 300;
            newPoints[i][0] = new Point((int) x2, (int) y2);
            newPoints[i][1] = new Point((int) s, 1);
        }

        return newPoints;
    }

    public static boolean isDrawable(Point[][] points){
        if(points[1][0].y > points[2][0].y || points[0][0].y > points[3][0].y){
            return false;
        }
        for(int i = 0; i < points.length; i++){
            if(points[i][1].x >= -20 && points[i][1].x <= 160){
                return true;
            }
        }

        return false;
    }

    public static void paint(Graphics2D g2d, Point[][] paintPoints){
        if(isDrawable(paintPoints)){
            int[] xPoints = new int[4];
            int[] yPoints = new int[4];
            for(int i = 0; i < 4; i++){

                xPoints[i] = paintPoints[i][0].x;
                yPoints[i] = paintPoints[i][0].y;
                System.out.println(xPoints[i] + ", " + yPoints[i]);
            }
            g2d.setColor(Color.BLACK);
            g2d.fillPolygon(xPoints, yPoints, 4);
        }
    }




}
