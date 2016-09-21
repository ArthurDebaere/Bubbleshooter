package Game;


public class PlayerRankingMP {
    String playerName;
    int scoreMP;

    public PlayerRankingMP(String playerName, int scoreMP) {
        this.playerName = playerName;
        this.scoreMP = scoreMP;
    }

    public String getPlayerName() {
        return playerName;
    }


    public int getScoreMP() {
        return scoreMP;
    }

    public void setScoreMP(int scoreMP) {
        this.scoreMP = scoreMP;
    }
}
