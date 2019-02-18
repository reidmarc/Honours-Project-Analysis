import java.text.DecimalFormat;
import java.util.ArrayList;

public class Calculations
{
    private final double startingX = 11;
    private final double startingY = 100;
    private final int targetWidth = 15;
    private double previousX;
    private double previousY;


    // Rounds to 5 decimal places
    public double roundADoubleValue(double value)
    {
        DecimalFormat newFormat = new DecimalFormat("#.#####");
        return Double.valueOf(newFormat.format(value));
    }


    // Calculates the Distance between two Co ordinates
    public double calculateTheDistance(double x, double y, boolean isItTheFirstSector)
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
    public double calculateTheIndexOfDifficulty(double Distance)
    {
        return roundADoubleValue (Math.log((2 * Distance) / targetWidth) / Math.log(2));
    }


    // Calculates the Index of Performance
    public double calculateTheIndexOfPerformance(double IndexDifficulty, double MovementTime)
    {
        return roundADoubleValue(IndexDifficulty / MovementTime);
    }


    // Calculates the mean
    public double calculateMean(ArrayList<Double> listOfValues)
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


    // Calculates the Standard Deviation
    public double calculateSD(ArrayList<Double> listOfValues, double mean)
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


    // Calculates how far from the mean each users sector is in terms of IoP
    public void calculateUserSectorDistanceFromMeanInSD(int numberOfCollections, int numberOfPatterns, DATA_Layer dataLayer)
    {
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


    // Returns the number of sectors in a pattern
    public int getNumberOfSectors(int pattern)
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


    public double getStartingX()
    {
        return startingX;
    }

    public double getStartingY()
    {
        return startingY;
    }
}
