import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

public class Main {
    private static final double centerLat = 57.954709;
    private static final double centerLng = -3.439590;
    private static final double radius = 380000;

    public static void main(String[] args) throws Exception {
        if (args[0] != null) {
            showPlacesNearby(args[0]);
        } else {
            System.err.println("Please provide filename.");
        }
    }

    private static void showPlacesNearby(String filename) {
        Type REVIEW_TYPE = new TypeToken<List<Hotel>>() {
        }.getType();

        Gson gson = new Gson();

        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        List<Hotel> data = gson.fromJson(reader, REVIEW_TYPE);

        for (Hotel hotel : data) {
            if (distance(centerLat, centerLng,
                    hotel.getLatitude(), hotel.getLongitude(), 0, 0) < radius) {
                System.out.println(gson.toJson(hotel));
            }
        }
    }

    private static double distance(double lat1, double lon1, double lat2,
                                   double lon2, double el1, double el2) {

        final int R = 6371;

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000;

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

}
