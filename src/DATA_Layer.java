import java.sql.*;
import java.util.ArrayList;


public class DATA_Layer implements DATA_Layer_Interface
{
    private Connection connection;
    private Statement statement;

    //private boolean dataExists = false;

    //private int count = 0;


    // Default Constructor
    public DATA_Layer()
    {

    }

    // Adds the new tables need to store the results from calculations
    public boolean addNewDatabaseTables()
    {
        try
        {
            connection = connectToDatabase();

            // Create a new SQL statement
            statement = connection.createStatement();

            // Creates the table `SectorIndexOfDifficulty`
            statement.executeUpdate("DROP TABLE IF EXISTS `SectorIndexOfDifficulty`;");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `SectorIndexOfDifficulty` " +
                    "( `SectorIODID`	INTEGER AUTO_INCREMENT, " +
                    " `PatternRef`	INTEGER, " +
                    " `SectorNumber`	INTEGER, " +
                    " `Distance`	REAL, " +
                    " `IndexOfDifficulty`	REAL, " +
                    " PRIMARY KEY (`SectorIODID`), " +
                    " FOREIGN KEY(`PatternRef`) REFERENCES `Pattern`(`PatternID`) " +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");


            // Creates the table `SectorIndexOfPerformance`
            statement.executeUpdate("DROP TABLE IF EXISTS `SectorIndexOfPerformance`;");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `SectorIndexOfPerformance` " +
                    "( `SectorIOPID`	INTEGER AUTO_INCREMENT, " +
                    " `CollectionRef`	INTEGER, " +
                    " `PatternRef`	INTEGER, " +
                    " `SectorNumber`	INTEGER, " +
                    " `DistanceOfDrawnPath`	REAL, " +
                    " `IndexOfPerformance`	REAL, " +
                    " PRIMARY KEY (`SectorIOPID`), " +
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



    // Get the sector index of performance for each pattern/sector and returns the value as a double
    public double getSectorIndexOfPerformance(int collection, int pattern, int sector, String column)
    {
        double sectorIndexOfPerformance = 0;


        try
        {
            ResultSet result;

            if (collection > 0)
            {
                result = connectToDatabase("SELECT * FROM SectorIndexOfPerformance WHERE CollectionRef = '" + collection + "' AND PatternRef = '" + pattern + "' AND SectorNumber = '" + sector + "'");
            }
            else
            {
                result = connectToDatabase("SELECT * FROM SectorIndexOfDifficulty WHERE PatternRef = '" + pattern + "' AND SectorNumber = '" + sector + "'");

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



    public ArrayList getDrawnPathCoords(int collection, int pattern, int sector)
    {
        ArrayList<Double> listOfDrawnPathSectorCoords = new ArrayList<>();

        try
        {
            ResultSet result = connectToDatabase("SELECT * FROM Coords WHERE CollectionRef = '" + collection + "' AND PatternRef = '" + pattern + "' AND SectorNumber = '" + sector + "'");

            result.beforeFirst();

            while (result.next())
            {
                listOfDrawnPathSectorCoords.add(result.getDouble("DrawnPathX"));
                listOfDrawnPathSectorCoords.add(result.getDouble("DrawnPathY"));
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

        return listOfDrawnPathSectorCoords;
    }




    //region Pattern Related


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

    public boolean storeUserIndexOfPerformance(int collection, int pattern, int sector, double indexOfPerformance)
    {
        try
        {
            connection = connectToDatabase();

            PreparedStatement insertIOP = connection.prepareStatement("INSERT INTO SectorIndexOfPerformance(CollectionRef, PatternRef, SectorNumber, IndexOfPerformance) VALUES (?, ?, ?, ?)");
            {
                insertIOP.setInt(1, collection);
                insertIOP.setInt(2, pattern);
                insertIOP.setInt(3, sector);
                insertIOP.setDouble(4, indexOfPerformance);
                insertIOP.executeUpdate();
            }

            insertIOP.close();
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

    public boolean storePatternIndexOfDifficulty(int pattern, int sector, double distance, double indexOfDifficulty)
    {
        try
        {
            connection = connectToDatabase();

            PreparedStatement insertIOD = connection.prepareStatement("INSERT INTO SectorIndexOfDifficulty(PatternRef, SectorNumber, Distance, IndexOfDifficulty) VALUES (?, ?, ?, ?)");
            {
                insertIOD.setInt(1, pattern);
                insertIOD.setInt(2, sector);
                insertIOD.setDouble(3, distance);
                insertIOD.setDouble(4, indexOfDifficulty);
                insertIOD.executeUpdate();
            }

            insertIOD.close();
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


    public boolean storeUserDrawnPathDistance(int collection, int pattern, int sector, double distanceDrawn)
    {
        try
        {
            connection = connectToDatabase();

            // Create a new SQL statement, build the query then executes the statement
            Statement statementIncident = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT * FROM SectorIndexOfPerformance WHERE CollectionRef = '" + collection + "' AND PatternRef = '" + pattern + "' AND SectorNumber = '" + sector + "'";
            ResultSet result = statementIncident.executeQuery(query);

            // If there is a result then enter the if statement
            if (result.isBeforeFirst())
            {
                // Sets the ResultSet to the first entry
                result.first();

                // Updates the relevant columns in the DB
                result.updateDouble("DistanceOfDrawnPath", distanceDrawn);

                // Update the row in the DB
                result.updateRow();

                result.close();
                connection.close();
            }
            else
            {
                result.close();
                connection.close();
                return false;
            }

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

        return true;
    }

    //endregion

    //region Private Methods
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

            //Establishes a connection to the database
            return DriverManager.getConnection("jdbc:mysql://localhost/honours-project?user=candidwebuser&password=pw4candid");

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
    //endregion
}
