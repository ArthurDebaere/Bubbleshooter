package Game;


public class Transform {
    private Position position;
    private Vector velocity;
    private Dimension dimension;
    private double rotation;

    public Transform() {
        this(0, 0, 0, 0, 0, 1, 1);
    }

    public Transform(double pos_x, double pos_y,  double rotation, double width, double height) {
        this(pos_x, pos_y, 0, 0, rotation, width, height);
    }

    public Transform(double pos_x, double pos_y, double vel_x, double vel_y, double rotation, double width, double height) {
        position = new Position(pos_x, pos_y);
        velocity = new Vector(vel_x, vel_y);
        this.rotation = rotation;
        dimension = new Dimension(width, height);
    }

    public double getPosition_x() {
        return position.getX();
    }

    public double getPosition_y() {
        return position.getY();
    }

    public double getVelocity_x() {
        return velocity.getX();
    }

    public double getVelocity_y() {
        return velocity.getY();
    }

    public double getHeight() {
        return dimension.getHeight();
    }

    public double getWidth() {
        return dimension.getWidth();
    }

    public double getRotation() {
        return rotation;
    }

    public void setVelocity(double x, double y) {
        velocity = new Vector(x, y);
    }

    public void setPosition(double x, double y) {
        position = new Position(x, y);
    }

    public void setDimension(double width, double height) {
        dimension = new Dimension(width, height);
    }

    public void setRotation(double rotation) {this.rotation = rotation;}

    public double getCenterPoint_x() {
        return position.getX() + dimension.getWidth()/2;
    }

    public double getCenterPoint_y() {
        return position.getY() + dimension.getHeight()/2;
    }

    public String toString() {
        return String.format(position.toString() + velocity.toString() + dimension.toString() + "Rotation: " + rotation + "\n\n");
    }
}
