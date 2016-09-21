//@formatter:off
package Game;

import java.text.MessageFormat;
public class Vector {
    private double x;
    private double y;

    public Vector() {
        this(0, 0);
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setX(double x) {this.x = x;}

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public void addX(double x) {
        this.x += x;
    }

    public void addY(double y) {
        this.y += y;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Vector:  X = {0}    Y = {1} \n", getX(), getY());
    }
}
