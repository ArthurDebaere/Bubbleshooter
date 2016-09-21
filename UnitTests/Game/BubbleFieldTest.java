package Game;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;


public class BubbleFieldTest {


    @Test
    public void ShouldReturn_Row0_Column0_GetNearestPositionInMatrix_WithGivenCoordinates_5_5_WithoutOffset() {
        BubbleField bubbleField = new BubbleField(0, 0, 2, 2,1);
        double x = 5;
        double y = 5;
        int expectedRowNumber = 0;
        int expectedColumnNumber = 0;

        Position positionInMatrix = bubbleField.getNearestPositionInMatrix(x, y);

        Assert.assertTrue(positionInMatrix.getX() == expectedColumnNumber);
        Assert.assertTrue(positionInMatrix.getY() == expectedRowNumber);
    }

    @Test
    public void ShouldReturn_Row2_Column3_GetNearestPositionInMatrix_WithGivenCoordinates_35_25_WithoutOffset() {
        BubbleField bubbleField = new BubbleField(0, 0, 2, 2,1);
        double x = 35+80;
        double y = 25+120;
        int expectedRowNumber = 3;
        int expectedColumnNumber = 2;

        Position positionInMatrix = bubbleField.getNearestPositionInMatrix(x, y);

        Assert.assertTrue(positionInMatrix.getX() == expectedColumnNumber);
        Assert.assertTrue(positionInMatrix.getY() == expectedRowNumber);
    }

    @Test
    public void ShouldReturn_Row0_Column0_GetNearestPositionInMatrix_WithGivenCoordinates_30_17_WithoutOffset() {
        BubbleField bubbleField = new BubbleField(0, 0, 2, 2,1);
        double x = 30;
        double y = 17;
        int expectedRowNumber = 0;
        int expectedColumnNumber = 0;

        Position positionInMatrix = bubbleField.getNearestPositionInMatrix(x, y);

        Assert.assertTrue(positionInMatrix.getX() == expectedColumnNumber);
        Assert.assertTrue(positionInMatrix.getY() == expectedRowNumber);
    }

    @Test
    public void ShouldReturn_Row4_Column5_GetNearestPositionInMatrix_WithGivenCoordinates_30_25_WithOffset_10_10() {
        BubbleField bubbleField = new BubbleField(10, 10, 2, 2,1);
        double x = 30+40*5;
        double y = 25+40*4;
        int expectedRowNumber = 4;
        int expectedColumnNumber = 5;

        Position positionInMatrix = bubbleField.getNearestPositionInMatrix(x, y);

        Assert.assertTrue(positionInMatrix.getX() == expectedColumnNumber);
        Assert.assertTrue(positionInMatrix.getY() == expectedRowNumber);
    }

    @Test
    public void numberOfRowsShouldBe17_AddRowOnTop_WhenStartingWith4Rows() {
        BubbleField bubbleField = new BubbleField(10, 10, 4, 4,1);
        int expectedNumberOfRows = 17;

        bubbleField.addRowOnTop();

        Assert.assertTrue(bubbleField.getNumberOfRows() == expectedNumberOfRows);
    }

   /* @Test
    public void CannonBallShouldBeAdded_AddCannonballToBubbleField() {
        GameObjectFactory factory = new GameObjectFactory();
        GameObject cannonBall = factory.getGameObject("CANNONBALL");
        BubbleField bubbleField = new BubbleField(30, 30, 3, 3,1);
        cannonBall.getTransform().setPosition(34.77, 143.66);
        int expectedCannonballColumn = 0;
        int expectedCannonballRow = 3;

        bubbleField.addCannonBallToMatrix(cannonBall);

        Assert.assertTrue(bubbleField.getSingleBubble(expectedCannonballColumn, expectedCannonballRow) != null);
    }*/

   /* @Test
    public void CannonBallShouldBeAdded_AddCannonballToBubbleField_WithAddingUnevenRowOnTop() {
        GameObjectFactory factory = new GameObjectFactory();
        GameObject cannonBall = factory.getGameObject("CANNONBALL");
        BubbleField bubbleField = new BubbleField(30, 30, 3, 3,1);
        cannonBall.getTransform().setPosition(40, 180);
        int expectedCannonballColumn = 0;
        int expectedCannonballRow = 4;
        bubbleField.addRowOnTop();

        bubbleField.addCannonBallToMatrix(cannonBall);

        Assert.assertTrue(bubbleField.getSingleBubble(expectedCannonballColumn, expectedCannonballRow) != null);
    }*/

    /*@Test
    public void CannonBallShouldBeAdded_AddCannonballToBubbleField_WithAddingEvenRowOnTop() {
        GameObjectFactory factory = new GameObjectFactory();
        GameObject cannonBall = factory.getGameObject("CANNONBALL");
        BubbleField bubbleField = new BubbleField(30, 30, 3, 4,1);
        cannonBall.getTransform().setPosition(20, 210);
        int expectedCannonballColumn = 0;
        int expectedCannonballRow = 5;
        bubbleField.addRowOnTop();

        bubbleField.addCannonBallToMatrix(cannonBall);

        Assert.assertTrue(bubbleField.getSingleBubble(expectedCannonballColumn, expectedCannonballRow) != null);
    }*/



}