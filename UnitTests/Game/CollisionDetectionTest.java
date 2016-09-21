package Game;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.junit.Assert;
import org.junit.Test;


public class CollisionDetectionTest {
    @Test
    public void shouldReturnFalse_Circle_Circle_WhenNoIntersection(){
        double circle1_centerPoint_x = 10;
        double circle1_centerPoint_y = 10;
        double circle1_radius = 10;

        double circle2_centerPoint_x = 30;
        double circle2_centerPoint_y = 30;
        double circle2_radius = 10;

        Circle circle1 = new Circle(circle1_centerPoint_x, circle1_centerPoint_y, circle1_radius);
        Circle circle2 = new Circle(circle2_centerPoint_x, circle2_centerPoint_y, circle2_radius);

        Assert.assertFalse(CollisionDetection.checkCollision(circle1, circle2));
    }

    @Test
    public void shouldReturnTrue_Circle_Circle_WhenIntersection() {
        double circle1_centerPoint_x = 10;
        double circle1_centerPoint_y = 10;
        double circle1_radius = 10;

        double circle2_centerPoint_x = 20;
        double circle2_centerPoint_y = 20;
        double circle2_radius = 15;

        Circle circle1 = new Circle(circle1_centerPoint_x, circle1_centerPoint_y, circle1_radius);
        Circle circle2 = new Circle(circle2_centerPoint_x, circle2_centerPoint_y, circle2_radius);

        Assert.assertTrue(CollisionDetection.checkCollision(circle1, circle2));
    }

    @Test
    public void shouldReturnFalse_Rectangle_Circle_WhenNoIntersection() {
        double circle_centerPoint_x = 10;
        double circle_centerPoint_y = 10;
        double circle_radius = 10;

        double rectangle_pos_x = 30;
        double rectangle_pos_y = 30;
        double rectangle_width = 20;
        double rectangle_height = 20;

        Circle circle = new Circle(circle_centerPoint_x, circle_centerPoint_y, circle_radius);
        Rectangle rectangle = new Rectangle(rectangle_pos_x, rectangle_pos_y, rectangle_width, rectangle_height);

        Assert.assertFalse(CollisionDetection.checkCollision(circle, rectangle));
    }

    @Test
    public void shouldReturnTrue_Rectangle_Circle_WhenIntersection() {
        double circle_centerPoint_x = 10;
        double circle_centerPoint_y = 10;
        double circle_radius = 10;

        double rectangle_pos_x = 10;
        double rectangle_pos_y = 10;
        double rectangle_width = 20;
        double rectangle_height = 20;

        Circle circle = new Circle(circle_centerPoint_x, circle_centerPoint_y, circle_radius);
        Rectangle rectangle = new Rectangle(rectangle_pos_x, rectangle_pos_y, rectangle_width, rectangle_height);

        Assert.assertTrue(CollisionDetection.checkCollision(circle, rectangle));
    }
}