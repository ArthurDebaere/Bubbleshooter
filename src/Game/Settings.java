package Game;


import java.util.List;


public class Settings {
    private final static Settings settings = new Settings();
    public static Settings getInstance(){return settings;}
    private String difficulty = "hard";
    private int ballsize=40;
    private int numberOfColours=8;
    private int gameHeight=800;
    private int Players;
    private int windowSize = 1000;
    private int gamewitdh;
    private int numberOfBallsBeforeAddingNewRow = 15;
    private GameDAO gameDAO;
    private boolean helpLine = false;
    private Players players;
    private boolean isSoundOn;
    private boolean isSFXOn;


    public void setGuestLogin(boolean guestLogin) {
        this.guestLogin = guestLogin;
    }

    private boolean guestLogin;


    public List<Powerball> getPowerballs() {
        return powerballs;
    }

    private List<Powerball> powerballs;
    public boolean isHelpLine() {
        return helpLine;
    }

    public void setHelpLine(boolean helpLine) {
        this.helpLine = helpLine;
    }



    private String[][] controlsPlayers=new String[2][4];

    public int getWindowSize() {
        return windowSize;
    }

    public String[][] getControls() {
        return controlsPlayers;
    }

    public int getPlayers() {
        return Players;
    }

    public int getBallsize() {
        return ballsize;
    }

    public int getNumberOfColours() {
        return numberOfColours;
    }

    public int getGameHeight() {
        return gameHeight;
    }

    public int getGamewitdh() {
        return gamewitdh;
    }

    public int getNumberOfBallsBeforeAddingNewRow() {
        return numberOfBallsBeforeAddingNewRow;
    }

    public void setGamewitdh(int gamewitdh) {
        this.gamewitdh = gamewitdh;
    }

    public void resetline(){
        if ("easy".equals(difficulty)){
            helpLine=true;
        }
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String setting){

        difficulty = setting;
        changeDiffuculty();

    }

    public void setPlayers(int numberOfPlayers){
        this.Players = numberOfPlayers;
        System.out.println("number of players " + Players);
    }

    public Settings(){
        gameDAO = GameDAO.getInstance();
        powerballs = gameDAO.getPowerballs();
        players = players.getInstance();

    }

    public void changeDiffuculty(){
        if ("easy".equals(difficulty)){
            numberOfColours=4;
            numberOfBallsBeforeAddingNewRow = 25;
            helpLine=true;
            writeDifficultyToDB("easy");
        }else
        if ("medium".equals(difficulty)){
            numberOfColours=5;
            numberOfBallsBeforeAddingNewRow = 20;
            writeDifficultyToDB("medium");
        }
        else{
            writeDifficultyToDB("hard");
            numberOfBallsBeforeAddingNewRow = 15;
            numberOfColours=8;
        };


    }

    private void writeDifficultyToDB(String difficulty){
        if(!guestLogin)
            gameDAO.writeDifficulty(players.getPlayerOneID(),difficulty);

    }


    public void loadControls(Control control) {

        controlsPlayers[0][0]=control.getLeftKey(); //left
        controlsPlayers[0][1]=control.getRightKey(); //right
        controlsPlayers[0][2]=control.getShootKey(); //shoot

        controlsPlayers[0][3]="DOWN"; //move the cannon faster 1st player

        controlsPlayers[1][0]="Q"; //left
        controlsPlayers[1][1]="D"; //right
        controlsPlayers[1][2]="Z"; //shoot

        controlsPlayers[1][3]="S"; //move the cannon faster 2th player
    }

    public void setDefaultControls() {
        controlsPlayers[0][0]="LEFT";
        controlsPlayers[0][1]="RIGHT";
        controlsPlayers[0][2]="UP";
        controlsPlayers[0][3]="DOWN";
        controlsPlayers[1][0]="Q";
        controlsPlayers[1][1]="D";
        controlsPlayers[1][2]="Z";
        controlsPlayers[1][3]="S";
    }
}

