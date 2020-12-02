import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class AkrieAPI {
    static Scanner reader = new Scanner(System.in);
    static ArrayList<Double> closeWerte = new ArrayList<>();
    static ArrayList<LocalDate> daten = new ArrayList<>();
    static String URL, auswahlAktie;
    static int elemente = 100;


    public static void main (String args[]) throws IOException, JSONException {
    AkrieAPI a = new AkrieAPI();
    a.inputUser();
    a.readURL();
    a.getWert(URL);
    a.connect();
    a.createNewTable();
    a.insert();
    a.selectAll();
    a.gleitenderDurschnitt();
   // System.out.println(daten);
        // System.out.println(closeWerte);
    }
    static void inputUser(){
        System.out.println("Aktie (nur USA): ");
        auswahlAktie = reader.next();
    }
    static void readURL () throws IOException, JSONException {
        URL = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="+auswahlAktie+ "&outputsize=compact&apikey=A0ZGRFDRZANZJGA8";
    }
    static void getWert(String URL) throws JSONException, IOException {
        JSONObject json = new JSONObject(IOUtils.toString(new URL(URL), Charset.forName("UTF-8")));
        json = json.getJSONObject("Time Series (Daily)");
            for(int i = 0; i < elemente; i++){
                daten.add(LocalDate.parse((CharSequence)json.names().get(i)));
                closeWerte.add(json.getJSONObject(LocalDate.parse((CharSequence)json.names().get(i)).toString()).getDouble("4. close"));
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
                + " date text, \n"
                + " close double\n"
                + ");";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
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
        String sql = "INSERT OR REPLACE INTO "+auswahlAktie+" (date, close) VALUES(?,?)";
        try{
            Connection conn = this.connection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for(int i = 0; i < elemente; i++) {
                pstmt.setString(1, daten.get(i).toString());
                pstmt.setDouble(2, closeWerte.get(i));
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void selectAll(){
        String sql = "SELECT * FROM "+ auswahlAktie;
        try {
            Connection conn = this.connection();
            Statement stmt  = conn.createStatement();
            ResultSet rs    = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println("Datum           Close Werte");
                System.out.println(
                        rs.getString("date") + "\t" + "\t" +
                        rs.getDouble("close"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void gleitenderDurschnitt(){
        double durchschnitt = 0, gleitenderDurchschnitt;
        for(int i = 0; i < elemente; i++){
            durchschnitt = durchschnitt + closeWerte.get(i);
        }
        gleitenderDurchschnitt = durchschnitt/elemente;
        System.out.println("");
        System.out.println("gleitender Durchschnitt:" + gleitenderDurchschnitt);
    }
}