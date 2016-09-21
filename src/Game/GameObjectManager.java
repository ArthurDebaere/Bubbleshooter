package Game;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



public class GameObjectManager {
    private static GameObjectManager gameObjectManager = new GameObjectManager();
    private BubbleField bubbleField1;
    private BubbleField bubbleField2;
    private HashMap<String, GameObject> gameObjectsInWaitingLine;
    private HashMap<String, GameObject> gameObjects;
    private Settings settings;

    private Group root;

    private GameObjectManager() {

    }

    public static GameObjectManager getInstance() {
        return gameObjectManager;
    }

    public void initialize(Group root) {
        this.root = root;
        settings = Settings.getInstance();
        gameObjects = new HashMap<>();
        gameObjectsInWaitingLine = new HashMap<>();
        bubbleField1 = new BubbleField(1);
        if (settings.getPlayers() == 2) {
            bubbleField2 = new BubbleField(2);
        }
    }

    public void add(String name, GameObject gameObject) {
            if(!gameObjects.containsKey(name)) {
                gameObjects.put(name, gameObject);
                root.getChildren().add(gameObject.getImageView());
            }
    }

    public void add(String name, GameObject gameObject, int playernumber) {
        if(!gameObjects.containsKey(name)) {
            gameObject.setPlayerNumber(playernumber);
            gameObjects.put(name, gameObject);
            root.getChildren().add(gameObject.getImageView());
        }
    }


    public GameObject get(String name) {
        if(gameObjects.containsKey(name)) {
            return gameObjects.get(name);
        } else {
            return null;
        }
    }

    public void addAtRuntime(String name, GameObject gameObject) {
        if(!gameObjectsInWaitingLine.containsKey(name)) {
            gameObjectsInWaitingLine.put(name, gameObject);
        }
    }

    public void createGameObjects() {
        for (int player =1; player<=settings.getPlayers();player++ ){
            GameObjectFactory gameObjectFactory = new GameObjectFactory(player);
            add("CANNON_BASE" + player, gameObjectFactory.getGameObject("CANNON_BASE"), player);
            add("CANNON"+ player, gameObjectFactory.getGameObject("CANNON"), player);
            add("SIDEWALL_LEFT" + player, gameObjectFactory.getGameObject("LEFT_SIDEWALL"), player);
            add("SIDEWALL_RIGHT" + player, gameObjectFactory.getGameObject("RIGHT_SIDEWALL"), player);
            add("TOP_WALL" + player, gameObjectFactory.getGameObject("TOP_WALL"), player);
            addBubbleField(player);
        }
    }

    public void initializeGameObjects() {
        for (GameObject gameObject : gameObjects.values()) {
            gameObject.initialize();
        }

    }

    public void updateGameObjects(double time) {

        removeFlaggedGameobjects();
        bubbleField1.update();
        if (settings.getPlayers()==2){
        bubbleField2.update();}
        addGameObjectsInWaitingLine();
        for (GameObject gameObject : gameObjects.values()) {
            gameObject.update(time);
        }
    }

    public void addGameObjectsInWaitingLine() {
        if(!gameObjectsInWaitingLine.isEmpty()) {
            for (HashMap.Entry<String, GameObject> entry : gameObjectsInWaitingLine.entrySet()) {
                String name = entry.getKey();
                GameObject gameObject = entry.getValue();
                gameObject.initialize();
                add(name, gameObject);
            }
            gameObjectsInWaitingLine.clear();
        }
    }

    public HashMap<String, GameObject> getGameObjects() {
        return gameObjects;
    }

    private void addBubbleField(int player) {
        ArrayList<GameObject> bubbleFieldBalls1 = bubbleField1.getAllNotNullBalls();
        for (GameObject ball: bubbleFieldBalls1) {
            add(ball.getName(), ball);
        }

        if (player==2){
        ArrayList<GameObject> bubbleFieldBalls2 = bubbleField2.getAllNotNullBalls();
            for (GameObject ball: bubbleFieldBalls2) {
                add(ball.getName(), ball);
            }
        }
    }

    private void removeFlaggedGameobjects() {
        Iterator<Map.Entry<String,GameObject>> gameObjectIterator = gameObjects.entrySet().iterator();
        while (gameObjectIterator.hasNext()) {
            Map.Entry<String,GameObject> entry = gameObjectIterator.next();
            if(entry.getValue().getRemoveFromGame()){
                removeImageView(entry.getValue().getImageView());
                gameObjectIterator.remove();
            }
        }
    }

    private void removeImageView(ImageView imageView) {
        root.getChildren().remove(imageView);
    }

    public BubbleField getBubbleField(int player) {
        if (player==1) return bubbleField1;
        if (player==2) return bubbleField2;
        return null;
    }
}
