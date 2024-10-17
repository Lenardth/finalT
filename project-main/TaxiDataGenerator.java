import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class TaxiDataGenerator {

    public static void main(String[] args) {
        ArrayList<Taxi> taxiList = generateTaxiData(1000);
        saveTaxiData(taxiList, "taxis.ser");
    }

    private static ArrayList<Taxi> generateTaxiData(int numberOfTaxis) {
        ArrayList<Taxi> taxis = new ArrayList<>();
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        Random random = new Random();

        for (int i = 0; i < numberOfTaxis; i++) {
            String taxiNumber = "Taxi " + (i + 1);
            long startTime = randomTime(random);
            String startTimeFormatted = timeFormat.format(new Date(startTime));
            long endTime = startTime + (random.nextInt(90) + 30) * 60 * 1000;
            String endTimeFormatted = timeFormat.format(new Date(endTime));

            Taxi taxi = new Taxi(taxiNumber, startTimeFormatted, endTimeFormatted);
            taxis.add(taxi);
        }

        return taxis;
    }

    private static long randomTime(Random random) {
        int hour = random.nextInt(24);
        int minute = random.nextInt(60);
        return (hour * 3600L + minute * 60L) * 1000L;
    }

    private static void saveTaxiData(ArrayList<Taxi> taxiList, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(taxiList);
            System.out.println("Taxi data successfully saved to " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
