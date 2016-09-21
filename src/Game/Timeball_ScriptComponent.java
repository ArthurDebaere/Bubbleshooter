package Game;

import javafx.scene.image.Image;


public class Timeball_ScriptComponent extends PowerBall_ScriptComponent {
    private GameObject gameObject;
    private GameObjectManager gameObjectManager;

    public void initialize(GameObject gameObject) {
        super.initialize(gameObject);
        String powerName = getSettings().getPowerballs().get(9).getPowerName();
        gameObject.getImageView().setImage(new Image(GameObjectFactory.getResource("res/PowerBalls/"+powerName+".png")));
        this.gameObject = gameObject;
        gameObject.removeComponent(GameObjectFactory.Components.PhysicsComponent);
        gameObject.removeComponent(GameObjectFactory.Components.ColliderComponent);
    }

    public void update(double time) {
        if(getIsFired()) {
            gameObjectManager = GameObjectManager.getInstance();
            gameObjectManager.getBubbleField(getPlayerNumber()).setNumberOfBallsAddedForAddingNewRow(5, true);
            setIsFired(false);
            gameObject.setRemoveFromGame(true);
        }
    }
}
