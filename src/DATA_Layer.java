import java.sql.*;
import java.util.ArrayList;


public class DATA_Layer implements DATA_Layer_Interface
{

    private Connection connection;
    private Statement statement;

    private boolean dataExists = false;

    private int count = 0;


    // Default Constructor
    public DATA_Layer()
    {

    }

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







    //region Pattern Related


    // Gets the number of patterns stored in the DB
    public int getNumberOfPatterns()
    {
        ResultSet result = connectToDatabase("SELECT * FROM Pattern");
        int noOfPatterns = 0;

        try
        {
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

    public boolean storePatternIndexOfDifficulty(int pattern, int sector, double distance, double indexOfDifficulty)
    {
        // COMMENT HERE MORE

        try
        {
            ResultSet result = connectToDatabase("SELECT * FROM SectorIndexOfDifficulty");


            if (!result.next())
            {
                System.out.println("TABLE EMPTY");
            }
            else
            {
                //System.out.println("TABLE NOT EMPTY");

                result.last();

                if((result.getInt("PatternRef")) == getNumberOfPatterns() &&  result.getInt("SectorNumber") == 21)
                {
                    statement.close();
                    connection.close();
                    return false;
                }
            }

            statement.close();
            connection.close();

            connection = connectToDatabase();

            PreparedStatement insert = connection.prepareStatement("INSERT INTO SectorIndexOfDifficulty(PatternRef, SectorNumber, Distance, IndexOfDifficulty) VALUES (?, ?, ?, ?)");
            {
                insert.setInt(1, pattern);
                insert.setInt(2, sector);
                insert.setDouble(3, distance);
                insert.setDouble(4, indexOfDifficulty);
                insert.executeUpdate();
                insert.close();
                connection.close();
            }

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
            statement = connection.createStatement();

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
