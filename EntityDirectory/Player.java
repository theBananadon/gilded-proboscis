import java.util.ArrayList;

public class Player extends Entity {
    double xAngle = 0;
    int[][] map;

    double[] xAxis = new double[]{Math.cos(xAngle), 0, Math.sin(xAngle)};
    double[] yAxis = new double[]{0,1,0};
    double[] normal = new double[]{Math.cos(xAngle + Math.PI / 2), 0, Math.sin(xAngle + Math.PI / 2)};
    boolean right = false, left = false, forward = false, backward = false;
    boolean turnRight = false, turnLeft = false, turnUp = false, turnDown = false;
    boolean isWorkingOnTask = false;

    boolean[] taskObjectCollection = new boolean[3];
    boolean[] taskCompletion = new boolean[3];
    boolean isKeyCollected = false;
    int taskTime = 0;

    public Player(GamePanel gp, double x, double y, double z, int[][] map) {
        super(gp, x, y, z);
        this.map = map;
    }

    public void updatePlayer(Tasks[] tasks, TaskObject[] taskObjects){
        if(gp.playState) {
            updateAxis();
            if (forward) {
                if(checkCollision(5 * normal[0], 5 * normal[2])){
                    z += 2 * normal[2];
                    x += 2 * normal[0];
                }
            }
            if (backward) {
                if(checkCollision(-5 * normal[0], -5 * normal[2])){
                    z -= 2 * normal[2];
                    x -= 2 * normal[0];
                }

            }
            if (right) {
                if(checkCollision(5 * normal[2], -5 * normal[0])){
                    x += 2 * normal[2];
                    z -= 2 * normal[0];
                }


            }
            if (left) {
                if(checkCollision(-5 * normal[2], 5 * normal[0])){
                    x -= 2 * normal[2];
                    z += 2 * normal[0];
                }


            }
            if (turnRight) {
                xAngle = (xAngle - Math.PI / 50) % (2 * Math.PI);
                x -= 20 * (- Math.sin(xAngle) + Math.sin(xAngle - Math.PI / 50));
                z -= 20 * ( Math.cos(xAngle) - Math.cos(xAngle - Math.PI / 50));

            }
            if (turnLeft) {
                xAngle = (xAngle + Math.PI / 50) % (2 * Math.PI);
                x -= 20 * (- Math.sin(xAngle) + Math.sin(xAngle + Math.PI / 50));
                z -= 20 * (Math.cos(xAngle) - Math.cos(xAngle + Math.PI / 50));
            }
        }
        isTaskObjectBeingCollected(taskObjects);
        isWorkingOnTask(tasks);
        tileX = (int) ((x) / gp.TILE_SIZE);
        tileZ = (int) ((z) / gp.TILE_SIZE);
    }

    public boolean checkCollision(double xSpeed, double zSpeed){
        int finalTileX = (int) ((x + xSpeed) / gp.TILE_SIZE);
        int finalTileZ = (int) ((z + zSpeed) / gp.TILE_SIZE);
        return map[finalTileX][finalTileZ] != 0;
    }

    public void updateAxis(){
        xAxis = new double[]{Math.cos(xAngle), 0 ,Math.sin(xAngle)};
        yAxis = new double[]{0,1,0};
        normal = new double[]{Math.cos(xAngle + Math.PI / 2), 0, Math.sin(xAngle + Math.PI / 2)};
    }

    public void isTaskObjectBeingCollected(TaskObject[] taskObjects){
        for(int i = 0; i < taskObjects.length; i++){
            if(taskObjects[i] != null){
                if(checkCollision(taskObjects[i])){
                    taskObjectCollection[i] = true;
                    taskObjects[i] = null;
                }
            }
        }
    }

    public void isWorkingOnTask(Tasks[] tasks) {
        for(int i = 0; i < tasks.length; i++) {
            if(tasks[i] != null) {
                if (tasks[i].tileX == tileX && tasks[i].tileZ == tileZ && isWorkingOnTask) {
                    taskTime++;
                    if (taskTime >= 150) {
                        tasks[i].isCompleted = true;
                    }
                } else {
                    taskTime = 0;
                }
            }
        }
    }

    public boolean checkCollision(Entity entity){
        return tileX == entity.tileX && tileZ == entity.tileZ;
    }


}