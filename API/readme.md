<h1>Feiertagzähler Erklärung</h1>

**Was macht das Programm?**
Diese Programm dient dazu, dass man herausfinden kann, wie viele Feiertage in den nächsten Jahren sind. Es berechnet die Feiertage im nächsten.
Jahr, kann dies aber auch die für die nächsten 10 oder 100 Jahre, dazu wird eine API.
Danach werden die Daten in einer Datenbank gespeichert und visuell in einem Balkendiagramm dargestellt, mithilfe von JavaFx.
Dieses Programm ist für die österreichischen Feiertage geschrieben.


**Was benötigt man dafür?**
1. commons-io-2.7.jar
2. java.json.jar
3. sqlite-jdbx-3.32.3.2.jar
4. JavaFX
Dateien sind im Ordner "notwendige Dateien" (außer JavaFX)

_Für IntelliJ:_
Die Dateien 1,2,3 in einen Ordner speichern, wo sie wieder aufindbar sind. Danach das Projekt starten, unter File -> Project Structure -> Libraries gehen. Auf das Plus (neben Minus und Copy) drücken, danach Java auswählen und dann die 3 Libraries einzeln einbinden. Zum Schluss
auf Apply drücken und OK.

_JavaFX:_
https://gluonhq.com/products/javafx/
Nach dem Download zu den anderen 3 Dateien Kopieren und im lib-Ordner die .zip entpacken. Danach wieder die gleichen Schritte 
wie zuvor und nach dem Plus die lib-Datei auswählen und einbinden. Zum Schluss muss man noch unter Run -> Edit Configurations bei VM options.
--module-path Pfad --add-modules javafx.controls,javafx.fxml   eingeben. Bei Pfad muss der Pfad bis zur lib eingefügt werden.
z. B. /path/to/javafx-sdk-15.0.1/lib . Danach wieder Apply und OK. (50Jahre)

![JavaFX](https://user-images.githubusercontent.com/59961104/98450277-d7aa9580-213b-11eb-9b17-564107d45f7a.JPG)

_Datenbank:_
Um eine Datenbank anzulegen, muss man im Programm nach der Methode connect() suchen. Darin ist ein try, das einen String url beinhaltet.
Als nächstes sucht man sich einen Ort, an dem die Datenbank gespeichert werden soll und den Pfad kopieren. Dieser wird dann
Bei url nach "jdbc:sqlite:" eingesetzt (String url = "jdbc:sqlite:Pfad\\API.db";). API.db ist nur ein Beispielname wie die Datei mit der 
Datenbank heißt. Den Pfad muss man außerdem noch bei createNewTable() und bei der connection() eingesetzt werden.


**Wie wird es ausgeführt?**

Nach dem Einbinden der Dateien muss man nur noch auf Run drücken und das Programm arbeitet. 
Wenn man die Datenbank ändern will, muss man sie einfach wieder löschen und es wird eine neue erzeugt.
