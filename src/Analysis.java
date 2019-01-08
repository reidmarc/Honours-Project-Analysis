public class Analysis
{
    public static void main(String[] args)
    {
        // Creates the data layer
        DATA_Layer dataLayer = new DATA_Layer();

        // Creates the application layer, passing in the data layer
        APP_Layer appLayer = new APP_Layer(dataLayer);

        // Creates the GUI layer, passing in the app layer
        GUI_Layer gui = new GUI_Layer(appLayer);
    }
}
