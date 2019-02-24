import java.util.ArrayList;

public interface DATA_Layer_Interface
{
    boolean addNewDatabaseTables();

    ArrayList getSectorTimes(int collection);

    double getStandardDeviation(int pattern, int sector);

    int getCollectionsUserNo(int collection);

    double getDistanceFromMeanInStandardDeviation(int collection, int pattern, int sector);

    double getSectorMean(int pattern, int sector);

    double getTotalStandardDeviationScore(int pattern, int sector);

    String getSectorDirection(int pattern, int sector);

    double getSectorIndexOfPerformance(int collection, int pattern, int sector, String column);

    int getNumberOfCollections();

    int getNumberOfPatterns();

    ArrayList getPatternCoords(int patternNo);

    boolean storeTotalStandardDeviationScore(int pattern, int sector, double totalStandardDeviationScore);

    boolean storeUserIndexOfPerformance(int collection, int pattern, int sector, double indexOfPerformance);

    boolean storeSectorIndexOfDifficultyAndDistanceAndDirection(int pattern, int sector, double distance, double indexOfDifficulty, String direction);

    boolean storeUsersDistanceFromMeanInSD(int collection, int pattern, int sector, double distance);

    boolean storeSectorStandardDeviationAndMean(int pattern, int sector, double standardDeviation, double mean);



}
