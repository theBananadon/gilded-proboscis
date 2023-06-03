import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;


public class GamePanel extends JPanel implements Runnable{
    private Thread thread;
    public final int TILE_SIZE = 48;
    Player player;
    public boolean startState = false, pauseState = false, playState = false;
    public boolean allowEscape = false;
    int[][] currentMap;


    public GamePanel(){
        this.setFocusable(true);
        this.setPreferredSize(new Dimension(600,600));
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
                    if(e.getKeyCode() == KeyEvent.VK_M){
                        //insert Map method call
                    }
                    if(e.getKeyCode() == KeyEvent.VK_I){
                        //insert Inventory method call
                    }
                }
                if(startState){
                    if(e.getKeyCode() == KeyEvent.VK_SPACE){
                        startState = false;
                        playState = true;
                    }
                }
                if(pauseState && allowEscape){
                    if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
                        pauseState = false;
                        startState = true;
                        resetGame();
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
            }
            if(pauseState){
                if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE && !allowEscape){
                    allowEscape = true;
                }
            }
            }
        });

        /*

        for(int i = 1; i < currentMap.length - 1; i++){
            for(int j = 1; j < currentMap[i].length - 1; j++){
                if(currentMap[i - 1][j] == 0 && currentMap[i - 1][j] + currentMap[i][j] > 0){
                    int[] zValues = new int[4];
                    java.awt.Point[] points = new java.awt.Point[4];
                    points[0] = new java.awt.Point(16 * i,-5);
                    points[1] = new java.awt.Point(16 * i,-5);
                    points[2] = new java.awt.Point(16 * i,12);
                    points[3] = new java.awt.Point(16 * i,12);
                    zValues = new int[]{16 * j, 16 * j + 16, 16 * j + 16, 16 * j};
                    wallOfCuebe[wallCount] = new Cube(points, zValues, this);
                    wallCount++;
                    for(int k = 0; k < zValues.length; k++){
                        zValues[k] += 16;
                    }
                    wallOfCuebe[wallCount] = new Cube(points, zValues, this);
                    wallCount++;
                    for(int k = 0; k < zValues.length; k++){
                        zValues[k] += 16;
                    }
                    wallOfCuebe[wallCount] = new Cube(points, zValues, this);
                    wallCount++;
                }
                if(currentMap[i + 1][j] == 0 && currentMap[i + 1][j] + currentMap[i][j] > 0){
                    int[] zValues = new int[4];
                    java.awt.Point[] points = new java.awt.Point[4];
                    points[0] = new java.awt.Point(16 * i + 16,-5);
                    points[1] = new java.awt.Point(16 * i + 16,-5);
                    points[2] = new java.awt.Point(16 * i + 16,12);
                    points[3] = new java.awt.Point(16 * i + 16,12);
                    zValues = new int[]{16 * j, 16 * j + 16, 16 * j + 16, 16 * j};
                    wallOfCuebe[wallCount] = new Cube(points, zValues, this);
                    wallCount++;
                    for(int k = 0; k < zValues.length; k++){
                        zValues[k] += 16;
                    }
                    wallOfCuebe[wallCount] = new Cube(points, zValues, this);
                    wallCount++;
                    for(int k = 0; k < zValues.length; k++){
                        zValues[k] += 16;
                    }
                    wallOfCuebe[wallCount] = new Cube(points, zValues, this);
                    wallCount++;

                }
                if(currentMap[i][j - 1] == 0 && currentMap[i][j - 1] + currentMap[i][j] > 0){
                    int[] zValues = new int[4];
                    java.awt.Point[] points = new java.awt.Point[4];
                    points[0] = new java.awt.Point(16 * i,-5);
                    points[1] = new java.awt.Point(16 * i + 16,-5);
                    points[2] = new java.awt.Point(16 * i + 16,12);
                    points[3] = new java.awt.Point(16 * i,12);
                    zValues = new int[]{16 * j, 16 * j, 16 * j, 16 * j};
                    wallOfCuebe[wallCount] = new Cube(points, zValues, this);
                    wallCount++;
                    for(int k = 0; k < points.length; k++){
                        points[k].x += 16;
                    }
                    wallOfCuebe[wallCount] = new Cube(points, zValues, this);
                    wallCount++;
                    for(int k = 0; k < points.length; k++){
                        points[k].x += 16;
                    }
                    wallOfCuebe[wallCount] = new Cube(points, zValues, this);
                    wallCount++;

                }
                if(currentMap[i][j + 1] == 0 && currentMap[i][j + 1] + currentMap[i][j] > 0){
                    int[] zValues = new int[4];
                    java.awt.Point[] points = new java.awt.Point[4];
                    points[0] = new java.awt.Point(16 * i,-5);
                    points[1] = new java.awt.Point(16 * i + 16,-5);
                    points[2] = new java.awt.Point(16 * i + 16,12);
                    points[3] = new java.awt.Point(16 * i,12);
                    zValues = new int[]{16 * j + 16, 16 * j + 16, 16 * j + 16, 16 * j + 16};
                    wallOfCuebe[wallCount] = new Cube(points, zValues, this);
                    wallCount++;
                    for(int k = 0; k < points.length; k++){
                        points[k].x += 16;
                    }
                    wallOfCuebe[wallCount] = new Cube(points, zValues, this);
                    wallCount++;
                    for(int k = 0; k < points.length; k++){
                        points[k].x += 16;
                    }
                    wallOfCuebe[wallCount] = new Cube(points, zValues, this);
                    wallCount++;

                }
            }
        }

         */

/*
        Point[] points = new Point[]{new Point(20 * (-1 + 1),0), new Point(20 * (0 + 1),0), new Point(20 * (0 + 1),20), new Point(20 * (-1 + 1),20)};
        int[] zValues = new int[]{5 * (0 + 1), 5 * (0 + 1), 5 * (0 + 1), 5 * (0 + 1)};
        testWall = new Wall(points, zValues, this);

 */

        startGame();
    }

    private void resetGame() {

    }

    private void changeGameState() {
    }

    private void getSprites(){

    }


    public void startGame(){
        currentMap = mapGenerator();

        int totalWalls = 0;
        for(int i = 1; i < currentMap.length - 1; i++){
            for(int j = 1; j < currentMap[i].length - 1; j++){
                if(currentMap[i - 1][j] == 0 && currentMap[i - 1][j] + currentMap[i][j] > 0){
                    totalWalls++;
                }
                if(currentMap[i + 1][j] == 0 && currentMap[i + 1][j] + currentMap[i][j] > 0){
                    totalWalls++;
                }
                if(currentMap[i][j - 1] == 0 && currentMap[i][j - 1] + currentMap[i][j] > 0){
                    totalWalls++;
                }
                if(currentMap[i][j + 1] == 0 && currentMap[i][j + 1] + currentMap[i][j] > 0){
                    totalWalls++;
                }
            }
        }
        totalWalls = 3 * totalWalls;
        int wallCount = 0;

        /*
            Rest of game stuff here
         */
        player = new Player(this,0,0,0);
        startState = true;
        thread = new Thread(this);
        thread.start();
    }


    public void paint(Graphics g){
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        if(startState){
            //print start screen
        }
        if(playState){

        }
        if(pauseState){

        }
        player.tileX = (int) (player.x / 48);
        player.tileZ = (int) (player.z / 48);

        //testCuebe.paint(g2d);
        /*
        ArrayList<Cube> printableWalls = new ArrayList<>();
        for(int i = 0; i < wallOfCuebe.length; i++){
            if(wallOfCuebe[i].calculateCentre().distance(0,0) < 25600) {
                printableWalls.add(wallOfCuebe[i]);
            }
        }
        Cube[] printedStuff = new Cube[printableWalls.size()];
        printableWalls.toArray(printedStuff);
        quickSort(printedStuff);
        for(int i = printedStuff.length - 1; i >= 0; i--){
            printedStuff[i].paint(g2d);
        }

         */


        // testWall.paint(g2d);

        g2d.dispose();


    }

    public static void quickSort(Entity[] arr)
    {
        quickSort(arr, 0, arr.length-1);
    }

    //Overloaded Quicksort used by the above method
    private static void quickSort(Entity[] arr, int left, int right)
    {
        int index = partition(arr, left, right);
        if (left < index - 1)
            quickSort(arr, left, index - 1);
        if (index < right)
            quickSort(arr, index, right);
    }

    //Internal method that partitions the given array
    private static int partition(Entity[] arr, int left, int right)
    {
        int i = left, j = right;
        Entity tmp;
        double pivot = ObjectPrinter.calculateCentre(arr[(left + right) / 2].points, arr[(left + right) / 2].distance).distance(0,0);
        while (i <= j) {
            while (ObjectPrinter.calculateCentre(arr[i].points, arr[i].distance).distance(0,0) < pivot)
                i++;
            while (ObjectPrinter.calculateCentre(arr[j].points, arr[j].distance).distance(0,0) < pivot)
                j--;
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
                // update the game
                update();

                // paint values regarding said update
                repaint();

                delta--;



            }

        }
    }


    public void update(){
        player.updatePlayer();




    }

    public int[][] mapGenerator() {
        int[][] maze;
        // dimensions of generated maze
        int r = 32, c = 32;

        // build maze and initialize with only walls
        StringBuilder s = new StringBuilder(c);
        for (int x = 0; x < c; x++)
            s.append('*');
        char[][] maz = new char[r][c];
        for (int x = 0; x < r; x++) maz[x] = s.toString().toCharArray();

        // select random point and open as start node
        Point st = new Point((int) (Math.random() * r), (int) (Math.random() * c), null);
        maz[st.r][st.c] = 'S';

        // iterate through direct neighbors of node
        ArrayList<Point> frontier = new ArrayList<Point>();
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
            Point cu = frontier.remove((int) (Math.random() * frontier.size()));
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


        maze = new int[r][c];
        for (int e = 0; e < r; e++) {
            for (int b = 0; b < c; b++) {
                if (maz[e][b] == '*') {
                    maze[e][b] = 0;
                } else {
                    maze[e][b] = 1;
                }
                if (13 <= e && e <= 20 && 13 <= b && b <= 20) {
                    maze[e][b] = 2;
                }
            }
        }
        int roomsize = 4;
        boolean tempPlace = true;
        int q = (int) (Math.random() * (32 - 8));
        for (int i = q; i < q + 8; i++) {
            for (int j = 30; j < c; j++) {
                maze[j][i] = 4;
            }
        }
        while (tempPlace) {
            int x = (int) (Math.random() * (32 - roomsize));
            int y = (int) (Math.random() * (32 - roomsize));


            for (int n = 0; n < roomsize; n++) {
                if (!tempPlace) {
                    break;
                }
                for (int f = 0; f < roomsize; f++) {


                    if (maze[x + n][y + f] >= 2) {
                        tempPlace = false;
                        break;
                    }


                }
            }
            if (tempPlace) {
                for (int i = x; i < x + roomsize; i++) {
                    for (int j = y; j < y + roomsize; j++) {
                        maze[i][j] = 3;
                    }
                }
            }
        }


        // print final maze
//        for (int i = 0; i < r; i++) {
//            for (int j = 0; j < c; j++)
//                System.out.print(maze[i][j]);
//            System.out.println();
//
//
//        }
        return maze;
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

