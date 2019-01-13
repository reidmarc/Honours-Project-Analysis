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

    //private ArrayList<ArrayList<Double>> listOfDrawnPathPatternSectors = new ArrayList<>();


    public APP_Layer(DATA_Layer dataLayer)
    {
        this.dataLayer = dataLayer;

        setupDatabase();                        // Step 1

        retrieveNumberOfCollectionsValue();     // Step 2

        retrieveNumberOfPatternsValue();        // Step 3

        setupIndexOfDifficulty();               // Step 4

        setupIndexOfPerformance();              // Step 5

        setupDrawnPath();                       // Step 6
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

                        //PROBLEM IS WHEN A NEW SECTOR STARTS THE FIRST CALC IS FROM PATTERN STARTING POSITION AND NOT THE LAST COORDS FROM PREVIOUS SECTOR
                        // Seems to be working now??

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
