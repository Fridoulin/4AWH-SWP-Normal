Was macht das Programm?
Diese Progamm dient dazu, dass man herausfinden kann, wieviele Feiertage in den nächsten Jahren sind. Es berechnet die Feiertage im nächsten
Jahr, kann dies aber auch die für die nächsten 10 oder 100 Jahre. Es verwendet dazu eine API, verwemdet die relevanten 
Dateien und beginnt dann zu arbeiten. Danach werden die Daten in einer Datenbank gespeichtert und visuell in einem Balkendiagramm Dargestellt, mithilfe von JavaFX.
Dieses Programm ist für die österreichischen Feiertage geschrieben



Was benötigt man dafür?
1. commons-io-2.7.jar
2. java.json.jar
3. sqlite-jdbx-3.32.3.2.jar
4. JavaFX

Dateien sind im Ordner "notwendige Dateien" (ausser JavaFX)
Für IntelliJ
Die Dateien 1,2,3 in einen Ordner speichern, wo sie wiederaufindbar sind. Danach das Projekt starten, unter File -> Project Structure ->
Libraries gehen. Auf das Plus (neben Minus und Copy) drücken, danach Java auswählen und dann die 3 Libraries einzeln einbinden. Zum Schluss
auf Apply drücken und OK.

Java FX:
https://gluonhq.com/products/javafx/
Nach dem Download zu den anderen 3 Datein Kopieren und im lib-Ordner die .zip entpacken. Danach wieder die gleichen Schritte wie zuvor und 
nach dem Plus die lib-Datei auswählen und einbinden. Zum Schluss muss man noch unter Run -> Edit Configurations bei VM options
--module-path Pfad --add-modules javafx.controls,javafx.fxml   eingeben. Bei Pfad muss der Pfad bis zur lib eingefügt werden
z.B. /path/to/javafx-sdk-15.0.1/lib . Danach wieder Apply und OK

Datenbank:
Um eine Datenbank anzulegen, muss man im Programm nach der Methode connect() suchen. Darin ist ein try, das einen String url beinhaltet.
Als nächstes muss such soll man sich einen Ort suchen, an dem die Datenbak gespeichert werden soll und den Pfad kopieren. Dieser wird dann
bei url nach "jdbc:sqlite:" eingesetzt (String url = "jdbc:sqlite:Pfad\\API.db";). API.db ist nur ein Beispielname wie die Datei mit der 
Datenbank heißt. Den Pfad muss man ausserdem noch bei createNewTable() und bei der connection() eingesetz werden.



Wie wird es ausgeführt?
Nach den Einbinden der Dateien muss man nur noch auf Run drücken und das Programm arbeitet. 
Wenn man die Datenbank ändern will, muss man sie einfach wieder löschen und es wird eine neue erzeugt