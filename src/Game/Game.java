package Game;

import Game.GUI.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

public class Game extends Application {
    private Settings settings = Settings.getInstance();
    private Group root;
    private GraphicsContext gc;
    private double elapsedTime;
    private GameObjectManager gameObjectManager;
    private static java.util.Timer gameTimer;
    //private int seconden = 180;
    private int seconden = 500;
    private int currentRunTime=0;
    private countDownTimer countDownTimer;
    private boolean timeEnd = false;
    private Players players = Players.getInstance();
    private GameDAO gameDAO = GameDAO.getInstance();
    private static boolean isColliderDrawingEnabled = false;
    private MediaPlayer mediaPlayer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        if (gameDAO.connection == null) {
            showNoDBConnection(primaryStage);
        } else {
            Stage stage;
            stage = primaryStage;
            stage.setTitle("BubbleCannon Project 2015");
            stage.setResizable(false);
            handleSceneChange("GUI/LoginScreen.fxml", stage, 430, 600);
            settings.setDefaultControls();
            playSound();
        }

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                final Stage dialog = new Stage();

                try{
                    //initialize the pauze game window
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.initOwner(primaryStage);

                    GameDAO.getInstance().connection.close();

                    //handleSceneChange("GUI/CloseMenu.fxml", dialog, 327, 147);
                    //event.consume();
                    System.exit(0);
                }
                catch(Exception exception){
                    exception.printStackTrace();
                }

                dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        System.out.println("Resume1 reached");
                    }
                });
            }
        });
    }

    private void showNoDBConnection(Stage primaryStage) throws IOException {
        Stage stage;
        stage = primaryStage;
        stage.setTitle("BubbleCannon Project 2015");
        stage.setResizable(false);
        stage.setHeight(305);
        handleSceneChange("GUI/noDBmenu.fxml", stage, 430, 305);
    }



    public void stopSound(){
        mediaPlayer.stop();
    }

    public void startGame(Stage primaryStage) throws IOException{
        setupScene(primaryStage);
        gameObjectManager = GameObjectManager.getInstance();
        newGame();

        LongValue lastNanoTime = new LongValue(System.nanoTime());

        new AnimationTimer() {

                @Override
                public void handle(long currentNanoTime){
                    elapsedTime = (currentNanoTime - lastNanoTime.value) / 1000000000.5;
                    lastNanoTime.value = currentNanoTime;
                    clearScreen();
                    gameObjectManager.updateGameObjects(elapsedTime);

                    if (isColliderDrawingEnabled) {
                        drawColliders(gc);
                    }

                    for (int player = 1; player <= settings.getPlayers(); player++) {
                        if (gameObjectManager.getBubbleField(player).isGameOver()) {
                            this.stop();
                            try{
                                showGameOverScreen(primaryStage, "bottomReached", player, players.getPlayersScore()[player - 1]);
                            }
                            catch(Exception exception){
                                exception.printStackTrace();
                            }
                        }
                            if (gameObjectManager.getBubbleField(player).isGameWon()){
                                this.stop();
                                try{
                                    showGameOverScreen(primaryStage,"win",player,players.getPlayersScore()[player - 1]);
                                }
                                catch(Exception exception){
                                    exception.printStackTrace();
                                }
                            }
                    }

                    Controller controller = new Controller();
                    if(Settings.getInstance().getPlayers() == 1){
                        controller.changeScoreLabelsinField(players.getPlayersScore(), primaryStage);
                    }else{
                        controller.changeScoreLabelsinField(players.getPlayersScore(), primaryStage);
                    }


                    if ( timeEnd) {
                        this.stop();
                        if (settings.getPlayers()==1){
                            try{
                                showGameOverScreen(primaryStage,"singlePlayer",0,players.getPlayersScore()[0]);
                            }
                            catch(Exception exception){
                                exception.printStackTrace();
                            }

                        }else if (players.getPlayersScore()[0] == players.getPlayersScore()[1]) {
                            try{
                                showGameOverScreen(primaryStage,"draw",0,0);
                            }
                            catch(Exception exception){
                                exception.printStackTrace();
                            }
                        } else if (players.getPlayersScore()[0] < players.getPlayersScore()[1]) {
                            try{
                                showGameOverScreen(primaryStage, "win", 2, players.getPlayersScore()[1]);
                            }
                            catch(Exception exception){
                                exception.printStackTrace();
                            }
                        } else {
                            try{
                                showGameOverScreen(primaryStage, "win", 1, players.getPlayersScore()[0]);
                            }
                            catch(Exception exception){
                                exception.printStackTrace();
                            }
                        }
                    }
                    InputManager inputMG = InputManager.getInstance();
                    ArrayList<String> input = inputMG.getInput();
                    ShowLine();

                    controller.changeTimeLabel(currentRunTime, seconden, primaryStage);

                    drawLosingLine();
                    if (input.contains("ESCAPE")){
                        this.stop();
                        input.remove(input.indexOf("ESCAPE"));

                        final Stage dialog = new Stage();

                        try{
                            pause();
                            //initialize the pauze game window
                            dialog.initModality(Modality.APPLICATION_MODAL);
                            dialog.initOwner(primaryStage);
                            handleSceneChange("Paused.fxml", dialog, 327, 147);
                        }
                        catch(Exception exception){
                            exception.printStackTrace();
                        }

                        dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            public void handle(WindowEvent we) {
                                start();
                                resume();
                            }
                        });
                    }
                }

            }.start();

    }

    public void drawLosingLine(){
        gc.setGlobalAlpha(0.25);
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,1000,590);
    }

    public void ShowLine(){
        if (settings.isHelpLine() ){
            gc.setGlobalAlpha(0.75);
            for (int player = 1; player<=settings.getPlayers();player++){
                lineCreator(player);
            }
        }
    }

    private void newGame() {
        players.resetGameScore();
        gameObjectManager.initialize(root);
        gameObjectManager.createGameObjects();
        gameObjectManager.initializeGameObjects();
        resume();
    }

    public void restartGame(Stage primaryStage){
        try{
            players.resetGameScore();
            resetTimer();
            startGame(primaryStage);
        }
        catch(Exception ex){
            System.out.println(ex.toString());
        }
    }

    public void resetTimer(){
        timeEnd = false;
        currentRunTime=0;
    }

    private void pause() {
        gameTimer.cancel();
        gameTimer.purge();

    }
    private void resume(){
            gameTimer = new java.util.Timer();
            countDownTimer = new countDownTimer();
            gameTimer.schedule(countDownTimer ,0 , 1000);
    }
    class countDownTimer extends TimerTask  {

        countDownTimer( ){}
        @Override
        public void run() {
            currentRunTime+=1;
            if (currentRunTime>=seconden)
            timeEnd = true;
        }
    }


   private void showGameOverScreen(Stage primaryStage,String status,  int player, int playerScore) throws IOException {
        pause();
        Controller controller = new Controller();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);

        if (status.equals("win")) {

            handleSceneChange("LevelCleared.fxml", dialog, 319, 203);


            controller.changeScoreLabels(playerScore, dialog);
        }
        if (status.equals("draw")){
            //it is a draw
        }
        if (status.equals("bottomReached")){
            handleSceneChange("LosingScreen.fxml", dialog, 373, 203);

        }

        if (status.equals("singlePlayer")){
            handleSceneChange("LevelCleared.fxml", dialog, 319, 203);

            gameDAO.updateScore(players.getPlayerOneID(),playerScore);
            controller.changeScoreLabels(playerScore,dialog);
        }
        if (status.equals("gameClear")) {
            if (players.getPlayersScore()[0] > players.getPlayersScore()[1]) {
                player = 1;
            } else {
                player = 2;
            }

        }

    }


    private void setupScene(Stage primaryStage) throws IOException{
        root = new Group();

        Parent background;
        if(Settings.getInstance().getPlayers() == 1){
            background = FXMLLoader.load(getClass().getResource("Game.fxml"));
        }else{
            background = FXMLLoader.load(getClass().getResource("GameMulti.fxml"));
        }
        Scene mainGameScene = new Scene(root, settings.getWindowSize(), settings.getGameHeight());
        primaryStage.setScene(mainGameScene);
        primaryStage.setResizable(false);
        Canvas canvas = new Canvas(settings.getWindowSize(), settings.getGameHeight());
        gc = canvas.getGraphicsContext2D();

        root.getChildren().add(background);
        root.getChildren().add(canvas);

        mainGameScene.setOnKeyPressed(new KeyPressedListener());
        mainGameScene.setOnKeyReleased(new KeyReleasedListener());
    }

    public void playSound(){
        URL resource = getClass().getResource("res/Soundtracks/AdventureAwaits.mp3");
        Media media = new Media(resource.toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    private void clearScreen() {
        gc.clearRect(0, 0, settings.getWindowSize(), settings.getGameHeight());
    }


    private void drawColliders(GraphicsContext gc) {
        HashMap<String, GameObject> gameObjects = gameObjectManager.getGameObjects();
         for (HashMap.Entry<String, GameObject> entry : gameObjects.entrySet()) {
                GameObject gameObject = entry.getValue();
                //gc.setLineDashes(0);


                Component colComponent = gameObject.getComponent(GameObjectFactory.Components.ColliderComponent);
                if(colComponent instanceof RectangleColliderComponent) {
                    Rectangle rectangleCol = ((RectangleColliderComponent) colComponent).getRectangleCollider();
                    double width = rectangleCol.getWidth();
                    double height = rectangleCol.getHeight();
                    double x_pos = rectangleCol.getX();
                    double y_pos = rectangleCol.getY();
                    gc.setStroke(Color.FORESTGREEN.brighter());
                    gc.setLineWidth(2);
                    gc.strokeRect(x_pos, y_pos, width, height);
                } else if(colComponent instanceof CircleColliderComponent) {
                    Circle circleCollider = ((CircleColliderComponent) colComponent).getCircleCollider();
                    double radius = circleCollider.getRadius();
                    double center_x_pos = circleCollider.getCenterX();
                    double center_y_pos = circleCollider.getCenterY();
                    double x_pos = center_x_pos - radius;
                    double y_pos = center_y_pos - radius;
                    gc.setStroke(Color.FORESTGREEN.brighter());
                    gc.setLineWidth(2);
                    gc.strokeOval(x_pos, y_pos, radius*2, radius*2);
                }
         }
        for (int player = 1; player<=settings.getPlayers();player++){
        lineCreator(player);
        }
    }

    public void lineCreator( int player){
        HashMap<String, GameObject> gameObjects = gameObjectManager.getGameObjects();

           GameObject cannon =  gameObjects.get("CANNON" + player);
                gc.setStroke(Color.RED);
                gc.setLineWidth(2);
                double beginX = cannon.getTransform().getPosition_x();
                double beginY = cannon.getTransform().getPosition_y();
                double rotation = cannon.getTransform().getRotation();
                double radiansControl = (Math.toRadians(rotation));

                double radians =radiansControl-0.001;

                double rechthoekszijdeA = beginX-gameObjects.get("SIDEWALL_LEFT"+player).getTransform().getPosition_x()+40;
                double rechthoeksZijdeB = Math.tan(radians)*rechthoekszijdeA;
                double schuineZijde= Math.pow(rechthoeksZijdeB,2) + Math.pow(rechthoekszijdeA,2);
                schuineZijde = Math.sqrt(schuineZijde);
                double size = schuineZijde;

                double velocity_x = -size * Math.cos(radians);
                double velocity_y = -size* Math.sin(radians);

                //gc.setLineDashes(4);
                gc.strokeLine(beginX + 50, beginY + 25, beginX + velocity_x + 50, beginY + velocity_y + 25);

                if (gameObjects.get("SIDEWALL_LEFT"+player).getTransform().getPosition_x()+30>=beginX + velocity_x + 50) {
                    double leftWall = gameObjects.get("SIDEWALL_LEFT"+player).getTransform().getPosition_x()+30;
                    gc.strokeLine(leftWall, beginY + velocity_y +25,leftWall   - velocity_x, beginY + velocity_y + velocity_y + 50);
                } else if (gameObjects.get("SIDEWALL_RIGHT"+player).getTransform().getPosition_x()<=beginX + velocity_x + 50) {
                    double rightWall = gameObjects.get("SIDEWALL_RIGHT"+player).getTransform().getPosition_x();
                    gc.strokeLine(rightWall, beginY + velocity_y +25,rightWall-velocity_x, beginY + velocity_y + velocity_y +55);
                }

    }


    public class KeyPressedListener implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
            InputManager inputManager = InputManager.getInstance();
            inputManager.addKey(event);
        }
    }

    public class KeyReleasedListener implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
            InputManager inputManager = InputManager.getInstance();
            inputManager.removeKey(event);
        }
    }

    public void handleSceneChange(String fxml, Stage stage, int width, int height) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxml));

        stage.setScene(new Scene(root, width, height));
        stage.setResizable(false);
        stage.show();
    }
}
