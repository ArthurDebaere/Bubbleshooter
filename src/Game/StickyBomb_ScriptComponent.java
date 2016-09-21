package Game;

import javafx.scene.image.Image;

import java.util.ArrayList;


public class StickyBomb_ScriptComponent extends PowerBall_ScriptComponent {
    private GameObject gameObject;
    private GameObjectManager gameObjectManager;

    public void initialize(GameObject gameObject) {
        super.initialize(gameObject);
        String powerName = getSettings().getPowerballs().get(1).getPowerName();
        gameObject.getImageView().setImage(new Image(GameObjectFactory.getResource("res/PowerBalls/"+powerName+".png")));
        this.gameObject = gameObject;
    }

    public void update(double time) {
        if(getIsFired()) {
            int range = getSettings().getPowerballs().get(1).getRadius();
            gameObjectManager = gameObjectManager.getInstance();
            boolean collidedWithBubbleFieldBall = false;
            CircleColliderComponent collider_ball = (CircleColliderComponent) gameObject.getComponent(GameObjectFactory.Components.ColliderComponent);

            if(!collidedWithBubbleFieldBall) {
                ArrayList<GameObject> bubbleFieldBalls = gameObjectManager.getBubbleField(getPlayerNumber()).getAllNotNullBalls();
                for (GameObject ball : bubbleFieldBalls) {
                    CircleColliderComponent bubblefield_ball_collider = (CircleColliderComponent) ball.getComponent(GameObjectFactory.Components.ColliderComponent);
                    if (bubblefield_ball_collider.getCircleCollider() != null && collider_ball.getCircleCollider() != null) {
                        if (CollisionDetection.checkCollision(bubblefield_ball_collider.getCircleCollider(), collider_ball.getCircleCollider())) {
                            Jukebox.playSFX(Jukebox.SFX.stickybombexplosion_v1.getValue());
                            gameObjectManager.getBubbleField(getPlayerNumber()).burstBubblesInRange(gameObject, range);
                            gameObjectManager.getBubbleField(getPlayerNumber()).findAndRemoveFloatingClusters();
                            gameObject.setRemoveFromGame(true);
                            setIsFired(false);
                        }
                    }
                }
            }
        }
    }
}
