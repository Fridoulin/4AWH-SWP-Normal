import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.NodeList;

public class API extends Application {
    static ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
    static LocalDate startJahr;
    static int dauer, jahre, mo, di, mi, don, fr, jahreStatisch;
    static String URL;
    static Scanner reader = new Scanner(System.in);

    public static void main(String[] args) throws JSONException, IOException {
        API api = new API();
        api.userInput();
        api.readURL();
        api.searchAPI();
        api.listFeiertage();
        api.vergleichen();
        api.connect();
        api.createNewDatabase("API.db");
        api.createNewTable();
        api.insert();
        api.selectAll();
        api.output();
        Application.launch(args);
    }

    public static void userInput() {
        System.out.println("Startjahr: ");
        startJahr = LocalDate.of(reader.nextInt(), reader.nextInt(), reader.nextInt());
        jahre = startJahr.getYear() - 1;
        jahreStatisch = startJahr.getYear();
        System.out.println("Dauer: ");
        dauer = reader.nextInt();
    }
    public static String readURL() {
        URL = "https://feiertage-api.de/api/?jahr=" + jahre + "&nur_land=by";
        jahre++;
        return URL;
    }
    static String getWert(JSONObject json, String key) throws JSONException {
        JSONObject date = (JSONObject) json.get(key);
        String anzahl = date.getString("datum");
        return anzahl;
    }
    static void searchAPI() throws JSONException, IOException {
        LocalDate date;
        for(int i = 0; i < dauer; i++){
            JSONObject json = new JSONObject(IOUtils.toString(new URL(readURL()), Charset.forName("UTF-8")));
            date = LocalDate.parse(getWert(json, "Neujahrstag"));
            dates.add(date);
            date = LocalDate.parse(getWert(json, "Ostermontag"));
            dates.add(date);
            date = LocalDate.parse(getWert(json, "Pfingstmontag"));
            dates.add(date);
            date = LocalDate.parse(getWert(json, "Christi Himmelfahrt"));
            dates.add(date);
            date = LocalDate.parse(getWert(json, "Fronleichnam"));
            dates.add(date);
            date = LocalDate.parse(getWert(json, "Allerheiligen"));
            dates.add(date);
            date = LocalDate.parse(getWert(json, "1. Weihnachtstag"));
            dates.add(date);
            date = LocalDate.parse(getWert(json, "2. Weihnachtstag"));
            dates.add(date);
        }
    }
    static void listFeiertage(){
        System.out.println(jahreStatisch);
        for(int i = jahreStatisch; i < jahreStatisch+dauer; i++) {
            System.out.println(i);
            dates.add(LocalDate.of(i,1,6));
            dates.add(LocalDate.of(i,5,1));
            dates.add(LocalDate.of(i,10,26));
            dates.add(LocalDate.of(i,12,8));
            dates.add(LocalDate.of(i,8,15));
        }
    }
    static void vergleichen() {

        System.out.println(dates);
        for (int i = 0; i < dates.size(); i++) {

            if (dates.get(i).getDayOfWeek() == DayOfWeek.MONDAY) {
                mo++;
            }
            if (dates.get(i).getDayOfWeek() == DayOfWeek.TUESDAY) {
                di++;
            }
            if (dates.get(i).getDayOfWeek() == DayOfWeek.WEDNESDAY) {
                mi++;
            }
            if (dates.get(i).getDayOfWeek() == DayOfWeek.THURSDAY) {
                don++;
            }
            if (dates.get(i).getDayOfWeek() == DayOfWeek.FRIDAY) {
                fr++;
            }
        }
    }
    void output(){
        System.out.println("Montag: "+mo);
        System.out.println("Dienstag: "+di);
        System.out.println("Mittwoch: "+mi);
        System.out.println("Donnerstag: "+don);
        System.out.println("Freitag: "+fr);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        API api = new API();
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Wochentage");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Tage");
        BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
        XYChart.Series<String, Number> feiertageJavaFX = new XYChart.Series<String, Number>();
        //feiertageJavaFX.setName("mit Ferien");

        feiertageJavaFX.getData().add(new XYChart.Data<String, Number>("Montag", api.mo));
        feiertageJavaFX.getData().add(new XYChart.Data<String, Number>("Dienstag", api.di));
        feiertageJavaFX.getData().add(new XYChart.Data<String, Number>("Mittwoch", api.mi));
        feiertageJavaFX.getData().add(new XYChart.Data<String, Number>("Donnerstag", api.don));
        feiertageJavaFX.getData().add(new XYChart.Data<String, Number>("Freitag", api.fr));

        barChart.getData().add(feiertageJavaFX);
        barChart.setTitle("Feiertage");
        VBox vbox = new VBox(barChart);
        Scene scene = new Scene(vbox, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.setHeight(300);
        primaryStage.setWidth(400);
        primaryStage.show();
    }
    public static void connect() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlite:C:\\Users\\nisch\\IdeaProjects\\API\\API.db";
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
    public static void createNewDatabase(String fileName) {

        String url = "jdbc:sqlite:C:\\Users\\nisch\\IdeaProjects\\API\\" + fileName;

        try {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void createNewTable() {
        String url = "jdbc:sqlite:C:\\Users\\nisch\\IdeaProjects\\API\\API.db";
        String sql = "CREATE TABLE IF NOT EXISTS feiertag (\n"
                + " id integer ,\n"
                + " montag integer,\n"
                + " dienstag integer,\n"
                + " mittwoch integer,\n"
                + " donnerstag integer,\n"
                + " freitag integer)";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private Connection connection() {
        String url = "jdbc:sqlite:C:\\Users\\nisch\\IdeaProjects\\API\\API.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    public void insert() {
        String sql = "INSERT INTO feiertag(montag, dienstag, mittwoch, donnerstag, freitag) VALUES(?,?,?,?,?)";
        try{
            Connection conn = this.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, mo);
            pstmt.setInt(2, di);
            pstmt.setInt(3, mi);
            pstmt.setInt(4, don);
            pstmt.setInt(5, fr);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void selectAll(){
        String sql = "SELECT * FROM feiertag";
        try {
            Connection conn = this.connection();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println(
                        rs.getInt("montag") + "\t" +
                        rs.getInt("dienstag") + "\t" +
                        rs.getInt("mittwoch") + "\t" +
                        rs.getInt("donnerstag") + "\t" +
                        rs.getInt("freitag") + "\t");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
