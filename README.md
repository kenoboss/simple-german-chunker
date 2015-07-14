# simple-german-chunker
## Deutsch

Dies ist ein Projekt im Rahmen des Projektseminares der Studierenden der Universität Trier des Faches Computerlinguistik.

Bei dem Programm handelt es sich um einen Chunker (eine Form des Parsings), der für die deutsche Sprache trainiert ist, mithilfe des Korpus Tuebda-DZ 9.0. 

In seiner jetzigen Form funktioniert der Chunker nur mit der Hilfe eines Eclipse-Editors (aktuell verwendet: Eclipse IDE for Java Developers, Version: Luna Service Release 2 (4.4.2), Build id: 20150219-0600). Java wird benötigt.
Zudem wird für die Vorverarbeitung der Eingabe ein POS-Tagger benötigt (aktuell verwendet: Stanford POS-Tagger, Version 3.5.2, Release: 20.04.2015) 

### Installation

1. Download von Eclipse IDE for Java Developers auf [https://eclipse.org/downloads/] (https://eclipse.org/downloads/)
2. Download von Stanford POS-Tagger auf [http://nlp.stanford.edu/software/tagger.shtml] (http://nlp.stanford.edu/software/tagger.shtml)
3. Entpacken der .zip-Datei von Eclipse in ein beliebiges Verzeichnis.
4. Starten der eclipse Anwendung
5. Erstellung eines Java-Projektes: File -> New -> Java Project -> Benennung des Projektes (z.B. simple-german-chunker) -> Finish (sonstige Einstellungen können als *default* übernommmen werden. 
6. Erstellung eines neuen Ordners im Java-Projekt (Benennung: taggers)
7. Entpacken der .zip-Datei des Stanford POS-Taggers in ein beliebiges Verzeichnis (nicht das gleiche wie Eclipse) und Öffnen der neu erstellten Ordnerstruktur.
8. In dem Ordner des Stanford POS-Taggers befindet sich ein weiterer Ordner *models*. Aus diesem Ordner werden folgende Dateien in den in Punkt 6 erstellten Ordner *taggers* kopiert: *german-fast.tagger* und *german-fast.tagger.props*. 
9. In Eclipse müssen nun ein paar Veränderungen am Projekt vorgenommmen werden. Dafür Rechtsklick auf das erstellte Java-Projekt -> Build Path -> Configure Build Path
10. In dem neu geöffneten Fenster auf das *libraries* Tab klicken, hier nun auf den Button *Add External Jars* klicken.
11. Hier kann jetzt die benötigte .jar-Datei *stanford-postagger.jar* ausgewählt werden, diese befindet sich in dem Ordner des Stanford POS-Taggers. 
12. Nun ist das Programm einsetzbar. 

### Benutzung

#### Eingabe

#### Ausgabe


## English
