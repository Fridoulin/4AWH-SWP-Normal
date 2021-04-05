Was macht das Programm?</br>
Allgemein:</br>
	Dieses Programm dient dazu, dass man aus einer API die close-Wert und das dazugehörige Datum auszulesen. 
	Dabei kann man des Unternehmen (z.B. AAPL (Apple), TSLA (Tesla), usw.) auswählen von welchem man die Aktienwerte haben will. </br>
Wichtig vor dem 1. Start:</br>
	In das Program gehen und bei Zeile 60 angeben wo die Textdatei liegt. Sie muss in dem src-Ordern liegen!</br>
Täglich ausführen:</br>
	Damit das Program täglich muss man das Program 1 Mal ausführen und die erste Zeile mit dem Pfad (Intllij) kopieren. Diesen in eine Textdatein kopieren und als cmd</br>           Abspeichern. Danach in Aufgabenplanung (Windows 10) gehen und eine neue Aufgabe erstellen (Name, Beschreibung, Zeit + Täglich und den Pfad zur Datei angeben).</br>
Textdatei:</br>
	1. Zeile: Damit das Programm funktioniert, muss man sich bei https://www.alphavantage.co/ einen Account erstelle und den Key in die Textdatei aktien eintragen </br>
	2. Zeile: Verzeichnis angeben, bis zu dem Ordner in dem sich ide src befindet!! (z.B. C:\\Users\\IdeaProjects\\Aktie\\), immer \\ und nach Aktie auch!</br>
	3. Zeile: Datenbankname eintragen!
	4. Zeile: Der Wert aus dem der Durchschnitt berechnet werden soll (z.B. 200 -> Standart)</br>
	5. Zeile: API-Wert -> full oder compact (nur 100 Werte, bei full alle seit der Aufzeichnung)</br>
	6. Zeile: alles ab der 5.Zeile sind aktien (max. 5)</br>
JavaFX, Mittelwert:</br>
	Darüber hinaus berechnet das Programm automatisch den Mittelwert der letzten 200 Tage, und gibt die zusammen mit dem Aktienwert </br>
	in JavaFX aus. Dabei achtet das Programm darauf, ob der Close-Wert über den Mittelwert ist, wenn ja, dann ist das Line-Chart grün,</br>
	wenn er unterhalb ist, dann Rot. Diese Charts werden im Ordner wo das Programm lieg abgspeichert, in Image und den dazugehörigen Ordner.</br>
	
![Unbenannt](https://user-images.githubusercontent.com/59961104/105870063-2c053900-5ff8-11eb-919a-6a57a12e78dd.PNG)

Datenbank:</br>
	Die Daten aus der API werden in der Datenbank gespeichert. Dafür muss man eine Verbindung mit MySQL herstellen - Den Connector einbinden und in MySQL Workbench eine</br>         neue Datenbank erstellen, den Name der Datenbank in die Variable DBURL eintragen.  </br>

Was sind close-Werte?</br>
	Close-Wert sind werte, die die Aktie am ende des Tages hat.</br>

Was benötigt man dafür?</br>
	1. https://dev.mysql.com/downloads/connector/j/</br>
	2. https://mvnrepository.com/artifact/commons-io/commons-ior</br>
	3. https://mvnrepository.com/artifact/org.json/json/20140107</br>
	4. JavaFX: https://gluonhq.com/products/javafx/</br>
		Nach dem Downloaden der Dateien 2-4, die Dateien an einem wiederauffinbaren Ort speichern und wie zufpr einbinden</br>
		Nach dem Download zu den anderen 3 Dateien Kopieren und im lib-Ordner die .zip entpacken.</br>
		Danach wieder die gleichen Schritte wie zuvor und nach dem Plus die lib-Datei auswählen und einbinden. Zum Schluss muss man noch unter Run</br> -> Edit 			Configurations bei VM options.</br>
		--module-path Pfad --add-modules javafx.controls,javafx.fxml   eingeben. Bei Pfad muss der Pfad bis zur lib eingefügt werden.</br>
		z. B. /path/to/javafx-sdk-15.0.1/lib javafx.controls,javafx.fxml. Danach wieder Apply und OK.</br>

Für IntelliJ: </br>
	Die Dateien 1,2,3 in einen Ordner speichern, wo sie wieder aufindbar sind. Danach das Projekt starten, </br>
	unter File -> Project Structure -> Libraries gehen. Auf das Plus (neben Minus und Copy) drücken, </br>
	danach Java auswählen und dann die 3 Libraries einzeln einbinden. Zum Schluss auf Apply drücken und OK.</br>

