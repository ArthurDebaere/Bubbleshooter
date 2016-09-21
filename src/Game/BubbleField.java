package Game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


//TODO: REFACTORING (PLACE MATRIX IN OTHER CLASS) ETC.
public class BubbleField {
    private double bubblefield_offset_x;
    private double bubblefield_offset_y;
    private double ball_width;
    private double horizontal_space_between_bubbles = 0;
    private int numberOfBallsAddedForAddingNewRow = 15;
    private double maxNumberOfRows = 16;
    private double numberOfRowsBeforeLoss=17;
    private int numberOfBallsAdded;
    private int numberOfBallsOnRow;
    private int numberOfStartRows;
    private int numberOfRowsAdded;
    private boolean addRowOnTop = false;
    private boolean isGameOver = false;
    private boolean isGameWon = false;
    private ArrayList<GameObject[]> bubbleFieldMatrix = new ArrayList<>();
    private GameObjectFactory gameObjectFactory = new GameObjectFactory();
    private GameObjectManager gameObjectManager;
    private Settings settings =Settings.getInstance();
    private Players players = Players.getInstance();
    private int player;
    private Timer timer;

    public BubbleField(int player) {
        this(30, 30, 23, 5,player);
    }

    public BubbleField(double bubblefield_offset_x, double bubblefield_offset_y, int numberOfBallsOnRow, int numberOfStartRows, int player) {
        gameObjectManager = GameObjectManager.getInstance();
        this.bubblefield_offset_x = bubblefield_offset_x+((player-1)*settings.getGamewitdh());
        this.player=player;
        this.bubblefield_offset_y = bubblefield_offset_y;
        this.ball_width = settings.getBallsize();
        this.numberOfBallsOnRow =(int)((settings.getGamewitdh()-bubblefield_offset_x-bubblefield_offset_y)/ball_width);
        this.numberOfStartRows = numberOfStartRows;
        this.numberOfBallsAddedForAddingNewRow = settings.getNumberOfBallsBeforeAddingNewRow();
        generateBubbleField();
        timer = new Timer();
    }

    public void update() {
        if(addRowOnTop) {
            addRowOnTop();
            addRowOnTop = false;
        }

        checkRowIsEmpty();
        if(getNumberOfRows() == numberOfRowsBeforeLoss) {
            isGameOver = true;
        }
    }

    public void burstBubblesInRange(GameObject bomb, int range) {
        Position bombPosition = getNearestPositionInMatrix(bomb.getTransform().getPosition_x(),bomb.getTransform().getPosition_y());
        ArrayList<GameObject> bubblesInRange = getBubblesInRadius(bombPosition, range);
        int ballsRemoved = 0;

        for(GameObject balls : bubblesInRange) {
            balls.setRemoveFromGame(true);
            removeBubbleFromMatrix(getNearestPositionInMatrix(balls.getTransform().getPosition_x(), balls.getTransform().getPosition_y()));
            ballsRemoved++;
        }

        players.setPlayersScore(player,(int) Math.pow(ballsRemoved,2));
        players.setPlayerExp(player,ballsRemoved);

    }

    public void changeAllBallColorsToRandom() {
        ArrayList<GameObject> matrixballs = getAllNotNullBalls();
        for(GameObject bubble : matrixballs) {
            String filename = colorPicker(random(0,settings.getNumberOfColours()));
            Image img_cannonball = new Image(getResource(filename));
            ImageView bubbleImageview = bubble.getImageView();
            if(bubbleImageview != null) {
                String imageViewPath = img_cannonball.impl_getUrl();
                String[] imageViewPathParts = imageViewPath.split("/");
                String imageViewName = imageViewPathParts[imageViewPathParts.length - 1];
                bubble.getImageView().setImage(img_cannonball);
                bubble.setImageViewName(imageViewName);
            }
        }
    }

    private void checkRowIsEmpty(){
       int numberOfBalls = countBalls(2);

        if (numberOfBalls==0 && getNumberOfRows()>3){
            bubbleFieldMatrix.remove(getNumberOfRows());
        }
        if (numberOfBalls!=0){
            bubbleFieldMatrix.add(getNumberOfRows()+1,new GameObject[numberOfBallsOnRow]);
        }
        if (getNumberOfRows()<=3){
            addRowOnTop();;
            if (numberOfRowsAdded>=maxNumberOfRows&& countBalls(getNumberOfRows())<8){
                isGameWon=true;
            }
        }
    }

    private int countBalls(int numberOfLines){
        int numberOfBalls=0;
        for (int line =getNumberOfRows(); line>=getNumberOfRows()-numberOfLines;line--){

            for (int ball =0; ball<bubbleFieldMatrix.get(line).length;ball++){
                if (bubbleFieldMatrix.get(line)[ball]!=null){
                    numberOfBalls+=1;
                }
            }
        }
        return numberOfBalls;
    }

    public Position getNearestPositionInMatrix(double x, double y) {
        int rowNumber = (int) Math.floor((y - bubblefield_offset_y) / ball_width);
        double offset_x = 0;
        if(!isRowEven(rowNumber)) {
            offset_x = ball_width/2;
        }
        int columnNumber = (int) Math.floor(((x - bubblefield_offset_x) - offset_x) / ball_width);

        return new Position(columnNumber, rowNumber);
    }

    public void addCannonBallToMatrix(GameObject cannonball) {
        cannonball.removeComponent(GameObjectFactory.Components.PhysicsComponent);
        Component spritePhysicsComponennt = new SpritePhysicsComponent();
        cannonball.addComponent(spritePhysicsComponennt, GameObjectFactory.Components.PhysicsComponent);
        cannonball.initializeComponent(spritePhysicsComponennt);

        Position centerPoint_Cannonball = new Position(cannonball.getTransform().getCenterPoint_x(), cannonball.getTransform().getCenterPoint_y());
        Position positionInMatrix = getNearestPositionInMatrix(centerPoint_Cannonball.getX(), centerPoint_Cannonball.getY());

        if(positionInMatrix.getY() >= maxNumberOfRows) {
            isGameOver = true;
        }

        bringPositionInBounds(positionInMatrix);

        boolean bubblePositionFound = false;

        if(getBubble((int)positionInMatrix.getX(), (int)positionInMatrix.getY()) != null) {
            for (int nextRow = (int)positionInMatrix.getY() + 1; nextRow < maxNumberOfRows; nextRow++) {
                if(getBubble((int)positionInMatrix.getX(), nextRow) == null) {
                    positionInMatrix.setY(nextRow);
                    bubblePositionFound= true;
                    break;
                }
            }
        } else {
            bubblePositionFound = true;
        }

        if(bubblePositionFound) {
            addBubble((int)positionInMatrix.getX(), (int)positionInMatrix.getY(), cannonball);
        }

        ArrayList<GameObject> cluster = findCluster((int) positionInMatrix.getX(), (int) positionInMatrix.getY(), true, true, false);

        if(cluster.size() >= 3) {
            for (GameObject bubble : cluster) {
                bubble.setRemoveFromGame(true);
                removeBubbleFromMatrix(getNearestPositionInMatrix((int)bubble.getTransform().getPosition_x(), (int)bubble.getTransform().getPosition_y()));
            }
            players.setPlayersScore(player,(int) Math.pow(cluster.size(),1.75));
            players.setPlayerExp(player,cluster.size());

        }

        ArrayList<ArrayList<GameObject>> floatingClusters = findFloatingClusters();

        if(floatingClusters.size() > 0) {
            players.setPlayersScore(player,(int) Math.pow(floatingClusters.size(),2.50));
            players.setPlayerExp(player,floatingClusters.size());
            for (ArrayList<GameObject> floatingCluster : floatingClusters) {
                for(GameObject bubble : floatingCluster) {
                    bubble.getTransform().setVelocity(bubble.getTransform().getVelocity_x(), bubble.getTransform().getVelocity_y() + 1000);
                    removeBubbleFromMatrix(getNearestPositionInMatrix((int)bubble.getTransform().getPosition_x(), (int)bubble.getTransform().getPosition_y()));
                }
            }
        }
    }

    public int getNumberOfRows() {
        return bubbleFieldMatrix.size()-1;
    }

    public ArrayList<GameObject> getAllNotNullBalls() {
        ArrayList<GameObject> balls = new ArrayList<>();
        for(GameObject[] row : bubbleFieldMatrix) {
            for (GameObject ball : row) {
                if(ball != null) {
                    balls.add(ball);
                }
            }
        }
        return balls;
    }

    public void addRowOnTop() {
        if(numberOfRowsAdded < maxNumberOfRows) {
            gameObjectManager = GameObjectManager.getInstance();

            GameObject[] ballRow = new GameObject[numberOfBallsOnRow];
            int numberOfBallsOnThisRow = numberOfBallsOnRow;
            boolean withOffsetX = true;

            if(isRowEven(0)) {
                //ADD UNEVEN ROW
                withOffsetX = false;
                if (settings.getPlayers()==2){
                numberOfBallsOnThisRow-=1;}
            }

            int row = 0;

           for (int ballNumber = 0; ballNumber < numberOfBallsOnThisRow; ballNumber++) {
                GameObject ball = gameObjectFactory.getGameObject("BUBBLEFIELDBALL");
                Position ball_position = calculateBallPosition(ballNumber, row, withOffsetX);
                ball.getTransform().setPosition(ball_position.getX(), ball_position.getY());
                ball.setName("ball_"  +0 + "_" + ballNumber +player + numberOfRowsAdded);
                ballRow[ballNumber] = ball;
                gameObjectManager.addAtRuntime("new_rowball_" + numberOfBallsAdded + "_" + ballNumber + player + numberOfRowsAdded, ball);
            }

            shiftTransforms();

            bubbleFieldMatrix.add(0, ballRow);

            numberOfRowsAdded++;
        }
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setNumberOfBallsAddedForAddingNewRow(int number, boolean enableAutoReset) {
        this.numberOfBallsAddedForAddingNewRow = number;

        if(enableAutoReset) {
            int secondsBeforeReset = 10;
                System.out.println("NUMBER OF BALLS ADDED ENABLED");
                timer.schedule(new ResetNumberOfBubblesBeforeAddingNewLine(), secondsBeforeReset * 1000);
        }
    }

    public boolean isGameWon() {return isGameWon;}

    public void findAndRemoveFloatingClusters() {
        ArrayList<ArrayList<GameObject>> floatingClusters = findFloatingClusters();

        if(floatingClusters.size() > 0) {
            players.setPlayersScore(player,(int) Math.pow(floatingClusters.size(),2.50));
            players.setPlayerExp(player,floatingClusters.size());
            for (ArrayList<GameObject> floatingCluster : floatingClusters) {
                for(GameObject bubble : floatingCluster) {
                    bubble.getTransform().setVelocity(bubble.getTransform().getVelocity_x(), bubble.getTransform().getVelocity_y() + 1000);
                    removeBubbleFromMatrix(getNearestPositionInMatrix((int)bubble.getTransform().getPosition_x(), (int)bubble.getTransform().getPosition_y()));
                }
            }
        }
    }

    public ArrayList<ArrayList<GameObject>> findFloatingClusters() {
        resetProcessedBubbles();

        ArrayList<ArrayList<GameObject>> floatingClusters = new ArrayList<>();

        ArrayList<GameObject> allBubblesInMatrix = getAllNotNullBalls();

        for (GameObject bubble : allBubblesInMatrix) {
            if(!bubble.isProcessedByBubblefield()) {
                Position bubblePosition = getNearestPositionInMatrix(bubble.getTransform().getPosition_x(), bubble.getTransform().getPosition_y());
                ArrayList<GameObject> cluster = findCluster((int)bubblePosition.getX(), (int)bubblePosition.getY(), false, false, true);

                if(cluster.size() == 0) {
                    continue;
                }

                boolean floating = true;
                for (GameObject clusterbubble : cluster) {
                    Position clusterBubblePosition = getNearestPositionInMatrix(clusterbubble.getTransform().getPosition_x(), clusterbubble.getTransform().getPosition_y());
                    if(clusterBubblePosition.getY() == 0) {
                        floating = false;
                        break;
                    }
                }

                if(floating) {
                    floatingClusters.add(cluster);
                }
            }
        }
        return floatingClusters;
    }

    private void removeBubbleFromMatrix(Position bubbleToRemovePosition) {
        int x = (int)bubbleToRemovePosition.getX();
        int y = (int)bubbleToRemovePosition.getY();

        bubbleFieldMatrix.get(y)[x] = null;
    }

    private ArrayList<GameObject> findCluster(int startball_x, int startball_y, boolean matchtype, boolean reset, boolean skipremoved) {
        if(reset) {
            resetProcessedBubbles();
        }

        GameObject startBubble = getBubble(startball_x, startball_y);

        ArrayList<GameObject> toProcess = new ArrayList<>();
        ArrayList<GameObject> cluster = new ArrayList<>();
        toProcess.add(startBubble);
        startBubble.setProcessedByBubblefield(true);

        while(toProcess.size() > 0) {
            GameObject currentBubble = toProcess.get(toProcess.size() - 1);
            toProcess.remove(toProcess.size() - 1);

            if(currentBubble == null || skipremoved && currentBubble.getRemoveFromGame()) {
                continue;
            }

            if(!matchtype || (currentBubble.getImageViewName().equals(startBubble.getImageViewName()))) {
                cluster.add(currentBubble);
                ArrayList<GameObject> neighbors = getNeigbors(getNearestPositionInMatrix(currentBubble.getTransform().getPosition_x(), currentBubble.getTransform().getPosition_y()));

                for (GameObject neighbor : neighbors) {
                    if(!neighbor.isProcessedByBubblefield()) {
                        toProcess.add(neighbor);
                        neighbor.setProcessedByBubblefield(true);
                    }
                }
            }
        }

        return cluster;
    }

    private ArrayList<GameObject> getNeigbors(Position positionToStartSearch) {
        ArrayList<GameObject> neighbors = new ArrayList<GameObject>();

        int x_pos_start = (int)positionToStartSearch.getX();
        int y_pos_start = (int)positionToStartSearch.getY();

        int neighborPositions_evenRow[][]={{1, 0},{0, 1},{-1, 1}, {-1, 0}, {-1, -1}, {0, -1}};
        int neighborPositions_unevenRow[][]={{1, 0},{1, 1},{0, 1}, {-1, 0}, {0, -1}, {1, -1}};

        int currentNeighborPositions[][] = null;

        if(isRowEven(y_pos_start)) {
            currentNeighborPositions = neighborPositions_evenRow;
        } else {
            currentNeighborPositions = neighborPositions_unevenRow;
        }

        for(int i = 0; i < currentNeighborPositions.length; i++) {
            int nx = x_pos_start + currentNeighborPositions[i][0];
            int ny = y_pos_start + currentNeighborPositions[i][1];

            int numberOfBallsOnThisRow = numberOfBallsOnRow;
            if (!isRowEven(ny) && settings.getPlayers()==2){
                numberOfBallsOnThisRow=numberOfBallsOnRow- 1;
            }

            if(nx >= 0 && nx < numberOfBallsOnThisRow && ny >= 0 && ny < maxNumberOfRows) {
                if(getBubble(nx, ny) != null) {
                    neighbors.add(getBubble(nx, ny));
                }
            }
        }

        return neighbors;
    }

    private ArrayList<GameObject> getBubblesInRadius(Position startPosition, int radius) {
        ArrayList<GameObject> bubbles = new ArrayList<>();

        ArrayList<GameObject> allBubblesInMatrix = getAllNotNullBalls();
        for (GameObject ball : allBubblesInMatrix) {
            Position bubblePosition = getNearestPositionInMatrix(ball.getTransform().getPosition_x(), ball.getTransform().getPosition_y());
            if(calculateDistanceBetweenPoints(bubblePosition, startPosition) <= radius) {
                bubbles.add(ball);
            }
        }
        return bubbles;
    }

    private double calculateDistanceBetweenPoints(Position p1, Position p2) {
        double delta_x = Math.pow((p2.getX() - p1.getX()), 2);
        double delta_y = Math.pow((p2.getY() - p1.getY()), 2);
        double distance = Math.sqrt(delta_x + delta_y);

        return distance;
    }

    private void resetProcessedBubbles() {
        ArrayList<GameObject> ballsInBubblefield = getAllNotNullBalls();
        for (GameObject ball : ballsInBubblefield) {
            ball.setProcessedByBubblefield(false);
        }
    }


    private void addBubble(int x, int y, GameObject cannonball) {
        Position ball_position = calculateBallPosition(x, y, isRowEven(y));
        cannonball.getTransform().setPosition(ball_position.getX(), ball_position.getY());
        cannonball.setName("ball_"  + y + "_" + x + player);
        GameObject[] bubbleRow = bubbleFieldMatrix.get(y);
        bubbleRow[x] = cannonball;
        numberOfBallsAdded++;

        if(numberOfBallsAdded % numberOfBallsAddedForAddingNewRow == 0) {
            addRowOnTop = true;
        }
    }

    private void shiftTransforms() {
        for(GameObject[] row : bubbleFieldMatrix) {
            for (GameObject ball : row) {
                if(ball != null) {
                    ball.getTransform().setPosition(ball.getTransform().getPosition_x(), ball.getTransform().getPosition_y() + ball_width);
                }
            }
        }
    }

    private GameObject getBubble(int column, int row) {
        if(bubbleFieldMatrix.get(row)[column] != null) {
            return bubbleFieldMatrix.get(row)[column];
        } else {
            return null;
        }
    }

    private void generateBubbleField() {
        int currentRow = 0;
        for (int rowIndex = 0; rowIndex < numberOfStartRows; rowIndex++) {
            bubbleFieldMatrix.add(rowIndex, createBallRow(rowIndex));
            currentRow++;
        }

        for(int rowIndex = currentRow; rowIndex <= maxNumberOfRows; rowIndex++) {
            bubbleFieldMatrix.add(rowIndex, createEmptyBallRow());
        }
    }

    private GameObject[] createBallRow(int row) {
        int numberOfBallsOnThisRow = numberOfBallsOnRow;

        if(row%2==1 && settings.getPlayers()==2){

         numberOfBallsOnThisRow = numberOfBallsOnThisRow -1;}

        GameObject[] ballRow = new GameObject[numberOfBallsOnThisRow];

        for (int ballNumber = 0; ballNumber < numberOfBallsOnThisRow; ballNumber++) {
            GameObject ball = gameObjectFactory.getGameObject("BUBBLEFIELDBALL");
            Position ball_position = calculateBallPosition(ballNumber, row, isRowEven(row));
            ball.getTransform().setPosition(ball_position.getX(), ball_position.getY());
            ball.setName("ball_"  + row + "_" + ballNumber +player);
            ballRow[ballNumber] = ball;
        }

        return ballRow;
    }

    private GameObject[] createEmptyBallRow() {
        GameObject[] ballRow = new GameObject[numberOfBallsOnRow];
        return ballRow;
    }

    private boolean isRowEven(int rowNumber) {
       return isEven(rowNumber + numberOfRowsAdded);
    }

    private Position calculateBallPosition(int ballNumber, int row, boolean withXOffset) {
        double rowOffset_x = bubblefield_offset_x + (ballNumber * horizontal_space_between_bubbles);
        if(!withXOffset) {
            rowOffset_x += ball_width/2;
        }
        double x = ballNumber * ball_width + rowOffset_x;
        double y = row * ball_width + bubblefield_offset_y;
        return new Position(x, y);
    }

    private boolean isEven(int number) {
        return number % 2 == 0;
    }

    private void bringPositionInBounds(Position positionToCheck) {
        if (positionToCheck.getX() < 0) {
            positionToCheck.setX(0);
        }

        if(positionToCheck.getY() < 0) {
            positionToCheck.setY(0);
        }

        if(positionToCheck.getX() >= numberOfBallsOnRow) {
            positionToCheck.setX(numberOfBallsOnRow - 1);
        }

        if(positionToCheck.getY() >= maxNumberOfRows) {
            positionToCheck.setY(maxNumberOfRows - 1);
        }
    }

    public int random(int min,int max){
        return (int) (Math.random() * max + min);
    }

    private String colorPicker(int number){
        return gameObjectFactory.colorPicker(number);
    }

    static String getResource(String path) {
        return BubbleField.class.getResource(path).toExternalForm();
    }

    class ResetNumberOfBubblesBeforeAddingNewLine extends TimerTask {

        @Override
        public void run() {
            System.out.println("Resetting to 15 number of balls before adding new line");
            numberOfBallsAddedForAddingNewRow = 15;
        }
    }
}


