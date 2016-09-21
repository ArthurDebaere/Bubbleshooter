package Game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import java.util.HashMap;



//TODO: REFACTORING --> Dynamic adding/removing components (maybe)

public class GameObject {
    private String name;
    private Transform transform;
    private ImageView imageView;
    private HashMap<Enum, Component> components;
    private boolean isMoving;
    private String imageViewName;
    private boolean processedByBubblefield = false;
    private boolean removeFromGame = false;
    private int player;

    public GameObject(Transform transform, HashMap<Enum, Component> components, ImageView imageView, String name)
    {
        this.name = name;
        this.transform = transform;
        this.components = components;
        this.imageView = imageView;
        String imageViewPath = imageView.getImage().impl_getUrl();
        String[] imageViewPathParts = imageViewPath.split("/");
        imageViewName = imageViewPathParts[imageViewPathParts.length - 1];
    }

    public void initialize() {
        for(GameObjectFactory.Components component : GameObjectFactory.Components.values()) {
            if(components.containsKey(component)) {
                components.get(component).initialize(this);
            }
        }
    }

    public void initializeComponent(Component component) {
        component.initialize(this);
    }

    public void update(double time)
    {
        for(GameObjectFactory.Components component : GameObjectFactory.Components.values()) {
            if(components.containsKey(component)) {
                components.get(component).update(time);
            }
        }


        //TODO: Collision detection needs to run twice in order to sync better with gameobject (not good solution)
        if(components.containsKey(GameObjectFactory.Components.ColliderComponent)) {
            if(components.get(GameObjectFactory.Components.ColliderComponent) instanceof RectangleColliderComponent) {
                RectangleColliderComponent rc = (RectangleColliderComponent)components.get(GameObjectFactory.Components.ColliderComponent);
                rc.update(time);
            } else if (components.get(GameObjectFactory.Components.ColliderComponent) instanceof  CircleColliderComponent) {
                CircleColliderComponent rc = (CircleColliderComponent) components.get(GameObjectFactory.Components.ColliderComponent);
                rc.update(time);
            }

        }
    }

    public Transform getTransform() {
        return transform;
    }

    public Component getComponent(GameObjectFactory.Components componentType) {
        if(components.containsKey(componentType)) {
           return components.get(componentType);
        } else {
           return new NullComponent();
        }
    }

    public void removeComponent(GameObjectFactory.Components componentType) {
        if(components.containsKey(componentType)) {
            components.remove(componentType);
        }
    }

    public void addComponent(Component component, GameObjectFactory.Components componentType) {
        if(!components.containsKey(componentType)) {
            components.put(componentType, component);
        }
    }

    public ImageView getImageView() {
        return imageView;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }

    public boolean getIsMoving() {
        return isMoving;
    }

    public String getImageViewName() {
        return imageViewName;
    }

    public void setImageViewName(String imageViewName) {
        this.imageViewName = imageViewName;
    }

    public boolean isProcessedByBubblefield() {
        return processedByBubblefield;
    }

    public void setProcessedByBubblefield(boolean status) {
        processedByBubblefield = status;
    }

    public boolean getRemoveFromGame() {
        return removeFromGame;
    }

    public void setRemoveFromGame(boolean status) {
        removeFromGame = status;
    }

    public int getPlayerNumber() {
        return player;
    }

    public void setPlayerNumber(int playerNumber) {
        this.player = playerNumber;
    }
}
