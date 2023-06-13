import java.awt.*;

public class Tasks extends Entity{
    boolean isCompleted = false;
    Wall[] taskWalls;
    public Tasks(GamePanel gp,double x, double y, double z) {
        super(gp, x, y, z);
        this.tileX = (int) (x / gp.TILE_SIZE);
        this.tileZ = (int) (z / gp.TILE_SIZE);
        createWall();
    }

    public void createWall(){
        taskWalls = new Wall[4];
        int[] zValues1 = new int[4];
        java.awt.Point[] points1 = new java.awt.Point[4];
        points1[0] = new java.awt.Point((int) (gp.TILE_SIZE * x + 16), (int) y);
        points1[1] = new java.awt.Point((int) (gp.TILE_SIZE * x + 16), (int) y);
        points1[2] = new java.awt.Point((int) (gp.TILE_SIZE * x + 16),12);
        points1[3] = new java.awt.Point((int) (gp.TILE_SIZE * x + 16),12);
        zValues1 = new int[]{(int) (gp.TILE_SIZE * z), (int) (gp.TILE_SIZE * z + 16), (int) (gp.TILE_SIZE * z + 16), (int) (gp.TILE_SIZE * z)};

        int[] zValues2 = new int[4];
        java.awt.Point[] points2 = new java.awt.Point[4];
        points2[0] = new java.awt.Point((int) (gp.TILE_SIZE * x), (int) y);
        points2[1] = new java.awt.Point((int) (gp.TILE_SIZE * x), (int) y);
        points2[2] = new java.awt.Point((int) (gp.TILE_SIZE * x),12);
        points2[3] = new java.awt.Point((int) (gp.TILE_SIZE * x),12);
        zValues2 = new int[]{(int) (gp.TILE_SIZE * z), (int) (gp.TILE_SIZE * z + 16), (int) (gp.TILE_SIZE * z + 16), (int) (gp.TILE_SIZE * z)};

        int[] zValues3 = new int[4];
        java.awt.Point[] points3 = new java.awt.Point[4];
        points3[0] = new java.awt.Point((int) (gp.TILE_SIZE * x), (int) y);
        points3[1] = new java.awt.Point((int) (gp.TILE_SIZE * x + 16), (int) y);
        points3[2] = new java.awt.Point((int) (gp.TILE_SIZE * x + 16),12);
        points3[3] = new java.awt.Point((int) (gp.TILE_SIZE * x),12);
        zValues3 = new int[]{(int) (gp.TILE_SIZE * z + 16), (int) (gp.TILE_SIZE * z + 16), (int) (gp.TILE_SIZE * z + 16), (int) (gp.TILE_SIZE * z + 16)};

        int[] zValues4 = new int[4];
        java.awt.Point[] points4 = new java.awt.Point[4];
        points4[0] = new java.awt.Point((int) (gp.TILE_SIZE * x), (int) y);
        points4[1] = new java.awt.Point((int) (gp.TILE_SIZE * x + 16), (int) y);
        points4[2] = new java.awt.Point((int) (gp.TILE_SIZE * x + 16),12);
        points4[3] = new java.awt.Point((int) (gp.TILE_SIZE * x),12);
        zValues4 = new int[]{(int) (gp.TILE_SIZE * z), (int) (gp.TILE_SIZE * z), (int) (gp.TILE_SIZE * z), (int) (gp.TILE_SIZE * z)};

        taskWalls[0] = new Wall(gp, points1, zValues1);
        taskWalls[0].colorIdentity = 2;
        taskWalls[1] = new Wall(gp, points2, zValues2);
        taskWalls[1].colorIdentity = 2;
        taskWalls[2] = new Wall(gp, points3, zValues3);
        taskWalls[2].colorIdentity = 2;
        taskWalls[3] = new Wall(gp, points4, zValues4);
        taskWalls[3].colorIdentity = 2;
        points = points1;
        distance = zValues1;

    }

    public boolean isNearPlayer(){
        return Math.abs(x - gp.player.tileX) <= 1.5 && Math.abs(z - gp.player.tileZ) <= 1.5;

    }

}