# simple-german-chunker
## Deutsch

Dies ist ein Projekt im Rahmen des Projektseminares der Studierenden der Universität Trier des Faches Computerlinguistik.

Bei dem Programm handelt es sich um einen Chunker (eine Form des Parsings), der für die deutsche Sprache trainiert ist, mithilfe des Korpus Tuebda-DZ 9.0. 

Es werden folgende Chunks produziert: 
* B-NC  - Beginn eines Nominalchunks
* I-NC  - weiterer Eintrag eines Nominalchunks
* B-VC  - Beginn eines Verbalchunks
* I-VC  - weiterer Eintrag eines Verbalchunks
* B-PC  - Beginn eines Präpositionalchunks
* I-PC  - weiterer Eintrag eines Präpositionalchunks

In seiner jetzigen Form funktioniert der Chunker nur mit der Hilfe eines Eclipse-Editors (aktuell verwendet: Eclipse IDE for Java Developers, Version: Luna Service Release 2 (4.4.2), Build id: 20150219-0600). Java wird benötigt.
Zudem wird für die Vorverarbeitung der Eingabe ein POS-Tagger benötigt (aktuell verwendet: Stanford POS-Tagger, Version 3.5.2, Release: 20.04.2015) 

### Installation

1. Download von Eclipse IDE for Java Developers auf [https://eclipse.org/downloads/] (https://eclipse.org/downloads/)
2. Download von Stanford POS-Tagger auf [http://nlp.stanford.edu/software/tagger.shtml] (http://nlp.stanford.edu/software/tagger.shtml)
3. Entpacken der .zip-Datei von Eclipse in ein beliebiges Verzeichnis.
4. Starten der eclipse Anwendung
5. Erstellung eines Java-Projektes: File -> New -> Java Project -> Benennung des Projektes (z.B. simple-german-chunker) -> Finish (sonstige Einstellungen können als *default* übernommmen werden. 
6. Die .java-Dateien aus diesem Repository aus src in den src-Ordner des Java-Projektes kopieren und aus dem Ordner *results* die Datei *trained_rules.txt* in den Ordner des Java-Projektes kopieren. Beispielpfad für *trained_rules*: ../../workspace/simple-german-chunker/results/trained_rules.txt (die Datei trained_rules.txt ist noch nicht verfügbar)
7. Erstellung eines neuen Ordners im Java-Projekt (Benennung: taggers)
8. Entpacken der .zip-Datei des Stanford POS-Taggers in ein beliebiges Verzeichnis (nicht das gleiche wie Eclipse) und Öffnen der neu erstellten Ordnerstruktur.
9. In dem Ordner des Stanford POS-Taggers befindet sich ein weiterer Ordner *models*. Aus diesem Ordner werden folgende Dateien in den in Punkt 7 erstellten Ordner *taggers* kopiert: *german-fast.tagger* und *german-fast.tagger.props*. 
10. In Eclipse müssen nun ein paar Veränderungen am Projekt vorgenommmen werden. Dafür Rechtsklick auf das erstellte Java-Projekt -> Build Path -> Configure Build Path
11. In dem neu geöffneten Fenster auf das *libraries* Tab klicken, hier nun auf den Button *Add External Jars* klicken.
12. Hier kann jetzt die benötigte .jar-Datei *stanford-postagger.jar* ausgewählt werden, diese befindet sich in dem Ordner des Stanford POS-Taggers. 
13. Nun ist das Programm einsetzbar. 

### Benutzung
#### Eingabe

Bei der Eingabe handelt es sich um eine Textdatei *input.txt* 
Diese muss im workspace des simple-german-chunker erstellt werden. (nicht im src-Ordner) 
Also wäre ein Beispielpfad: ../../workspace/simple-german-chunker/input.txt
Nun kann ein beliebiger Text in diese Datei kopiert oder geschrieben werden, der gechunkt werden soll.

Eine mögliche Eingabe wäre: Das ist ein kleiner Satz. 

Nun zurück in Eclipse: Hier kann nun mit Strg + F11 oder durch das klicken des Run-Buttons das Programm gestartet werden.

#### Ausgabe

Bei der Ausgabe handelt es sich wieder um eine Text-Datei *output.txt*, die sich in dem gleichen Verzeichnis, wie die *input.txt* befindet. 

Folgende Ausgabe entsteht bei der Eingabe von: Das ist ein kleiner Satz. 

* Das_PDS_0_B-NC
* ist_VAFIN_1_B-VC
* ein_ART_2_B-NC
* kleiner_ADJA_3_I-NC
* Satz_NN_4_I-NC
* ._$._5


## English
