package Game.GUI;


import Game.*;

import Game.Control;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Observable;

public class Controller extends Game {

    public TextField txtUsername;
    public PasswordField txtPassword;
    public Label lblAimRight;
    public Label lblAimLeft;
    public Label lblShoot;
    public ListView listRanking;
    private static int previousSeconds = 0;
    Settings settings = Settings.getInstance();

    Boolean isSettingsClicked = false;
    Boolean isSoundToggled = false;
    Boolean isMusicToggled = false;
    Boolean canKeymapBeChanged = true;
    GameDAO gameDAO = GameDAO.getInstance();
    Players players = Players.getInstance();



    @FXML
    private void startSPgameEASY (ActionEvent event) throws IOException{
        settings.setDifficulty("easy");
        startNewGame(event);
    }
    @FXML
    private void startSPgameMEDIUM (ActionEvent event) throws IOException{
        settings.setDifficulty("medium");
       startNewGame(event);

    }
    @FXML
    private void startSPgameHARD (ActionEvent event) throws IOException{
        settings.setDifficulty("hard");
        startNewGame(event);
    }

    private void startNewGame(ActionEvent event) throws IOException{
        settings.setPlayers(1);
        settings.setGamewitdh(settings.getWindowSize());
        Stage app_stage = getStage(event);

        //Start new singleplayer game
        startGame(app_stage);
    }

    @FXML
    private void btnSP_click(ActionEvent event) throws IOException{
        Stage app_stage = getStage(event);
        handleSceneChange("Difficulty.fxml", app_stage, 430, 600);
    }

    @FXML
    private void btnMP_Click(ActionEvent event) throws IOException{
        settings.setPlayers(2);
        settings.setGamewitdh(settings.getWindowSize()/2);
        Stage app_stage = getStage(event);

        //Start new multiplayer game
            startGame(app_stage);
    }

    @FXML
    private void btnSettings_Click (ActionEvent event) throws IOException{

        //specify element
        Node element = getNode(event);
        //get setting controls
        Label settingsNormal = (Label)element.getScene().lookup("#lblSettingsNormal");
        Label settingsExtended = (Label)element.getScene().lookup("#lblSettingsExtended");
        Button sound = (Button)element.getScene().lookup("#btnSound");
        Button music = (Button)element.getScene().lookup("#btnMusic");
        Button controls = (Button)element.getScene().lookup("#btnControls");

        if(isSettingsClicked){
            //make setting controls invisible
            //sound
            sound.getStyleClass().add("hidden");
            //music
            music.getStyleClass().add("hidden");
            //controls
            controls.getStyleClass().add("hidden");

            //change settings image and boolean value
            settingsExtended.getStyleClass().add("hidden");
            settingsNormal.getStyleClass().remove("hidden");
            isSettingsClicked = false;
        }else{
            //make setting controls visible
            //sound
            sound.getStyleClass().remove("hidden");
            //music
            music.getStyleClass().remove("hidden");
            //controls
            controls.getStyleClass().remove("hidden");

            //change settings image and boolean value
            settingsExtended.getStyleClass().removeAll("hidden");
            settingsExtended.getStyleClass().removeAll("settingsExtended");
            settingsExtended.getStyleClass().removeAll("btnToggleAll");
            settingsExtended.getStyleClass().removeAll("btnSoundClick");
            settingsExtended.getStyleClass().removeAll("btnMusicClick");
            settingsNormal.getStyleClass().add("hidden");
            handleToggleSettings(event);
            isSettingsClicked = true;
        }
    }



    @FXML
    private void handleToggleSettings (ActionEvent event) throws IOException{
        //specify element
        Node element = getNode(event);
        String elementId = element.getId();
        Label settingsExtended = (Label)element.getScene().lookup("#lblSettingsExtended");

        //Change image and boolean values to respective toggle events

        switch(elementId){
            case "btnMusic":
                if(isMusicToggled){
                    //isMusicToggled = false;
                    //super.playSound();
                } else{
                    //isMusicToggled = true;
                    //super.stopSound();
                }
                break;
            case "btnSound":
                if(isSoundToggled){
                    //isSoundToggled = false;
                } else{
                    //isSoundToggled = true;
                }
                break;
        }

        if(isMusicToggled & !isSoundToggled){
            settingsExtended.getStyleClass().removeAll("settingsExtended");
            settingsExtended.getStyleClass().removeAll("btnToggleAll");
            settingsExtended.getStyleClass().removeAll("btnSoundClick");
            settingsExtended.getStyleClass().add("btnMusicClick");
        } else if(!isMusicToggled & isSoundToggled){
            settingsExtended.getStyleClass().removeAll("btnMusicClick");
            settingsExtended.getStyleClass().removeAll("settingsExtended");
            settingsExtended.getStyleClass().removeAll("btnToggleAll");
            settingsExtended.getStyleClass().add("btnSoundClick");
        }else if (isSoundToggled & isMusicToggled){
            settingsExtended.getStyleClass().removeAll("btnMusicClick");
            settingsExtended.getStyleClass().removeAll("btnSoundClick");
            settingsExtended.getStyleClass().removeAll("settingsExtended");
            settingsExtended.getStyleClass().add("btnToggleAll");
        } else{
            settingsExtended.getStyleClass().add("settingsExtended");
            settingsExtended.getStyleClass().removeAll("btnMusicClick");
            settingsExtended.getStyleClass().removeAll("btnSoundClick");
            settingsExtended.getStyleClass().removeAll("btnToggleAll");
        }
    }

    @FXML
    private void changeKeymaps (MouseEvent event) throws IOException{
        //specify element
        Node element = getNode(event);

            element.requestFocus();
            element.addEventHandler(KeyEvent.KEY_PRESSED, keyPressHandler);
            canKeymapBeChanged = false;

    }


    EventHandler keyPressHandler = new EventHandler() {
        @Override
        public void handle(Event event) {
            KeyEvent newEvent = (KeyEvent) event;
            Node element = getNode(newEvent);
            Label elementLabel = (Label) element;

            String characterPressed = newEvent.getCode().toString();
            String characterToBeSaved;

            switch (characterPressed){
                case "SPACE":
                    characterToBeSaved = characterPressed;
                    break;
                case "ESCAPE":
                    characterToBeSaved = "";
                    break;
                case "LEFT":
                    characterToBeSaved = characterPressed;
                    break;
                case "RIGHT":
                    characterToBeSaved = characterPressed;
                    break;
                case "UP":
                    characterToBeSaved = characterPressed;
                    break;
                case "DOWN":
                    characterToBeSaved = characterPressed;
                    break;
                default:
                    characterToBeSaved = newEvent.getText().toUpperCase();
                    break;
            }
            element.removeEventHandler(KeyEvent.KEY_PRESSED, keyPressHandler);
            canKeymapBeChanged = true;
            elementLabel.setText(characterToBeSaved);
        }
    };

    @FXML
    private void gotoKeymaps (ActionEvent event) throws IOException{
        Stage app_stage = getStage(event);
        handleSceneChange("Settings.fxml", app_stage, 430, 600);

    }

    @FXML
    private void backToMain (ActionEvent event) throws IOException{
        Stage app_stage = getStage(event);
        handleSceneChange("MainMenu.fxml", app_stage, 430, 600);
    }

    @FXML
    private void backToMainfromDialog (ActionEvent event) throws IOException {
        System.out.println("back to main");
        Stage app_stage = getStage(event);
        Stage mainParent = ((Stage) app_stage.getOwner());
        handleSceneChange("MainMenu.fxml", mainParent, 430, 600);
        app_stage.close();
    }

    @FXML
    private void btnReset_click (ActionEvent event) throws IOException{
            Stage app_stage = getStage(event);
            Stage mainParent = ((Stage) app_stage.getOwner());
            app_stage.close();
            resetTimer();
            restartGame(mainParent);
    }

    @FXML
    public void btnLogin_Click(ActionEvent event) throws IOException{
        players.setPlayerOneID(gameDAO.getPlayer(txtUsername.getText()).getPlayerID());


        if(!gameDAO.hasPlayerControl(players.getPlayerOneID())){
            gameDAO.writeDefaultControls(players.getPlayerOneID());
        } // nieuwe players hebben default controls: left,right,up


        if(!gameDAO.hasPlayerControl(players.getPlayerOneID())){
            gameDAO.writeDefaultControls(players.getPlayerOneID());
        } // nieuwe players hebben default controls: left,right,up


        if(checkLogin()){
            backToMain(event);
            settings.loadControls(gameDAO.getControls(players.getPlayerOneID()));
        }
    }

    @FXML
    public void btnLoginGuest_Click(ActionEvent event) throws IOException {
        settings.setDefaultControls();
        Stage app_stage = getStage(event);
        handleSceneChange("MainMenu.fxml", app_stage, 430, 600);
        settings.setGuestLogin(true);
    }

    @FXML
    public void bttnLogout(MouseEvent event) throws IOException {
        Stage app_stage = getStage(event);
        handleSceneChange("LoginScreen.fxml", app_stage, 430, 600);
    }

    @FXML
    public void saveControls(ActionEvent event) throws IOException {
        String left = lblAimLeft.getText();
        String right = lblAimRight.getText();
        String shoot = lblShoot.getText();
        Control control = new Control(left,right,shoot);
        gameDAO.updateControls(control,players.getPlayerOneID());
        settings.loadControls(gameDAO.getControls(players.getPlayerOneID()));
        Stage app_stage = getStage(event);
        handleSceneChange("MainMenu.fxml", app_stage, 430, 600);
    }


    public void changeScoreLabelsinField (int playerscore[], Stage app_stage){
        Label lblPlayerScore = (Label)app_stage.getScene().lookup("#lblScorePlayer1");
        Label lblPlayer2Score = (Label)app_stage.getScene().lookup("#lblScorePlayer2");
        if (lblPlayer2Score != null){
            lblPlayer2Score.setText(playerscore[1] + " pts");
        }

        if (lblPlayerScore != null){
            lblPlayerScore.setText(playerscore[0] + " pts");
        }
    }

    public void changeTimeLabel(int secondsRun, int startSeconds, Stage app_stage) {
        Label lblTime = (Label)app_stage.getScene().lookup("#lblTimeLeft");

        if(lblTime != null) {
            int milliSecondsLeft = (startSeconds - secondsRun) * 1000;
            int seconds = (int) (milliSecondsLeft / 1000) % 60 ;
            int minutes = (int) ((milliSecondsLeft / (1000*60)) % 60);
            if(minutes == 0 && seconds <= 5 && previousSeconds != seconds) {
                Jukebox.playSFX(Jukebox.SFX.timeAlmostOverBlip_v1.getValue());
            }
            lblTime.setText(String.format("%02d:%02d", minutes, seconds));
            previousSeconds = seconds;
        }
    }

    public void changeScoreLabels (int playerscore, Stage app_stage){
        Label lblPlayerScore = (Label)app_stage.getScene().lookup("#lblScore");
        Label lblHighscore = (Label)app_stage.getScene().lookup("#lblHighscore");
        String playerName = gameDAO.getPlayerNameWithID(players.getPlayerOneID());

        int highscore = gameDAO.getPlayer(playerName).getScoreSP();
        if(playerscore>highscore){
            highscore = playerscore;
        }

        lblPlayerScore.setText(playerscore + " pts");
        lblHighscore.setText(highscore+ " pts");

        Stage main_stage = (Stage)app_stage.getOwner();
        Stage mainParent = ((Stage) app_stage.getOwner());
    }

    private Stage getStage (Event event){
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    private Node getNode (Event event){
        return (Node) event.getSource();
    }

    private boolean checkLogin() {

        boolean correct = false;

        String username = txtUsername.getText();
        String password = txtPassword.getText();
        Player player = gameDAO.getPlayer(username);

        if(username.equals(""))
            correct = false;
        else if(password.equals(""))
            correct = false;
        else
            correct = (player.getPlayerPassword().equals(password));

        return correct;
    }

    @FXML
    public void btnRanking_Click(ActionEvent actionEvent) throws IOException {
        Stage app_stage = getStage(actionEvent);

        handleSceneChange("Rankings.fxml", app_stage, 430, 600);

        String dataElementSP = "";
        String dataElementMP = "";

        List<PlayerRankingSP> playerListSP = gameDAO.getRankingSP();
        List<PlayerRankingMP> playerListMP = gameDAO.getRankingMP();
        System.out.println(playerListMP.get(0).getScoreMP());

        ListView rankingSP = (ListView)app_stage.getScene().lookup("#listRankingSingle");
        ObservableList<String> dataSP = FXCollections.observableArrayList();

        ListView rankingMP = (ListView)app_stage.getScene().lookup("#listRankingMulti");
        ObservableList<String> dataMP = FXCollections.observableArrayList();

        for(PlayerRankingSP playerSP: playerListSP){
            dataElementSP = playerSP.getPlayerName() + ": \t" + playerSP.getTopScore()+ " points";
            dataSP.add(dataElementSP);
        }

        rankingSP.setItems(dataSP);

        for(PlayerRankingMP playerMP:playerListMP){
            dataElementMP = playerMP.getPlayerName() + ": \t" + playerMP.getScoreMP()+ " points";
            dataMP.add(dataElementMP);
        }
        rankingMP.setItems(dataMP);

    }
}
