package Game;

import javafx.scene.shape.Rectangle;



public class RectangleColliderComponent implements Component{
    private Rectangle rectangleCollider;
    private Transform transform;
    private GameObject gameObject;

    public void initialize(GameObject gameObject) {
        this.gameObject = gameObject;
        this.transform = gameObject.getTransform();
        double width = transform.getWidth();
        double height = transform.getHeight();
        double pos_x = transform.getPosition_x();
        double pos_y = transform.getPosition_y();
        rectangleCollider = new Rectangle(pos_x, pos_y, width, height);
    }

    public void update(double time) {
        rectangleCollider.setX(transform.getPosition_x());
        rectangleCollider.setY(transform.getPosition_y());
    }

    public double getWidth() {
        return rectangleCollider.getWidth();
    }

    public double getHeight() {
        return rectangleCollider.getHeight();
    }

    public double get_x_pos() {
        return rectangleCollider.getX();
    }

    public double get_y_pos() {
        return rectangleCollider.getY();
    }

    public double getCenterX() {
        return get_x_pos() + getWidth()/2;
    }

    public double getCenterY() {
        return get_y_pos() + getHeight()/2;
    }

    public Rectangle getRectangleCollider() {
        return rectangleCollider;
    }
}
