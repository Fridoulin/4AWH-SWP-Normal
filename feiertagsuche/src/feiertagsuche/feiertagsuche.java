package feiertagsuche;

import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class feiertagsuche {
	static Scanner reader = new Scanner(System.in);
	static int moDate = 0, diDate = 0, miDate = 0, doDate = 0, frDate = 0;
	static int mo = 0, di = 0, mi = 0, don = 0, fr = 0;
	static String land;
	static int moDayofWeek = 0, diDayofWeek = 0, miDayofWeek = 0, donDayofWeek = 0, frDayofWeek = 0;
	static ArrayList<String> dayofweekArr = new ArrayList<String>();
	static ArrayList<LocalDate> dateArr = new ArrayList<LocalDate>();
	static LocalDate jahr;
	static int jahre;
	static LocalDate startDatum;
	static LocalDate endDatum;
	static NodeList date, dayofweek;
	static File file;
	
	public static void main(String[] args) throws Exception {
		feiertagsuche Feiertage = new feiertagsuche();
		Feiertage.eingabe();
		Feiertage.fileImport();
		Feiertage.inputInList();
		Feiertage.checkDayweek();
		Feiertage.checkDate();
		Feiertage.dayCalculate();
		
		System.out.println("Montag: "+mo);
		System.out.println("Dienstag: "+di);
		System.out.println("Mitwoch: "+mi);
		System.out.println("Donnerstag: "+don);
		System.out.println("Freitag: "+fr);
	}
	void eingabe () {
		System.out.println("Anzahl Jahre: ");
		jahre = reader.nextInt();
		System.out.println("Startdatum (yyyy/mm/dd): ");
		startDatum = LocalDate.of(reader.nextInt(), reader.nextInt(), reader.nextInt());
		endDatum = startDatum.plusYears(jahre);
	}
	void fileImport() {		
		try {
			file = new File("at.xml");
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
			date = doc.getElementsByTagName("date");
			dayofweek = doc.getElementsByTagName("dayofweek");	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	void inputInList() {
		for (int i = 0; i < dayofweek.getLength(); i++) {
			Node node = dayofweek.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				dayofweekArr.add(eElement.getElementsByTagName("day").item(0).getTextContent());
			}
		}
		for (int i = 0; i < date.getLength(); i++) {
			Node node = date.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				dateArr.add(LocalDate.of(2020,
						Integer.parseInt(eElement.getElementsByTagName("month").item(0).getTextContent()),
						Integer.parseInt(eElement.getElementsByTagName("day").item(0).getTextContent())));
			}
		}
		for(int i = 1; i <= jahre; i++) {
			for(int j = 0; j < date.getLength(); j++) {
				dateArr.add(dateArr.get(j).plusYears(i));
			}
		}
	}	

	void checkDayweek() {
		for (int i = 0; i < dayofweekArr.size(); i++) {
			if (dayofweekArr.get(i).equals("montag")){
				moDayofWeek++;
			}
			if (dayofweekArr.get(i).equals("dienstag")) {
				diDayofWeek++;
			}
			if (dayofweekArr.get(i).equals("mittwoch")) {
				miDayofWeek++;
			}
			if (dayofweekArr.get(i).equals("donnerstag")) {
				donDayofWeek++;
			}
			if (dayofweekArr.get(i).equals("freitag")) {
				frDayofWeek++;
			}
			System.out.println(dateArr.get(i));
		}
	}	
	void checkDate() {
		for(int i = 0; i < dateArr.size(); i++) {
			switch(dateArr.get(i).getDayOfWeek()) {
			case MONDAY:
				moDate++;
				break;
			case TUESDAY:
				diDate++;
				break;
			case WEDNESDAY:
				miDate++;
				break;
			case THURSDAY:
				doDate++;
			case FRIDAY:
				frDate++;
				break;
			default:
			break;
			}
			System.out.println(dateArr.get(i));
		}		
	}
	void dayCalculate() {
		mo = moDate + (moDayofWeek * jahre);
		di = diDate + (diDayofWeek * jahre);
		mi = miDate + (miDayofWeek * jahre);
		don = doDate + (donDayofWeek * jahre);
		fr = frDate + (frDayofWeek * jahre);
	}	
}