import java.sql.*;
import java.util.ArrayList;



public class DATA_Layer implements DATA_Layer_Interface
{
    private Connection connection;
    private Statement statement;


    // Default Constructor
    public DATA_Layer()
    {

    }

    // Adds the new tables need to store the results from calculations
    public boolean addNewDatabaseTables()
    {
        Connection connection;

        try
        {
            connection = connectToDatabase();

            // Create a new SQL statement
            statement = connection.createStatement();

            // Creates the table `SectorData`
            statement.executeUpdate("DROP TABLE IF EXISTS `SectorData`;");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `SectorData` " +
                    "( `SectorDataID`	INTEGER AUTO_INCREMENT, " +
                    " `PatternRef`	INTEGER, " +
                    " `SectorNumber`	INTEGER, " +
                    " `Distance`	REAL, " +
                    " `IndexOfDifficulty`	REAL, " +
                    " `Mean`	REAL, " +
                    " `StandardDeviation`	REAL, " +
                    " `TotalIP`	REAL, " +
                    " `Direction`	VARCHAR(6), " +
                    " PRIMARY KEY (`SectorDataID`), " +
                    " FOREIGN KEY(`PatternRef`) REFERENCES `Pattern`(`PatternID`) " +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");


            // Creates the table `userPerformanceData`
            statement.executeUpdate("DROP TABLE IF EXISTS `userPerformanceData`;");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `userPerformanceData` " +
                    "( `userPerformanceDataID`	INTEGER AUTO_INCREMENT, " +
                    " `CollectionRef`	INTEGER, " +
                    " `PatternRef`	INTEGER, " +
                    " `SectorNumber`	INTEGER, " +
                    //" `DistanceOfDrawnPath`	REAL, " +
                    " `IndexOfPerformance`	REAL, " +
                    " `DistanceFromMeanInStandardDeviation`	REAL, " +
                    " PRIMARY KEY (`userPerformanceDataID`), " +
                    " FOREIGN KEY(`PatternRef`) REFERENCES `Pattern`(`PatternID`), " +
                    " FOREIGN KEY(`CollectionRef`) REFERENCES `Collection`(`CollectionID`) " +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");

            statement.close();
            connection.close();

            return true;
        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (NullPointerException ex)
        {
            nullEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return false;
    }

    // Get the sector times for each collection and adds them to a list
    public ArrayList getSectorTimes(int collection)
    {
        ArrayList<Double> timings = new ArrayList<>();

        try
        {
            ResultSet result = connectToDatabase("SELECT * FROM Timings WHERE CollectionRef = '" + collection + "'");

            result.beforeFirst();

            while (result.next())
            {
                timings.add(result.getDouble("PatternRef"));
                timings.add(result.getDouble("SectorNumber"));
                timings.add(result.getDouble("SectorDuration"));
            }

            statement.close();
            connection.close();
        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (NullPointerException ex)
        {
            nullEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return timings;
    }

    public double getStandardDeviation(int pattern, int sector)
    {
        double standardDeviation = 0.0;

        try
        {
            ResultSet result = connectToDatabase("SELECT * FROM SectorData WHERE PatternRef = '" + pattern + "' AND SectorNumber = '" + sector + "'");

            result.beforeFirst();

            while (result.next())
            {
                standardDeviation = result.getDouble("StandardDeviation");
            }

            statement.close();
            connection.close();
        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (NullPointerException ex)
        {
            nullEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return standardDeviation;
    }

    public int getCollectionsUserNo(int collection)
    {
        int userNum = 0;

        try
        {
            ResultSet result = connectToDatabase("SELECT * FROM Users WHERE ID = '" + collection + "'");

            result.beforeFirst();

            while (result.next())
            {
                userNum = result.getInt("UserID");
            }

            statement.close();
            connection.close();
        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (NullPointerException ex)
        {
            nullEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return userNum;
    }

    public double getDistanceFromMeanInStandardDeviation(int collection, int pattern, int sector)
    {
        double standardDeviation = 0.0;

        try
        {
            ResultSet result = connectToDatabase("SELECT * FROM UserPerformanceData WHERE CollectionRef = '" + collection + "' AND PatternRef = '" + pattern + "' AND SectorNumber = '" + sector + "'");

            result.beforeFirst();

            while (result.next())
            {
                standardDeviation = result.getDouble("DistanceFromMeanInStandardDeviation");
            }

            statement.close();
            connection.close();
        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (NullPointerException ex)
        {
            nullEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return standardDeviation;
    }


    public double getSectorMean(int pattern, int sector)
    {
        double mean = 0.0;

        try
        {
            ResultSet result = connectToDatabase("SELECT * FROM SectorData WHERE PatternRef = '" + pattern + "' AND SectorNumber = '" + sector + "'");

            result.beforeFirst();

            while (result.next())
            {
                mean = result.getDouble("Mean");
            }

            statement.close();
            connection.close();
        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (NullPointerException ex)
        {
            nullEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return mean;
    }

    public double getTotalIP(int pattern, int sector)
    {
        double score = 0.0;

        try
        {
            ResultSet result = connectToDatabase("SELECT * FROM SectorData WHERE PatternRef = '" + pattern + "' AND SectorNumber = '" + sector + "'");

            result.beforeFirst();

            while (result.next())
            {
                score = result.getDouble("TotalIP");
            }

            statement.close();
            connection.close();
        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (NullPointerException ex)
        {
            nullEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return score;

    }

    public String getSectorDirection(int pattern, int sector)
    {
        String direction = "";

        try
        {
            ResultSet result = connectToDatabase("SELECT * FROM SectorData WHERE PatternRef = '" + pattern + "' AND SectorNumber = '" + sector + "'");

            result.beforeFirst();

            while (result.next())
            {
                direction = result.getString("Direction");
            }

            statement.close();
            connection.close();
        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (NullPointerException ex)
        {
            nullEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return direction;
    }



    // Get the sector index of performance for each pattern/sector and returns the value as a double
    public double getSectorIndexOfPerformance(int collection, int pattern, int sector, String column)
    {
        double sectorIndexOfPerformance = 0;


        try
        {
            ResultSet result;

            if (collection > 0)
            {
                result = connectToDatabase("SELECT * FROM userPerformanceData WHERE CollectionRef = '" + collection + "' AND PatternRef = '" + pattern + "' AND SectorNumber = '" + sector + "'");
            }
            else
            {
                result = connectToDatabase("SELECT * FROM SectorData WHERE PatternRef = '" + pattern + "' AND SectorNumber = '" + sector + "'");

            }

            result.beforeFirst();

            while (result.next())
            {
                sectorIndexOfPerformance = result.getDouble(column);
            }

            statement.close();
            connection.close();

            return sectorIndexOfPerformance;
        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (NullPointerException ex)
        {
            nullEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return sectorIndexOfPerformance;
    }


    // Gets the number of patterns stored in the DB
    public int getNumberOfCollections()
    {
        int noOfCollections = 0;

        try
        {
            ResultSet result = connectToDatabase("SELECT * FROM Collection");

            result.last();
            noOfCollections = result.getRow();

            statement.close();
            connection.close();

        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (NullPointerException ex)
        {
            nullEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return noOfCollections;
    }


    // Gets the number of patterns stored in the DB
    public int getNumberOfPatterns()
    {
        int noOfPatterns = 0;

        try
        {

            ResultSet result = connectToDatabase("SELECT * FROM Pattern");

            result.last();
            noOfPatterns = result.getRow();

            statement.close();
            connection.close();

        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (NullPointerException ex)
        {
            nullEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return noOfPatterns;
    }

    // Retrieves the co ords for each pattern
    public ArrayList getPatternCoords(int patternNo)
    {
        ArrayList<Integer> coords = new ArrayList<>();

        try
        {
            ResultSet result = connectToDatabase("SELECT * FROM Pattern WHERE PatternID = '" + patternNo + "'");


            while (result.next())
            {
                coords.add(result.getInt("Dot01X"));
                coords.add(result.getInt("Dot01Y"));

                coords.add(result.getInt("Dot02X"));
                coords.add(result.getInt("Dot02Y"));

                coords.add(result.getInt("Dot03X"));
                coords.add(result.getInt("Dot03Y"));

                coords.add(result.getInt("Dot04X"));
                coords.add(result.getInt("Dot04Y"));

                coords.add(result.getInt("Dot05X"));
                coords.add(result.getInt("Dot05Y"));

                coords.add(result.getInt("Dot06X"));
                coords.add(result.getInt("Dot06Y"));

                coords.add(result.getInt("Dot07X"));
                coords.add(result.getInt("Dot07Y"));

                coords.add(result.getInt("Dot08X"));
                coords.add(result.getInt("Dot08Y"));

                coords.add(result.getInt("Dot09X"));
                coords.add(result.getInt("Dot09Y"));


                if (patternNo > 4)
                {
                    coords.add(result.getInt("Dot10X"));
                    coords.add(result.getInt("Dot10Y"));

                    coords.add(result.getInt("Dot11X"));
                    coords.add(result.getInt("Dot11Y"));

                    coords.add(result.getInt("Dot12X"));
                    coords.add(result.getInt("Dot12Y"));

                    coords.add(result.getInt("Dot13X"));
                    coords.add(result.getInt("Dot13Y"));

                    coords.add(result.getInt("Dot14X"));
                    coords.add(result.getInt("Dot14Y"));

                    coords.add(result.getInt("Dot15X"));
                    coords.add(result.getInt("Dot15Y"));
                }

                if (patternNo > 6)
                {
                    coords.add(result.getInt("Dot16X"));
                    coords.add(result.getInt("Dot16Y"));

                    coords.add(result.getInt("Dot17X"));
                    coords.add(result.getInt("Dot17Y"));

                    coords.add(result.getInt("Dot18X"));
                    coords.add(result.getInt("Dot18Y"));

                    coords.add(result.getInt("Dot19X"));
                    coords.add(result.getInt("Dot19Y"));

                    coords.add(result.getInt("Dot20X"));
                    coords.add(result.getInt("Dot20Y"));

                    coords.add(result.getInt("Dot21X"));
                    coords.add(result.getInt("Dot21Y"));
                }
            }


            // Closes the statement and connection
            statement.close();
            connection.close();

            // Returns an ArrayList of Coords
            return coords;

        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (NullPointerException ex)
        {
            nullEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return null;

    }


    public boolean storeIPTotal(int pattern, int sector, double totalIP)
    {
        try
        {
            connection = connectToDatabase();

            // Create a new SQL statement, build the query then executes the statement
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT * FROM SectorData WHERE PatternRef = '" + pattern + "' AND SectorNumber = '" + sector+ "'";

            ResultSet result = statement.executeQuery(query);

            // If there is a result then enter the if statement
            if (result.isBeforeFirst())
            {
                // Sets the ResultSet to the first entry
                result.first();

                // Updates the Mean value
                result.updateDouble("TotalIP", totalIP);

                // Update the row in the DB
                result.updateRow();
            }
            else
            {
                return false;
            }

            statement.close();
            connection.close();
            return true;

        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (NullPointerException ex)
        {
            nullEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return false;
    }


    public boolean storeUserIndexOfPerformance(int collection, int pattern, int sector, double indexOfPerformance)
    {
        try
        {
            connection = connectToDatabase();

            PreparedStatement statement = connection.prepareStatement("INSERT INTO userPerformanceData(CollectionRef, PatternRef, SectorNumber, IndexOfPerformance) VALUES (?, ?, ?, ?)");
            {
                statement.setInt(1, collection);
                statement.setInt(2, pattern);
                statement.setInt(3, sector);
                statement.setDouble(4, indexOfPerformance);
                statement.executeUpdate();
            }

            statement.close();
            connection.close();
            return true;

        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (NullPointerException ex)
        {
            nullEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return false;
    }


    public boolean storeSectorIndexOfDifficultyAndDistanceAndDirection(int pattern, int sector, double distance, double indexOfDifficulty, String direction)
    {
        try
        {
            connection = connectToDatabase();

            PreparedStatement statement = connection.prepareStatement("INSERT INTO SectorData(PatternRef, SectorNumber, Distance, IndexOfDifficulty, Direction) VALUES (?, ?, ?, ?, ?)");
            {
                statement.setInt(1, pattern);
                statement.setInt(2, sector);
                statement.setDouble(3, distance);
                statement.setDouble(4, indexOfDifficulty);
                statement.setString(5, direction);
                statement.executeUpdate();
            }

            statement.close();
            connection.close();
            return true;
        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (NullPointerException ex)
        {
            nullEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return false;
    }


    public boolean storeUsersDistanceFromMeanInSD(int collection, int pattern, int sector, double distance)
    {
        try
        {
            connection = connectToDatabase();

            // Create a new SQL statement, build the query then executes the statement
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT * FROM UserPerformanceData WHERE CollectionRef = '" + collection + "' AND PatternRef = '" + pattern + "' AND SectorNumber = '" + sector+ "'";

            ResultSet result = statement.executeQuery(query);

            // If there is a result then enter the if statement
            if (result.isBeforeFirst())
            {
                // Sets the ResultSet to the first entry
                result.first();

                // Updates the Mean value
                result.updateDouble("DistanceFromMeanInStandardDeviation", distance);

                // Update the row in the DB
                result.updateRow();
            }
            else
            {
                return false;
            }

            statement.close();
            connection.close();
            return true;
        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (NullPointerException ex)
        {
            nullEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return false;
    }

    public boolean storeSectorStandardDeviationAndMean(int pattern, int sector, double standardDeviation, double mean)
    {
        try
        {
            connection = connectToDatabase();

            // Create a new SQL statement, build the query then executes the statement
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT * FROM SectorData WHERE PatternRef = '" + pattern + "' AND SectorNumber = '" + sector+ "'";

            ResultSet result = statement.executeQuery(query);

            // If there is a result then enter the if statement
            if (result.isBeforeFirst())
            {
                // Sets the ResultSet to the first entry
                result.first();

                // Updates the Mean value
                result.updateDouble("Mean", mean);

                // Updates the Standard Deviation value
                result.updateDouble("StandardDeviation", standardDeviation);

                // Update the row in the DB
                result.updateRow();
            }
            else
            {
                return false;
            }

            statement.close();
            connection.close();
            return true;
        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (NullPointerException ex)
        {
            nullEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return false;
    }

    private ResultSet connectToDatabase(String query)
    {
        try
        {
            // Loads the driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            //Establishes a connection to the database
            connection =  DriverManager.getConnection("jdbc:mysql://localhost/honours-project?user=candidwebuser&password=pw4candid");

            // Create a new SQL statement
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);

            // Execute the statement
            return statement.executeQuery(query);

        }
        catch (ClassNotFoundException ex)
        {
            classNotFoundEx(ex);
        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return null;
    }

    private Connection connectToDatabase()
    {
        try
        {
            // Loads the driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost/honours-project?user=candidwebuser&password=pw4candid";

            //Establishes a connection to the database
            return DriverManager.getConnection(url);
        }
        catch (ClassNotFoundException ex)
        {
            classNotFoundEx(ex);
        }
        catch (SQLException ex)
        {
            sqlEx(ex);
        }
        catch (Exception ex)
        {
            generalException(ex);
        }

        return null;
    }

    private void classNotFoundEx(ClassNotFoundException ex)
    {
        // Removes duplicate code when an exception is raised
        System.err.println("Could not load the DB driver.\n" + ex.getMessage());
        ex.printStackTrace();
        System.exit(-1);
    }


    private void sqlEx(SQLException ex)
    {
        // Removes duplicate code when an exception is raised
        System.err.println("SQL error.\n" + ex.getMessage());
        ex.printStackTrace();
        System.exit(-1);
    }


    private void nullEx(NullPointerException ex)
    {
        // Removes duplicate code when an exception is raised
        System.err.println("Null Pointer Exception error.\n" + ex.getMessage());
        ex.printStackTrace();
        System.exit(-1);
    }


    private void generalException(Exception ex)
    {
        // Removes duplicate code when an exception is raised
        System.err.println("Error.\n" + ex.getMessage());
        ex.printStackTrace();
        System.exit(-1);
    }
}
