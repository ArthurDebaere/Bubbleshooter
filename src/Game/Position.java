//@formatter:off
package Game;

import javafx.geometry.Pos;import java.text.MessageFormat;
public class Position {
    private double x;
    private double y;

    public Position(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Position:  X = {0}    Y = {1} \n", getX(), getY());
    }
}
