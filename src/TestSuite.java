import org.junit.*;

import java.util.ArrayList;

public class TestSuite
{
        private Calculations calculations = new Calculations();
        private double returnedValue = 0.0;



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
                                System.out.println("[Passed] - The value: " + testValues.get(i) + " when rounded, does equal: " + testAnswers.get(i));
                        }
                        else
                        {
                                System.out.println("\n[Failed] - The value: " + testValues.get(i) + " when rounded, does NOT equal: " + testAnswers.get(i) + "\n");
                                Assert.assertTrue(false);
                        }
                }
                System.out.println("============================================================================================");
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
                                System.out.println("[Passed] - The distance from A: (" + testValuesX.get(i - 1) + ", " + testValuesY.get(i - 1) + ") to B: (" + testValuesX.get(i) + ", " + + testValuesY.get(i) + ") equals: " + testAnswers.get(i) + " when rounded.");
                        }
                        else
                        {
                                System.out.println("\n[Passed] - The distance from A: (" + testValuesX.get(i - 1) + ", " + testValuesY.get(i - 1) + ") to B: (" + testValuesX.get(i) + ", " + + testValuesY.get(i) + ") does NOT equal: " + returnedValue + " when rounded.\n");
                                Assert.assertTrue(false);

                        }
                }
                System.out.println("============================================================================================");
        }

        @Test
        public void calculateTheIndexOfDifficulty()
        {
                //targetWidth = 15;
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
                                        System.out.println("[Passed] - As the test value of: " + testValues.get(i) + " is negative the Index of Difficulty cannot be calculated, thus the method is working as intended.");
                                }
                                else
                                {
                                        System.out.println("[Passed] - The Index of Difficulty - when the distance is: " + testValues.get(i) + " and the target width is 15, does equal: " + testAnswers.get(i) + " when rounded.");
                                }
                        }
                        else
                        {
                                System.out.println("[Failed] - The Index of Difficulty - when the distance is: " + testValues.get(i) + " and the target width is 15, does NOT equal: " + returnedValue + " when rounded.");
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
                                        System.out.println("[Passed] - As the Movement Time value of: " + testValuesMovementTime.get(i) + " is negative and negative time is impossible, thus the method is working as intended.");
                                }
                                else
                                {
                                        System.out.println("[Passed] - The Index of Performance - when the Index of Difficulty is: " + testValuesIndexOfDifficulty.get(i) + " and the Movement Time is: " + testValuesMovementTime.get(i) + " does equal: " + testAnswers.get(i) + " when rounded.");
                                }
                        }
                        else
                        {
                                System.out.println("[Failed] - The Index of Performance - when the Index of Difficulty is: " + testValuesIndexOfDifficulty.get(i) + " and the Movement Time is: " + testValuesMovementTime.get(i) + " does NOT equal: " + returnedValue + " when rounded.");
                                Assert.assertTrue(false);
                        }
                }

                System.out.println("============================================================================================");


        }


        /*@Test
        public void calculateMean()
        {




        }



        @Test
        public void calculateSD()
        {




        }


        @Test
        public void calculateUserSectorDistanceFromMeanInSD()
        {




        }*/
}