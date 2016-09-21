package Game;

import javafx.scene.image.Image;

import java.util.ArrayList;


public class BeachBall_ScriptComponent extends PowerBall_ScriptComponent {
    private GameObject gameObject;
    private GameObjectManager gameObjectManager;
    private CircleColliderComponent collider_ball;

    public void initialize(GameObject gameObject) {
        super.initialize(gameObject);
        String powerName = getSettings().getPowerballs().get(0).getPowerName();
        System.out.println(powerName);
        gameObject.getImageView().setImage(new Image(GameObjectFactory.getResource("res/PowerBalls/"+powerName+".png")));
        this.gameObject = gameObject;
    }

    public void update(double time) {
        if(getIsFired()) {
            gameObjectManager = gameObjectManager.getInstance();

            boolean collidedWithBubbleFieldBall = false;

            String top_wall_name = "TOP_WALL";
            if(getPlayerNumber() == 1) {
                top_wall_name += 1;
            } else {
                top_wall_name += 2;
            }

            RectangleColliderComponent top_wall_collider = (RectangleColliderComponent) gameObjectManager.get(top_wall_name).getComponent(GameObjectFactory.Components.ColliderComponent);
            collider_ball = (CircleColliderComponent) gameObject.getComponent(GameObjectFactory.Components.ColliderComponent);

            if(!collidedWithBubbleFieldBall) {
                ArrayList<GameObject> bubbleFieldBalls = gameObjectManager.getBubbleField(getPlayerNumber()).getAllNotNullBalls();
                for (GameObject ball : bubbleFieldBalls) {

                    CircleColliderComponent bubblefield_ball_collider = (CircleColliderComponent) ball.getComponent(GameObjectFactory.Components.ColliderComponent);

                    if (bubblefield_ball_collider.getCircleCollider() != null && collider_ball.getCircleCollider() != null) {
                        if (CollisionDetection.checkCollision(bubblefield_ball_collider.getCircleCollider(), collider_ball.getCircleCollider())) {
                            String imageViewNameOfBubbleFieldBall = ball.getImageViewName();
                            gameObject.setImageViewName(imageViewNameOfBubbleFieldBall);
                            gameObject.getImageView().setImage(ball.getImageView().getImage());
                            collidedWithBubbleFieldBall = true;
                            //TODO: SOMETIMES BALL GOES AWAY, SOMETIMES NOT
                        }
                    }
                }
            }

            if(collider_ball.getCircleCollider() != null && top_wall_collider.getRectangleCollider() != null) {
                if(CollisionDetection.checkCollision(collider_ball.getCircleCollider(), top_wall_collider.getRectangleCollider())) {
                    gameObject.setRemoveFromGame(true);
                    setIsFired(false);
                }
            }
        }
    }
}
