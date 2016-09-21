package Game;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class CollisionDetection {

    public static boolean checkCollision(Circle circle, Rectangle rectangle) {
        double rectangle_center_x = rectangle.getX() + rectangle.getWidth()/2;
        double rectangle_center_y = rectangle.getY() + rectangle.getHeight()/2;

        double distanceFromCircle_x = Math.abs(circle.getCenterX() - rectangle_center_x);
        double distanceFromCircle_y = Math.abs(circle.getCenterY() - rectangle_center_y);

        if(distanceFromCircle_x > (rectangle.getWidth()/2 + circle.getRadius())) {
            return false;
        }

        if(distanceFromCircle_y > (rectangle.getHeight()/2) + circle.getRadius()) {
            return false;
        }

        if(distanceFromCircle_x <= (rectangle.getWidth()/2)) {
            return true;
        }

        if(distanceFromCircle_y <= (rectangle.getHeight()/2)) {
            return true;
        }

        //corner of rectangle
        double distance_corner_to_circle = Math.pow((distanceFromCircle_x - rectangle.getWidth()/2), 2) + Math.pow((distanceFromCircle_y - rectangle.getHeight()/2), 2);
        return (distance_corner_to_circle <= Math.pow(circle.getRadius(), 2));
    }

    public static boolean checkCollision(Circle circle1, Circle circle2) {
        double x_difference = circle2.getCenterX() - circle1.getCenterX();
        double y_difference = circle2.getCenterY() - circle1.getCenterY();
        double radius_sum = circle1.getRadius() + circle2.getRadius();

        if((x_difference * x_difference) + (y_difference * y_difference) < radius_sum * radius_sum) {
            return true;
        } else {
            return false;
        }
    }
}
