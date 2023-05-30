import java.awt.*;

public class ObjectPrinter {

    Point[] points;
    int[] distance;
    Player player;


    public ObjectPrinter(Point[] points, int[] distance, Player player){
        this.points = points;
        this.distance = distance;
        this.player = player;
    }

    public double calculateCentre(){
        double xAvg = 0, yAvg = 0, zAvg = 0;

        int num = points.length;
        for (int i = 0; i < num; i++) {
            xAvg += Math.abs((player.x - points[i].x) / num);
            yAvg += Math.abs((player.y - points[i].y) / num);
            zAvg += Math.abs((player.z - distance[i]) / num);
        }

        return xAvg * xAvg + yAvg * yAvg + zAvg * zAvg;
    }

    public Point[][] convertPoints(){
        Point[][] newPoints = new Point[4][2];
        double[] n = player.normal;
        double[] xAxis = player.xAxis;
        double[] yAxis = player.yAxis;
        for(int i = 0; i < points.length; i++){
            double[] r_P = new double[]{points[i].x - player.x, points[i].y - player.y, distance[i] - player.z};
            double s = n[0] * r_P[0] + n[2] * r_P[2];
            double x2 = 300 * ((xAxis[0] * r_P[0] + xAxis[2] * r_P[2]) / (1 + s / 20)) + 300;
            double y2 = 300 * ((r_P[1]) / (1 + s / 20)) + 300;
            newPoints[i][0] = new Point((int) x2, (int) y2);
            newPoints[i][1] = new Point((int) s, 1);
        }

        return newPoints;
    }

    public boolean isDrawable(Point[][] points){
        for(int i = 0; i < points.length; i++){
            if(points[i][1].x >= -16 && points[i][1].x <= 60){
                return true;
            }
        }

        return false;
    }

    public void paint(Graphics2D g2d){
        Point[][] paintPoints = convertPoints();
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
