package Game;

import javafx.scene.shape.Circle;


public class CircleColliderComponent implements Component {
    private Circle circleCollider;
    private Transform transform;
    private GameObject gameObject;

    public void initialize(GameObject gameObject) {
        this.gameObject = gameObject;
        this.transform = gameObject.getTransform();
        double radius = transform.getWidth()/2;
        double centerpoint_x = transform.getCenterPoint_x();
        double centerpoint_y = transform.getCenterPoint_y();
        circleCollider = new Circle (centerpoint_x, centerpoint_y, radius);
    }

    public void update(double time) {
        circleCollider.setCenterX(transform.getCenterPoint_x());
        circleCollider.setCenterY(transform.getCenterPoint_y());
    }


    public Circle getCircleCollider() {
        return circleCollider;
    }

}
