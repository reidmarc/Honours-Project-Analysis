import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class APP_Layer implements APP_Layer_Interface
{
    // The underlying data layer this application layer sits upon
    private DATA_Layer dataLayer;

    private final int targetWidth = 5;
    private final double startingX = 11;
    private final double startingY = 100;
    private int numberOfCollections;
    private int numberOfPatterns;

    private boolean isItTheFirstSector = true;
    private double previousX;
    private double previousY;

    private ArrayList<ArrayList<Double>> listOfTimings = new ArrayList<>();
    private ArrayList<Double> listOfIndexOfDifficulty = new ArrayList<>();

    private ArrayList<Double> listOfIndexOfPerformance = new ArrayList<>();
    private ArrayList<ArrayList<Double>> listOfListOfIndexOfPerformance = new ArrayList<>();




    public APP_Layer(DATA_Layer dataLayer)
    {
        this.dataLayer = dataLayer;


        //setupDatabase();                        // Step 1

        retrieveNumberOfCollectionsValue();     // Step 2

        retrieveNumberOfPatternsValue();        // Step 3

        //setupIndexOfDifficulty();               // Step 4

        //setupIndexOfPerformance();              // Step 5

        //setupDrawnPath();                       // Step 6

        //prepareDataToWriteToCSV();              // Step 7

        tempTesting();
    }

    // method to compare distance of drawn path with the actual distance of a sector
    private void tempTesting()
    {
        int numberOfSectors;
        double drawnDistance = 0;
        double distance = 0;
        int anomalyCounter = 0;
        int passesCounter = 0;

        int sector1Counter = 0;
        int sector2Counter = 0;
        int sector3Counter = 0;
        int sector4Counter = 0;
        int sector5Counter = 0;
        int sector6Counter = 0;
        int sector7Counter = 0;
        int sector8Counter = 0;
        int sector9Counter = 0;
        int sector10Counter = 0;
        int sector11Counter = 0;
        int sector12Counter = 0;
        int sector13Counter = 0;
        int sector14Counter = 0;
        int sector15Counter = 0;
        int sector16Counter = 0;
        int sector17Counter = 0;
        int sector18Counter = 0;
        int sector19Counter = 0;
        int sector20Counter = 0;
        int sector21Counter = 0;


        for (int pattern = 1; pattern <= numberOfPatterns; pattern++)
        {
            // Patterns 1 + 2 + 3 + 4 have 9 sectors
            if (pattern < 5)
            {
                numberOfSectors = 9;
            }
            // Patterns 7 + 8 have 21 sectors
            else if (pattern > 6)
            {
                numberOfSectors = 21;
            }
            // Patterns 5 + 6 have 15 sectors
            else
            {
                numberOfSectors = 15;
            }


            for (int sector = 1; sector <= numberOfSectors; sector++)
            {
                for (int collection = 1; collection <= numberOfCollections; collection++)
                {

                    drawnDistance = dataLayer.getSectorIndexOfPerformance(collection, pattern, sector, "DistanceOfDrawnPath");

                    distance = dataLayer.getSectorIndexOfPerformance(0, pattern, sector, "Distance");


                    if (drawnDistance < distance)
                    //if ((distance - drawnDistance) > 10)
                    {
                        anomalyCounter = anomalyCounter + 1;

                        if (sector == 1)
                        {
                            sector1Counter = sector1Counter + 1;
                        }

                        if (sector == 2)
                        {
                            sector2Counter = sector2Counter + 1;
                        }

                        if (sector == 3)
                        {
                            sector3Counter = sector3Counter + 1;
                        }

                        if (sector == 4)
                        {
                            sector4Counter = sector4Counter + 1;
                        }

                        if (sector == 5)
                        {
                            sector5Counter = sector5Counter + 1;
                        }

                        if (sector == 6)
                        {
                            sector6Counter = sector6Counter + 1;
                        }

                        if (sector == 7)
                        {
                            sector7Counter = sector7Counter + 1;
                        }

                        if (sector == 8)
                        {
                            sector8Counter = sector8Counter + 1;
                        }

                        if (sector == 9)
                        {
                            sector9Counter = sector9Counter + 1;
                        }

                        if (sector == 10)
                        {
                            sector10Counter = sector10Counter + 1;
                        }

                        if (sector == 11)
                        {
                            sector11Counter = sector11Counter + 1;
                        }

                        if (sector == 12)
                        {
                            sector12Counter = sector12Counter + 1;
                        }

                        if (sector == 13)
                        {
                            sector13Counter = sector13Counter + 1;
                        }

                        if (sector == 14)
                        {
                            sector14Counter = sector14Counter + 1;
                        }

                        if (sector == 15)
                        {
                            sector15Counter = sector15Counter + 1;
                        }

                        if (sector == 16)
                        {
                            sector16Counter = sector16Counter + 1;
                        }

                        if (sector == 17)
                        {
                            sector17Counter = sector17Counter + 1;
                        }

                        if (sector == 18)
                        {
                            sector18Counter = sector18Counter + 1;
                        }

                        if (sector == 19)
                        {
                            sector19Counter = sector19Counter + 1;
                        }

                        if (sector == 20)
                        {
                            sector20Counter = sector20Counter + 1;
                        }

                        if (sector == 21)
                        {
                            sector21Counter = sector21Counter + 1;
                        }
                    }



                    passesCounter = passesCounter + 1;

                }
            }
        }


        System.out.println("How many anomalies are there: " + anomalyCounter + "/" + passesCounter);


        System.out.println("Sector 1 Anomalies: " + sector1Counter);
        System.out.println("Sector 2 Anomalies: " + sector2Counter);
        System.out.println("Sector 3 Anomalies: " + sector3Counter);
        System.out.println("Sector 4 Anomalies: " + sector4Counter);
        System.out.println("Sector 5 Anomalies: " + sector5Counter);
        System.out.println("Sector 6 Anomalies: " + sector6Counter);
        System.out.println("Sector 7 Anomalies: " + sector7Counter);
        System.out.println("Sector 8 Anomalies: " + sector8Counter);
        System.out.println("Sector 9 Anomalies: " + sector9Counter);
        System.out.println("Sector 10 Anomalies: " + sector10Counter);
        System.out.println("Sector 11 Anomalies: " + sector11Counter);
        System.out.println("Sector 12 Anomalies: " + sector12Counter);
        System.out.println("Sector 13 Anomalies: " + sector13Counter);
        System.out.println("Sector 14 Anomalies: " + sector14Counter);
        System.out.println("Sector 15 Anomalies: " + sector15Counter);
        System.out.println("Sector 16 Anomalies: " + sector16Counter);
        System.out.println("Sector 17 Anomalies: " + sector17Counter);
        System.out.println("Sector 18 Anomalies: " + sector18Counter);
        System.out.println("Sector 19 Anomalies: " + sector19Counter);
        System.out.println("Sector 20 Anomalies: " + sector20Counter);
        System.out.println("Sector 21 Anomalies: " + sector21Counter);




    }




    private void prepareDataToWriteToCSV()
    {
        double value = 0;
        boolean isItTheFirstSectorIOP = true;
        boolean isItTheFirstDistanceDrawn = true;


        String filepath;
        int numberOfSectors;



        for (int pattern = 1; pattern <= numberOfPatterns; pattern++)
        {
            // Patterns 1 + 2 + 3 + 4 have 9 sectors
            if (pattern < 5)
            {
                numberOfSectors = 9;
            }
            // Patterns 7 + 8 have 21 sectors
            else if (pattern > 6)
            {
                numberOfSectors = 21;
            }
            // Patterns 5 + 6 have 15 sectors
            else
            {
                numberOfSectors = 15;
            }


            for (int sector = 1; sector <= numberOfSectors; sector++)
            {
                for (int collection = 1; collection <= numberOfCollections ; collection++)
                {

                    value = dataLayer.getSectorIndexOfPerformance(collection, pattern, sector, "IndexOfPerformance");
                    filepath = "SectorIndexOfPerformanceData/Pattern - " + pattern + " - Sector - " + sector + ".csv";

                    if (isItTheFirstSectorIOP)
                    {
                        setRowHeadingsCSV(filepath, "Index of Performance");
                        isItTheFirstSectorIOP = false;
                    }

                    exportDataToCSV(filepath, collection, pattern, sector, value);




                    value = dataLayer.getSectorIndexOfPerformance(collection, pattern, sector, "DistanceOfDrawnPath");
                    filepath = "DistanceOfDrawnPathData/Pattern - " + pattern + " - Sector - " + sector + ".csv";

                    if (isItTheFirstDistanceDrawn)
                    {
                        setRowHeadingsCSV(filepath, "Distance of Drawn Path");
                        isItTheFirstDistanceDrawn = false;
                    }

                    exportDataToCSV(filepath, collection, pattern, sector, value);

                }

                isItTheFirstDistanceDrawn = true;
                isItTheFirstSectorIOP = true;
            }
        }

        System.out.println("Exported Data to CSV Files: Completed");
    }



    private void exportDataToCSV(String filepath, int collection, int pattern, int sector, double value)
    {
        try
        {
            FileWriter fw = new FileWriter(filepath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(collection + ", " + pattern + ", " + sector + ", " + value);
            pw.flush();
            pw.close();
        }
        catch (Exception ex)
        {
            System.out.println("CSV File NOT saved");
            System.out.println(ex);
            ex.printStackTrace();
            System.exit(-1);
        }
    }

    private void setRowHeadingsCSV(String filepath, String value)
    {
        try
        {
            FileWriter fw = new FileWriter(filepath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println("Collection, Pattern, Sector, " + value);
            pw.flush();
            pw.close();
        }
        catch (Exception ex)
        {
            System.out.println("CSV File NOT saved");
            System.out.println(ex);
            ex.printStackTrace();
            System.exit(-1);
        }
    }




    private void setupIndexOfDifficulty()
    {
        boolean isSetupOk = true;

        // A FOR loop to iterate through each patterns coords
        for (int i = 1; i <= numberOfPatterns; i++)
        {
            ArrayList coords = dataLayer.getPatternCoords(i);

            isItTheFirstSector = true;

            for (int j = 0; j < coords.size(); j+=2)
            {
                // Allows for the casting from Integer to double
                double x = (double) (Integer) coords.get(j);
                double y = (double) (Integer) coords.get(j + 1);

                double distance = calculateTheDistance(x , y);
                double indexOfDifficulty = calculateTheIndexOfDifficulty(distance);

                if (!dataLayer.storePatternIndexOfDifficulty(i, (( j / 2 ) + 1), distance, indexOfDifficulty))
                {
                    System.out.println("Error adding Index of Difficulty to the DB for Pattern: " + i + " - Sector: " + (( j / 2 ) + 1));
                    isSetupOk = false;
                }

                listOfIndexOfDifficulty.add((double) (Integer) i);
                listOfIndexOfDifficulty.add((double) (Integer) (( j / 2 ) + 1));
                listOfIndexOfDifficulty.add(indexOfDifficulty);

                isItTheFirstSector = false;
            }
        }

        if (isSetupOk)
        {
            System.out.println("Index of Difficulty calculations: Completed");
        }
    }

    private void setupDrawnPath()
    {
        ArrayList<Double> listOfDrawnPathSectorCoords;
        ArrayList<Double> listOfSectorCoords = new ArrayList<>();

        boolean isSetupOk = true;

        int numberOfSectors;

        // Adds each collection of Coords to a list
        for (int i = 1; i <= numberOfCollections; i++)
        {
            for (int j = 1; j <= numberOfPatterns; j++)
            {
                // Patterns 1 + 2 + 3 + 4 have 9 sectors
                if (j < 5)
                {
                    numberOfSectors = 9;
                }
                // Patterns 7 + 8 have 21 sectors
                else if (j > 6)
                {
                    numberOfSectors = 21;
                }
                // Patterns 5 + 6 have 15 sectors
                else
                {
                    numberOfSectors = 15;
                }

                isItTheFirstSector = true;

                for (int k = 1; k <= numberOfSectors; k++)
                {
                    listOfDrawnPathSectorCoords = dataLayer.getDrawnPathCoords(i, j, k);

                    for (int m = 0; m < (listOfDrawnPathSectorCoords.size()) / 2; m = m + 2)
                    {
                        listOfSectorCoords.add(calculateTheDistance(listOfDrawnPathSectorCoords.get(m), listOfDrawnPathSectorCoords.get(m + 1)));
                        isItTheFirstSector = false;

                    }

                    double totalDistanceDrawn = 0;

                    for (int n = 0; n < listOfSectorCoords.size(); n++)
                    {
                        totalDistanceDrawn = totalDistanceDrawn + listOfSectorCoords.get(n);
                    }

                    if (!dataLayer.storeUserDrawnPathDistance(i, j, k, totalDistanceDrawn))
                    {
                        System.out.println("Error adding distance drawn to the DB for Collection: " + i + " - Pattern: " + j + " - Sector: " + k);
                        isSetupOk = false;
                    }

                    listOfSectorCoords.clear();
                    listOfDrawnPathSectorCoords.clear();
                }
            }
        }

        if (isSetupOk)
        {
            System.out.println("Drawn path calculations: Completed");
        }
    }


    private void setupIndexOfPerformance()
    {
        boolean isSetupOk = true;

        for (int i = 1; i <= numberOfCollections; i++)
        {
            listOfTimings.add(dataLayer.getSectorTimes(i));
        }

        for (int z = 0; z < listOfTimings.size(); z++)
        {
            for (int y = 0; y < listOfTimings.get(z).size(); y = y + 3)
            {
                // IF the pattern number and sector number both match
                if ((listOfTimings.get(z).get(y)).equals(listOfIndexOfDifficulty.get(y)) && (listOfTimings.get(z).get(y + 1)).equals(listOfIndexOfDifficulty.get(y + 1)))
                {
                    // Adds the Pattern Number
                    listOfIndexOfPerformance.add(listOfIndexOfDifficulty.get(y));

                    // Adds the Sector Number
                    listOfIndexOfPerformance.add(listOfIndexOfDifficulty.get(y + 1));

                    // Adds the Index of Performance value
                    listOfIndexOfPerformance.add(calculateTheIndexOfPerformance(listOfIndexOfDifficulty.get(y + 2), listOfTimings.get(z).get(y + 2)));
                }
            }
            // Adds the collection  to a list
            listOfListOfIndexOfPerformance.add(new ArrayList<>(listOfIndexOfPerformance));

            // Clears the list ready for a new collection
            listOfIndexOfPerformance.clear();
        }

        // FOR loop which iterates through the list within the list to send values to `storeUserIndexOfPerformance`
        for (int i = 0; i < listOfListOfIndexOfPerformance.size(); i++)
        {
            for (int j = 0; j < listOfListOfIndexOfPerformance.get(i).size(); j = j + 3)
            {
                if (!dataLayer.storeUserIndexOfPerformance((i + 1), listOfListOfIndexOfPerformance.get(i).get(j).intValue(), listOfListOfIndexOfPerformance.get(i).get(j + 1).intValue(), listOfListOfIndexOfPerformance.get(i).get(j + 2)))
                {
                    System.out.println("Index of Performance value NOT ADDED - Collection: " + (i + 1) + " - Pattern: " + listOfListOfIndexOfPerformance.get(i).get(j) + " - Sector: " + listOfListOfIndexOfPerformance.get(i).get(j + 1) + " - IoP: " + listOfListOfIndexOfPerformance.get(i).get(j + 2));
                    isSetupOk = false;
                }
            }
        }

        if (isSetupOk)
        {
            System.out.println("Index of Performance calculations: Completed");
        }
    }




    // Calculates the Distance between two Co ordinates
    private double calculateTheDistance(double x, double y)
    {
        double xDiff;
        double yDiff;

        if (isItTheFirstSector)
        {
            xDiff = x - startingX;
            yDiff = y - startingY;
        }
        else
        {
            xDiff = x - previousX;
            yDiff = y - previousY;
        }

        // Sets the previous values for x and y for the next iteration
        previousX = x;
        previousY = y;

        return Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
    }

    // Calculates the Index of Difficulty
    private double calculateTheIndexOfDifficulty(double Distance)
    {
        return (Math.log((2 * Distance) / targetWidth) / Math.log(2));
    }


    // Calculates the Index of Performance
    private double calculateTheIndexOfPerformance(double IndexDifficulty, double MovementTime)
    {
        //System.out.println("Index of Performance: " + IndexDifficulty / MovementTime);
        return (IndexDifficulty / MovementTime);

    }

    // Calls the method `addNewDatabaseTables` from the datalayer
    private void setupDatabase()
    {
        if (dataLayer.addNewDatabaseTables())
        {
            System.out.println("Database Setup: Completed");

        }
        else
        {
            System.out.println("Database Setup: ERROR - NOT Completed");
        }
    }

    private void retrieveNumberOfCollectionsValue()
    {
        numberOfCollections = dataLayer.getNumberOfCollections();

        System.out.println("Number of Collections: " + numberOfCollections);
    }

    private void retrieveNumberOfPatternsValue()
    {
        numberOfPatterns = dataLayer.getNumberOfPatterns();

        System.out.println("Number of Patterns: " + numberOfPatterns);
    }

}
