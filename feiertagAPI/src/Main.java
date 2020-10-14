import org.json.JSONException;

import java.io.IOException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main {
    public static void main(String[] args) throws IOException, JSONException {
        Scanner reader = new Scanner(System.in);
        int startYear, endYear;
        String state;
        System.out.println("Startjahr: ");
        startYear = reader.nextInt();

        System.out.println("EndYear: ");
        endYear = reader.nextInt();

        System.out.println("Bundesland: ");
        state = reader.next();
        System.out.println(FeiertagAPI.getFreeDayBetweenYear(state,startYear, endYear));
    }
}
