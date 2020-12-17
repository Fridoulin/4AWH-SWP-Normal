Was macht das Programm?
Allgemein:
	Dieses Programm dient dazu, dass man aus einer API die close-Wert und das dazugehörige Datum auszulesen.
	Dabei kann man des Unternehmen (z.B. AAPL (Apple), TSLA (Tesla), usw.) auswählen von welchem man die Aktienwerte haben will. 
JavaFX, Mittelwert:
	Darüber hinaus berechnet das Programm automatisch den Mittelwert der letzten 200 Tage, und gibt die zusammen mit dem Aktienwert 
	in JavaFX aus. Dabei achtet das Programm darauf, ob der Close-Wert über den Mittelwert ist, wenn ja, dann ist das Line-Chart grün,
	wenn er unterhalb ist, dann Rot.
	![JavaFXGrün](https://user-images.githubusercontent.com/59961104/102459896-864cca80-4046-11eb-9104-3d2050661676.jpeg)
	![JavaFXRot](https://user-images.githubusercontent.com/59961104/102459906-89e05180-4046-11eb-9de1-454d9f502528.jpeg)

  
Datenbank:
	Alle Werte, die das Program berechnt oder sich von der API holt, werden in einer Datenbank gespeichert. Bei jedem Aufruf werden die neuen
	Werte in der Datenbank gespeichert, damit die Grafik immer genauer wird. 

Was sind close-Werte?
	Close-Wert sind werte, die die Aktie am ende des Tages hat.

Was benötigt man dafür?
	1. commons-io-2.7.jar
	2. java.json.jar
	3. sqlite-jdbx-3.32.3.2.jar

Für IntelliJ: 
	Die Dateien 1,2,3 in einen Ordner speichern, wo sie wieder aufindbar sind. Danach das Projekt starten, 
	unter File -> Project Structure -> Libraries gehen. Auf das Plus (neben Minus und Copy) drücken, 
	danach Java auswählen und dann die 3 Libraries einzeln einbinden. Zum Schluss auf Apply drücken und OK.

