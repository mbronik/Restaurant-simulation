package bin.Logic;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Statistics {
    private static double revenue;
    private static int accepted;
    private static int completed;

    private static Stream stream = new Stream("src\\data\\RestaurantStatisticsArchive.txt");

    public static void init(){
        revenue = 0;
        accepted = 0;
        completed = 0;
        stream.readSegment();
    }

    public static void orderDone (double addToRevenue){
        revenue+=addToRevenue;
        completed++;
    }
    public static void subtractFromRevenue(double subtract){
        revenue-=subtract;
    }
    public static void orderAccepted(){
        accepted++;
    }

    public static void show(){
        System.out.println("\nRevenue = " + (int)(revenue*100)/100 +
                "\nAccepted orders = " + accepted +
                "\nCompleted orders = " + completed
        );
    }
    public static void saveClose(){
        stream.writeToFile("\n\n"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss")) +
                "\n\tRevenue = " + ((int)(revenue*100))/100 +
                        "\n\tAccepted orders = " + accepted +
                        "\n\tCompleted orders = " + completed);
        stream.closeFile();
    }
}
