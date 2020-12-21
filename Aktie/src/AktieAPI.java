import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;

public class AktieAPI extends Application{
    static Scanner reader = new Scanner(System.in);
    static ArrayList<Double> closeWerte = new ArrayList<>();
    static ArrayList<Double> gleitenderDurchschnitt = new ArrayList<>();
    static ArrayList<LocalDate> daten = new ArrayList<>();
    static ArrayList<Double> avgDB = new ArrayList<>();
    static ArrayList<Double> closeDB = new ArrayList<>();
    static ArrayList<String> dateDB = new ArrayList<>();
    static String URL, auswahlAktie;

    public static void main (String args[]) throws IOException, JSONException {
        AktieAPI a = new AktieAPI();
        a.inputUser();
        a.readURL();
        a.getWert(URL);
        a.connect();
        a.createNewTable();
        a.durchschnitt();
        a.insert();
        a.insertAVG();
        a.selectAll();
        Application.launch(args);
    }
    static void inputUser() {
        System.out.println("Aktie (nur USA): ");
        auswahlAktie = reader.next();
    }
    static void readURL() {
        URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="+auswahlAktie+ "&outputsize=compact&apikey=A0ZGRFDRZANZJGA8";
    }
    static void getWert(String URL) throws JSONException, IOException {
        JSONObject json = new JSONObject(IOUtils.toString(new URL(URL), Charset.forName("UTF-8")));
        json = json.getJSONObject("Time Series (Daily)");
        for(int i = 0; i < json.names().length(); i++){
            daten.add(LocalDate.parse((CharSequence)json.names().get(i)));
            closeWerte.add(json.getJSONObject(LocalDate.parse((CharSequence)json.names().get(i)).toString()).getDouble("4. close"));
        }
    }
    void durchschnitt() {
        String sql = "SELECT * FROM "+ auswahlAktie +" order by datum";
        try{
            Connection conn = this.connection();
            Statement stmt  = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                rs.getDouble("close");
                closeDB.add(rs.getDouble("close"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        int count = 0;
        double wert = 0, x,avg;
        for(int i = 0; i <= closeDB.size()-1; i++){
            count++;
            if(count <= 20){
                wert = wert + closeDB.get(i);
                avg = wert/count;
                gleitenderDurchschnitt.add(avg);
            }
            if(count > 20) {
                x = closeDB.get(i-20);
                wert = wert - x;
                wert = wert + closeDB.get(i);
                avg = wert/20;
                gleitenderDurchschnitt.add(avg);
            }
        }
    }
    public static void connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:C:\\Users\\nisch\\IdeaProjects\\Aktie\\Aktie.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    public static void createNewTable() {
        String url = "jdbc:sqlite:C:\\Users\\nisch\\IdeaProjects\\Aktie\\Aktie.db";
        String sql = "CREATE TABLE IF NOT EXISTS "+ auswahlAktie +" (\n"
                + "datum text primary key, close real)";
        String sqlAVG = "CREATE TABLE IF NOT EXISTS "+ auswahlAktie+"AVG (\n"
                + "date text primary key, avg real)";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.execute(sqlAVG);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private Connection connection() {
        String url = "jdbc:sqlite:C:\\Users\\nisch\\IdeaProjects\\Aktie\\Aktie.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    public void insert() {
        String sql = "INSERT OR REPLACE INTO " + auswahlAktie + " (datum, close) VALUES(?, ?)";
        try {
            Connection conn = this.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < closeWerte.size(); i++) {
                pstmt.setString(1, daten.get(i).toString());
                pstmt.setDouble(2, closeWerte.get(i));
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void insertAVG() {
        String sqlAVG = "INSERT OR REPLACE INTO "+auswahlAktie+"AVG (date, avg) VALUES(?, ?)";
        try{
            Connection conn = this.connection();
            PreparedStatement pstmt = conn.prepareStatement(sqlAVG);
            for (int i = 0; i < gleitenderDurchschnitt.size(); i++) {
                pstmt.setString(1, daten.get(i).toString());
                pstmt.setDouble(2, gleitenderDurchschnitt.get(i));
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void selectAll() {
        String sql = "SELECT * FROM "+ auswahlAktie +" order by datum";
        String sqlAVG = "SELECT * FROM "+ auswahlAktie+"AVG";
        try {
            Connection conn = this.connection();
            Statement stmt = conn.createStatement();
            Statement stmtAVG  = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSet rsAVG = stmtAVG.executeQuery(sqlAVG);

            System.out.println("Datum               Close Werte             Durchschnitt");
            while (rs.next() && rsAVG.next()) {
                System.out.println(
                        rs.getString("datum")  + "\t \t \t \t" +
                                rs.getDouble("close") + "\t \t \t \t" +
                                rsAVG.getDouble("avg")
                );
                dateDB.add(rsAVG.getString("date"));
                avgDB.add(rsAVG.getDouble("avg"));
            }
            dateDB.sort(null);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void start(Stage primaryStage) {
        try {
            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Datum");
            yAxis.setLabel("close-Wert");
            final LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
            lineChart.setTitle("Aktienkurs");
            XYChart.Series<String, Number> tatsaechlich = new XYChart.Series();
            tatsaechlich.setName("Close-Werte");
            for (int i = 0; i < closeWerte.size() - 1; i++) {
                tatsaechlich.getData().add(new XYChart.Data(dateDB.get(i), closeDB.get(i)));
            }
            XYChart.Series<String, Number> durchschnitt = new XYChart.Series();
            durchschnitt.setName("gleitender Durchschnitt");
            for (int i = 0; i < gleitenderDurchschnitt.size() - 1; i++) {
                durchschnitt.getData().add(new XYChart.Data(dateDB.get(i), avgDB.get(i)));
            }
            Scene scene = new Scene(lineChart, 1000, 600);
            lineChart.getData().add(tatsaechlich);
            lineChart.getData().add(durchschnitt);

            if (closeWerte.get(closeWerte.size()-1) >= avgDB.get(avgDB.size()-1)) {
                tatsaechlich.getNode().setStyle("-fx-stroke: #ff0000; ");
            }
            if (closeWerte.get(closeWerte.size()-1) < avgDB.get(avgDB.size()-1)) {
                tatsaechlich.getNode().setStyle("-fx-stroke: #15ff00; ");
            }
            lineChart.setCreateSymbols(false);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
