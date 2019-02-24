import java.io.File;
import java.util.ArrayList;

public class APP_Layer implements APP_Layer_Interface
{
    // The underlying data layer this application layer sits upon
    private DATA_Layer dataLayer;

    private Calculations calculations;
    private Analysis analysis;
    private ExportData exportData;

    private int numberOfCollections;
    private int numberOfPatterns;

    private boolean isItTheFirstSector = true;

    private double oldY;



    private ArrayList<ArrayList<Double>> listOfTimings = new ArrayList<>();
    private ArrayList<Double> listOfIndexOfDifficulty = new ArrayList<>();

    private ArrayList<Double> listOfIndexOfPerformance = new ArrayList<>();
    private ArrayList<ArrayList<Double>> listOfListOfIndexOfPerformance = new ArrayList<>();

    public APP_Layer(DATA_Layer dataLayer)
    {
        this.dataLayer = dataLayer;
    }



    public void initialStartup()
    {
        calculations = new Calculations();
        analysis = new Analysis();
        exportData = new ExportData();


        setupDatabase();

        retrieveNumberOfCollectionsValue();

        retrieveNumberOfPatternsValue();

        setupIndexOfDifficulty();

        setupIndexOfPerformance();

        prepareIndexOfPerformanceDataToWriteToCSV();

        calculations.calculateUserSectorDistanceFromMeanInSD(numberOfCollections, numberOfPatterns, dataLayer);

        prepareStandardDeviationDataToWriteToCSV();

        analysis.standardDeviationAnalysis(numberOfCollections, numberOfPatterns, calculations, dataLayer);

        analysis.sectorDirectionAndScoreAnalysis(numberOfPatterns, calculations, dataLayer);

        analysis.directionOfSectorForDistanceFromTheMeanInStandardDeviationAnalysis(numberOfCollections, numberOfPatterns, calculations, dataLayer);
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
                    for (int sector = 1; sector <= calculations.getNumberOfSectors(pattern); sector++)
                    {
                        value = dataLayer.getDistanceFromMeanInStandardDeviation(collection, pattern, sector);
                        filepath = "StandardDeviationData/Collection - " + collection + ".csv";


                        if (isItTheFirstCollection)
                        {
                            exportData.setRowHeadingsCSV(filepath, "Pattern, Sector, Distance from the mean in SD");
                            isItTheFirstCollection = false;
                        }

                        exportData.exportDataToCSV(filepath, pattern, sector, value);
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
        boolean isItTheFirstSectorIOPAllInOneFile = true;
        boolean isItTheFirstSectorIOPCollection = true;



        String filepath;
        String filepathAllInOneFile;
        String filepathCollection;
        ArrayList<Double> listOfIOPValues = new ArrayList<>();
        double standardDeviation;
        double mean;

        // Creates a folder inside the project for Data CSV
        File fileIOP = new File("[Sector]IndexOfPerformanceData");
        File fileIOPAllInOneFile = new File("[AllInOneFile]IndexOfPerformanceData");
        File fileIOPCollection = new File("[Collection]IndexOfPerformanceData");



        if (fileIOP.mkdir() && fileIOPAllInOneFile.mkdir())
        {
            for (int pattern = 1; pattern <= numberOfPatterns; pattern++)
            {
                for (int sector = 1; sector <= calculations.getNumberOfSectors(pattern); sector++)
                {
                    for (int collection = 1; collection <= numberOfCollections ; collection++)
                    {

                        value = dataLayer.getSectorIndexOfPerformance(collection, pattern, sector, "IndexOfPerformance");
                        filepath = "[Sector]IndexOfPerformanceData/Pattern - " + pattern + " - Sector - " + sector + ".csv";

                        if (isItTheFirstSectorIOP)
                        {
                            exportData.setRowHeadingsCSV(filepath, "Collection, Pattern, Sector, Index of Performance");
                            isItTheFirstSectorIOP = false;
                        }

                        exportData.exportDataToCSV(filepath, collection, pattern, sector, value);


                        filepathAllInOneFile = "[AllInOneFile]IndexOfPerformanceData/Data.csv";

                        if (isItTheFirstSectorIOPAllInOneFile)
                        {
                            exportData.setRowHeadingsCSV(filepathAllInOneFile, "IndexOfPerformance");
                            isItTheFirstSectorIOPAllInOneFile = false;
                        }

                        exportData.exportDataToCSV(filepathAllInOneFile, value);

                        listOfIOPValues.add(value);
                    }

                    // Gets the mean
                    mean = calculations.calculateMean(listOfIOPValues);

                    // Gets the standard deviation
                    standardDeviation = calculations.calculateSD(listOfIOPValues, mean);

                    //System.out.println("Pattern: " + pattern + " - Sector: " + sector + " - Standard Deviation: " + standardDeviation);

                    // Inserts the standard deviation into the db table sectorData
                    if (!dataLayer.storeSectorStandardDeviationAndMean(pattern, sector, standardDeviation, mean))
                    {
                        System.out.println("Error inserting standard deviation data");
                    }



                    isItTheFirstSectorIOP = true;
                }
            }

            System.out.println("Exported Data to CSV Files: Completed");
        }
        else
        {
            System.out.println("Error creating new directory - *Hint* did the directory already exist?");
        }

        if(fileIOPCollection.mkdir())
        {
            for (int collection = 1; collection <= numberOfCollections; collection++)
            {
                for (int pattern = 1; pattern <= numberOfPatterns; pattern++)
                {
                    for (int sector = 1; sector <= calculations.getNumberOfSectors(pattern); sector++)
                    {
                        value = dataLayer.getSectorIndexOfPerformance(collection, pattern, sector, "IndexOfPerformance");

                        filepathCollection = "[Collection]IndexOfPerformanceData/Collection - " + collection + ".csv";

                        if (isItTheFirstSectorIOPCollection)
                        {
                            exportData.setRowHeadingsCSV(filepathCollection, "Pattern, Sector, Index of Performance");
                            isItTheFirstSectorIOPCollection = false;
                        }

                        exportData.exportDataToCSV(filepathCollection, pattern, sector, value);


                    }
                }

                isItTheFirstSectorIOPCollection = true;
            }
        }
        else
        {
            System.out.println("Error creating new directory - *Hint* did the directory already exist?");
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

                double distance = calculations.calculateTheDistance(x , y, isItTheFirstSector);
                double indexOfDifficulty = calculations.calculateTheIndexOfDifficulty(distance);
                double roundedIndexOfDifficulty = calculations.roundADoubleValue(indexOfDifficulty);

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
                    listOfIndexOfPerformance.add(calculations.calculateTheIndexOfPerformance(listOfIndexOfDifficulty.get(y + 2), listOfTimings.get(z).get(y + 2)));
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
            if(y < calculations.getStartingY())
            {
                direction = "UP";
            }
            else if (y > calculations.getStartingY())
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
