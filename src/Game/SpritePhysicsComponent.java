package Game;


public class SpritePhysicsComponent implements Component {
    private GameObject gameObject;
    private Transform transform;

    public void initialize(GameObject gameObject) {
        this.gameObject = gameObject;
        this.transform = gameObject.getTransform();
    }

    public void update(double time) {

        if(transform.getPosition_y() > 900 ) {
            gameObject.setRemoveFromGame(true);
        }

        double newPositionX = transform.getPosition_x() + (transform.getVelocity_x() * time);
        double newPositionY = transform.getPosition_y() + (transform.getVelocity_y() * time);
        transform.setPosition(newPositionX, newPositionY);
    }
}
