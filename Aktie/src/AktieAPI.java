import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javafx.application.Application;

import javax.imageio.ImageIO;

public class AktieAPISQL extends Application{
    public static String DBurl = "jdbc:mysql://localhost:3306/aktiendb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    static Statement myStmt;
    public static Connection connection;

    static Scanner reader = new Scanner(System.in);
    static ArrayList<Double> closeWerte = new ArrayList<>();
    static ArrayList<Double> gleitenderDurchschnitt = new ArrayList<>();
    static ArrayList<String> daten = new ArrayList<>();
    static ArrayList<Double> avgDB = new ArrayList<>();
    static ArrayList<Double> closeDB = new ArrayList<>();
    static ArrayList<String> dateDB = new ArrayList<>();
    static ArrayList<Double> adjustedSplit = new ArrayList<>();
    static String URL, auswahlAktie, type;
    static int avgauswahl;

    public static void main (String args[]) throws Exception {
        AktieAPISQL a = new AktieAPISQL();
        a.connectToMySql();
        a.inputUser();
        a.readURL();
        a.selectToCheck();
        a.createTable();
        a.getWert(URL);
        a.writeDataInDB();
        a.durchschnitt();
        a.getData();
        Application.launch(args);
    }

    static boolean connectToMySql() throws SQLException {
        {
            try {
                connection = DriverManager.getConnection(DBurl, "root", "NicerSpeck#");
                myStmt = connection.createStatement();
                System.out.println("Datenbank verknüpft");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
    static void readtxt() throws FileNotFoundException {
        FileReader r = new FileReader("aktien.txt");
    }

    static void inputUser() {
        System.out.println("Aktie (nur USA): ");
        auswahlAktie = reader.next();
        System.out.println("full (alle Elemente)/ compact (letzten 100 Tage): ");
        type = reader.next();
        System.out.println("Durchschnitt: ");
        avgauswahl = reader.nextInt();
    }
    static void readURL() throws Exception{
        try {
            URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" + auswahlAktie + "&outputsize=" + type + "&apikey=A0ZGRFDRZANZJGA8";//Schlüssel eingeben
        }
        catch (Exception e){
            System.out.println("Keine Internetverbingung");
        }
    }


    public static void selectToCheck() {
        try {
            myStmt = connection.createStatement();
            String querry = "select * from "+auswahlAktie+";";
            ResultSet rs = myStmt.executeQuery(querry);
            System.out.println("Es ist ein Table verfügbar");
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Es wurde noch keine Tabel angelegt");
        }
    }

    static boolean createTable() throws SQLException {
        try{
            myStmt = connection.createStatement();
            String createtable = "create table if not exists "+auswahlAktie+" (datum varchar(255) primary key, close double, split double);";
            String createtableAVG = "create table if not exists "+auswahlAktie+"AVG (datumAVG varchar(255) primary key, avg double);";
            myStmt.executeUpdate(createtable);
            myStmt.executeUpdate(createtableAVG);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    static void getWert(String URL) throws JSONException, IOException {
        JSONObject json = new JSONObject(IOUtils.toString(new URL(URL), Charset.forName("UTF-8")));
        json = json.getJSONObject("Time Series (Daily)");
        for (int i = 0; i < json.names().length(); i++) {
            daten.add(LocalDate.parse((CharSequence) json.names().get(i)).toString());
            closeWerte.add(json.getJSONObject(LocalDate.parse((CharSequence) json.names().get(i)).toString()).getDouble("4. close"));
            adjustedSplit.add(json.getJSONObject(LocalDate.parse((CharSequence) json.names().get(i)).toString()).getDouble("5. adjusted close"));
        }
    }

    static void writeDataInDB(){
        try {
            myStmt = connection.createStatement();
            for(int i = 0; i < daten.size(); i++) {
                String writeData = "insert ignore into " + auswahlAktie + " (datum, close, split) values('" + daten.get(i) + "', '" + closeWerte.get(i) + "', '" + adjustedSplit.get(i) + "');";
                myStmt.executeUpdate(writeData);
            }
            System.out.println("Datensatz eingetragen");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    void durchschnitt() {
        //fehler in der Matrix
        //komische Berechnung - fehler
        try {
            Statement myStmt = connection.createStatement();
            String querry = "SELECT * from " + auswahlAktie;
            ResultSet rs = myStmt.executeQuery(querry);
            while (rs.next()) {
                closeDB.add(rs.getDouble("close"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        int count = 0;
        double wert = 0, x,avg;
        for(int i = 0; i <= closeDB.size()-1; i++){
            count++;
            if(count <= avgauswahl){
                wert = wert + closeDB.get(i);
                avg = wert/count;
                gleitenderDurchschnitt.add(avg);
            }
            if(count > avgauswahl) {
                x = closeDB.get(i-avgauswahl);
                wert = wert - x;
                wert = wert + closeDB.get(i);
                avg = wert/avgauswahl;
                gleitenderDurchschnitt.add(avg);
            }
        }
        try {
                myStmt = connection.createStatement();
                for(int i = 0; i < daten.size(); i++) {
                    String writeData = "insert ignore into "+auswahlAktie+"AVG (datumAVG, avg) values('"+daten.get(i)+"', '" + gleitenderDurchschnitt.get(i) + "');";
                    myStmt.executeUpdate(writeData);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }


    public static void getData() {
        try {
            Statement myStmt = connection.createStatement();
            Statement myStmtAVG = connection.createStatement();

            ResultSet rsNormal = myStmt.executeQuery( "SELECT * from " + auswahlAktie + " order by datum");
            ResultSet rsAVG = myStmtAVG.executeQuery("SELECT * from " + auswahlAktie+"AVG order by datumAVG");
            System.out.println("Datum               Close Werte             Durchschnitt");
            while (rsNormal.next()&& rsAVG.next()) {
                    System.out.println(
                            rsNormal.getString("datum") + "\t \t \t \t" +
                                    rsNormal.getDouble("close") + "\t \t \t \t" +
                                    rsAVG.getDouble("avg"));
                    dateDB.add(rsNormal.getString("datum"));
                    avgDB.add(rsAVG.getDouble("avg"));
                    closeDB.add(rsNormal.getDouble("close"));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage primaryStage) {
            try {
                final CategoryAxis xAxis = new CategoryAxis();
                final NumberAxis yAxis = new NumberAxis();
                String newFolder = LocalDate.now().toString();
                xAxis.setLabel("Datum");
                yAxis.setLabel("close-Wert");
                final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
                lineChart.setTitle("Aktienkurs " + auswahlAktie);
                XYChart.Series<String, Number> tatsaechlich = new XYChart.Series();
                tatsaechlich.setName("Close-Werte");

                for (int i = 0; i < dateDB.size() - 1; i++) {
                    tatsaechlich.getData().add(new XYChart.Data(dateDB.get(i), closeDB.get(i)));
                }
                XYChart.Series<String, Number> durchschnitt = new XYChart.Series();
                durchschnitt.setName("gleitender Durchschnitt");
                for (int i = 0; i < dateDB.size() - 1; i++) {
                    durchschnitt.getData().add(new XYChart.Data(dateDB.get(i), avgDB.get(i)));
                }
                Scene scene = new Scene(lineChart, 1000, 600);
                lineChart.getData().add(tatsaechlich);
                lineChart.getData().add(durchschnitt);
                yAxis.setAutoRanging(false);
                double verschiebenOben = Collections.max(closeDB);
                double verschiebenUnten = Collections.min(closeDB);
                yAxis.setLowerBound(verschiebenUnten-20);
                yAxis.setUpperBound(verschiebenOben+20);

                if (closeDB.get(closeDB.size()-1) > avgDB.get(avgDB.size()-1)) {
                    scene.getStylesheets().add("backgroundRed.css");
                }
                if (closeDB.get(closeDB.size()-1) < avgDB.get(avgDB.size()-1)) {
                    scene.getStylesheets().add("backgroundGreen.css");
                }
                tatsaechlich.getNode().setStyle("-fx-stroke: #000000; ");
                durchschnitt.getNode().setStyle("-fx-stroke: #ffffff; ");
                lineChart.setCreateSymbols(false);
                primaryStage.setScene(scene);
                primaryStage.show();
                WritableImage image = scene.snapshot(null);
                try {
                    File directory = new File("C:" + File.separator + "Users" + File.separator + "nisch" + File.separator + "IdeaProjects" + File.separator + "Aktie" + File.separator + "Image" + File.separator + newFolder);
                    directory.mkdir();
                }
                catch (Exception e){
                    System.out.println("Ordner wurde bereits erstellt");
                }
                File file = new File("C:\\Users\\nisch\\IdeaProjects\\Aktie\\Image\\"+newFolder+"\\"+auswahlAktie+" "+LocalDate.now()+".png"); //Pfad einfügen
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "PNG", file);
                System.out.println("Image Saved");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
}
