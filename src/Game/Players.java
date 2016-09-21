package Game;

import Game.GUI.Controller;


public class Players {
    private GameDAO gameDAO = GameDAO.getInstance();
    private final static Players players = new Players();
    public static Players getInstance(){return players;}

    public int getPlayerOneID() {
        return playerOneID;
    }

    public void setPlayerOneID(int playerOneID) {
        this.playerOneID = playerOneID;
    }

    public int getPlayerTwoID() {
        return playerTwoID;
    }

    public void setPlayerTwoID(int playerTwoID) {
        this.playerTwoID = playerTwoID;
    }

    private int playerOneID;
    private int playerTwoID;

    private int[] playersScore= {0,0};
    private int[] playerExp = {0,0};

    public void setPlayersScore(int player, int playersScore) {
        if (playersScore>0){
        this.playersScore[player-1] += playersScore;
        }
    }

    public int[] getPlayersScore() {
        return playersScore;
    }

    public void setPlayerExp(int player, int playersXp) {
        this.playerExp[player-1] += playersXp;
        System.out.println(playersScore[0]);

    }


    public void resetGameScore(){
        for (int i =0; i<playersScore.length;i++){
            playersScore[i]=0;
        }
    }

}
