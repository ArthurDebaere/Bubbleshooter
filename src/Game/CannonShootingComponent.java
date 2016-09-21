package Game;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class CannonShootingComponent implements Component {

    private GameObjectManager gameObjectManager;
    private GameObjectFactory gameObjectFactory;
    private GameObject gameObject;
    private Transform transform;
    private final double CANNON_SHOOT_SPEED = 900;
    private int numberOfBallsShot = 0;
    private int player;
    private int currentAmmoPosition;
    private int maxAmmoInCannon = 30;
    private Settings settings = Settings.getInstance();
    private ArrayList<GameObject> cannonAmmo = new ArrayList<>();
   private double percentOfPowerBalls = 0.10;

    public void setPercentOfPowerBallsAndMaxAmmo(double percentOfPowerBalls, int ammo) {
        this.percentOfPowerBalls = percentOfPowerBalls;
        this.maxAmmoInCannon = ammo;
        fillCannonWithAmmo();
        numberOfBallsShot+=1;
    }

    public void initialize(GameObject gameObject) {
        gameObjectManager = GameObjectManager.getInstance();
        gameObjectFactory = new GameObjectFactory(false);
        transform = gameObject.getTransform();
        this.gameObject = gameObject;
        fillCannonWithAmmo();
    }
    public CannonShootingComponent(int player){
        this.player=player;
    }

   public void setPlayer(int player) {
       this.player = player;
    }

    public void update(double time) {
        if(ammoCapReached()) {
            fillCannonWithAmmo();
        }

        cannonAmmo.get(currentAmmoPosition).getTransform().setPosition(transform.getPosition_x()+30, 681);
        if(gameObjectManager.get("cannonball_" + numberOfBallsShot + player) == null) {
            gameObjectManager.addAtRuntime("cannonball_" + numberOfBallsShot + player, cannonAmmo.get(currentAmmoPosition));
        }
    }

    public GameObject getNextBallInCannon() {
        if(ammoCapReached()) {
            fillCannonWithAmmo();
            return cannonAmmo.get(currentAmmoPosition);
        }
        return cannonAmmo.get(currentAmmoPosition);
    }

    //TODO: REFACTORING DUPLICATE CODE (use shoot() inside shootMachineGun() or only use shoot)
    public void shoot() {

        if(noBallIsMoving()) {
            Jukebox.playSFX(Jukebox.SFX.cannon_shoot_v1.getValue());
            GameObject cannonBall = cannonAmmo.get(currentAmmoPosition);

            double rotation = 0;

            if(cannonBall.getTransform().getRotation() == 0) {
                rotation = transform.getRotation();
                transform.setRotation(rotation);
            } else {
                rotation = cannonBall.getTransform().getRotation();
            }

            float scale_x = (float) Math.cos(Math.toRadians(-rotation));
            float scale_y = (float) Math.sin(Math.toRadians(-rotation));
            double velocity_x = scale_x * -CANNON_SHOOT_SPEED;
            double velocity_y = scale_y * CANNON_SHOOT_SPEED;

            Component scriptComponent = cannonBall.getComponent(GameObjectFactory.Components.ScriptComponent);
            if(scriptComponent instanceof PowerBall_ScriptComponent) {
                PowerBall_ScriptComponent powerball_script = (PowerBall_ScriptComponent) scriptComponent;
                powerball_script.fireBall(player);

                if(scriptComponent instanceof BeachBall_ScriptComponent || scriptComponent instanceof StickyBomb_ScriptComponent) {
                    //Don't remove physics & collidercomponent
                } else {
                    cannonBall.removeComponent(GameObjectFactory.Components.PhysicsComponent);
                    cannonBall.removeComponent(GameObjectFactory.Components.ColliderComponent);
                }
            }

            cannonBall.getTransform().setVelocity(velocity_x, velocity_y);
            cannonBall.setIsMoving(true);
            gameObjectManager.addAtRuntime("cannonball_" + numberOfBallsShot+player, cannonBall);
            numberOfBallsShot++;
            currentAmmoPosition++;
        }
   }

    public void shootMachineGun(double direction) {
        settings.setHelpLine(false);
        percentOfPowerBalls = 0;
        maxAmmoInCannon = 10;
        Jukebox.playSFX(Jukebox.SFX.cannon_shoot_v1.getValue());
            double rotation = direction;
            float scale_x = (float) Math.cos(Math.toRadians(-direction));
            float scale_y = (float) Math.sin(Math.toRadians(-direction));
            double velocity_x = scale_x * -CANNON_SHOOT_SPEED;
            double velocity_y = scale_y * CANNON_SHOOT_SPEED;

            GameObject cannonBall = cannonAmmo.get(currentAmmoPosition);

            fillCannonWithAmmo();

            Component scriptComponent = cannonBall.getComponent(GameObjectFactory.Components.ScriptComponent);
            if(scriptComponent instanceof PowerBall_ScriptComponent) {
                PowerBall_ScriptComponent powerball_script = (PowerBall_ScriptComponent) scriptComponent;
                powerball_script.fireBall(player);

                if(scriptComponent instanceof BeachBall_ScriptComponent) {
                    //don't remove components
                } else {
                    cannonBall.removeComponent(GameObjectFactory.Components.PhysicsComponent);
                    cannonBall.removeComponent(GameObjectFactory.Components.ColliderComponent);
                }

            }

            cannonBall.getTransform().setRotation(rotation);
            transform.setRotation(rotation);
            cannonBall.getTransform().setVelocity(velocity_x, velocity_y);
            cannonBall.setIsMoving(true);
            gameObjectManager.addAtRuntime("cannonball_" + numberOfBallsShot+player, cannonBall);
            numberOfBallsShot++;
            currentAmmoPosition++;

    }

    private void fillCannonWithAmmo() {
        cannonAmmo = new ArrayList<>();
        currentAmmoPosition = 0;
        String difficulty = Settings.getInstance().getDifficulty();

        Random rdm_intgenerator = new Random();
        int numberOfPowerBalls = (int) (maxAmmoInCannon * percentOfPowerBalls);
        int numberOfPowerUpBalls = 0;
        int numberOfPowerDownBalls = 0;

        switch (difficulty) {
            case "easy":
                numberOfPowerUpBalls = (int)(numberOfPowerBalls * .80);
                numberOfPowerDownBalls = (int)(numberOfPowerBalls * .40);
                break;
            case "medium":
                numberOfPowerUpBalls = (int)(numberOfPowerBalls * .50);
                numberOfPowerDownBalls = (int)(numberOfPowerBalls * .50);
                break;
            default:
                numberOfPowerUpBalls = (int)(numberOfPowerBalls * .40);
                numberOfPowerDownBalls = (int)(numberOfPowerBalls * .80);
                break;
        }

        //TODO: REFACTORING DUPLICATE CODE
        ArrayList<Integer> positionOfPowerUpBalls = new ArrayList<>();
        for(int pos_upBall = 0; pos_upBall < numberOfPowerUpBalls; pos_upBall++) {
            boolean valueInserted = false;
            while(!valueInserted) {
                int randomPosition = rdm_intgenerator.nextInt(maxAmmoInCannon);
                if(!positionOfPowerUpBalls.contains(randomPosition)) {
                    positionOfPowerUpBalls.add(randomPosition);
                    valueInserted = true;
                }
            }
        }

        ArrayList<Integer> positionOfPowerDownBalls = new ArrayList<>();
        for(int pos_downBall = 0; pos_downBall < numberOfPowerDownBalls; pos_downBall++) {
            boolean valueInserted = false;
            while(!valueInserted) {
                int randomPosition = rdm_intgenerator.nextInt(maxAmmoInCannon);
                if(!positionOfPowerUpBalls.contains(randomPosition) && !positionOfPowerDownBalls.contains(randomPosition)) {
                    positionOfPowerDownBalls.add(randomPosition);
                    valueInserted = true;
                }
            }
        }

        Component[] PowerDownComponents = {new Timeball_ScriptComponent(), new MachinegunBall_ScriptComponent(), new DrunkBowBall_ScriptComponent(), new TrollBall_ScriptComponent(), new LineBall_ScriptComponent()};
        //ADD GUIDELINE BALL......
        Component[] PowerUpComponents = {new SlowMoBall_ScriptComponent(), new FreezeBall_ScriptComponent(), new BeachBall_ScriptComponent(), new StickyBomb_ScriptComponent()};

        for (int i = 0; i < maxAmmoInCannon; i++) {
            GameObject cannonBall = gameObjectFactory.getGameObject("CANNONBALL");
            cannonBall.setPlayerNumber(player);
            cannonAmmo.add(cannonBall);
            CannonballPhysicsComponent physics = (CannonballPhysicsComponent) cannonAmmo.get(i).getComponent(GameObjectFactory.Components.PhysicsComponent);
            //TODO: REFACTORING DUPLICATE CODE
            if(positionOfPowerDownBalls.contains(i)) {
                //ADD POWERDOWN BALL
                Random rdm_int = new Random();
                int numberOfPowerDowns = 5;
                Component powerDownComponent = PowerDownComponents[rdm_int.nextInt(numberOfPowerDowns)];
                cannonBall.addComponent(powerDownComponent, GameObjectFactory.Components.ScriptComponent);
            }

            if(positionOfPowerUpBalls.contains(i)) {
                //ADD POWERUP BALL
                Random rdm_int = new Random();
                int numberOfPowerUps = 4;
                Component powerUpComponent = PowerUpComponents[rdm_int.nextInt(numberOfPowerUps)];
                cannonBall.addComponent(powerUpComponent, GameObjectFactory.Components.ScriptComponent);
            }

            physics.setPlayer(player);
        }
    }

    private boolean ammoCapReached() {
        return currentAmmoPosition >= maxAmmoInCannon - 1;
    }

    private boolean noBallIsMoving() {
        HashMap<String, GameObject> gameObjects = gameObjectManager.getGameObjects();
        for (GameObject gameObject : gameObjects.values()){
            Component ballPhysicsComponent = gameObject.getComponent(GameObjectFactory.Components.PhysicsComponent);
            if(ballPhysicsComponent instanceof CannonballPhysicsComponent) {
            CannonballPhysicsComponent physics = (CannonballPhysicsComponent)  ballPhysicsComponent;
                if(gameObject.getIsMoving() && physics.getPlayer()==player ) {
                    return false;
                }
            }
        }
        return true;
    }
}
