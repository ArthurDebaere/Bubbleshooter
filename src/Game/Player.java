package Game;


public class Player {
    private int playerID;
    private String playerName;
    private String playerPassword;
    private int scoreSP;
    private int scoreMP;

    public Player(int playerID, String playerName, String playerPassword, int scoreSP, int scoreMP) {
        this.playerID = playerID;
        this.playerName = playerName;
        this.playerPassword = playerPassword;
        this.scoreSP = scoreSP;
        this.scoreMP = scoreMP;

    }

    public int getPlayerID() {
        return playerID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerPassword() {
        return playerPassword;
    }

    public int getScoreSP() {
        return scoreSP;
    }
    public int getScoreMP() {
        return scoreMP;
    }


}
