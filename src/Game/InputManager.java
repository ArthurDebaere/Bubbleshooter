package Game;

import javafx.scene.input.KeyEvent;

import java.util.ArrayList;


public class InputManager {
    private static InputManager inputManager = new InputManager();
    private ArrayList<String> input = new ArrayList<String>();
    private boolean isHoldingSpace = false;

    private InputManager() {}

    public static InputManager getInstance() {
        return inputManager;
    }

    public void addKey(KeyEvent event) {
        String code = getCharacter(event);


        if(!input.contains(code)) {
            input.add(code);
        }
    }

    public void removeKey(KeyEvent event) {

        String code = getCharacter(event);

        input.remove(code);

        if(code.equals("UP") || code.equals("Z")) {
            isHoldingSpace = false;
        }
    }

    private String getCharacter(KeyEvent event){
        String characterPressed = event.getCode().toString();

        switch (characterPressed){
            case "SPACE":
                return characterPressed;

            case "ESCAPE":
                return characterPressed;

            case "LEFT":
                return characterPressed;

            case "RIGHT":
                return characterPressed;

            case "UP":
                return characterPressed;

            case "DOWN":
                return characterPressed;

            default:
                return event.getText().toUpperCase();

        }
    }

    public ArrayList<String> getInput() {
        return input;
    }

    public void setIsHoldingSpace(boolean isHoldingSpace) {
        this.isHoldingSpace = isHoldingSpace;
    }

    public boolean getIsHoldingSpace() {
        return isHoldingSpace;
    }
}
