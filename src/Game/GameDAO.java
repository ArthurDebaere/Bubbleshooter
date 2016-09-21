package Game;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GameDAO {
    Connection connection;
    private static GameDAO GameDAO = new GameDAO();

    private GameDAO() {
        try {
            Class.forName("com.mysql.jdbc.Driver");

        } catch (ClassNotFoundException e) {
            connection = null;
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bubblecannon", "userGroup3", "howest");
            System.out.println("Connection to database successfully");

        } catch (SQLException e) {
            connection = null;
            e.printStackTrace();
        }
    }

    public static GameDAO getInstance() {
        return GameDAO;
    }

    public String getPlayerNameWithID(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(("select * from players where playerID = ?"));
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            String playerName = "";

            while (resultSet.next()) {
                playerName = resultSet.getString("playerName");
            }
            return playerName;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Player getPlayer(String username) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(("select * from players where playerName = ?"));
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            int playerID = 0;
            String playerPassword = "";
            int scoreSP = 0;
            int scoreMP = 0;

            while (resultSet.next()) {
                playerID = resultSet.getInt("playerID");
                playerPassword = resultSet.getString("playerPassword");
                scoreSP = resultSet.getInt("topScore_SP");
                scoreMP = resultSet.getInt("topScore_MP");
            }
            return new Player(playerID,username,playerPassword,scoreSP,scoreMP);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PlayerRankingSP> getRankingSP() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(("SELECT playerName, topScore_SP FROM players ORDER BY topScore_SP DESC "));

            ResultSet resultSet = preparedStatement.executeQuery();

            String playerName = "";

            int scoreSP = 0;

            List<PlayerRankingSP> playerlist = new ArrayList<>();

            while (resultSet.next()) {
                playerName = resultSet.getString("playerName");
                scoreSP = resultSet.getInt("topScore_SP");

                playerlist.add(new PlayerRankingSP(playerName,scoreSP));
            }

            return playerlist;


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PlayerRankingMP> getRankingMP() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(("SELECT playerName, topScore_MP FROM players ORDER BY topScore_MP DESC "));

            ResultSet resultSet = preparedStatement.executeQuery();

            String playerName = "";

            int scoreMP = 0;

            List<PlayerRankingMP> playerlist = new ArrayList<>();

            while (resultSet.next()) {
                playerName = resultSet.getString("playerName");
                scoreMP = resultSet.getInt("topScore_MP");

                playerlist.add(new PlayerRankingMP(playerName,scoreMP));
            }

            return playerlist;


        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Control getControls(int playerID){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("select * from controls where playerID = ?");
            preparedStatement.setInt(1,playerID);
            ResultSet resultSet = preparedStatement.executeQuery();
            String shootKey = "";
            String leftKey = "";
            String rightKey = "";
            while (resultSet.next()){
                leftKey = resultSet.getString("leftKey");
                rightKey = resultSet.getString("rightKey");
                shootKey = resultSet.getString("shootKey");
            }
            return new Control(leftKey,rightKey,shootKey);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateControls(Control control, int id){
        PreparedStatement preparedStatement;
        try{
            if(hasPlayerControl(id)){
                preparedStatement = connection.prepareStatement("update controls set leftKey = ?, rightKey = ?, shootKey = ? where playerID = ?");
                preparedStatement.setString(1,control.getLeftKey());
                preparedStatement.setString(2,control.getRightKey());
                preparedStatement.setString(3,control.getShootKey());
                preparedStatement.setInt(4,id);
            }else{
                preparedStatement = connection.prepareStatement("insert into controls (playerID,leftKey,rightKey,shootKey) values (?,?,?,?)");
                preparedStatement.setInt(1,id);
                preparedStatement.setString(2,control.getLeftKey());
                preparedStatement.setString(3,control.getRightKey());
                preparedStatement.setString(4,control.getShootKey());
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasPlayerControl(int id){
        try{
            System.out.println(id);
            PreparedStatement preparedStatement = connection.prepareStatement("select * from controls where playerID = ?");
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public List<Gameball> getGameballs(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from gameballs");
            ResultSet resultSet = preparedStatement.executeQuery();

            String ballName = "";
            String color = "";

            List<Gameball> gameballs = new ArrayList<>();
            while (resultSet.next()) {

                ballName = resultSet.getString("ballName");
                color = resultSet.getString("color");

                gameballs.add(new Gameball(ballName, color));
            }
            return gameballs;


        }catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public List<Powerball> getPowerballs(){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("select * from powerballs");

            ResultSet resultSet = preparedStatement.executeQuery();
            int powerID = 0;
            String powerName = "";
            int nrOfBalls = 0;
            int radius = 0;
            int nrOfLines = 0;
            int timeElement = 0;

            List<Powerball> powerballs = new ArrayList<>();

            while (resultSet.next()){
                powerID = resultSet.getInt("powerID");
                powerName = resultSet.getString("powerName");
                nrOfBalls = resultSet.getInt("nrOfBalls");
                radius = resultSet.getInt("radius");
                nrOfLines = resultSet.getInt("nrOfLines");
                timeElement = resultSet.getInt("timeElement");

                powerballs.add(new Powerball(powerID,powerName,nrOfBalls,radius,nrOfLines,timeElement));
            }

            return powerballs;
        } catch (SQLException e){
            return null;
        }
    }

    public void updateScore(int playerID, int playerScore) {

        try{
            PreparedStatement preparedStatement = connection.prepareStatement("update players set topScore_SP = ? where playerID = ? and topScore_SP < ? ");
            preparedStatement.setInt(1,playerScore);
            preparedStatement.setInt(2,playerID);
            preparedStatement.setInt(3,playerScore);
            preparedStatement.executeUpdate();
            System.out.println("score bijgewerkt");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void writeDefaultControls(int playerOneID) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("insert into controls (playerID,leftKey,rightKey,shootKey) values(?,?,?,?)");
            preparedStatement.setInt(1,playerOneID);
            preparedStatement.setString(2,"LEFT");
            preparedStatement.setString(3,"RIGHT");
            preparedStatement.setString(4,"UP");

            preparedStatement.executeUpdate();
            System.out.println("player " + playerOneID + " heeft nu default controls");

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void writeDifficulty(int playerID, String difficulty) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("insert into game (playerID,difficulty) values (?,?)");
            preparedStatement.setInt(1,playerID);
            preparedStatement.setString(2,difficulty);

            preparedStatement.executeUpdate();
            System.out.println("Difficulty" + playerID + " updated");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean hasPlayerDifficulty(int id){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("select * from game where playerID = ?");
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;

    }




}




