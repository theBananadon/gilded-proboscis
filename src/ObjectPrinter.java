import java.awt.*;

public class ObjectPrinter {

    static GamePanel gp;
    static Player player;

    public ObjectPrinter(GamePanel gp, Player player){
        this.gp = gp;
        ObjectPrinter.player = player;
    }


    public static Point[][] convertPoints(Point[] points, int[] distance){
        Point[][] newPoints = new Point[4][2];
        double[] n = player.normal;
        double[] xAxis = player.xAxis;
        double[] yAxis = player.yAxis;
        for(int i = 0; i < points.length; i++){
            double[] r_P = new double[]{points[i].x - player.x, points[i].y - player.y, distance[i] - player.z};
            double s = n[0] * r_P[0] + n[2] * r_P[2];
            double x2 = 75 * ((xAxis[0] * r_P[0] + xAxis[2] * r_P[2]) / (1 + s / 20)) + 432;
            double y2 = 75 * ((r_P[1]) / (1 + s / 20)) + 336;
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
            if(points[i][1].x >= -2 && points[i][1].x <= 160){
                return true;
            }
        }

        return false;
    }

    public static void paint(Graphics2D g2d, Entity entity){
        Point[][] paintPoints = convertPoints(entity.points, entity.distance);
        if(isDrawable(paintPoints)){
            int[] xPoints = new int[4];
            int[] yPoints = new int[4];
            for(int i = 0; i < 4; i++){
                xPoints[i] = paintPoints[i][0].x;
                yPoints[i] = paintPoints[i][0].y;
            }
            int scaleConstant = 0;
            if(gp.isFlashLightOn){
                scaleConstant++;
            }
            Point dis = entity.calculateCentre(player);
            double max = Math.max(0, Math.min(200 * (50 + 100 * scaleConstant - dis.distance(0,0))/ 200.0, 255));
            if(entity instanceof Wall) {
                g2d.setColor(new Color((int) (max), (int) (max), (int) max));
                g2d.fillPolygon(xPoints, yPoints, 4);
            }

            if(entity instanceof TaskObject || entity instanceof Monster){
                g2d.drawImage(entity.defaultImage, paintPoints[3][0].x, paintPoints[3][0].y, paintPoints[2][0].x - paintPoints[3][0].x, paintPoints[3][0].y - paintPoints[0][0].y, null);

            }

        }
    }




}