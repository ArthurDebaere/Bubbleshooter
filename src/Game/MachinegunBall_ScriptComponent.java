package Game;

import javafx.scene.image.Image;


public class MachinegunBall_ScriptComponent extends PowerBall_ScriptComponent {
    private GameObject gameObject;
    private GameObjectManager gameObjectManager;
    private int maxNumberOfBalls = 10; //getSettings().getPowerballs().get(5).getNrOfBalls();
    private Settings settings =Settings.getInstance();

    private double startRotation = 20;
    private double rotationInterval;


    public void initialize(GameObject gameObject) {
        super.initialize(gameObject);
        String powerName = getSettings().getPowerballs().get(5).getPowerName();
        gameObject.getImageView().setImage(new Image(GameObjectFactory.getResource("res/PowerBalls/"+powerName+".png")));
        this.gameObject = gameObject;
        this.rotationInterval = 140/maxNumberOfBalls;
    }

    public void update(double time) {
        if(getIsFired()) {
            gameObjectManager = GameObjectManager.getInstance();
            //FUNCTIONALITY
            GameObject cannon = null;
            if(getPlayerNumber() == 1) {
                cannon = gameObjectManager.get("CANNON1");
            } else {
                cannon = gameObjectManager.get("CANNON2");
            }

            if(cannon != null) {
                CannonShootingComponent cannonShootCPNT = (CannonShootingComponent) cannon.getComponent(GameObjectFactory.Components.ShootingComponent);
                if(maxNumberOfBalls > 0) {
                    cannonShootCPNT.shootMachineGun(rotationInterval * maxNumberOfBalls + startRotation);
                    maxNumberOfBalls--;
                } else {
                    settings.resetline();
                    cannonShootCPNT.setPercentOfPowerBallsAndMaxAmmo(0.1,30);

                    setIsFired(true);
                    gameObject.setRemoveFromGame(true);
                }
            }
        }
    }
}
