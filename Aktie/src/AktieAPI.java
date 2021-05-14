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

import java.io.*;
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
    static Statement myStmt;
    public static Connection connection;

    static ArrayList<Double> closeWerte = new ArrayList<>();
    static ArrayList<Double> gleitenderDurchschnitt = new ArrayList<>();
    static ArrayList<String> daten = new ArrayList<>();
    static ArrayList<String> dateDB = new ArrayList<>();
    static ArrayList<Double> adjustedSplit = new ArrayList<>();
    static ArrayList<String> auswahlAktie = new ArrayList<>();
    static ArrayList<Double> adjustedCoefficient = new ArrayList<>();

    static String URL, type, key, verzeichnis, aktienDB;
    static int avgauswahl;


    public static void main (String args[]){
        Application.launch(args);
    }
    static boolean connectToMySql() throws SQLException {
        try {
            String DBurl = "jdbc:mysql://localhost:3306/"+aktienDB+"?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

            connection = DriverManager.getConnection(DBurl, "root", "NicerSpeck#");
            myStmt = connection.createStatement();
            System.out.println("Datenbank verknüpft");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }
    static void inputUser() throws IOException {
        try {
            File file = new File("C:\\Users\\nisch\\IdeaProjects\\AktieAPISQL\\src\\aktien.txt"); //Pfad
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            key = br.readLine();
            verzeichnis = br.readLine();
            aktienDB = br.readLine();
            avgauswahl = Integer.parseInt(br.readLine());
            while ((st = br.readLine()) != null)
                if (st.equals("compact") || st.equals("full")) {
                    type = st;
                } else {
                    auswahlAktie.add(st);
                }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    static void readURL(String tempAktie) throws Exception{
        try {
            URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol=" + tempAktie + "&outputsize=" + type + "&apikey="+key;
        } catch (Exception e) {
            System.out.println("Keine Internetverbingung");
        }
    }
    static void selectToCheck(String tempAktie) {
        try {
            myStmt = connection.createStatement();
            String querry = "select * from "+tempAktie+";";
            ResultSet rs = myStmt.executeQuery(querry);
            System.out.println("Es ist ein Table verfügbar");
        } catch (SQLException e) {
            System.out.println("Es wurde noch kein Tabel angelegt");
        }
    }

    static boolean createTable(String tempAktie) throws SQLException {
        try{
            myStmt = connection.createStatement();
            String createtable = "create table if not exists "+tempAktie+"_Roh (datum varchar(255) primary key, close double, split double);";
            myStmt.executeUpdate(createtable);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    static boolean createTableClac(String tempAktie) throws SQLException{
        try{
            myStmt = connection.createStatement();
            String createtable = "create table if not exists "+tempAktie+"_Calc (datum varchar(255) primary key, closeCorrect double, avg double );";
            myStmt.executeUpdate(createtable);
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
            adjustedCoefficient.add(json.getJSONObject(LocalDate.parse((CharSequence) json.names().get(i)).toString()).getDouble("8. split coefficient"));
        }
    }

    static void clear() {
        daten.clear();
        closeWerte.clear();
        adjustedCoefficient.clear();
        adjustedSplit.clear();
        dateDB.clear();
        gleitenderDurchschnitt.clear();
    }

    static void writeDataInDB(String tempAktie) {
        try {
            for (int i = 0; i < daten.size(); i++) {
                String writeData = "insert ignore into " + tempAktie+"_Roh (datum, close, split) values('" + daten.get(i) + "', '" + closeWerte.get(i) + "', '" + adjustedCoefficient.get(i) + "');";
                myStmt.executeUpdate(writeData);
            }
            System.out.println("Rohdaten eingetragen");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void splitCorrection(String tempAktie) {
        ArrayList<Double> coe = new ArrayList<>();
        ArrayList<Double> close = new ArrayList<>();
        double coefficient = 1.0;
        double temp;
        try {
            String querry = "SELECT * from " + tempAktie+"_Roh order by datum asc;";
            ResultSet rs = myStmt.executeQuery(querry);
            while (rs.next()) {
                close.add(rs.getDouble("close"));
                coe.add(rs.getDouble("split"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        for (int i = 0; i < coe.size(); i++) {
            coefficient *= coe.get(i);
            temp = coefficient * close.get(i);
            adjustedSplit.add((double) Math.round(temp * 100) / 100);
        }
        coe.clear();
        close.clear();

    }

    static void durchschnitt(String tempAktie) {
        int count = 0;
        double wert = 0, x, avg;
        for (int i = 0; i <= adjustedSplit.size() - 1; i++) {
            count++;
            if (count <= avgauswahl) {
                wert = wert + adjustedSplit.get(i);
                avg = wert / count;
                gleitenderDurchschnitt.add(avg);
            }
            if (count > avgauswahl) {
                x = adjustedSplit.get(i - avgauswahl);
                wert = wert - x;
                wert = wert + adjustedSplit.get(i);
                avg = wert / avgauswahl;
                gleitenderDurchschnitt.add(avg);
            }
        }
    }

    static void writeCorrectDataInDB(String tempAktie) {
        try {
            if(adjustedSplit.size() == 0){
                splitCorrection(tempAktie);
            }
            if(gleitenderDurchschnitt.size() == 0){
                durchschnitt(tempAktie);
            }
            for (int i = 0; i < daten.size(); i++) {
                String writeData = "insert ignore into " + tempAktie+"_Calc (datum, closeCorrect, avg) values('" + daten.get(i) + "', '" + adjustedSplit.get(i) + "', '" + gleitenderDurchschnitt.get(i) + "');";
                myStmt.executeUpdate(writeData);
            }
            System.out.println("Datensatz eingetragen");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void getData(String tempAktie) {
        try {
            Statement myStmt = connection.createStatement();
            ResultSet rsNormal = myStmt.executeQuery( "SELECT * from " + tempAktie+"_Calc");
            while (rsNormal.next()) {

                dateDB.add(rsNormal.getString("datum"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage primaryStage) throws SQLException, IOException {
        try {
            inputUser();

            for(int x = 0; x < auswahlAktie.size(); x++) {
                String tempAktie = auswahlAktie.get(x);
                connectToMySql();
                clear();
                readURL(tempAktie);
                selectToCheck(tempAktie);
                createTable(tempAktie);
                createTableClac(tempAktie);
                getWert(URL);
                splitCorrection(tempAktie);
                durchschnitt(tempAktie);
                writeDataInDB(tempAktie);
                writeCorrectDataInDB(tempAktie);
                getData(tempAktie);

                final CategoryAxis xAxis = new CategoryAxis();
                final NumberAxis yAxis = new NumberAxis();
                String newFolder = LocalDate.now().toString();
                LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
                Scene scene = new Scene(lineChart, 1000, 600);

                xAxis.setLabel("Datum");
                yAxis.setLabel("close-Wert");
                lineChart.setTitle("Aktienkurs " + tempAktie);
                XYChart.Series<String, Number> tatsaechlich = new XYChart.Series();
                XYChart.Series<String, Number> durchschnitt = new XYChart.Series();
                tatsaechlich.setName("Close-Werte");
                for (int i = 0; i < dateDB.size() - 1; i++) {
                    tatsaechlich.getData().add(new XYChart.Data(dateDB.get(i), adjustedSplit.get(i)));
                }
                durchschnitt.setName("gleitender Durchschnitt");
                for (int i = 0; i < gleitenderDurchschnitt.size()-1; i++) {
                    durchschnitt.getData().add(new XYChart.Data(dateDB.get(i), gleitenderDurchschnitt.get(i)));
                }
                lineChart.getData().add(tatsaechlich);
                lineChart.getData().add(durchschnitt);
                yAxis.setAutoRanging(false);
                double verschiebenOben = Collections.max(adjustedSplit);
                double verschiebenUnten = Collections.min(adjustedSplit);
                yAxis.setLowerBound(verschiebenUnten - 20);
                yAxis.setUpperBound(verschiebenOben + 20);
                tatsaechlich.getNode().setStyle("-fx-stroke: #000000; ");
                durchschnitt.getNode().setStyle("-fx-stroke: #ffffff; ");
                lineChart.setCreateSymbols(false);
                if(adjustedSplit.size() > 0 && gleitenderDurchschnitt.size() > 0){
                    if(adjustedSplit.get(adjustedSplit.size()-1) > gleitenderDurchschnitt.get(gleitenderDurchschnitt.size()-1)) {
                        scene.getStylesheets().add("backgroundGreen.css");
                    }else {
                        scene.getStylesheets().add("backgroundRed.css");
                    }
                }
                primaryStage.setScene(scene);
                WritableImage image = scene.snapshot(null);

                File directoryImage = new File(verzeichnis + File.separator + "Image" );
                directoryImage.mkdir();
                File directory = new File(verzeichnis + "Image\\" + File.separator + newFolder);
                directory.mkdir();
                File file = new File(verzeichnis + "Image\\" + newFolder + "\\" + tempAktie + " " + LocalDate.now().minusDays(1) + ".png"); //Pfad einfügen
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "PNG", file);
                System.out.println("Image Saved " + tempAktie);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
