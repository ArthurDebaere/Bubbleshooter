package Game;


public class PlayerRankingSP {
    public String getPlayerName() {
        return playerName;
    }


    public int getTopScore() {
        return topScore;
    }

    public void setTopScore(int topScore) {
        this.topScore = topScore;
    }

    private String playerName;
    private int topScore;


    public PlayerRankingSP(String playerName, int topScore) {
        this.playerName = playerName;
        this.topScore = topScore;
    }
}
