import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.io.IOUtils;
import org.json.*;

public class API extends Application {
    static ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
    static ArrayList<LocalDate> datesFerienStart = new ArrayList<>();
    static ArrayList<LocalDate> datesFerienEnde = new ArrayList<>();
    static ArrayList<LocalDate> datesAlleDaten = new ArrayList<>();
    static LocalDate startJahr;
    static int dauer, jahre, mo, di, mi, don, fr,  jahreStatisch;
    //static int moF, diF, miF, donF, frF; -> WIP
    static String URL, URLFerien;
    static Scanner reader = new Scanner(System.in);

    public static void main(String[] args) throws JSONException, IOException {
        API api = new API();
        api.userInput();
        //for (int i = jahreStatisch; i < (jahreStatisch+dauer); i++) {
        //       api.readURL();
       //     JSONArray json = new JSONArray(IOUtils.toString(new URL(URLFerien), Charset.forName("UTF-8")));
      //      api.getWertFerien(json);
      //  } 
        api.readURL();
        api.searchAPI();
        api.listFeiertage();
        api.vergleichen();
        api.connect();
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

    public static void readURL() {
        URL = "https://feiertage-api.de/api/?jahr=" + jahre + "&nur_land=by";
        //URLFerien = "https://ferien-api.de/api/v1/holidays/BY/" + i;
        jahre++;
    }

    static String getWert(JSONObject json, String key) throws JSONException {
        JSONObject date = (JSONObject) json.get(key);
        String anzahl = date.getString("datum");
        return anzahl;
    }

    //static String getWertFerien(JSONArray json) throws JSONException {
        /*for (int i = 0; i < json.length(); i++) {
            String start, ende;
            JSONObject startFerien = json.getJSONObject(i);
            start = startFerien.getString("start");
            ende = startFerien.getString("end");
            String[] spiltStart = start.split("-");
            String[] splitEnde = ende.split("-");
            datesFerienStart.add(LocalDate.of(Integer.parseInt(spiltStart[0]), Integer.parseInt(spiltStart[1]), Integer.parseInt(spiltStart[2].replace("T00:00Z", ""))));
            datesFerienEnde.add(LocalDate.of(Integer.parseInt(splitEnde[0]), Integer.parseInt(splitEnde[1]), Integer.parseInt(splitEnde[2].replace("T00:00Z", ""))));
        }
        for (int j = 0; j < datesFerienStart.size(); j++) {
            datesFerienStart.get(j).datesUntil(datesFerienEnde.get(j)).forEach(localDate -> datesAlleDaten.add(localDate));
        }
        return null;
    }*/

    static void searchAPI() throws JSONException, IOException {
        LocalDate date;
        for (int i = 0; i < dauer; i++) {
            JSONObject json = new JSONObject(IOUtils.toString(new URL(URL), Charset.forName("UTF-8")));
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

    static void listFeiertage() {
        // System.out.println(jahreStatisch);
        for (int i = jahreStatisch; i < jahreStatisch + dauer; i++) {
            System.out.println(i);
            dates.add(LocalDate.of(i, 1, 6));
            dates.add(LocalDate.of(i, 5, 1));
            dates.add(LocalDate.of(i, 10, 26));
            dates.add(LocalDate.of(i, 12, 8));
            dates.add(LocalDate.of(i, 8, 15));
        }
    }

    static void vergleichen() {
        //mit Ferien
       /* for (int i = 0; i < dates.size(); i++) {
            for (int j = 0; j < datesAlleDaten.size(); j++) {
                if (datesAlleDaten.get(j).getDayOfYear() != dates.get(i).getDayOfYear() && datesAlleDaten.get(j).getDayOfMonth() != dates.get(i).getDayOfMonth() &&
                        datesAlleDaten.get(j).getDayOfWeek() != dates.get(i).getDayOfWeek() ){
                    if (dates.get(i).getDayOfWeek() == DayOfWeek.MONDAY) {
                        moF++;
                    }
                    if (dates.get(i).getDayOfWeek() == DayOfWeek.TUESDAY) {
                        diF++;
                    }
                    if (dates.get(i).getDayOfWeek() == DayOfWeek.WEDNESDAY) {
                        miF++;
                    }
                    if (dates.get(i).getDayOfWeek() == DayOfWeek.THURSDAY) {
                        donF++;
                    }
                    if (dates.get(i).getDayOfWeek() == DayOfWeek.FRIDAY) {
                        frF++;
                    }
                }
            }
        }*/

        //ohne Ferien
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

    void output() {
        System.out.println("ohne Ferien");
        System.out.println("Montag: " + mo);
        System.out.println("Dienstag: " + di);
        System.out.println("Mittwoch: " + mi);
        System.out.println("Donnerstag: " + don);
        System.out.println("Freitag: " + fr);
      /*  System.out.println();
        System.out.println("mit Ferien");
        System.out.println("Montag: " + moF);
        System.out.println("Dienstag: " + diF);
        System.out.println("Mittwoch: " + miF);
        System.out.println("Donnerstag: " + donF);
        System.out.println("Freitag: " + frF);
       */
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        API api = new API();
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Wochentage");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Tage");
        BarChart<String, Number> barChart = new BarChart<String, Number>(xAxis, yAxis);
        //ohne Ferien
        XYChart.Series<String, Number> feiertageJavaFX = new XYChart.Series<String, Number>();
        feiertageJavaFX.setName("ohne Ferien");
        feiertageJavaFX.getData().add(new XYChart.Data<String, Number>("Montag", api.mo));
        feiertageJavaFX.getData().add(new XYChart.Data<String, Number>("Dienstag", api.di));
        feiertageJavaFX.getData().add(new XYChart.Data<String, Number>("Mittwoch", api.mi));
        feiertageJavaFX.getData().add(new XYChart.Data<String, Number>("Donnerstag", api.don));
        feiertageJavaFX.getData().add(new XYChart.Data<String, Number>("Freitag", api.fr));
        barChart.getData().add(feiertageJavaFX);
        //mit Ferien
       /* XYChart.Series<String, Number> FerienJavaFX = new XYChart.Series<String, Number>();
        FerienJavaFX.setName("mit Ferien");
        FerienJavaFX.getData().add(new XYChart.Data<String, Number>("Montag", api.moF));
        FerienJavaFX.getData().add(new XYChart.Data<String, Number>("Dienstag", api.diF));
        FerienJavaFX.getData().add(new XYChart.Data<String, Number>("Mittwoch", api.miF));
        FerienJavaFX.getData().add(new XYChart.Data<String, Number>("Donnerstag", api.donF));
        FerienJavaFX.getData().add(new XYChart.Data<String, Number>("Freitag", api.frF));
        barChart.getData().add(FerienJavaFX);
*/
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
            String url = "jdbc:sqlite:C:\\Users\\nisch\\IdeaProjects\\API\\API.db"; //Pfad eingügen
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
        String url = "jdbc:sqlite:C:\\Users\\nisch\\IdeaProjects\\API\\API.db"; //Pfad eingügen
        String sql = "CREATE TABLE IF NOT EXISTS feiertag (\n"
                + " date text, \n"
                + " time text, \n"
                + " montag integer,\n"
                + " dienstag integer,\n"
                + " mittwoch integer,\n"
                + " donnerstag integer,\n"
                + " freitag integer)";
        try {
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection connection() {
        String url = "jdbc:sqlite:C:\\Users\\nisch\\IdeaProjects\\API\\API.db"; //Pfad eingügen
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert() {
        String sql = "INSERT INTO feiertag(date, time, montag, dienstag, mittwoch, donnerstag, freitag) VALUES(?,?,?,?,?,?,?)";
        try {
            Connection conn = this.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, LocalDate.now().format(DateTimeFormatter.ofPattern("YYYY/MM/DD")));
            pstmt.setString(2, LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            pstmt.setInt(3, mo);
            pstmt.setInt(4, di);
            pstmt.setInt(5, mi);
            pstmt.setInt(6, don);
            pstmt.setInt(7, fr);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void selectAll() {
        String sql = "SELECT * FROM feiertag";
        try {
            Connection conn = this.connection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println("Datum          Uhrzeit        Montag      Dienstag        Mittwoch        Donnerstag          Freitag");
                System.out.println(
                        rs.getString("date") + "\t \t" +
                                rs.getString("time") + "\t \t" +
                                rs.getInt("montag") + "\t \t \t" +
                                rs.getInt("dienstag") + "\t \t \t \t" +
                                rs.getInt("mittwoch") + "\t \t \t \t" +
                                rs.getInt("donnerstag") + "\t \t \t \t \t" +
                                rs.getInt("freitag"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}