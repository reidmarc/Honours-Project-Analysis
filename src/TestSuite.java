import org.junit.*;

import java.util.ArrayList;

public class TestSuite
{
    private Calculations calculations = new Calculations();
    private double returnedValue = 0.0;

    ArrayList<Double> meanAndStandardDevTestValues1 = new ArrayList<>();
    ArrayList<Double> meanAndStandardDevTestValues2 = new ArrayList<>();
    ArrayList<Double> meanAndStandardDevTestValues3 = new ArrayList<>();
    ArrayList<Double> meanAndStandardDevTestValues4 = new ArrayList<>();
    ArrayList<ArrayList<Double>> listOfListOfMeanAndStandardDevTestValues = new ArrayList<>();
    ArrayList<Double> meanTestAnswers = new ArrayList<>();
    ArrayList<Double> standardDevTestAnswers = new ArrayList<>();

    String status;
    boolean meanAndStandardDevSetupComplete = false;


    private void meanAndStandardDevTestValuesSetup()
    {
        meanAndStandardDevTestValues1.add(2.00);
        meanAndStandardDevTestValues1.add(4.00);
        meanAndStandardDevTestValues1.add(6.00);
        meanAndStandardDevTestValues1.add(8.00);
        meanTestAnswers.add(5.00);
        standardDevTestAnswers.add(2.23607);


        meanAndStandardDevTestValues2.add(-2.00);
        meanAndStandardDevTestValues2.add(-4.00);
        meanAndStandardDevTestValues2.add(6.00);
        meanAndStandardDevTestValues2.add(8.00);
        meanTestAnswers.add(2.00);
        standardDevTestAnswers.add(5.09902);


        meanAndStandardDevTestValues3.add(5.27384);
        meanAndStandardDevTestValues3.add(15.09867);
        meanAndStandardDevTestValues3.add(44.77680);
        meanAndStandardDevTestValues3.add(1223.47586);
        meanAndStandardDevTestValues3.add(0.0200001);
        meanAndStandardDevTestValues3.add(0.000003);
        meanTestAnswers.add(214.77420);
        standardDevTestAnswers.add(451.36574);


        meanAndStandardDevTestValues4.add(-5.27384);
        meanAndStandardDevTestValues4.add(15.09867);
        meanAndStandardDevTestValues4.add(44.77680);
        meanAndStandardDevTestValues4.add(-1223.47586);
        meanAndStandardDevTestValues4.add(0.0200001);
        meanAndStandardDevTestValues4.add(-0.000003);
        meanTestAnswers.add(-194.80904);
        standardDevTestAnswers.add(460.33512);


        listOfListOfMeanAndStandardDevTestValues.add(meanAndStandardDevTestValues1);
        listOfListOfMeanAndStandardDevTestValues.add(meanAndStandardDevTestValues2);
        listOfListOfMeanAndStandardDevTestValues.add(meanAndStandardDevTestValues3);
        listOfListOfMeanAndStandardDevTestValues.add(meanAndStandardDevTestValues4);

        meanAndStandardDevSetupComplete = true;
    }

        @Test
        public void roundADoubleValue()
        {
            ArrayList<Double> testValues = new ArrayList<>();
            ArrayList<Double> testAnswers = new ArrayList<>();

            testValues.add(1.2345678910111213);
            testAnswers.add(1.23457);
            testValues.add(-1.2345678910111213);
            testAnswers.add(-1.23457);
            testValues.add(345.758697666);
            testAnswers.add(345.75870);
            testValues.add(-345.758697666);
            testAnswers.add(-345.75870);
            testValues.add(1.000000000000001);
            testAnswers.add(1.0);
            testValues.add(-1.000000000000001);
            testAnswers.add(-1.0);

            System.out.println("\nroundADoubleValue() Test Results");

            for (int i = 0; i < testAnswers.size(); i++)
            {
                returnedValue = calculations.roundADoubleValue(testValues.get(i));

                if (returnedValue == testAnswers.get(i))
                {
                    System.out.println("[Passed] Expected Result: " + testAnswers.get(i) +
                            " Actual Result: " + returnedValue );
                }
                else
                {
                    System.out.println("\n[Passed] Expected Result: " + testAnswers.get(i) +
                            " Actual Result: " + returnedValue+ "\n");
                    Assert.assertTrue(false);
                }
            }
            System.out.println("=============================================================");
        }



        @Test
        public void calculateTheDistance()
        {
            ArrayList<Double> testValuesX = new ArrayList<>();
            ArrayList<Double> testValuesY = new ArrayList<>();
            ArrayList<Double> testAnswers = new ArrayList<>();

            testValuesX.add(0.0);
            testValuesY.add(0.0);
            testAnswers.add(0.0);

            testValuesX.add(3.0);
            testValuesY.add(4.0);
            testAnswers.add(5.0);

            testValuesX.add(55.0);
            testValuesY.add(45.0);
            testAnswers.add(66.21933);

            testValuesX.add(14567.0);
            testValuesY.add(34445.0);
            testAnswers.add(37335.74887);

            testValuesX.add(-3.0);
            testValuesY.add(-4.0);
            testAnswers.add(37403.45574);

            testValuesX.add(-55.55555);
            testValuesY.add(44.44444);
            testAnswers.add(71.47692);

            testValuesX.add(0.0000003);
            testValuesY.add(-0.209890);
            testAnswers.add(71.27712);


            System.out.println("\ncalculateTheDistance() Test Results");

            for (int i = 1; i < testAnswers.size(); i++)
            {
                returnedValue = calculations.calculateTheDistance(testValuesX.get(i),testValuesY.get(i), false);

                if (returnedValue == testAnswers.get(i))
                {
                        System.out.println("[Passed] Expected Result: " + testAnswers.get(i) + " Actual Result: " + returnedValue + " [Input Values - XY1: " + testValuesX.get(i - 1) + ", " + testValuesY.get(i - 1) + " - XY2: " + testValuesX.get(i) + ", " + + testValuesY.get(i) + "]");
                }
                else
                {
                    System.out.println("[Failed] Expected Result: " + testAnswers.get(i) + " Actual Result: " + returnedValue + " [Input Values - XY1: " + testValuesX.get(i - 1) + ", " + testValuesY.get(i - 1) + " - XY2: " + testValuesX.get(i) + ", " + + testValuesY.get(i) + "]");
                    Assert.assertTrue(false);
                }
            }
            System.out.println("============================================================================================");
        }

        @Test
        public void calculateTheIndexOfDifficulty()
        {
            ArrayList<Double> testValues = new ArrayList<>();
            ArrayList<Double> testAnswers = new ArrayList<>();

            testValues.add(2.0);
            testAnswers.add(-1.90689);

            testValues.add(123.0);
            testAnswers.add(4.03562);

            testValues.add(594857.0);
            testAnswers.add(16.27529);

            testValues.add(1.958697);
            testAnswers.add(-1.93700);

            testValues.add(-2.0);
            testAnswers.add(0.0);

            testValues.add(-123.0);
            testAnswers.add(0.0);

            System.out.println("\ncalculateTheIndexOfDifficulty() Test Results");

            for (int i = 0; i < testAnswers.size(); i++)
            {
                returnedValue = calculations.calculateTheIndexOfDifficulty(testValues.get(i));

                if (returnedValue == testAnswers.get(i))
                {
                    if (Double.compare(testValues.get(i), 0.0) < 0)
                    {
                            System.out.println("[Passed] Expected Result: " + testAnswers.get(i) + " Actual Result: " + returnedValue + " [Input Values - Distance: " + testValues.get(i) + "] The distance cannot be negative, thus the method returns 0.0");
                    }
                    else
                    {
                            System.out.println("[Passed] Expected Result: " + testAnswers.get(i) + " Actual Result: " + returnedValue + " [Input Values - Distance: " + testValues.get(i) + "] The target width is 15.");
                    }
                }
                else
                {
                        System.out.println("[Failed] Expected Result: " + testAnswers.get(i) + " Actual Result: " + returnedValue + " [Input Values - Distance: " + testValues.get(i) + "] The target width is 15.");
                        Assert.assertTrue(false);
                }
            }

            System.out.println("============================================================================================");
        }


        @Test
        public void calculateTheIndexOfPerformance()
        {
            ArrayList<Double> testValuesIndexOfDifficulty = new ArrayList<>();
            ArrayList<Double> testValuesMovementTime = new ArrayList<>();
            ArrayList<Double> testAnswers = new ArrayList<>();


            testValuesIndexOfDifficulty.add(100.0);
            testValuesMovementTime.add(10.0);
            testAnswers.add(10.0);

            testValuesIndexOfDifficulty.add(3.6475);
            testValuesMovementTime.add(22.485);
            testAnswers.add(0.16222);

            testValuesIndexOfDifficulty.add(9.748596);
            testValuesMovementTime.add(61.00000003);
            testAnswers.add(0.15981);

            testValuesIndexOfDifficulty.add(-100.0);
            testValuesMovementTime.add(10.0);
            testAnswers.add(-10.0);

            testValuesIndexOfDifficulty.add(3.6475);
            testValuesMovementTime.add(-22.485);
            testAnswers.add(0.0);

            testValuesIndexOfDifficulty.add(9.748596);
            testValuesMovementTime.add(-61.00000003);
            testAnswers.add(0.0);



            System.out.println("\ncalculateTheIndexOfPerformance() Test Results");

            for (int i = 0; i < testAnswers.size(); i++)
            {
                returnedValue = calculations.calculateTheIndexOfPerformance(testValuesIndexOfDifficulty.get(i), testValuesMovementTime.get(i));

                if (returnedValue == testAnswers.get(i))
                {
                    if (Double.compare(testValuesMovementTime.get(i), 0.0) < 0)
                    {
                            System.out.println("[Passed] Expected Result: " + testAnswers.get(i) + " Actual Result: " + returnedValue + " [Input Values - IoD: " + testValuesIndexOfDifficulty.get(i) + " - MT: " + testValuesMovementTime.get(i) + "] MT cannot be negative, thus the method returns 0.0");
                    }
                    else
                    {
                            System.out.println("[Passed] Expected Result: " + testAnswers.get(i) + " Actual Result: " + returnedValue + " [Input Values - IoD: " + testValuesIndexOfDifficulty.get(i) + " - MT: " + testValuesMovementTime.get(i) + "]");
                    }
                }
                else
                {
                        System.out.println("[Failed] Expected Result: " + testAnswers.get(i) + " Actual Result: " + returnedValue + " [Input Values - IoD: " + testValuesIndexOfDifficulty.get(i) + " - MT: " + testValuesMovementTime.get(i) + "]");
                        Assert.assertTrue(false);
                }
            }

            System.out.println("============================================================================================");
        }


        @Test
        public void calculateMean()
        {
            if (!meanAndStandardDevSetupComplete)
            {
                meanAndStandardDevTestValuesSetup();
            }

            System.out.println("\ncalculateMean() Test Results");

            for (int i = 0; i < listOfListOfMeanAndStandardDevTestValues.size(); i++)
            {
                returnedValue = calculations.calculateMean(listOfListOfMeanAndStandardDevTestValues.get(i));

                if (returnedValue == meanTestAnswers.get(i))
                {
                    status = "Passed";
                }
                else
                {
                    status = "Failed";
                }

                String output = "[" + status + "] Expected Result: " + meanTestAnswers.get(i) + " Actual Result: " + returnedValue + " [Input Values: " + listOfListOfMeanAndStandardDevTestValues.get(i).get(0) + ", " + listOfListOfMeanAndStandardDevTestValues.get(i).get(1) + ", " + listOfListOfMeanAndStandardDevTestValues.get(i).get(2) + ", " + listOfListOfMeanAndStandardDevTestValues.get(i).get(3);


                if (listOfListOfMeanAndStandardDevTestValues.get(i).size() == 4)
                {
                    System.out.println(output + "]");
                }
                else
                {
                    System.out.println(output + ", " + listOfListOfMeanAndStandardDevTestValues.get(i).get(4) + ", " + listOfListOfMeanAndStandardDevTestValues.get(i).get(5) + "]");
                }

                if (status.equals("Failed"))
                {
                    Assert.assertTrue(false);
                }
            }

            System.out.println("============================================================================================");
        }



        @Test
        public void calculateSD()
        {

            if (!meanAndStandardDevSetupComplete)
            {
                meanAndStandardDevTestValuesSetup();
            }

            System.out.println("\ncalculateSD() Test Results");

            for (int i = 0; i < listOfListOfMeanAndStandardDevTestValues.size(); i++)
            {
                returnedValue = calculations.calculateSD(listOfListOfMeanAndStandardDevTestValues.get(i), meanTestAnswers.get(i));

                if (returnedValue == standardDevTestAnswers.get(i))
                {
                    status = "Passed";
                }
                else
                {
                    status = "Failed";
                }

                String output = "[" + status + "] Expected Result: " + standardDevTestAnswers.get(i) + " Actual Result: " + returnedValue + " [Input Values: " + listOfListOfMeanAndStandardDevTestValues.get(i).get(0) + ", " + listOfListOfMeanAndStandardDevTestValues.get(i).get(1) + ", " + listOfListOfMeanAndStandardDevTestValues.get(i).get(2) + ", " + listOfListOfMeanAndStandardDevTestValues.get(i).get(3);


                if (listOfListOfMeanAndStandardDevTestValues.get(i).size() == 4)
                {
                    System.out.println(output + "]");
                }
                else
                {
                    System.out.println(output + ", " + listOfListOfMeanAndStandardDevTestValues.get(i).get(4) + ", " + listOfListOfMeanAndStandardDevTestValues.get(i).get(5) + "]");
                }

                if (status.equals("Failed"))
                {
                    Assert.assertTrue(false);
                }
            }

            System.out.println("============================================================================================");
        }
}