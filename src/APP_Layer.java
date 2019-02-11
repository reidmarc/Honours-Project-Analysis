import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class APP_Layer implements APP_Layer_Interface
{
    // The underlying data layer this application layer sits upon
    private DATA_Layer dataLayer;

    private final int targetWidth = 15;
    private final double startingX = 11;
    private final double startingY = 100;
    private int numberOfCollections;
    private int numberOfPatterns;
    private int totalNumberOfSectors = 108;

    private boolean isItTheFirstSector = true;
    private double previousX;
    private double previousY;
    private double oldY;

    int upCounter = 0;
    int downCounter = 0;
    int levelCounter = 0;

    private ArrayList<ArrayList<Double>> listOfTimings = new ArrayList<>();
    private ArrayList<Double> listOfIndexOfDifficulty = new ArrayList<>();

    private ArrayList<Double> listOfIndexOfPerformance = new ArrayList<>();
    private ArrayList<ArrayList<Double>> listOfListOfIndexOfPerformance = new ArrayList<>();

    private ArrayList<Double> listOfInterestingResults = new ArrayList<>();






    public APP_Layer(DATA_Layer dataLayer)
    {
        this.dataLayer = dataLayer;

        setupDatabase();                                    // Step 1

        retrieveNumberOfCollectionsValue();                 // Step 2

        retrieveNumberOfPatternsValue();                    // Step 3

        setupIndexOfDifficulty();                           // Step 4

        setupIndexOfPerformance();                          // Step 5

        //setupDrawnPath();                                 // Step 6

        prepareIndexOfPerformanceDataToWriteToCSV();        // Step 7

        calculateUserSectorDistanceFromMeanInSD();          // Step 8

        prepareStandardDeviationDataToWriteToCSV();         // Step 9

        moreStandardDeviationCalcs();

        sectorDirectionAndScoreAnalysis();

        directionOfSectorForDistanceFromTheMeanInStandardDeviationAnalysis();

        //tempTesting();
    }

    // Returns the number of sectors in a pattern
    private int getNumberOfSectors(int pattern)
    {
        int numberOfSectors;

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

        return numberOfSectors;
    }

    private void sectorDirectionAndScoreAnalysis()
    {
        for (int pattern = 1; pattern <= numberOfPatterns; pattern = pattern + 2)
        {
            for (int sector = 1; sector <= getNumberOfSectors(pattern); sector++)
            {
                double patternScore;
                double patternScoreInverse;
                String direction = "";


                // GET PATTERN 1 SECTOR 1 SCORE
                patternScore = dataLayer.getTotalStandardDeviationScore(pattern, sector);

                // GET PATTERN 2 SECTOR 2 SCORE
                patternScoreInverse = dataLayer.getTotalStandardDeviationScore(pattern + 1, sector);

                // COMPARE TO SEE WHICH IS LOWER AND OUTPUT THE LOWER AND THE DIRECTION
                if (patternScore < patternScoreInverse)
                {
                    direction = dataLayer.getSectorDirection(pattern, sector);
                    //System.out.println("Pattern: " + pattern + " - Sector: " + sector + " - Direction: " + direction);
                }
                else if (patternScoreInverse < patternScore)
                {
                    direction = dataLayer.getSectorDirection(pattern + 1, sector);
                    //System.out.println("Pattern: " + (pattern + 1) + " - Sector: " + sector + " - Direction: " + direction);
                }
                else
                {
                    System.out.println("Sector scores are equal for Pattern: " + pattern + " and its inverse Pattern: " + (pattern + 1) + " - Sector: " + sector);
                }

                if (direction.equals("UP"))
                {
                    upCounter = upCounter + 1;
                }
                else if (direction.equals("DOWN"))
                {
                    downCounter = downCounter + 1;
                }
                else
                {
                    levelCounter = levelCounter + 1;
                }
            }
        }

        System.out.println("---Totals for Direction of Sector which users performed poorly on" +
                        "\nUP: " + upCounter +
                        "\nDOWN: " + downCounter +
                        "\nLEVEL: " + levelCounter);
    }


    private void directionOfSectorForDistanceFromTheMeanInStandardDeviationAnalysis()
    {
        double distanceFromTheMean = 0.0;
        String direction = "";
        int upCounterAboveTheMean = 0;
        int downCounterAboveTheMean = 0;
        int levelAboveCounter = 0;
        int upCounterBelowTheMean = 0;
        int downCounterBelowTheMean = 0;
        int levelBelowCounter = 0;

        for (int collection = 1; collection <= numberOfCollections; collection++)
        {
            for (int pattern = 1; pattern <= numberOfPatterns; pattern++)
            {
                for (int sector = 1; sector <= getNumberOfSectors(pattern); sector++)
                {
                    // GET USERS DISTANCE FROM THE MEAN IN SD VALUE
                    distanceFromTheMean = dataLayer.getDistanceFromMeanInStandardDeviation(collection, pattern, sector);

                    // CHECK IF ITS NEGATIVE
                    if (distanceFromTheMean < 0)
                    {
                        //GET THE DIRECTION
                        direction = dataLayer.getSectorDirection(pattern, sector);

                        if (direction.equals("UP"))
                        {
                            upCounterBelowTheMean = upCounterBelowTheMean + 1;
                        }
                        else if (direction.equals("DOWN"))
                        {
                            downCounterBelowTheMean = downCounterBelowTheMean + 1;
                        }
                        else
                        {
                            levelBelowCounter = levelBelowCounter + 1;
                        }

                    }
                    else if (distanceFromTheMean > 0)
                    {
                        //GET THE DIRECTION
                        direction = dataLayer.getSectorDirection(pattern, sector);

                        if (direction.equals("UP"))
                        {
                            upCounterAboveTheMean = upCounterAboveTheMean + 1;
                        }
                        else if (direction.equals("DOWN"))
                        {
                            downCounterAboveTheMean = downCounterAboveTheMean + 1;
                        }
                        else
                        {
                            levelAboveCounter = levelAboveCounter + 1;
                        }

                    }
                    else
                    {
                        // Value is equal to the SD, therefore not interesting
                    }



                    // OUTPUT THE DATA


                }
            }

            if ( (upCounterAboveTheMean + downCounterAboveTheMean + levelAboveCounter + upCounterBelowTheMean + downCounterBelowTheMean + levelBelowCounter) == 108)
            {
                System.out.println("\nCollection: " + collection +
                        "\nTotal sectors above the mean in the UP direction: " + upCounterAboveTheMean +
                        "\nTotal sectors above the mean in the DOWN direction: " + downCounterAboveTheMean +
                        "\nTotal sectors above the mean in the LEVEL direction: " + levelAboveCounter +

                        "\nTotal sectors below the mean in the UP direction: " + upCounterBelowTheMean +
                        "\nTotal sectors below the mean in the DOWN direction: " + downCounterBelowTheMean +
                        "\nTotal sectors below the mean in the LEVEL direction: " + levelBelowCounter);
            }

            upCounterAboveTheMean = 0;
            downCounterAboveTheMean = 0;
            levelAboveCounter = 0;
            upCounterBelowTheMean = 0;
            downCounterBelowTheMean = 0;
            levelBelowCounter = 0;
        }
    }

    // Rounds to 5 decimal places
    private double roundADoubleValue(double value)
    {
        DecimalFormat newFormat = new DecimalFormat("#.#####");
        return Double.valueOf(newFormat.format(value));
    }

    private void moreStandardDeviationCalcs()
    {
        double value = 0;

        int greaterThan2SDCounter = 0;
        int greaterThan1halfSDCounter = 0;
        int greaterThan1SDCounter = 0;

        int greaterThan2SDCounterNeg = 0;
        int greaterThan1halfSDCounterNeg = 0;
        int greaterThan1SDCounterNeg = 0;

        int withinSDCounter = 0;

        double distanceFromMeanInStandardDeviationCounter = 0;
        double roundedValue = 0;

        for (int collection = 1; collection <= numberOfCollections; collection++)
        {

            for (int pattern = 1; pattern <= numberOfPatterns; pattern++)
            {
                for (int sector = 1; sector <= getNumberOfSectors(pattern); sector++)
                {
                    double distanceFromMean = dataLayer.getDistanceFromMeanInStandardDeviation(collection, pattern, sector);

                    if (distanceFromMean > 2)
                    {
                        greaterThan2SDCounter = greaterThan2SDCounter + 1;
                    }
                    else if (distanceFromMean > 1.5)
                    {
                        greaterThan1halfSDCounter = greaterThan1halfSDCounter + 1;
                    }
                    else if (distanceFromMean > 1)
                    {
                        greaterThan1SDCounter = greaterThan1SDCounter + 1;
                    }
                    else if (distanceFromMean < -2)
                    {
                        greaterThan2SDCounterNeg = greaterThan2SDCounterNeg + 1;
                    }
                    else if (distanceFromMean < -1.5)
                    {
                        greaterThan1halfSDCounterNeg = greaterThan1halfSDCounterNeg + 1;
                    }
                    else if (distanceFromMean < -1)
                    {
                        greaterThan1SDCounterNeg = greaterThan1SDCounterNeg + 1;
                    }
                    else
                    {
                        withinSDCounter = withinSDCounter + 1;
                    }


                    // Grubbs related Upper .1% sig lvl for 11 collections
                    if (distanceFromMean > 2.48 || distanceFromMean < -2.48) //
                    {
                        listOfInterestingResults.add((double) collection);
                        listOfInterestingResults.add((double) pattern);
                        listOfInterestingResults.add((double) sector);
                        listOfInterestingResults.add(distanceFromMean);
                    }

                    if (distanceFromMean >= 0)
                    {
                        value = value + distanceFromMean;
                    }
                    else
                    {
                        value = value - distanceFromMean;
                    }
                }
            }

            // Gets the User ID
            int userID = dataLayer.getCollectionsUserNo(collection);


            // Checks the each sector has been accounted for
            if (greaterThan2SDCounter + greaterThan1halfSDCounter + greaterThan1SDCounter + greaterThan2SDCounterNeg + greaterThan1halfSDCounterNeg + greaterThan1SDCounterNeg + withinSDCounter == totalNumberOfSectors)
            {
                /*
                System.out.println( "\nCollection: " + collection +
                                    "\nUser ID: " + userID +
                                    "\n\nNo of sectors with a positive value greater than 2 SD: " + greaterThan2SDCounter +
                                    "\nNo of sectors with a positive value greater than 1.5 SD and less than 2 SD: " + greaterThan1halfSDCounter +
                                    "\nNo of sectors with a positive value greater than 1 SD and less than 1.5 SD: " + greaterThan1SDCounter +

                                    "\n\nNo of sectors with a negative value greater than 2 SD: " + greaterThan2SDCounterNeg +
                                    "\nNo of sectors with a negative value greater than 1.5 SD and less than 2 SD: " + greaterThan1halfSDCounterNeg +
                                    "\nNo of sectors with a negative value greater than 1 SD and less than 1.5 SD: " + greaterThan1SDCounterNeg +

                                    "\n\nNo of sectors within expected SD range: " + withinSDCounter +
                                    "\nTotal Average distance from mean in SD: " + (value / totalNumberOfSectors) +
                                    "\n------------------------------------------------------------------------------");
                */
            }
            else
            {
                System.out.println("Error with collection: " + collection + "'s SD calculations");
            }

            value = 0.0;
            greaterThan2SDCounter = 0;
            greaterThan1halfSDCounter = 0;
            greaterThan1SDCounter = 0;

            greaterThan2SDCounterNeg = 0;
            greaterThan1halfSDCounterNeg = 0;
            greaterThan1SDCounterNeg = 0;

            withinSDCounter = 0;
        }

        for (int i = 0; i < listOfInterestingResults.size(); i = i + 4)
        {
            /*
            System.out.println( "\n\nCollection: " + listOfInterestingResults.get(i) +
                                "\nPattern: " + listOfInterestingResults.get(i + 1) +
                                "\nSector: " + listOfInterestingResults.get(i + 2) +
                                "\nValue: " + listOfInterestingResults.get(i + 3) +
                                "\n----------------------------------------");
            */
        }


        for (int pattern = 1; pattern <= numberOfPatterns; pattern++)
        {
            for (int sector = 1; sector <= getNumberOfSectors(pattern); sector++)
            {

                for (int collection = 1; collection <= numberOfCollections; collection++)
                {
                    // Rounds Value to 5dp as the totals produced are large numbers

                    distanceFromMeanInStandardDeviationCounter = distanceFromMeanInStandardDeviationCounter + dataLayer.getDistanceFromMeanInStandardDeviation(collection, pattern, sector);

                    roundedValue = roundADoubleValue(distanceFromMeanInStandardDeviationCounter);
                }

                if (!dataLayer.storeTotalStandardDeviationScore(pattern, sector, roundedValue))
                {
                    System.out.println("Error storing total standard deviation score for Pattern: " + pattern + " - Sector:" + sector + " - SD Score: " + roundedValue);
                }

                distanceFromMeanInStandardDeviationCounter = 0;
            }
        }
    }

    private void prepareStandardDeviationDataToWriteToCSV()
    {
        String filepath;
        double value = 0;
        boolean isItTheFirstCollection = true;

        // Creates a folder inside the project for Data CSV
        File fileIOP = new File("StandardDeviationData");

        if (fileIOP.mkdir())
        {
            for (int collection = 1; collection <= numberOfCollections; collection++)
            {
                for (int pattern = 1; pattern <= numberOfPatterns; pattern++)
                {
                    for (int sector = 1; sector <= getNumberOfSectors(pattern); sector++)
                    {
                        value = dataLayer.getDistanceFromMeanInStandardDeviation(collection, pattern, sector);
                        filepath = "StandardDeviationData/Collection - " + collection + ".csv";


                        if (isItTheFirstCollection)
                        {
                            setRowHeadingsCSV(filepath, "Pattern, Sector, Distance from the mean in SD");
                            isItTheFirstCollection = false;
                        }

                        exportDataToCSV(filepath, pattern, sector, value);
                    }
                }

                isItTheFirstCollection = true;
            }
        }
        else
        {
            System.out.println("Error creating new directory - *Hint* did the directory already exist?");
        }
    }

    private void prepareIndexOfPerformanceDataToWriteToCSV()
    {
        double value = 0;
        boolean isItTheFirstSectorIOP = true;
        /* DISTANCE DRAWN CODE
        boolean isItTheFirstDistanceDrawn = true;
        */

        String filepath;
        int numberOfSectors;
        ArrayList<Double> listOfIOPValues = new ArrayList<>();
        double standardDeviation;
        double mean;

        // Creates a folder inside the project for Data CSV
        File fileIOP = new File("SectorIndexOfPerformanceData");

         /* DISTANCE DRAWN CODE
        File fileDrawnPath = new File("DistanceOfDrawnPathData");
        if (fileIOP.mkdir() && fileDrawnPath.mkdir())
        */

        if (fileIOP.mkdir())
        {
            for (int pattern = 1; pattern <= numberOfPatterns; pattern++)
            {
                for (int sector = 1; sector <= getNumberOfSectors(pattern); sector++)
                {
                    for (int collection = 1; collection <= numberOfCollections ; collection++)
                    {

                        value = dataLayer.getSectorIndexOfPerformance(collection, pattern, sector, "IndexOfPerformance");
                        filepath = "SectorIndexOfPerformanceData/Pattern - " + pattern + " - Sector - " + sector + ".csv";

                        if (isItTheFirstSectorIOP)
                        {
                            setRowHeadingsCSV(filepath, "Collection, Pattern, Sector, Index of Performance");
                            isItTheFirstSectorIOP = false;
                        }

                        exportDataToCSV(filepath, collection, pattern, sector, value);



                        /* DISTANCE DRAWN CODE
                        value = dataLayer.getSectorIndexOfPerformance(collection, pattern, sector, "DistanceOfDrawnPath");
                        filepath = "DistanceOfDrawnPathData/Pattern - " + pattern + " - Sector - " + sector + ".csv";

                        if (isItTheFirstDistanceDrawn)
                        {
                            setRowHeadingsCSV(filepath, "Distance of Drawn Path");
                            isItTheFirstDistanceDrawn = false;
                        }

                        exportDataToCSV(filepath, collection, pattern, sector, value);
                        */

                        listOfIOPValues.add(value);
                    }

                    // Gets the mean
                    mean = calculateMean(listOfIOPValues);

                    // Gets the standard deviation
                    standardDeviation = calculateSD(listOfIOPValues, mean);

                    //System.out.println("Pattern: " + pattern + " - Sector: " + sector + " - Standard Deviation: " + standardDeviation);

                    // Inserts the standard deviation into the db table sectorData
                    if (!dataLayer.storeSectorStandardDeviationAndMean(pattern, sector, standardDeviation, mean))
                    {
                        System.out.println("Error inserting standard deviation data");
                    }

                    /* DISTANCE DRAWN CODE
                    isItTheFirstDistanceDrawn = true;
                    */

                    isItTheFirstSectorIOP = true;
                }
            }

            System.out.println("Exported Data to CSV Files: Completed");
        }
        else
        {
            System.out.println("Error creating new directory - *Hint* did the directory already exist?");
        }
    }

    private void calculateUserSectorDistanceFromMeanInSD()
    {
        int numberOfSectors;
        double mean;
        double indexOfPerformance;
        double standardDeviation;


        for (int pattern = 1; pattern <= numberOfPatterns; pattern++)
        {
            for (int sector = 1; sector <= getNumberOfSectors(pattern); sector++)
            {
                // Get the Sector MEAN
                mean = dataLayer.getSectorMean(pattern, sector);
                //System.out.println("Mean: " + mean);


                // Get the Sector STANDARD DEVIATION
                standardDeviation = dataLayer.getStandardDeviation(pattern, sector);
                //System.out.println("SD: " + standardDeviation);


                for (int collection = 1; collection <= numberOfCollections ; collection++)
                {
                    // Get the user's Sector Index of Performance
                    indexOfPerformance = dataLayer.getSectorIndexOfPerformance(collection, pattern, sector, "IndexOfPerformance");

                    /*
                    // Calculate how far from the mean the IOP is
                    if (indexOfPerformance < mean)
                    {
                        difference = (mean - indexOfPerformance) / standardDeviation;
                    }
                    else if (indexOfPerformance > mean)
                    {
                        difference = (indexOfPerformance - mean) / standardDeviation;
                        //difference = 0;
                    }
                    else
                    {
                        difference = 0;
                    }
                    //System.out.println("Difference: " + difference);
                    */




                    double roundedDifference = roundADoubleValue((indexOfPerformance - mean) / standardDeviation);


                    // Insert data into the db
                    if (!dataLayer.storeUsersDistanceFromMeanInSD(collection, pattern, sector, roundedDifference))
                    {
                        System.out.println("Error adding the distance from mean in SD's to the DB for Collection: " + collection + "Pattern: " + pattern + " - Sector: " + sector);
                    }
                }
            }
        }

        System.out.println("Standard Deviation Calculations: Completed");
    }


    private double calculateMean(ArrayList<Double> listOfValues)
    {
        int length = listOfValues.size();
        double sum = 0.0;

        // retrieves each value in listOfValues and adds them to the sum
        for(int i = 0; i < length; i++)
        {
            sum = sum + listOfValues.get(i);
        }

        // Returns the mean


        return roundADoubleValue(sum / length);
    }

    private double calculateSD(ArrayList<Double> listOfValues, double mean)
    {
        int length = listOfValues.size();
        double standardDeviation = 0.0;

        // Retrieves each value in listOfValues, subtracts the mean then squares the value then adds it to the standardDeviation
        for (int j = 0; j < length; j++)
        {
            standardDeviation = standardDeviation + Math.pow((listOfValues.get(j) - mean), 2);
        }

        // Calculates the square root of standardDeviation divided by the number of values in the data set, then returns that value
        return roundADoubleValue(Math.sqrt(standardDeviation / length));

        // Bessel's correction not needed
        // return Math.sqrt(standardDeviation / (length - 1));
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

    private void exportDataToCSV(String filepath, int pattern, int sector, double value)
    {
        try
        {
            FileWriter fw = new FileWriter(filepath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println("" + pattern + ", " + sector + ", " + value);
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

            pw.println(value);
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
        for (int pattern = 1; pattern <= numberOfPatterns; pattern++)
        {
            ArrayList coords = dataLayer.getPatternCoords(pattern);

            isItTheFirstSector = true;

            for (int j = 0; j < coords.size(); j+=2)
            {
                // Allows for the casting from Integer to double
                double x = (double) (Integer) coords.get(j);
                double y = (double) (Integer) coords.get(j + 1);

                double distance = calculateTheDistance(x , y);
                double indexOfDifficulty = calculateTheIndexOfDifficulty(distance);
                double roundedIndexOfDifficulty = roundADoubleValue(indexOfDifficulty);

                String direction = ascertainTheDirection(y);

                if (!dataLayer.storeSectorIndexOfDifficultyAndDistanceAndDirection(pattern, (( j / 2 ) + 1), distance, roundedIndexOfDifficulty, direction))
                {
                    System.out.println("Error adding Index of Difficulty to the DB for Pattern: " + pattern + " - Sector: " + (( j / 2 ) + 1));
                    isSetupOk = false;
                }

                listOfIndexOfDifficulty.add((double) (Integer) pattern);
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

    /* DISTANCE DRAWN CODE
    private void setupDrawnPath()
    {
        ArrayList<Double> listOfDrawnPathSectorCoords;
        ArrayList<Double> listOfSectorCoords = new ArrayList<>();

        boolean isSetupOk = true;

        int numberOfSectors;

        // Adds each collection of Coords to a list
        for (int collection = 1; collection <= numberOfCollections; collection++)
        {
            for (int pattern = 1; pattern <= numberOfPatterns; pattern++)
            {
                isItTheFirstSector = true;

                for (int sector = 1; sector <= getNumberOfSectors(pattern); sector++)
                {
                    listOfDrawnPathSectorCoords = dataLayer.getDrawnPathCoords(collection, pattern, sector);

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

                    if (!dataLayer.storeUserDrawnPathDistance(collection, pattern, sector, totalDistanceDrawn))
                    {
                        System.out.println("Error adding distance drawn to the DB for Collection: " + collection + " - Pattern: " + pattern + " - Sector: " + sector);
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
    */



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

    private String ascertainTheDirection(double y)
    {
        String direction;

        if (isItTheFirstSector)
        {
            if(y < startingY)
            {
                direction = "UP";
            }
            else if (y > startingY)
            {
                direction = "DOWN";
            }
            else
            {
                direction = "LEVEL";
            }
        }
        else
        {
            if(y < oldY)
            {
                direction = "UP";
            }
            else if (y > oldY)
            {
                direction = "DOWN";
            }
            else
            {
                direction = "LEVEL";
            }
        }

        oldY = y;
        return direction;

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

        return roundADoubleValue(Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2)));
    }

    // Calculates the Index of Difficulty
    private double calculateTheIndexOfDifficulty(double Distance)
    {
        return roundADoubleValue (Math.log((2 * Distance) / targetWidth) / Math.log(2));
    }


    // Calculates the Index of Performance
    private double calculateTheIndexOfPerformance(double IndexDifficulty, double MovementTime)
    {
        //System.out.println("Index of Performance: " + IndexDifficulty / MovementTime);
        //return (IndexDifficulty / MovementTime);

        return roundADoubleValue(IndexDifficulty / MovementTime);
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

    /* TEST CODE
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
    */


}
