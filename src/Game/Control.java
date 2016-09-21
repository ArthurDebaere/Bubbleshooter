package Game;


public class Control {
    private String leftKey;
    private String rightKey;
    private String shootKey;

    public Control(String leftKey, String rightKey, String shootKey) {
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.shootKey = shootKey;
    }

    public String getLeftKey() {
        return leftKey;
    }

    public String getRightKey() {
        return rightKey;
    }

    public String getShootKey() {
        return shootKey;
    }
}
