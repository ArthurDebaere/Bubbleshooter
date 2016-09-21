package Game;

import javafx.scene.image.Image;

import java.util.Random;


public class DrunkBowBall_ScriptComponent extends PowerBall_ScriptComponent {
    private GameObject gameObject;
    private GameObjectManager gameObjectManager;

    public void initialize(GameObject gameObject) {
        super.initialize(gameObject);
        String powerName = getSettings().getPowerballs().get(8).getPowerName();
        gameObject.getImageView().setImage(new Image(GameObjectFactory.getResource("res/PowerBalls/"+powerName+".png")));
        this.gameObject = gameObject;
        gameObject.removeComponent(GameObjectFactory.Components.PhysicsComponent);
        gameObject.removeComponent(GameObjectFactory.Components.ColliderComponent);
    }

    public void update(double time) {
        if(getIsFired()) {
            gameObjectManager = GameObjectManager.getInstance();
            //FUNCTIONALITY
            int playerNumber = gameObject.getPlayerNumber();

            GameObject cannon = null;

            if(playerNumber == 1) {
                cannon = gameObjectManager.get("CANNON1");
            } else {
                cannon = gameObjectManager.get("CANNON2");
            }

            if(cannon != null) {
                CannonShootingComponent cannonShootCPNT = (CannonShootingComponent) cannon.getComponent(GameObjectFactory.Components.ShootingComponent);
                GameObject nextCannonBall = cannonShootCPNT.getNextBallInCannon();

                Random rand = new Random();
                int randomDegreesOfCannon = rand.nextInt(140) + 20;

                nextCannonBall.getTransform().setRotation(randomDegreesOfCannon);

            }
            setIsFired(false);
            gameObject.setRemoveFromGame(true);
            }
        }
    }

