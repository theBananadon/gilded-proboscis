import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

abstract class Monster extends Entity{
    protected int visionRange;
    protected double speed;
    protected int reactionTime;
    protected int respawnX;
    protected int respawnY;
    protected int respawnRange;

    protected double x;
    protected double y;
    protected double z;
    protected boolean isChasing;
    protected boolean isVisible;

    public Monster(int visionRange, double speed, int reactionTime, int respawnX, int respawnY, int respawnRange, GamePanel gp) {
        super(gp, null, null);
        this.visionRange = visionRange;
        this.speed = speed;
        this.reactionTime = reactionTime;
        this.respawnX = respawnX;
        this.respawnY = respawnY;
        this.respawnRange = respawnRange;

        this.x = respawnX;
        this.y = -3;
        this.z = respawnY;
        this.isChasing = false;
        this.isVisible = true;
        points = new Point[]{new Point((int) (x - 5), -20), new Point((int) (x + 5), -20), new Point((int) (x + 5), 0), new Point((int) (x - 5), 0)};
        distance = new int[]{(int) z, (int) z, (int) z, (int) z};
    }

    public void update(Player player) {
        move();
        if (!isChasing) {
            if (isVisible) {
                if (canSeePlayer()) {
                    isChasing = true;
                } else {
                    move();
                }
            } else {
                respawn();
            }
        } else {
            if (gp.isFlashLightOn) {
                isVisible = false;
                respawn();
                isVisible = true;
            } else {
                chase(player);
            }
        }
    }

    public boolean canSeePlayer() {
        double playerX = gp.player.x;
        double playerZ = gp.player.z;

        int distance = (int) Math.sqrt((playerX - x) * (playerX - x) + (playerZ - z) * (playerZ - z));

        return distance <= visionRange && !gp.isFlashLightOn;
    }

    public abstract void move();

    public void chase(Player player) {
        double playerX = gp.player.x;
        double playerZ = gp.player.z;

        if (x < playerX) {
            x += speed;
        } else if (x > playerX) {
            x -= speed;
        }

        if (z < playerZ) {
            z += speed;
        } else if (z > playerZ) {
            z -= speed;
        }
    }

    public void respawn() {
        x = (int) (respawnX + Math.random() * respawnRange - respawnRange / 2);
        z = (int) (respawnY + Math.random() * respawnRange - respawnRange / 2);
        isVisible = true;

    }


    public boolean isNearPlayer() {
        double playerX = gp.player.x;
        double playerZ = gp.player.z;

        int distance = (int) Math.sqrt((playerX - x) * (playerX - x) + (playerZ - z) * (playerZ - z));

        return distance == 0;
    }
}

class Noctis extends Monster {
    Node[][] node;
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> checkedList = new ArrayList<>();

    public Noctis(int visionRange, double speed, int reactionTime, int respawnX, int respawnY, int respawnRange, GamePanel gp, String s) {
        super(visionRange, speed, reactionTime, respawnX, respawnY, respawnRange, gp);
        try {
            defaultImage = ImageIO.read(new File("images\\boxis.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void move() {
        tileX = (int)(x / gp.TILE_SIZE);
        tileZ = (int) (z / gp.TILE_SIZE);
        int[][] map = gp.currentMap;

        node = new Node[map.length][map.length];


        boolean goalReached = false;
        int playerX = gp.player.tileX;
        int playerZ = gp.player.tileZ;
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map.length; j++){
                node[i][j] = new Node(i,j);
                if(map[i][j] == 0){
                    node[i][j].solid = true;
                } else {
                    node[i][j].solid = false;
                }
            }
        }
        Node startNode = new Node(tileX, tileZ);
        node[tileX][tileZ] = startNode;
        startNode.start = true;
        Node currentNode = startNode;
        Node endNode = new Node(playerX, playerZ);
        node[playerX][playerZ] = endNode;
        endNode.goal = true;
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map.length; j++){
                node[i][j].gCost = Math.abs(i - startNode.col) + Math.abs(j - startNode.row);
                node[i][j].hCost = Math.abs(i - endNode.col) + Math.abs(j - endNode.col);
                node[i][j].fCost = node[i][j].gCost + node[i][j].hCost;
            }
        }


        search(goalReached, currentNode, endNode, map);
        trackPath(startNode, endNode);
        if(node[tileX - 1][tileZ].isPath){
            x -= speed;
        }
        if(node[tileX + 1][tileZ].isPath){
            x += speed;
        }
        if(node[tileX][tileZ - 1].isPath){
            z -= speed;
        }
        if(node[tileX][tileZ +1].isPath){
            z += speed;
        }


        if(Math.abs(tileX - playerX) < 3 && Math.abs(tileZ - playerZ) < 3) {
            double distanced = Math.sqrt((x - gp.player.x) * (x - gp.player.x) + (z - gp.player.z) * (z - gp.player.z));
            double xChange = (gp.player.x - x);
            double zChange = (gp.player.z - z);

            x += speed * xChange / distanced;
            z += speed * zChange / distanced;
        }
        points = new Point[]{new Point((int) (x - 2 * Math.cos(gp.player.xAngle)), -8), new Point((int) (x + 2 * Math.cos(gp.player.xAngle)), -8), new Point((int) (x + 2 * Math.cos(gp.player.xAngle)), 0), new Point((int) (x - 2 * Math.cos(gp.player.xAngle)), 0)};
        distance = new int[]{(int) (z - 2 * Math.sin(gp.player.xAngle)), (int) (z + 2 * Math.sin(gp.player.xAngle)), (int) (z + 2 * Math.sin(gp.player.xAngle)), (int) (z - 2 * Math.sin(gp.player.xAngle))};




    }

    private void search(boolean goalReached, Node currentNode, Node goalNode, int[][] map){
        int step = 0;
        while(!goalReached && step < 34 * 34){
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);
            if(row >= 1) {
                openNode(node[col][row - 1], currentNode);
            }
            if(row < map.length - 1) {
                openNode(node[col][row + 1], currentNode);
            }
            if(col >= 1) {
                openNode(node[col - 1][row], currentNode);
            }
            if(col < map.length - 1){
                openNode(node[col + 1][row], currentNode);
            }


            int bestNodeIndex = 0;
            int bestNodefCost = 999;
            for(int i = 0; i < openList.size(); i++){
                if(openList.get(i).fCost < bestNodefCost){
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                else if(openList.get(i).fCost == bestNodefCost){
                    if(openList.get(i).gCost < openList.get(bestNodeIndex).gCost){
                        bestNodeIndex = i;
                    }
                }
            }
            currentNode = openList.get(bestNodeIndex);
            if(currentNode == goalNode){
                goalNode.parent = currentNode.parent;
                goalReached = true;

            }
            step++;
        }
    }

    private void openNode(Node node, Node currentNode){
        if(!node.open && !node.checked && !node.solid){
            node.setAtOpen();
            node.parent = currentNode;
            openList.add(node);

        }
    }

    private void trackPath(Node startNode, Node goalNode){
        Node current = goalNode;
        while(current != startNode){
            current = current.parent;
            if(current == null) {
                break;
            }

            if(current != startNode){
                current.setAsPath();
            }
        }
    }

}

class Boxis extends Monster {
    public Boxis(int visionRange, int speed, int reactionTime, int respawnX, int respawnY, int respawnRange, GamePanel gp, String s) {
        super(visionRange, speed, reactionTime, respawnX, respawnY, respawnRange, gp);
    }

    @Override
    public void move() {
        // Implement pathfinding logic for Boxis
    }



}

class Node{

    Node parent;
    int col;
    int row;
    int gCost;
    int hCost;
    int fCost;
    boolean start;
    boolean goal;
    boolean solid = false;
    boolean open;
    boolean checked;
    boolean isPath;


    public Node(int col, int row){
        this.col = col;
        this.row = row;

    }

    public void setAtOpen(){
        open = true;
    }

    public void setAsChecked(){
        checked = true;
    }

    public void setAsPath(){
        isPath = true;
    }

}