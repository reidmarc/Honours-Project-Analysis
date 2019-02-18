import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class ExportData
{

    public void exportDataToCSV(String filepath, int collection, int pattern, int sector, double value)
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
            handleExceptions(ex);
        }
    }

    public void exportDataToCSV(String filepath, double value)
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
            handleExceptions(ex);
        }
    }

    public void exportDataToCSV(String filepath, int pattern, int sector, double value)
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
            handleExceptions(ex);
        }
    }

    public void setRowHeadingsCSV(String filepath, String value)
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
            handleExceptions(ex);
        }
    }

    private void handleExceptions(Exception ex)
    {
        System.out.println("CSV File NOT saved");
        System.out.println(ex);
        ex.printStackTrace();
        System.exit(-1);
    }



}
