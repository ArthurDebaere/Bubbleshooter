package Game;

import javafx.scene.image.ImageView;


public class SpriteRenderComponent implements Component {
    private GameObject gameObject;
    private ImageView imageView;
    private Transform transform;

    public void initialize(GameObject gameObject) {
        this.gameObject = gameObject;
        this.transform = gameObject.getTransform();
        this.imageView = gameObject.getImageView();
    }

    public void update(double time) {
        imageView.setX(transform.getPosition_x());
        imageView.setY(transform.getPosition_y());
        imageView.setRotate(transform.getRotation());
    }
}
