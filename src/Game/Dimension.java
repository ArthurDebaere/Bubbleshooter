//@formatter:off
package Game;

import java.text.MessageFormat;


public class Dimension {
    private double height;
    private double width;

    public Dimension(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Dimensions:  width = {0}    height = {1} \n", getWidth(), getHeight());
    }
}
