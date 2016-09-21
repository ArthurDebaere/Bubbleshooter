package Game;

import java.util.ArrayList;


public class CannonInputComponent implements Component {
    private  double CANNON_ROTATION_SPEED = 0.4;
    private final double CANNON_MIN_ROTATION_DEGREES = 20;
    private final double CANNON_MAX_ROTATION_DEGREES = 160;
    private final double START_ROTATION = 90;
    private GameObject gameObject;
    private Transform transform;
    private Settings settings = Settings.getInstance();

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    private int player;



    public void initialize(GameObject gameObject) {
        this.gameObject = gameObject;
        transform = gameObject.getTransform();
        transform.setRotation(START_ROTATION);
    }


    public void update(double time) {
            InputManager inputMG = InputManager.getInstance();
            ArrayList<String> input = inputMG.getInput();

            if (input.contains(settings.getControls()[player][0])) {
                rotateToLeft();
            }

            if (input.contains(settings.getControls()[player][1])) {
                rotateToRight();
            }

            if (input.contains(settings.getControls()[player][2]) && !inputMG.getIsHoldingSpace()) {
                    CannonShootingComponent cannonShootingComponent = (CannonShootingComponent) gameObject.getComponent(GameObjectFactory.Components.ShootingComponent);
                    cannonShootingComponent.setPlayer(player+1);
                    cannonShootingComponent.shoot();
                    inputMG.setIsHoldingSpace(true);
            }
            if (input.contains(settings.getControls()[player][3])) {
                CANNON_ROTATION_SPEED=2;
            }else {
                CANNON_ROTATION_SPEED=0.4;
            }
        }
    public void rotateToLeft() {
        if(transform.getRotation() > CANNON_MIN_ROTATION_DEGREES) {
            addRotation(CANNON_ROTATION_SPEED * -1);
        }
    }

    public void rotateToRight() {
        if(transform.getRotation() < CANNON_MAX_ROTATION_DEGREES) {
            addRotation(CANNON_ROTATION_SPEED);
        }
    }

    public void addRotation(double rotation) {
        transform.setRotation(transform.getRotation() + rotation);
    }
}
