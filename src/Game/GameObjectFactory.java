package Game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.HashMap;
import java.util.List;

public class GameObjectFactory {
    public GameObjectFactory(boolean b) {

    }

    public enum Components {
        InputComponent,
        ShootingComponent,
        ColliderComponent,
        PhysicsComponent,
        ScriptComponent,
        RenderComponent
    }
    private GameDAO gameDAO = GameDAO.getInstance();

    private int player;
    private int previousRandom=0;
    private int difficultySetting;

    private List<Gameball> gameballList = gameDAO.getGameballs();



    private Settings settings = Settings.getInstance();
    public GameObjectFactory(){this(1);

        }

    public GameObjectFactory(int player){
    if("easy".equals(settings.getDifficulty())){
        difficultySetting=3;
    }else if ("medium".equals(settings.getDifficulty())){
        difficultySetting=2;
    }else {difficultySetting=0;}

    this.player=player-1;


}


    public GameObject getGameObject(String name) {
        //TODO: Work with table instead of if else...
        //TODO: GETFITWIDTH / GETFITHEIGHT DOES NOT WORK HERE

        HashMap<Enum, Component> components = new HashMap<>();



        if(name.equals("CANNONBALL")) {
            components.put(Components.RenderComponent, new SpriteRenderComponent());
            components.put(Components.PhysicsComponent, new CannonballPhysicsComponent());
            components.put(Components.ColliderComponent, new CircleColliderComponent());



            String filename = colorPicker(random(0,settings.getNumberOfColours()));

            Image img_cannonball = new Image(getResource(filename));
            ImageView ball = new ImageView(img_cannonball);

            double width = settings.getBallsize();
            double height = settings.getBallsize();

            Transform transform = new Transform(0, 0, 0, width, height);
            return new GameObject(transform, components, ball, "cannonball");
        }

        if(name.equals("BUBBLEFIELDBALL")) {
            components.put(Components.RenderComponent, new SpriteRenderComponent());
            components.put(Components.PhysicsComponent, new SpritePhysicsComponent());
            components.put(Components.ColliderComponent, new CircleColliderComponent());

            String filename = colorPicker(random(0,settings.getNumberOfColours()));
            Image img_cannon = new Image(getResource(filename));
            ImageView ball = new ImageView(img_cannon);

            double width = 40;
            double height = 40;
            Transform transform = new Transform(0, 0, 0, width, height);
            return new GameObject(transform, components, ball, "bubblefieldball");
        }

        if(name.equals("CANNON")) {
            components.put(Components.InputComponent, new CannonInputComponent());

            components.put(Components.RenderComponent, new CannonRenderComponent());
            components.put(Components.ShootingComponent, new CannonShootingComponent(player+1));
            CannonInputComponent cannonInput = (CannonInputComponent)components.get(Components.InputComponent);
            cannonInput.setPlayer(player);


            Image img_cannon = new Image(getResource("res/cannonV3.png"));
            ImageView cannon = new ImageView(img_cannon);


            double width = img_cannon.getWidth();
            double height = img_cannon.getHeight();
            double rotation = 0;
            double pos_x = settings.getGamewitdh()/2 - width/2+(player*(settings.getGamewitdh()));
            double pos_y = 675;
            Transform transform = new Transform(pos_x, pos_y, rotation, width, height);
            return new GameObject(transform, components, cannon, "cannon");
        }

        if(name.equals("CANNON_BASE")) {
            components.put(Components.RenderComponent, new SpriteRenderComponent());

            Image img_cannon_base = new Image(getResource("res/cannon_base.png"));
            ImageView cannon_base = new ImageView(img_cannon_base);

            double width = img_cannon_base.getWidth();
            double height = img_cannon_base.getHeight();
            double rotation = 0;
            double pos_x = settings.getGamewitdh()/2 - width/2+(player*(settings.getGamewitdh()));
            double pos_y = 700;
            Transform transform = new Transform(pos_x, pos_y, rotation, width, height);
            return new GameObject(transform, components, cannon_base, "cannon_base");
        }

        if(name.equals("LEFT_SIDEWALL")) {
            components.put(Components.RenderComponent, new SpriteRenderComponent());
            components.put(Components.ColliderComponent, new RectangleColliderComponent());

            Image img_sidewall = new Image(getResource("res/sidewallLeft.png"));
            ImageView sidewall = new ImageView(img_sidewall);

            double width = img_sidewall.getWidth();
            double height = img_sidewall.getHeight();
            double rotation = 0;
            double pos_x = 0+(player*(settings.getGamewitdh()));
            double pos_y = 0;
            Transform transform = new Transform(pos_x, pos_y, rotation, width, height);
            return new GameObject(transform, components, sidewall, "left_sidewall");
        }

        if(name.equals("RIGHT_SIDEWALL")) {
            components.put(Components.RenderComponent, new SpriteRenderComponent());
            components.put(Components.ColliderComponent, new RectangleColliderComponent());

            Image img_sidewall = new Image(getResource("res/sidewallRight.png"));
            ImageView sidewall = new ImageView(img_sidewall);

            double width = img_sidewall.getWidth();
            double height = img_sidewall.getHeight();
            double rotation = 0;
            double pos_x = settings.getGamewitdh()-30+(player*(settings.getGamewitdh()));
            double pos_y = 0;
            Transform transform = new Transform(pos_x, pos_y, rotation, width, height);
            return new GameObject(transform, components, sidewall, "right_sidewall");
        }

        if(name.equals("TOP_WALL")) {
            components.put(Components.RenderComponent, new SpriteRenderComponent());
            components.put(Components.ColliderComponent, new RectangleColliderComponent());

            Image img_top_wall = new Image(getResource("res/sidewallTop.png"));
            ImageView wall_top = new ImageView(img_top_wall);
            wall_top.setScaleX((double)1/settings.getPlayers());

            double width = img_top_wall.getWidth();
            double height = img_top_wall.getHeight();
            double rotation = 0;
            double pos_x = 30+(player*(settings.getGamewitdh()));
            double pos_y = 0;
            int locationbug =0;
            if (settings.getPlayers()==2){locationbug=240;}
            Transform transform = new Transform(pos_x-locationbug, pos_y, rotation, width, height);
            return new GameObject(transform, components, wall_top, "top_wall");
        }
        return null;
    }

    public int random(int min,int max){
          if ((int) (Math.random() * 4 + 0)<difficultySetting){
              return previousRandom;
          }

        previousRandom = randomGen(min,max);
        return previousRandom;
    }
    private int randomGen(int min,int max){
        return(int) (Math.random() * max + min);
    }

    public String colorPicker(int number){
        return "res/balls/" + gameballList.get(number).getColor() + "_ball.png";
    }

    static String getResource(String path) {
        return GameObjectFactory.class.getResource(path).toExternalForm();
    }
}