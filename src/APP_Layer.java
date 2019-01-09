import java.util.ArrayList;

public class APP_Layer implements APP_Layer_Interface
{
    // The underlying data layer this application layer sits upon
    private DATA_Layer dataLayer;

    private final int targetWidth = 5;
    private final double startingX = 11;
    private final double startingY = 100;
    private final int numberOfUsers = 11; // Query the collection table for total number of collections?

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
        setupDatabase();


        setupIndexOfDifficulty(); // Step 1

        setupIndexOfPerformance(); // Step 2
    }





    private void setupIndexOfDifficulty()
    {
        // Gets the number of patterns stored in the Database
        int noOfPatterns = dataLayer.getNumberOfPatterns();

        // A FOR loop to iterate through each patterns coords
        for (int i = 1; i < (noOfPatterns + 1); i++)
        {
            ArrayList coords = dataLayer.getPatternCoords(i);


            //System.out.println("Pattern No: " + i);

            isItTheFirstSector = true;

            for (int j = 0; j < coords.size(); j+=2)
            {
                //System.out.println("X: " + coords.get(j));
                //System.out.println("Y: " + coords.get(j + 1));

                // Allows for the casting from Integer to double
                double x = (double) (Integer) coords.get(j);
                double y = (double) (Integer) coords.get(j + 1);

                double distance = calculateTheDistance(x , y);
                double indexOfDifficulty = calculateTheIndexOfDifficulty(distance);



                if (dataLayer.storePatternIndexOfDifficulty(i, (( j / 2 ) + 1), distance, indexOfDifficulty))
                {
                    //System.out.println("Index of Difficulty SUCCESSFULLY added to the DB");
                }
                else
                {
                    //System.out.println("Index of Difficulty UNSUCCESSFULLY added to the DB");
                }

                listOfIndexOfDifficulty.add((double) (Integer) i);
                listOfIndexOfDifficulty.add((double) (Integer) (( j / 2 ) + 1));
                listOfIndexOfDifficulty.add(indexOfDifficulty);

                isItTheFirstSector = false;
            }
        }
    }



    private void setupIndexOfPerformance()
    {
        for (int i = 1; i <= numberOfUsers; i++)
        {
            listOfTimings.add(dataLayer.getSectorTimes(i));
        }

        /*
        // TESTING
        for (int z = 0; z < listOfTimings.size(); z++)
        {
            for (int y = 0; y < listOfTimings.get(z).size(); y++)
            {
                System.out.println("* " + listOfTimings.get(z).get(y));
                System.out.println("& " + listOfIndexOfDifficulty.get(y));

            }
        }


        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("---------------------------------------------------------------------------------");


        for (int x = 0; x < listOfIndexOfDifficulty.size(); x++)
        {

            System.out.println(listOfIndexOfDifficulty.get(x));
        }
        */

        for (int z = 0; z < listOfTimings.size(); z++)
        {
            for (int y = 0; y < listOfTimings.get(z).size(); y = y + 3)
            {
                /*
                System.out.println("Pattern Number: " + listOfTimings.get(z).get(y));
                System.out.println("Pattern Number: " + listOfIndexOfDifficulty.get(y));

                System.out.println("Sector Number: " + listOfTimings.get(z).get(y + 1));
                System.out.println("Sector Number: " + listOfIndexOfDifficulty.get(y + 1));

                System.out.println("Timing: " + listOfTimings.get(z).get(y + 2));
                System.out.println("IOD: " + listOfIndexOfDifficulty.get(y + 2));
                */

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

            listOfListOfIndexOfPerformance.add(new ArrayList<>(listOfIndexOfPerformance));
            listOfIndexOfPerformance.clear();
        }






        for (int i = 0; i < listOfListOfIndexOfPerformance.size(); i++)
        {
            for (int j = 0; j < listOfListOfIndexOfPerformance.get(i).size(); j = j + 3)
            {
                System.out.println("Collection: " + (i + 1));
                System.out.println("Pattern: " + listOfListOfIndexOfPerformance.get(i).get(j));
                System.out.println("Sector: " + listOfListOfIndexOfPerformance.get(i).get(j + 1));
                System.out.println("IoP: " + listOfListOfIndexOfPerformance.get(i).get(j + 2));

                if (dataLayer.storeUserIndexOfPerformance((i + 1), listOfListOfIndexOfPerformance.get(i).get(j).intValue(), listOfListOfIndexOfPerformance.get(i).get(j + 1).intValue(), listOfListOfIndexOfPerformance.get(i).get(j + 2)))
                {
                    // ADDED

                }
                else
                {
                    // NOT ADDED
                }

            }
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
            System.out.println("Database Setup SUCCESSFUL");

        }
        else
        {
            System.out.println("Database Setup UNSUCCESSFUL");
        }
    }

}
