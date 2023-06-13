import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class GamePanel extends JPanel implements Runnable{
    private final Thread thread;
    public final int TILE_SIZE = 32;
    Player player;
    public boolean startState = false, pauseState = false, playState = false, winState = false, loseState = false;
    public boolean allowEscape = false;
    int[][] currentMap;
    Wall[] walls;
    Floor[] floor;
    Wall[] taskWalls;
    ObjectPrinter obj;
    Wall testWall;
    public BufferedImage startScreen = null, victoryScreen1 = null, victoryScreen2 = null, deathScreen = null, creditScreen = null;
    boolean isMap = false;
    boolean isInventory = false;
    Tasks[] tasks = new Tasks[3];
    TaskObject[] taskObjects = new TaskObject[3];
    BufferedImage[] taskImages = new BufferedImage[3];
    boolean isFlashLightOn = false;
    boolean flashLightToggle = true;
    double flashLightBattery = 10;
    double chargingBattery = 0;
    Noctis nox;

    //mapmap = border
    //mapMapMap = Actual minimap (excluding border)
    public GamePanel(){
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(864,672));
        this.setBackground(new Color(0,0,0));
        getSprites();
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(playState) {
                    if (e.getKeyCode() == KeyEvent.VK_A) {
                        player.left = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_D) {
                        player.right = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_W) {
                        player.forward = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_S) {
                        player.backward = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        player.turnRight = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        player.turnLeft = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        player.turnUp = true;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        player.turnDown = true;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
                        playState = false;
                        pauseState = true;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_M && !isInventory){
                        isMap = true;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_I && !isMap){
                        isInventory = true;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_E){
                        player.isWorkingOnTask = true;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_SPACE && flashLightToggle){
                        if(flashLightBattery <= 0){
                            isFlashLightOn = false;
                        } else {
                            isFlashLightOn = !isFlashLightOn;
                        }
                        flashLightToggle = false;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_SPACE){
                        if(flashLightBattery <= 0){
                            chargingBattery += 1.0 / 30;
                        }
                        if(chargingBattery >= 2){
                            flashLightBattery = 10;
                            chargingBattery = 0;
                        }
                    }
                    if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
                        pauseState = true;
                        playState = false;
                    }
                }
                if(startState){
                    if(e.getKeyCode() == KeyEvent.VK_SPACE){
                        startState = false;
                        resetGame();
                        playState = true;
                    }
                }
                if(pauseState && allowEscape){
                    isMap = false;
                    if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
                        pauseState = false;
                        playState = true;
                        allowEscape = false;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                        pauseState = false;
                        startState = true;
                        resetGame();
                    }
                }

                if(winState || loseState){
                    if(e.getKeyCode() == KeyEvent.VK_SPACE){
                        winState = false;
                        loseState = false;
                        startState = true;
                    }
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(playState) {
                    if (e.getKeyCode() == KeyEvent.VK_A) {
                        player.left = false;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_D) {
                        player.right = false;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_W) {
                        player.forward = false;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_S) {
                        player.backward = false;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        player.turnRight = false;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        player.turnLeft = false;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_UP) {
                        player.turnUp = false;
                    }
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        player.turnDown = false;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_M){
                        isMap = false;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_I){
                        isInventory = false;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_SPACE && !flashLightToggle){
                        flashLightToggle = true;
                    }
                    if(e.getKeyCode() == KeyEvent.VK_E){
                        player.isWorkingOnTask = false;
                    }
                }
                if(pauseState){
                    if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && !allowEscape){
                        allowEscape = true;
                    }
                }
            }
        });
        startState = true;
        thread = new Thread(this);
        thread.start();
    }

    private void resetGame() {


        currentMap = makeMap();
//        while(!mapChecker(currentMap)){
//            currentMap = makeMap();
//        }

        tasks = new Tasks[3];
        int[] taskXTile = new int[tasks.length];
        int[] taskZTile = new int[tasks.length];
        int taskCount = 0;
        int totalWalls = 0;
        for(int i = 0; i < currentMap.length; i++){
            for(int j = 0; j < currentMap[i].length; j++){
                if(currentMap[i][j] == 0){
                    totalWalls += 4;
                }
                if(currentMap[i][j] == 9){
                    taskXTile[taskCount] = i;
                    taskZTile[taskCount] = j;
                    taskCount++;
                }

            }
        }

        for(int i = 0; i < tasks.length; i++){
            tasks[i] = new Tasks(this, taskXTile[i], 0, taskZTile[i]);
        }


        int wallCount = 0;
        int wallNumber = TILE_SIZE / 16;
        totalWalls = wallNumber * (totalWalls);
        walls = new Wall[totalWalls];

        for(int i = 0; i < currentMap.length; i++){
            for(int j = 0; j < currentMap[i].length; j++){
                if(currentMap[i][j] == 0){
                    int[] zValues = new int[4];
                    java.awt.Point[] points = new java.awt.Point[4];
                    points[0] = new java.awt.Point(wallNumber * 16 * i + TILE_SIZE,-5);
                    points[1] = new java.awt.Point(wallNumber * 16 * i + TILE_SIZE,-5);
                    points[2] = new java.awt.Point(wallNumber * 16 * i + TILE_SIZE,12);
                    points[3] = new java.awt.Point(wallNumber * 16 * i + TILE_SIZE,12);
                    zValues = new int[]{wallNumber * 16 * j, wallNumber * 16 * j + 16, wallNumber * 16 * j + 16, wallNumber * 16 * j};

                    walls[wallCount] = new Wall(this, points, zValues);
                    wallCount++;

                }
            }
        }

        for(int i = 0; i < currentMap.length; i++){
            for(int j = 0; j < currentMap[i].length; j++){
                if(currentMap[i][j] == 0){
                    int[] zValues = new int[4];
                    java.awt.Point[] points = new java.awt.Point[4];
                    points[0] = new java.awt.Point(wallNumber * 16 * i + TILE_SIZE,-5);
                    points[1] = new java.awt.Point(wallNumber * 16 * i + TILE_SIZE,-5);
                    points[2] = new java.awt.Point(wallNumber * 16 * i + TILE_SIZE,12);
                    points[3] = new java.awt.Point(wallNumber * 16 * i + TILE_SIZE,12);
                    zValues = new int[]{wallNumber * 16 * j + 16, wallNumber * 16 * j + 32, wallNumber * 16 * j + 32, wallNumber * 16 * j + 16};

                    walls[wallCount] = new Wall(this, points, zValues);
                    wallCount++;

                }
            }
        }

        for(int i = 0; i < currentMap.length; i++){
            for(int j = 0; j < currentMap[i].length; j++){
                if(currentMap[i][j] == 0){
                    int[] zValues = new int[4];
                    java.awt.Point[] points = new java.awt.Point[4];
                    points[0] = new java.awt.Point(wallNumber * 16 * i,-5);
                    points[1] = new java.awt.Point(wallNumber * 16 * i,-5);
                    points[2] = new java.awt.Point(wallNumber * 16 * i,12);
                    points[3] = new java.awt.Point(wallNumber * 16 * i,12);
                    zValues = new int[]{wallNumber * 16 * j, wallNumber * 16 * j + 16, wallNumber * 16 * j + 16, wallNumber * 16 * j};

                    walls[wallCount] = new Wall(this, points, zValues);
                    wallCount++;
                }
            }
        }

        for(int i = 0; i < currentMap.length; i++){
            for(int j = 0; j < currentMap[i].length; j++){
                if(currentMap[i][j] == 0){
                    int[] zValues = new int[4];
                    java.awt.Point[] points = new java.awt.Point[4];
                    points[0] = new java.awt.Point(wallNumber * 16 * i,-5);
                    points[1] = new java.awt.Point(wallNumber * 16 * i,-5);
                    points[2] = new java.awt.Point(wallNumber * 16 * i,12);
                    points[3] = new java.awt.Point(wallNumber * 16 * i,12);
                    zValues = new int[]{wallNumber * 16 * j + 16, wallNumber * 16 * j + 32, wallNumber * 16 * j + 32, wallNumber * 16 * j + 16};

                    walls[wallCount] = new Wall(this, points, zValues);
                    wallCount++;

                }
            }
        }

        for(int i = 0; i < currentMap.length; i++){
            for(int j = 0; j < currentMap[i].length; j++){
                if(currentMap[i][j] == 0){
                    int[] zValues = new int[4];
                    java.awt.Point[] points = new java.awt.Point[4];
                    points[0] = new java.awt.Point(wallNumber * 16 * i,-5);
                    points[1] = new java.awt.Point(wallNumber * 16 * i + 16,-5);
                    points[2] = new java.awt.Point(wallNumber * 16 * i + 16,12);
                    points[3] = new java.awt.Point(wallNumber * 16 * i,12);
                    zValues = new int[]{wallNumber * 16 * j, wallNumber * 16 * j, wallNumber * 16 * j, wallNumber * 16 * j};
                    walls[wallCount] = new Wall(this, points, zValues);
                    wallCount++;
                }
            }
        }
        for(int i = 0; i < currentMap.length; i++){
            for(int j = 0; j < currentMap[i].length; j++){
                if(currentMap[i][j] == 0){
                    int[] zValues = new int[4];
                    java.awt.Point[] points = new java.awt.Point[4];
                    points[0] = new java.awt.Point(wallNumber * 16 * i + 16,-5);
                    points[1] = new java.awt.Point(wallNumber * 16 * i + 32,-5);
                    points[2] = new java.awt.Point(wallNumber * 16 * i + 32,12);
                    points[3] = new java.awt.Point(wallNumber * 16 * i + 16,12);
                    zValues = new int[]{wallNumber * 16 * j, wallNumber * 16 * j, wallNumber * 16 * j, wallNumber * 16 * j};
                    walls[wallCount] = new Wall(this, points, zValues);
                    wallCount++;
                }
            }
        }




        for(int i = 0; i < currentMap.length; i++){
            for(int j = 0; j < currentMap[i].length; j++){
                if(currentMap[i][j] == 0){
                    int[] zValues = new int[4];
                    java.awt.Point[] points = new java.awt.Point[4];

                    points[0] = new java.awt.Point(wallNumber * 16 * i,-5);
                    points[1] = new java.awt.Point(wallNumber * 16 * i + 16,-5);
                    points[2] = new java.awt.Point(wallNumber * 16 * i + 16,12);
                    points[3] = new java.awt.Point(wallNumber * 16 * i,12);
                    zValues = new int[]{wallNumber * 16 * j + TILE_SIZE, wallNumber * 16 * j + TILE_SIZE, wallNumber * 16 * j + TILE_SIZE, wallNumber * 16 * j + TILE_SIZE};
                    walls[wallCount] = new Wall(this, points, zValues);
                    wallCount++;

                }
            }
        }

        for(int i = 0; i < currentMap.length; i++){
            for(int j = 0; j < currentMap[i].length; j++){
                if(currentMap[i][j] == 0){
                    int[] zValues = new int[4];
                    java.awt.Point[] points = new java.awt.Point[4];

                    points[0] = new java.awt.Point(wallNumber * 16 * i + 16,-5);
                    points[1] = new java.awt.Point(wallNumber * 16 * i + 32,-5);
                    points[2] = new java.awt.Point(wallNumber * 16 * i + 32,12);
                    points[3] = new java.awt.Point(wallNumber * 16 * i + 16,12);
                    zValues = new int[]{wallNumber * 16 * j + TILE_SIZE, wallNumber * 16 * j + TILE_SIZE, wallNumber * 16 * j + TILE_SIZE, wallNumber * 16 * j + TILE_SIZE};
                    walls[wallCount] = new Wall(this, points, zValues);
                    wallCount++;

                }
            }
        }

        int totalFloors = 4 * 34 * 34;
        floor = new Floor[totalFloors];
        int floorCount = 0;
        for(int i = 0; i < 2 * currentMap.length; i++){
            for(int j = 0; j < 2 * currentMap.length; j++){
                if(currentMap[i / 2][j / 2] != 0){
                    int[] zValues = new int[4];
                    java.awt.Point[] points = new java.awt.Point[4];

                    points[0] = new java.awt.Point((TILE_SIZE / wallNumber) * i,12);
                    points[1] = new java.awt.Point((TILE_SIZE / wallNumber) * (i + 1),12);
                    points[2] = new java.awt.Point((TILE_SIZE / wallNumber) * (i + 1),12);
                    points[3] = new java.awt.Point((TILE_SIZE / wallNumber) * i,12);
                    zValues = new int[]{(TILE_SIZE / wallNumber) * j, (TILE_SIZE / wallNumber) * j, (TILE_SIZE / wallNumber) * (j + 1), (TILE_SIZE / wallNumber) * (j + 1)};
                    floor[floorCount] = new Floor(this, points, zValues, (floorCount % 2 + i % 2) % 2);
                    floorCount++;

                }
            }
        }







//        java.awt.Point[] points = new java.awt.Point[]{new java.awt.Point(16,-10), new java.awt.Point(-0,-10), new java.awt.Point(-0, 10), new java.awt.Point(16, 10)};
//        int[] zValues = new int[]{30,30,30,30};
//        testWall = new Wall(this, points, zValues);
        int r = 0, s = 0;
        for(int i = 0; i < currentMap.length; i++){
            for(int j = 0; j < currentMap[0].length; j++){
                if(currentMap[i][j] == 10){
                    r = i;
                    s = j;
                    break;
                }
            }
        }
        player = new Player(this,r * TILE_SIZE + TILE_SIZE / 2.0,0,s * TILE_SIZE + TILE_SIZE / 2.0, currentMap);
//        player = new Player(this,0,0,0, currentMap);
        int taskObjectNumber = 0;
        for(int i = 0; i < currentMap.length; i++){
            for(int j = 0; j < currentMap.length; j++){
                if(currentMap[i][j] == 7){
                    taskObjects[taskObjectNumber] = new TaskObject(this, i * TILE_SIZE + TILE_SIZE / 2.0, 0, j * TILE_SIZE  + TILE_SIZE / 2.0, taskImages[taskObjectNumber]);
                    taskObjectNumber++;
                }
            }
        }


        obj = new ObjectPrinter(this, player);
        int x = 17;
        int z = 17;
//        while(currentMap[x][z] == 0){
//            x++;
//            if(x >= 34){
//                z--;
//                x = 0;
//            }
//        }

        nox = new Noctis(5, 0.25, 1, x * TILE_SIZE, z * TILE_SIZE, 5, this, "null");

/*
Editor note 1 for wall creation (Burhanuddin)
yeah i have no clue what the hell is going on
When printing these walls together, The positions of the vertical walls (x is constant) keep messing up. specifically they print diagonally, as if the x position has been disproportionally changed
after splitting the horizontal and vertical walls, the vertical ones replaced each other upon creation despite no repetition of variables
finally after splitting the left and right walls apart, the program functions properly
my best guess is that this has something to do with the point class
i've had some other issues with this class when originally testing printing objects in 3d
that or i just suck that much at code
my intuition is telling me its the latter

Editor note 2 for wall creation (burhanuddin)
yeah the horizontal walls are plagued with similar issue
when trying to edit wall sizes for maximum scaryness, constant z wall 1 was non existant


Tasks to complete for George:
-Code key spawn (random) after all other tasks are complete
-Create condition to check if all other tasks are complete
-Code ability to collect key by pressing e
-Create condition for the ability to pick up the key by checking if the player is within 1 tile
 */
    }

    private void checkLosing() {
        if(Math.abs(player.x - nox.x) <= 1 && Math.abs(player.z - nox.z) <= 1 ){
            playState = false;
            loseState = true;
        }
    }

    private void getSprites(){
        try{
            startScreen = ImageIO.read(new File("images\\startScreen.jpg"));
            victoryScreen1 = ImageIO.read(new File("images\\congratulations.png"));
            victoryScreen2 = ImageIO.read(new File("images\\lore.png"));
            deathScreen = ImageIO.read(new File("images\\defeatscreen.png"));
            creditScreen = ImageIO.read(new File("images\\creditscreen.png"));
            taskImages[0] = ImageIO.read(new File("images\\wrench.png"));
            taskImages[1] = ImageIO.read(new File("images\\wires.png"));
            taskImages[2] = ImageIO.read(new File("images\\wire_cutters.png"));
        }catch(NullPointerException | IOException e){
            e.printStackTrace();
        }
    }



    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        if(startState){
            g2d.drawImage(startScreen, 0,0,this.getWidth(),this.getHeight(), null);
        }
        if(playState || pauseState){

            int scaleConstant = 0;
            if(isFlashLightOn){
                scaleConstant++;
            }


            ArrayList<Entity> printableEntities = new ArrayList<>();
            for(int i = 0; i < walls.length; i++){
                if(walls[i] != null) {
                    if (walls[i].calculateCentre(player).distance(0, 0) < 50 + 100 * scaleConstant) {
                        printableEntities.add(walls[i]);
                    }
                }
            }
            for(int i = 0; i < taskObjects.length; i++) {
                if(taskObjects[i] != null){
                    taskObjects[i].updateImage();
                    if(taskObjects[i].calculateCentre(player).distance(0,0) < 50 + 100 * scaleConstant){
                        printableEntities.add(taskObjects[i]);
                    }
                }
            }

            if(nox != null){
                nox.update(player);
                if(nox.calculateCentre(player).distance(0,0) < 50 + 100 * scaleConstant){
                    printableEntities.add(nox);
                }
            }

            for(int i = 0; i < floor.length; i++){
                if(floor[i] != null){
                    if(floor[i].calculateCentre(player).distance(0,0) < 50 + 100 * scaleConstant){
                        printableEntities.add(floor[i]);
                    }
                }
            }

            for(int i = 0; i < tasks.length; i++){
                if(tasks[i] != null){
                    if(tasks[i].calculateCentre2(player).distance(0,0) < 50 + 100 * scaleConstant){
                        printableEntities.addAll(Arrays.asList(tasks[i].taskWalls));
                    }
                }
            }

            Entity[] printedStuff = new Entity[printableEntities.size()];
            printableEntities.toArray(printedStuff);
            if(printedStuff.length > 0) {

                quickSort(printedStuff);
            }
            for(int i = printedStuff.length - 1; i >= 0; i--){
                if(printedStuff[i] instanceof TaskObject){

                }
                ObjectPrinter.paint(g2d, printedStuff[i]);
            }

//            ObjectPrinter.paint(g2d, testWall);


            g2d.setColor(Color.WHITE);
            g2d.fillRoundRect(604,35,205,80, 15,15);
            g2d.setColor(Color.BLACK);
            g2d.fillRect(619,45,175,60);
            g2d.setColor(Color.WHITE);
            if(flashLightBattery <= 2){
                g2d.setColor(Color.red);
            }

            if(flashLightBattery > 0) {
                Polygon polygon = new Polygon(new int[]{624, 656, 641, 624}, new int[]{50,50,100,100}, 4);
                g2d.fillPolygon(polygon);
                for (int i = 0; i < flashLightBattery / 2 - 1; i++) {
                    if(i+1 < flashLightBattery / 2 - 1 || flashLightBattery < 8) {
                        Polygon extraPolygon = new Polygon(new int[]{656 + 5 * (i + 1) + 32 * i, 656 + 5 * (i + 1) + 32 * (i + 1), 656 + 5 * (i + 1) + 32 * (i + 1) - 15, 656 + 5 * (i + 1) + 32 * i - 15}, new int[]{50, 50, 100, 100}, 4);
                        g2d.fillPolygon(extraPolygon);
                    } else {
                        Polygon extraPolygon = new Polygon(new int[]{656 + 5 * (i + 1) + 32 * i, 656 + 5 * (i + 1) + 32 * (i + 1) - 15, 656 + 5 * (i + 1) + 32 * (i + 1) - 15, 656 + 5 * (i + 1) + 32 * i - 15}, new int[]{50, 50, 100, 100}, 4);
                        g2d.fillPolygon(extraPolygon);
                    }

                }
            }

            for(int i = 0; i < tasks.length; i++){
                if(tasks[i].isNearPlayer() && (!player.isWorkingOnTask || taskObjects[i] != null)){
                    g2d.setColor(Color.GREEN);
                    if(taskObjects[i] != null){
                        g2d.setColor(Color.red);
                    }
                    g2d.fillRoundRect(3 * this.getWidth() / 8, 7 * this.getHeight() / 8, this.getHeight() / 10, this.getHeight() / 10, 5, 5);
                    g2d.drawImage(taskImages[i],3 * this.getWidth() / 8 + 5, 7 * this.getHeight() / 8 + 5,  this.getHeight() / 10 - 10, this.getHeight() / 10 - 10, null );
                } else if(tasks[i].isNearPlayer() && player.isWorkingOnTask && taskObjects[i] == null){
                    g2d.setColor(Color.BLUE);
                    g2d.fillRoundRect(3 * this.getWidth() / 8, 7 * this.getHeight() / 8, this.getHeight() / 10, this.getHeight() / 10, 5, 5);
                }
            }

            if (isMap)
            {

                for (int i = 33; i >= 0; i--) {
                    for (int j = 0; j < 34; j++) {

                        //Drew Main Room, Tasks Room, Floor Panels + Wall
                        if (currentMap[i][j] == 0) {
                            g2d.setColor(Color.WHITE);
                            g2d.fillRect(16 * i + (this.getWidth() - 16 * 34) / 2, 16 * j + (this.getHeight() - 16 * 34) / 2, 16, 16);
                        }
                        if (currentMap[i][j] != 0) {
                            int alpha = 240;
                            Color transparency = new Color(0,0,0,alpha);
                            g2d.setColor(transparency);
                            g2d.fillRect(16 * i + (this.getWidth() - 16 * 34) / 2, 16 * j + (this.getHeight() - 16 * 34) / 2, 16, 16);
                        }
                        if (currentMap[i][j] == 8) {
                            int alpha = 240;
                            if(player.taskCompletion[0] && player.taskCompletion[1] && player.taskCompletion[2]) {
                                g2d.setColor(Color.GREEN);
                            }
                            Color keyInvisibility = new Color(0,0,0,alpha);
                            g2d.setColor(keyInvisibility);
                            g2d.fillRect(16 * i + (this.getWidth() - 16 * 34) / 2, 16 * j + (this.getHeight() - 16 * 34) / 2, 16, 16);
                        }
                        if(currentMap[i][j] == 9){
                            g2d.setColor(Color.pink);
                            g2d.fillRect(16 * i + (this.getWidth() - 16 * 34) / 2, 16 * j + (this.getHeight() - 16 * 34) / 2, 16, 16);
                        }



                    }
                }

                for(int i = 0; i < taskObjects.length; i++){
                    if(taskObjects[i] != null){
                        g2d.setColor(Color.RED);
                        g2d.fillRect(16 * taskObjects[i].tileX + (this.getWidth() - 16 * 34) / 2, 16 * taskObjects[i].tileZ + (this.getHeight() - 16 * 34) / 2, 16, 16);
                    }
                }


                g2d.setColor(Color.BLUE);
                g2d.setStroke(new BasicStroke(2));
                g2d.fillRect(16 * player.tileX + (this.getWidth() - 16 * 34) / 2, 16 * player.tileZ + (this.getHeight() - 16 * 34) / 2, 16, 16);

                g2d.setColor(Color.GREEN);
                g2d.setStroke(new BasicStroke(2));
                g2d.fillRect(16 * nox.tileX + (this.getWidth() - 16 * 34) / 2, 16 * nox.tileZ + (this.getHeight() - 16 * 34) / 2, 16, 16);



            }

            if(isInventory){
                int width = 3 * this.getWidth() / 4 - 40;
                int height = 4 * getHeight() / 5 - 32;
                g2d.setColor(Color.black);
                g2d.fillRoundRect(this.getWidth() / 8, this.getHeight() / 10, 3 * this.getWidth() / 4, 4 * getHeight() / 5, 20, 20);
                g2d.setColor(Color.gray);
                g2d.fillRoundRect(this.getWidth() / 8 + 20, this.getHeight() / 10 + 16, width, height, 20, 20);
                for(int i = 0; i < taskObjects.length; i++){
                    if(taskObjects[i] == null){
                        g2d.setColor(Color.GREEN);
                    } else {
                        g2d.setColor(Color.RED);
                    }

                    g2d.fillRoundRect(this.getWidth() / 8 + 20 + (2 * i + 1) * width / 7, this.getHeight() / 10 + 16 + height / 5, width / 7, height / 5, 15,15);
                    if(taskImages[i] != null) {
                        g2d.drawImage(taskImages[i], this.getWidth() / 8 + 20 + (2 * i + 1) * width / 7 + 10, this.getHeight() / 10 + 16 + height / 5 + 10, width / 7 - 20, height / 5 - 20, null);
                    }
                }

                for(int i = 0; i < tasks.length; i++){
                    if(tasks[i].isCompleted){
                        g2d.setColor(Color.GREEN);
                    } else {
                        g2d.setColor(Color.RED);
                    }

                    g2d.fillRoundRect(this.getWidth() / 8 + 20 + (2 * i + 1) * width / 7, this.getHeight() / 10 + 16 + 3 * height / 5, width / 7, height / 5, 15,15);
                    if(taskImages[i] != null) {
                        g2d.drawImage(taskImages[i], this.getWidth() / 8 + 20 + (2 * i + 1) * width / 7 + 10, this.getHeight() / 10 + 16 + 3 * height / 5 + 10, width / 7 - 20, height / 5 - 20, null);
                    }
                }


            }

        }
        if(pauseState){

        }
        if(loseState){
            g2d.drawImage(deathScreen, 0,0, this.getWidth(), this.getHeight(), null);
            if(nox != null){
                g2d.drawImage(nox.defaultImage, this.getWidth() / 2 - this.getWidth() / 8, this.getHeight() / 2 + this.getHeight() / 8, this.getWidth() / 4, this.getHeight() / 4, null);
            }
        }

        // testWall.paint(g2d);

        g2d.dispose();


    }

    public void quickSort(Entity[] arr)
    {
        quickSort(arr, 0, arr.length-1);
    }

    //Overloaded Quicksort used by the above method
    private void quickSort(Entity[] arr, int left, int right)
    {
        int index = partition(arr, left, right);
        if (left < index - 1) {
            quickSort(arr, left, index - 1);
        }
        if (index < right) {
            quickSort(arr, index, right);
        }
    }

    //Internal method that partitions the given array
    private int partition(Entity[] arr, int left, int right)
    {
        int i = left, j = right;
        Entity tmp;
        double pivot = arr[(left + right) / 2].calculateCentre(player).distance(0,0);
        while (i <= j) {
            while (arr[i].calculateCentre(player).distance(0,0) < pivot) {
                i++;
            }
            while (arr[j].calculateCentre(player).distance(0,0) > pivot) {
                j--;
            }
            if (i <= j) {
                tmp = arr[i];
                arr[i] = arr[j];
                arr[j] = tmp;
                i++;
                j--;
            }
        }
        return i;
    }




    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(3);
        frame.add(new GamePanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    @Override
    public void run() {

        double drawInterval = 1000000000.0/30;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;


        while(thread != null){

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;

            if(delta >= 1) {
                long firstTime = System.nanoTime();
                // update the game
                update();

                // paint values regarding said update
                repaint();

                long lastTie = System.nanoTime();

//                System.out.println((lastTie - firstTime) / 1000000.0);
                delta--;



            }

        }
    }


    public void update(){
        if(playState) {
            player.updatePlayer(tasks, taskObjects);
            if(isFlashLightOn){
                if(flashLightBattery > 0) {
                    flashLightBattery -= 1 / 30.0;
                } else {
                    isFlashLightOn = false;
                }
            }
            checkLosing();
            nox.speed = 0.25;
            for(int i = 0; i < taskObjects.length; i++){
                if(taskObjects[i] == null){
                    nox.speed += 0.25;
                }
            }
        }


    }

    public boolean mapChecker(int[][] maze2) {

        for (int i = 0; i < 32; i++) {
            if (maze2[i][0] == 1) {
                return true;
            }
        }
        return false;
    }




    public int[][] makeMap() {

        int[][] mapMap = new int[34][34];
        int[][] mapMapMap = new int[32][32];


        // dimensions of generated maze
        int r = 32, c = 32;

        // build maze and initialize with only walls
        StringBuilder s = new StringBuilder(c);
        for (int x = 0; x < c; x++)
            s.append('*');
        char[][] maz = new char[r][c];
        for (int x = 0; x < r; x++) maz[x] = s.toString().toCharArray();

        // select random point and open as start node
        Point st = new Point((int)(Math.random() * r), (int)(Math.random() * c), null);
        maz[st.r][st.c] = 'S';

        // iterate through direct neighbors of node
        ArrayList <Point> frontier = new ArrayList <Point> ();
        for (int x = -1; x <= 1; x++)
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0 || x != 0 && y != 0)
                    continue;
                try {
                    if (maz[st.r + x][st.c + y] == '.') continue;
                } catch (Exception e) { // ignore ArrayIndexOutOfBounds
                    continue;
                }
                // add eligible points to frontier
                frontier.add(new Point(st.r + x, st.c + y, st));
            }

        Point last = null;
        while (!frontier.isEmpty()) {

            // pick current node at random
            Point cu = frontier.remove((int)(Math.random() * frontier.size()));
            Point op = cu.opposite();
            try {
                // if both node and its opposite are walls
                if (maz[cu.r][cu.c] == '*') {
                    if (maz[op.r][op.c] == '*') {

                        // open path between the nodes
                        maz[cu.r][cu.c] = '.';
                        maz[op.r][op.c] = '.';

                        // store last node in order to mark it later
                        last = op;

                        // iterate through direct neighbors of node, same as earlier
                        for (int x = -1; x <= 1; x++)
                            for (int y = -1; y <= 1; y++) {
                                if (x == 0 && y == 0 || x != 0 && y != 0)
                                    continue;
                                try {
                                    if (maz[op.r + x][op.c + y] == '.') continue;
                                } catch (Exception e) {
                                    continue;
                                }
                                frontier.add(new Point(op.r + x, op.c + y, op));
                            }
                    }
                }
            } catch (Exception e) { // ignore NullPointer and ArrayIndexOutOfBounds
            }

            // if algorithm has resolved, mark end node
            if (frontier.isEmpty())
                maz[last.r][last.c] = 'E';
        }




        for (int e = 0; e < r; e++) {
            for (int b = 0; b < c; b++) {
                if (maz[e][b] == '*') {
                    mapMapMap[e][b] = 0;
                } else {
                    mapMapMap[e][b] = 1;
                }
                if (13 <= e && e <= 20 && 13 <= b && b <= 20) {
                    mapMapMap[e][b] = 2;
                }
            }
        }
        int roomsize = 4;
        boolean tempPlace = true;
        int q = (int) (Math.random() * (32 - 8));
        for (int i = q; i < q+8; i++) {
            for (int j = 30; j < c; j++) {
                mapMapMap[j][i] = 4;
            }
        }


        int roomNumber = 0;

        while (roomNumber < 3) {


            int x = (int) (Math.random() * (32 - roomsize));
            int y = (int) (Math.random() * (32 - roomsize));


            for (int n = 0; n < roomsize; n++) {
                if (!tempPlace) {
                    break;
                }
                for (int f = 0; f < roomsize; f++) {


                    if (mapMapMap[x + n][y + f] >= 2) {
                        tempPlace = false;
                        break;
                    }


                }
            }
            if (tempPlace) {
                for (int i = x; i < x+roomsize; i++) {
                    for (int j = y; j < y+roomsize; j++) {
                        mapMapMap[i][j] = 3;
                    }

                }
                int v = 0;
                int z = 0;

                /*
                Add Task objects from Game panel here to make life easier
                should be random point on the walls of the room
                 */


                //Randomizer for Red Squares
                while (!(mapMapMap[v][z] == 1)) {
                    v = (int) (Math.random() * 32);
                    z = (int) (Math.random() * 32);
                }
                mapMapMap[v][z] = 7;

                v = (int) (Math.random() * (roomsize - 2)) +  x + 1;
                z = (int) (Math.random() * (roomsize - 2)) +  y + 1;

                mapMapMap[v][z] = 9;



                roomNumber++;
            }

            tempPlace = true;

        }

        int v = 0;
        int z = 0;
        while (!(mapMapMap[v][z] == 1)) {
            v = (int) (Math.random() * 32);
            z = (int) (Math.random() * 32);
        }
        mapMapMap[v][z] = 8;
        v = 0;
        z = 0;
        while (!(mapMapMap[v][z] == 1)) {
            v = (int) (Math.random() * 32);
            z = (int) (Math.random() * 32);
        }
        mapMapMap[v][z] = 10;

        for (int i = 1; i < 33; i++) { //top wall
            for (int j = 1; j < 33; j++) {
                mapMap[i][j] = mapMapMap[i-1][j-1];

            }
        }












        // print final maze

        return mapMap;
    }


    static class Point {
        Integer r;
        Integer c;
        Point parent;

        public Point(int x, int y, Point p) {
            r = x;
            c = y;
            parent = p;
        }

        // compute opposite node given that it is in the other direction from the parent
        public Point opposite() {
            if (this.r.compareTo(parent.r) != 0)
                return new Point(this.r + this.r.compareTo(parent.r), this.c, this);
            if (this.c.compareTo(parent.c) != 0)
                return new Point(this.r, this.c + this.c.compareTo(parent.c), this);
            return null;
        }
    }

}