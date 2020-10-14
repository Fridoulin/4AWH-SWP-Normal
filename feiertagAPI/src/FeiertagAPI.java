import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;

public class FeiertagAPI {
    public static HashMap<String, Integer> getFreeDayInYear(String state, int year) throws IOException, JSONException {
        HashMap<String, Integer> countDays = new HashMap<>();
        JSONObject json = utility.getJsonFromURL("https://feiertage-api.de/api/?jahr="+year+"&nur_land="+state);
        String[] keys = JSONObject.getNames(json);
        for(int i = 0; i < keys.length; i++){
           JSONObject day = (JSONObject) json.get(keys[i]);
            LocalDate date = LocalDate.parse(day.get("datum").toString());
            String weekday = date.getDayOfWeek().toString();
            if(countDays.get(weekday) == null){
                countDays.put(weekday, 1);
            } else {
                countDays.put(weekday, countDays.get(weekday) + 1);
            }
        }
        return countDays;
    }
    public static HashMap<String, Integer> getFreeDayBetweenYear(String state, int startYear, int endYear) throws IOException, JSONException {
        HashMap<String, Integer> countDays = new HashMap<>();
        for(int year = startYear; year <= endYear; year++){
            HashMap <String, Integer> map =  getFreeDayInYear(state, year);
            for (String key : map.keySet()) {
                if(countDays.get(key) == null){
                    countDays.put(key, map.get(key));
                } else {
                    countDays.put(key, countDays.get(key) + map.get(key));
                }
            }
        }
        return countDays;
    }
}
