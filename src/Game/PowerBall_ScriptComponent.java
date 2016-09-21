package Game;


public class PowerBall_ScriptComponent implements Component {
    private GameObject gameObject;
    private GameObjectManager gameObjectManager;
    private boolean isFired = false;
    private int playerNumber;


    private Settings settings;

    public void initialize(GameObject gameObject) {
        this.gameObject = gameObject;
        this.gameObjectManager = gameObjectManager.getInstance();
        settings = Settings.getInstance();
    }

    public Settings getSettings() {
        return settings;
    }



    public void update(double time) {

    }

    public void fireBall(int playerNumber) {
        this.playerNumber = playerNumber;
        isFired = true;
    }

    public boolean getIsFired() {
        return isFired;
    }

    public void setIsFired(boolean isFired) {
        this.isFired = isFired;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
