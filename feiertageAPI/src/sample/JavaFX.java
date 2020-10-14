package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Scanner;

public class JavaFX extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception  {

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

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Feiertage");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Tage");
        BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
        XYChart.Series<String, Number> dataSeries1 = new XYChart.Series<String, Number>();
        dataSeries1.setName("Tage");

        for(var entrySet : FeiertagAPI.getFreeDayBetweenYear(state,startYear,endYear).entrySet()){
            int i = entrySet.getValue();
            String s = entrySet.getKey();
            dataSeries1.getData().add(new XYChart.Data<String, Number>(s, i));
        }
        barChart.getData().add(dataSeries1);
        barChart.setTitle("Feiertage");
        VBox vbox = new VBox(barChart);
        primaryStage.setScene(new Scene(vbox, 300, 275));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
