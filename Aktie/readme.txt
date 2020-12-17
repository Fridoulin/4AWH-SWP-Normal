Was macht das Programm?
Allgemein:
	Dieses Programm dient dazu, dass man aus einer API die close-Wert und das dazugehörige Datum auszulesen.
	Dabei kann man des Unternehmen (z.B. AAPL (Apple), TSLA (Tesla), usw.) auswählen von welchem man die Aktienwerte haben will. 
JavaFX, Mittelwert:
	Darüber hinaus berechnet das Programm automatisch den Mittelwert der letzten 200 Tage, und gibt die zusammen mit dem Aktienwert 
	in JavaFX aus. Dabei achtet das Programm darauf, ob der Close-Wert über den Mittelwert ist, wenn ja, dann ist das Line-Chart grün,
	wenn er unterhalb ist, dann Rot.
![JavaFXRot](https://user-images.githubusercontent.com/59961104/102460309-1723a600-4047-11eb-8ce7-9c6304a625dc.jpg)

![JavaFXGrün](https://user-images.githubusercontent.com/59961104/102460313-17bc3c80-4047-11eb-9bb1-24728bc38dc3.jpg)
  
Datenbank:
	Alle Werte, die das Program berechnt oder sich von der API holt, werden in einer Datenbank gespeichert. Bei jedem Aufruf werden die neuen
	Werte in der Datenbank gespeichert, damit die Grafik immer genauer wird. 

Was sind close-Werte?
	Close-Wert sind werte, die die Aktie am ende des Tages hat.

Was benötigt man dafür?
	1. commons-io-2.7.jar
	2. java.json.jar
	3. sqlite-jdbx-3.32.3.2.jar
	4. JavaFX: https://gluonhq.com/products/javafx/
		Nach dem Download zu den anderen 3 Dateien Kopieren und im lib-Ordner die .zip entpacken. Danach wieder die gleichen Schritte 
		wie zuvor und nach dem Plus die lib-Datei auswählen und einbinden. Zum Schluss muss man noch unter Run -> Edit Configurations bei VM options.
		--module-path Pfad --add-modules javafx.controls,javafx.fxml   eingeben. Bei Pfad muss der Pfad bis zur lib eingefügt werden.
		z. B. /path/to/javafx-sdk-15.0.1/lib . Danach wieder Apply und OK.

Für IntelliJ: 
	Die Dateien 1,2,3 in einen Ordner speichern, wo sie wieder aufindbar sind. Danach das Projekt starten, 
	unter File -> Project Structure -> Libraries gehen. Auf das Plus (neben Minus und Copy) drücken, 
	danach Java auswählen und dann die 3 Libraries einzeln einbinden. Zum Schluss auf Apply drücken und OK.

