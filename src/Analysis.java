import java.util.ArrayList;

public class Analysis
{
    private int totalNumberOfSectors = 108;

    // Counters
    private int upCounter = 0;
    private int downCounter = 0;
    private int levelCounter = 0;
    private int upCounterAboveTheMean = 0;
    private int downCounterAboveTheMean = 0;
    private int levelAboveCounter = 0;
    private int upCounterBelowTheMean = 0;
    private int downCounterBelowTheMean = 0;
    private int levelBelowCounter = 0;
    private int greaterThan2SDCounter = 0;
    private int greaterThan1halfSDCounter = 0;
    private int greaterThan1SDCounter = 0;
    private int greaterThan2SDCounterNeg = 0;
    private int greaterThan1halfSDCounterNeg = 0;
    private int greaterThan1SDCounterNeg = 0;
    private int withinSDCounter = 0;


    private ArrayList<Double> listOfInterestingResults = new ArrayList<>();


    // Perform analysis
    public void standardDeviationAnalysis(int numberOfCollections, int numberOfPatterns, Calculations calculations, DATA_Layer dataLayer)
    {
        double value = 0;

        double distanceFromMeanInStandardDeviationTotal = 0;
        double roundedValue = 0;

        for (int collection = 1; collection <= numberOfCollections; collection++)
        {

            for (int pattern = 1; pattern <= numberOfPatterns; pattern++)
            {
                for (int sector = 1; sector <= calculations.getNumberOfSectors(pattern); sector++)
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
            System.out.println( "\n\nCollection: " + listOfInterestingResults.get(i) +
                                "\nPattern: " + listOfInterestingResults.get(i + 1) +
                                "\nSector: " + listOfInterestingResults.get(i + 2) +
                                "\nValue: " + listOfInterestingResults.get(i + 3) +
                                "\n----------------------------------------");
        }


        for (int pattern = 1; pattern <= numberOfPatterns; pattern++)
        {
            for (int sector = 1; sector <= calculations.getNumberOfSectors(pattern); sector++)
            {

                for (int collection = 1; collection <= numberOfCollections; collection++)
                {
                    distanceFromMeanInStandardDeviationTotal = distanceFromMeanInStandardDeviationTotal + dataLayer.getDistanceFromMeanInStandardDeviation(collection, pattern, sector);

                    roundedValue = calculations.roundADoubleValue(distanceFromMeanInStandardDeviationTotal);
                }

                if (!dataLayer.storeTotalStandardDeviationScore(pattern, sector, roundedValue))
                {
                    System.out.println("Error storing total standard deviation score for Pattern: " + pattern + " - Sector:" + sector + " - SD Score: " + roundedValue);
                }

                distanceFromMeanInStandardDeviationTotal = 0;
            }
        }
    }


    public void sectorDirectionAndScoreAnalysis(int numberOfPatterns, Calculations calculations, DATA_Layer dataLayer)
    {
        for (int pattern = 1; pattern <= numberOfPatterns; pattern = pattern + 2)
        {
            for (int sector = 1; sector <= calculations.getNumberOfSectors(pattern); sector++)
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

        System.out.println("Totals for Direction of Sector which users performed below the mean" +
                "\nUP: " + upCounter +
                "\nDOWN: " + downCounter +
                "\nLEVEL: " + levelCounter);
    }


    public void directionOfSectorForDistanceFromTheMeanInStandardDeviationAnalysis(int numberOfCollections, int numberOfPatterns, Calculations calculations, DATA_Layer dataLayer)
    {
        double distanceFromTheMean = 0.0;
        String direction = "";


        for (int collection = 1; collection <= numberOfCollections; collection++)
        {
            for (int pattern = 1; pattern <= numberOfPatterns; pattern++)
            {
                for (int sector = 1; sector <= calculations.getNumberOfSectors(pattern); sector++)
                {
                    // GET USERS DISTANCE FROM THE MEAN IN SD VALUE
                    distanceFromTheMean = dataLayer.getDistanceFromMeanInStandardDeviation(collection, pattern, sector);

                    //GET THE DIRECTION
                    direction = dataLayer.getSectorDirection(pattern, sector);

                    if (direction.equals("UP"))
                    {
                        if (distanceFromTheMean < 0)
                        {
                            upCounterBelowTheMean = upCounterBelowTheMean + 1;
                        }
                        else if (distanceFromTheMean > 0)
                        {
                            upCounterAboveTheMean = upCounterAboveTheMean + 1;
                        }
                        else
                        {
                            // Value is equal to the SD, therefore not interesting
                        }

                    }
                    else if (direction.equals("DOWN"))
                    {
                        if (distanceFromTheMean < 0)
                        {
                            downCounterBelowTheMean = downCounterBelowTheMean + 1;
                        }
                        else if (distanceFromTheMean > 0)
                        {
                            downCounterAboveTheMean = downCounterAboveTheMean + 1;
                        }
                        else
                        {
                            // Value is equal to the SD, therefore not interesting
                        }

                    }
                    else
                    {
                        if (distanceFromTheMean < 0)
                        {
                            levelBelowCounter = levelBelowCounter + 1;
                        }
                        else if (distanceFromTheMean > 0)
                        {
                            levelAboveCounter = levelAboveCounter + 1;
                        }
                        else
                        {
                            // Value is equal to the SD, therefore not interesting
                        }
                    }
                }
            }

            // Checks that all values are accounted for
            if ( (upCounterAboveTheMean + downCounterAboveTheMean + levelAboveCounter + upCounterBelowTheMean + downCounterBelowTheMean + levelBelowCounter) == totalNumberOfSectors)
            {
                System.out.println("\nCollection: " + collection +
                        "\nTotal sectors above the mean in the UP direction: " + upCounterAboveTheMean +
                        "\nTotal sectors above the mean in the DOWN direction: " + downCounterAboveTheMean +
                        "\nTotal sectors above the mean in the LEVEL direction: " + levelAboveCounter +

                        "\nTotal sectors below the mean in the UP direction: " + upCounterBelowTheMean +
                        "\nTotal sectors below the mean in the DOWN direction: " + downCounterBelowTheMean +
                        "\nTotal sectors below the mean in the LEVEL direction: " + levelBelowCounter);
            }

            // Resets counters to 0
            upCounterAboveTheMean = downCounterAboveTheMean = levelAboveCounter = upCounterBelowTheMean = downCounterBelowTheMean = levelBelowCounter = 0;
        }
    }
}
