package Game;


import org.junit.Assert;
import org.junit.Test;


public class GameDAOTest {

    @Test
    public void testPowerBallListNotNull(){
        GameDAO database = GameDAO.getInstance();
        Assert.assertNotNull( database.getPowerballs());
    }
    @Test
    public void testGameBallListNotNull(){
        GameDAO database = GameDAO.getInstance();
        Assert.assertNotNull( database.getGameballs());
    }

    @Test
    public void testRankingsSingelPLayerl(){
        GameDAO database = GameDAO.getInstance();
        Assert.assertNotNull( database.getRankingSP());
    }

    @Test
    public void testRankingsMultiPLayerl(){
        GameDAO database = GameDAO.getInstance();
        Assert.assertNotNull( database.getRankingMP());
    }

    @Test
    public void testGetPlayer(){
        GameDAO database = GameDAO.getInstance();
        Assert.assertNotNull( database.getPlayer("geust"));
    }

    @Test
    public void testGetPlayerControls(){
        GameDAO database = GameDAO.getInstance();
        int playerID = database.getPlayer("geust").getPlayerID();
        Assert.assertNotNull(database.getControls(playerID) );
    }


}
