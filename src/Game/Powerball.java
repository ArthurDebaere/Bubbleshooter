package Game;



public class Powerball {
    private int powerID;
    private String powerName;
    private int nrOfBalls;
    private int radius;
    private int nrOfLines;

    public int getTimeElement() {
        return timeElement;
    }

    public int getNrOfLines() {
        return nrOfLines;
    }

    public int getRadius() {
        return radius;
    }

    public int getNrOfBalls() {
        return nrOfBalls;
    }

    public String getPowerName() {
        return powerName;
    }

    private int timeElement;

    public Powerball(int powerID,String powerName, int nrOfBalls, int radius, int nrOfLines, int timeElement) {
        this.powerID = powerID;
        this.powerName = powerName;
        this.nrOfBalls = nrOfBalls;
        this.radius = radius;
        this.nrOfLines = nrOfLines;
        this.timeElement = timeElement;
    }
}
