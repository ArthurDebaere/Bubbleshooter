package Game;



import java.util.ArrayList;



public class CannonballPhysicsComponent implements Component {
    private GameObject gameObject;
    private Transform transform;
    private GameObjectManager gameObjectManager;
    private CircleColliderComponent collider_ball;
    private RectangleColliderComponent collider_sidewall_left;
    private RectangleColliderComponent collider_sidewall_right;
    private RectangleColliderComponent collider_top_wall;
    private boolean collidersLoaded;
    private boolean addedBallToBubbleField = false;
    private int player;

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public void initialize(GameObject gameObject) {
        this.gameObject = gameObject;
        this.transform = gameObject.getTransform();
        this.gameObjectManager = GameObjectManager.getInstance();
    }

    public void update(double time) {
        if(!collidersLoaded) {
            loadColliders();
        }

        if(CollisionDetection.checkCollision(collider_ball.getCircleCollider(), collider_sidewall_left.getRectangleCollider())) {
            bounceOffWall(false);
        } else if (CollisionDetection.checkCollision(collider_ball.getCircleCollider(), collider_sidewall_right.getRectangleCollider())) {
            bounceOffWall(true);
        } else if (CollisionDetection.checkCollision(collider_ball.getCircleCollider(), collider_top_wall.getRectangleCollider())) {
            if(!addedBallToBubbleField) {
                stopBall();
                gameObjectManager.getBubbleField(player).addCannonBallToMatrix(gameObject);
                addedBallToBubbleField = true;
            }
        }

        ArrayList<GameObject> bubbleFieldBalls = gameObjectManager.getBubbleField(player).getAllNotNullBalls();
        for (GameObject ball: bubbleFieldBalls) {
            if(!addedBallToBubbleField) {
                CircleColliderComponent bubblefield_ball_collider = (CircleColliderComponent) ball.getComponent(GameObjectFactory.Components.ColliderComponent);
                if(bubblefield_ball_collider.getCircleCollider() != null && collider_ball.getCircleCollider() != null) {
                    if (CollisionDetection.checkCollision(collider_ball.getCircleCollider(), bubblefield_ball_collider.getCircleCollider())) {
                        stopBall();
                        gameObjectManager.getBubbleField(player).addCannonBallToMatrix(gameObject);
                        addedBallToBubbleField = true;
                    }
                }
            }
        }

        double newPositionX = transform.getPosition_x() + (transform.getVelocity_x() * time);
        double newPositionY = transform.getPosition_y() + (transform.getVelocity_y() * time);
        transform.setPosition(newPositionX, newPositionY);
    }

    private void loadColliders() {
        collider_ball = (CircleColliderComponent)gameObject.getComponent(GameObjectFactory.Components.ColliderComponent);
        collider_sidewall_left = (RectangleColliderComponent)gameObjectManager.get("SIDEWALL_LEFT" +player).getComponent(GameObjectFactory.Components.ColliderComponent);
        collider_sidewall_right = (RectangleColliderComponent)gameObjectManager.get("SIDEWALL_RIGHT"+player).getComponent(GameObjectFactory.Components.ColliderComponent);
        collider_top_wall = (RectangleColliderComponent)gameObjectManager.get("TOP_WALL"+player).getComponent(GameObjectFactory.Components.ColliderComponent);
        collidersLoaded = true;
    }

    private void bounceOffWall(boolean isRightWall) {
        if(isRightWall && transform.getVelocity_x()>0) {
            transform.setVelocity(-transform.getVelocity_x(), transform.getVelocity_y());
        } else if (transform.getPosition_x()<30+transform.getWidth()/2 +500*(player-1) &&transform.getVelocity_x()<0){
            transform.setVelocity(-transform.getVelocity_x(), transform.getVelocity_y());
        }

    }

    private void stopBall() {
        gameObject.setIsMoving(false);
        transform.setVelocity(0, 0);
    }
}
