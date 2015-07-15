/*
 * Diese File ist Inhalt des Programms "Simple-German-Chunker"
 * 
 * Hier findet zum einen die Eingabe, das POS-Tagging, das Chunking 
 * und die Ausgabe statt.
 * Hier werden folgende Dateien und Programme benoetigt und verarbeitet. 
 * Rule.java
 * Token.java
 * input.txt (in UTF-8)
 * regel_auswertung.txt
 * POS-Tagging (siehe Readme)
 * 
 * Die Ausgabedatei output.txt wird beim Programmdurchlauf erzeugt
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class chunker  {

	public static void main(String[] args) {
		
		System.out.println("START");
		final double timeStart = System.currentTimeMillis(); // Beginn der Zeitmessung des Programms
		// POS-TAGGER (Stanford POS-Tagger)
		// Pfadangabe
	    File text = new File ("input.txt"); 									// Speicherort der unbearbeitete Eingabe
		MaxentTagger tagger = new MaxentTagger("taggers/german-fast.tagger");	// verwendeter POS-Tagger 
		// es besteht auch die Moeglichkeit ein anderes Tagging-Modell aus den POS-Tagger zu waehlen

		List<String> unbearbeitet = new ArrayList<String>(); 	// ArrayList fuer den Text der Eingabe
		
		
		
		// Einlesen des Textes aus der Eingabe aus input.txt
		try {
			String line = null; //Zeilenweises Einlesen der Eingabe
			BufferedReader inut  = new BufferedReader(new InputStreamReader(new FileInputStream (text), "UTF8"));	// inut = unbearbeiter Input fuer das Tagging			
			while (( line = inut.readLine()) != null) {
				unbearbeitet.add(line);
			}
			inut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();  
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// POS-Tagging des eingelesenen Textes
		String text_unbearbeitet = ""; // Hilfsvariable fuer das Tagging
		for (int i = 0; i < unbearbeitet.size(); i++){	// For-Schleife fuer die Eingabe, sodass beim Tagging ein String verarbeitet werden kann
			text_unbearbeitet = text_unbearbeitet.concat(unbearbeitet.get(i));
		}
		String tagged = tagger.tagString(text_unbearbeitet); // hier findet das eigentliche POS-Tagging statt, Ergebnis wird in dem String "tagged"  gespeichert
		
		String [] split_tagged = tagged.split(" ");	//Zerlegung des Tagging-Strings an den Leerzeichen
		
		List<String> input = new ArrayList<String>();	// Liste fuer den POS-getaggten Text
		
		for (int i = 0; i < split_tagged.length; i++) {	// das Array "tagged" wird zur weiteren Verarbeitung im Chunker in eine Liste eingelesen
			input.add(split_tagged[i]+"_"+i);
		}
		// ENDE POS-TAGGER


		// CHUNKER
		// Pfadangaben
		File rules_file = new File ("results/regel_auswertung.txt"); 	// Speicherort der trainierten Regeln
		File outputtext = new File ("output.txt");						// Speicherort des Ausgabetext mit Chunks


		List<String> rules = new ArrayList<String>();	// Eingelesene Regeln werden als ArrayList gespeichert
		List<String> output = new ArrayList<String>();	// Ausgabe des Chunkers wird zunaechst als ArrayList erzeugt

		//Einlesen der Regeln
		try {
			String line = null; // Zeilenweises Einlesen der Regeln
			BufferedReader inrf  = new BufferedReader(new InputStreamReader(new FileInputStream (rules_file), "UTF8"));	// inrf = input der Regel-Datei		
			while (( line = inrf.readLine()) != null) {
				rules.add(line);
			}
			inrf.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();  
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Auslesen der trainierten Regeln zum Chunken
		List<String> P0 = new ArrayList <String>();			// Regeln fuer die Position 0
		List<String> P0M1 = new ArrayList <String>();		// Regeln fuer die Position -1,0
		List<String> P0P1 = new ArrayList <String>();		// Regeln fuer die Position 0,1
		List<String> P0M2M1 = new ArrayList <String>();		// Regeln fuer die Position -2,-1,0
		List<String> P0M1P1 = new ArrayList <String>();		// Regeln fuer die Position -1,0,1
		List<String> P0P1P2 = new ArrayList <String>();		// Regeln fuer die Position 0,1,2

		List<Double> accP0 = new ArrayList <Double>();		// Genauigkeit der Regeln fuer die Position 0
		List<Double> accP0M1 = new ArrayList <Double>();	// Genauigkeit der Regeln fuer die Position -1,0
		List<Double> accP0P1 = new ArrayList <Double>();	// Genauigkeit der Regeln fuer die Position 0,1
		List<Double> accP0M2M1 = new ArrayList <Double>();	// Genauigkeit der Regeln fuer die Position -2,-1,0
		List<Double> accP0M1P1 = new ArrayList <Double>();	// Genauigkeit der Regeln fuer die Position -1,0,1
		List<Double> accP0P1P2 = new ArrayList <Double>();	// Genauigkeit der Regeln fuer die Position 0,1,2

		//Setzen des unteren Grenzwertes fuer die Genauigkeit der trainierten Regeln
		double grenze1 = 50.0;
		// Auslesen der trainierten Genauigkeit und Regeln aus rules.txt
		
		/* die trainierten Regeln wurden in einer bestimmten Reihenfolge gespeichert und koennen nun auch in dieser gleichen Reihenfolge
		 * wieder ausgelsen werden, damit zum einen die Regel selbst und deren trainierte Genauigkeit bestimmt werden kann. 
		 */

		for (int i = 0; i < rules.size(); i++){		// Einlesen der Regeln 
			Rule rul1 = new Rule (rules.get(i));
			String accs = rul1.getArruracy();		// Auslesen der Genauigkeit aus den einzelnen Regeln
			double acc = Double.parseDouble(accs); 	// Speicherung der Genauigkeit als double
			
			// Regeln fuer die Position 0 befinden sich in "rules.txt" in den Zeilen 0-330
			if (i < 330 && acc > grenze1) {	// Abfrage der Regeln in der Zeile i unter der Bedingung, dass die trainierte Genauigkeit der Regel den oberen Grenzwert Ã¼bersteigt	
				P0.add(rules.get(i));		// Speicherung der aktuellen Regel in der vorgesehenen ArrayList 
				accP0.add(acc);				// Speicherung der Genauigkeit der aktuellen Regel in der vorgesehenen ArrayList
			}
			// diese Abfrage der Regeln wiederholt sich auch bei den Regeln, die eine andere Laenge und Position besitzen 
			
			// Regeln fuer die Position -1,0 befinden sich in "rules.txt" in den Zeilen 331-18480
			if (i > 330 && i <= 18480 && acc > grenze1){		
				P0M1.add(rules.get(i));						
				accP0M1.add(acc);							
			}
			// Regeln fuer die Position 0,1 befinden sich in "rules.txt" in den Zeilen 18481-36630
			if (i > 18480 && i <= 36630 && acc > grenze1){
				P0P1.add(rules.get(i));
				accP0P1.add(acc);
			}
			// Regeln fuer die Position -2,-1,0 befinden sich in "rules.txt" in den Zeilen 36631-1034880
			if (i > 36630 && i <= 1034880 && acc > grenze1){
				P0M2M1.add(rules.get(i));
				accP0M2M1.add(acc);
			}
			// Regeln fuer die Position -1,0,1 befinden sich in "rules.txt" in den Zeilen 1034881-2033130
			if (i > 1034880 && i <= 2033130 && acc > grenze1){
				P0M1P1.add(rules.get(i));
				accP0M1P1.add(acc);
			}
			// Regeln fuer die Position 0,1,2 befinden sich in "rules.txt" in den Zeilen ab 2033131 bis Ende der "rules.txt"-Datei
			if (i > 2033130 && acc > grenze1){
				P0P1P2.add(rules.get(i));
				accP0P1P2.add(acc);
			}
		}
		

		//Chunking-Algorithmus
		/*
		 * Abfrage der Regeln nach folgender Reihenfolge:
		 * (Zahlen sind die betrachteten Positionen, um das aktuelle Wort an der Position 0)
		 * 1. -2,-1,0
		 * 2. -1,0,1
		 * 3. 0,1,2
		 * 4. -1,0
		 * 5. 0,1
		 * 6. 0
		 */

		for (int i = 0; i < input.size(); i++) {	// For-Schleife zum Durchlaufen der Eingabe aus "input.txt"
			String posM2 = "";	// Inistialisierung einer String-Variable fuer das POS-Tag an der Position -2
			String posM1 = "";	// Inistialisierung einer String-Variable fuer das POS-Tag an der Position -1
			String posP0 = "";	// Inistialisierung einer String-Variable fuer das POS-Tag an der Position 0
			String posP1 = "";	// Inistialisierung einer String-Variable fuer das POS-Tag an der Position 1
			String posP2 = "";	// Inistialisierung einer String-Variable fuer das POS-Tag an der Position 2
			
			/*
			 * Bei i < 2 muss verhindert werden, dass der Chunker versucht die Positionen -2 und -1 
			 * zu belegen, da er sonst versuchen wuerde auf negative i´s zuzugreifen. Dadurch wird ein 
			 * ArrayOutOfBounds - Fehler verursachet werden. 
			 * Um bei den Positionen 1 und 2 zu verhinderen, dass hier i > input.size() wird und 
			 * dadurch einen ArrayOutOfBounds - Fehler verursacht, wird auch hier abgefragt, ob es einen 
			 * weiteren Eintrag nach i gibt. 
			 */
			// Token und POS-Tag an der Position -2
			if (i < 2){
				
			}
			else {
				Token tokM2 = new Token (input.get(i-2));	// Auslesen des Tokens an der Position -2	
				posM2 = tokM2.getTag();						// POS-Tag des Tokens an der Position -2
			}
			
			// Token und POS-Tag an der Position -1
			if (i < 1){
				
			}
			else {
				Token tokM1 = new Token (input.get(i-1));	// Auslesen des Tokens an der Position -1
				posM1 = tokM1.getTag();						// POS-Tag des Tokens an der Position -1
			}
			
			// Token und POS-Tag an der Position 0
			Token tokP0 = new Token (input.get(i));			// Auslesen des Tokens an der Position 0
			posP0 = tokP0.getTag();							// POS-Tag des Tokens an der Position 0
			
			// Token und POS-Tag an der Position 2
			if (i >= input.size()-2) {
			}
			else {
				Token tokP2 = new Token (input.get(i+2));	// Auslesen des Tokens an der Position 2
				posP2 = tokP2.getTag();						// POS-Tag des Tokens an der Position 2
			}
			
			// Token und POS-Tag an der Position 1
			if (i >= input.size()-1) {
			}
			else {
				Token tokP1 = new Token (input.get(i+1));	// Auslesen des Tokens an der Position 1
				posP1 = tokP1.getTag();						// POS-Tag des Tokens an der Position 1
			}
			
			String ctagMax = "";	// Inistialisierung des Chunk-Tags - Strings fuer die Anwendung der Regel
								// hier soll, wenn vorhanden das Ergebnis der Regeln stehen. 
			double accMax = 0.0;	// Inistialisierung der Genauigkeit - double Variable 
								// hier soll, die Genauigkeit der hoechstbewerteten Regel stehen
			
			
			//Beginn des eigentlichen Algorithmus
			//Algorithmus fuer Positionen -2,-1,0
			for (int j = 0; j < P0M2M1.size(); j++){ // For-Schleife, die alle Regeln fuer die Positionen -2,-1,0 durchlaeuft
				Rule rul1 = new Rule (P0M2M1.get(j));	// Auslesen der Regel an der Position j 
				String pos1 = rul1.getPostag()[0];		// Auslesen des POS-Tags aus der aktuellen Regeln an der Position -2
				String pos2 = rul1.getPostag()[1];		// Auslesen des POS-Tags aus der aktuellen Regeln an der Position -1
				String pos3 = rul1.getPostag()[2];		// Auslesen des POS-Tags aus der aktuellen Regeln an der Position 0
				String ctag = rul1.getChunktag();		// Auslesen des Chunk-Tags aus der aktuellen Regeln

				/* folgende if-Bedingung beinhaltet: 
				 * Vergleich der POS-Tags aus den Regeln mit den POS-Tags aus der Eingabe unter der Bedingung 
				 * dass es die Genauigkeit ueber der unteren Grenze liegt. 
				 */
				if (pos1.equals(posM2) && pos2.equals(posM1) && pos3.equals(posP0) && accP0M2M1.get(j) > grenze1){
					/* zusaetzliche Bedingung fuer die Genauigkeit ist, dass es keine Regel gibt, die 
					*  eine Genauigkeit besitzt, die hoeher ist als die aktuelle.
					*/
					if (accP0M2M1.get(j) > accMax){ // wenn aktuelle Genauigkeit hoeher als die bisher maximale Genauigkeit 
													// ist, wird die aktuelle als neue maximale Genauigkeit gesetzt 
						accMax = accP0M2M1.get(j);	
						ctagMax = ctag;				// das aktuelle Chunk-Tag ist das neue Ergebnis der Regel
					}
				}
			}
			if (ctagMax == "") { /* Wenn keine ausreichend gewertete Regel gefunden wurde, 
			 					* wird in weiteren Regeln nach einer passenden gesucht.
			 					*/
				/*
				 * Die Algorithmen fuer die Regeln, die andere Positionen als Kriterium beinhalten 
				 * laufen ab, wie der Algorithmus fuer die Regeln mit den Positionen -2,-1,0
				 */
				//Algorithmus fuer Positionen -1,0,1
				for (int j = 0; j < P0M1P1.size(); j++){	// For-Schleife, die alle Regeln fuer die Positionen -1,0,1 durchlaeuft
					Rule rul1 = new Rule (P0M1P1.get(j));	// Auslesen der Regel an der Position j 
					String pos1 = rul1.getPostag()[0];		// Auslesen des POS-Tags aus der aktuellen Regeln an der Position -1
					String pos2 = rul1.getPostag()[1];		// Auslesen des POS-Tags aus der aktuellen Regeln an der Position 0
					String pos3 = rul1.getPostag()[2];		// Auslesen des POS-Tags aus der aktuellen Regeln an der Position 1
					String ctag = rul1.getChunktag();		// Auslesen des Chunk-Tags aus der aktuellen Regeln

					if (pos1.equals(posM1) && pos2.equals(posP0) && pos3.equals(posP1) && accP0M1P1.get(j) > grenze1){
						if (accP0M1P1.get(j) > accMax){
							accMax = accP0M1P1.get(j);
							ctagMax = ctag;
						}
					}
				}
				if (ctagMax == "") {			
					//Algorithmus fuer Positionen 0,1,2
					for (int j = 0; j < P0P1P2.size(); j++){	// For-Schleife, die alle Regeln fuer die Positionen 0,1,2 durchlaeuft
						Rule rul1 = new Rule (P0P1P2.get(j));	// Auslesen der Regel an der Position j 
						String pos1 = rul1.getPostag()[0];		// Auslesen des POS-Tags aus der aktuellen Regeln an der Position 0
						String pos2 = rul1.getPostag()[1];		// Auslesen des POS-Tags aus der aktuellen Regeln an der Position 1
						String pos3 = rul1.getPostag()[2];		// Auslesen des POS-Tags aus der aktuellen Regeln an der Position 2
						String ctag = rul1.getChunktag();		// Auslesen des Chunk-Tags aus der aktuellen Regeln

						if (pos1.equals(posP0) && pos2.equals(posP1) && pos3.equals(posP2) && accP0P1P2.get(j) > grenze1){
							if (accP0P1P2.get(j) > accMax){
								accMax = accP0P1P2.get(j);
								ctagMax = ctag;
							}
						}
					}
					if (ctagMax == ""){
						//Algorithmus fuer Positionen -1,0
						for (int j = 0; j < P0M1.size(); j++){		// For-Schleife, die alle Regeln fuer die Positionen -1,0 durchlaeuft
							Rule rul1 = new Rule (P0M1.get(j));		// Auslesen der Regel an der Position j 
							String pos1 = rul1.getPostag()[0];		// Auslesen des POS-Tags aus der aktuellen Regeln an der Position -1
							String pos2 = rul1.getPostag()[1];		// Auslesen des POS-Tags aus der aktuellen Regeln an der Position 0
							String ctag = rul1.getChunktag();		// Auslesen des Chunk-Tags aus der aktuellen Regeln

							if (pos1.equals(posM1) && pos2.equals(posP0)&& accP0M1.get(j) > grenze1){
								if (accP0M1.get(j) > accMax){
									accMax = accP0M1.get(j);
									ctagMax = ctag;
								}
							}
						}
						if (ctagMax == "") {
							//Algorithmus fuer Positionen 0,1
							for (int j = 0; j < P0P1.size(); j++){		// For-Schleife, die alle Regeln fuer die Positionen 0,1 durchlaeuft
								Rule rul1 = new Rule (P0P1.get(j));		// Auslesen der Regel an der Position j 
								String pos1 = rul1.getPostag()[0];		// Auslesen des POS-Tags aus der aktuellen Regeln an der Position 0
								String pos2 = rul1.getPostag()[1];		// Auslesen des POS-Tags aus der aktuellen Regeln an der Position 1
								String ctag = rul1.getChunktag();		// Auslesen des Chunk-Tags aus der aktuellen Regeln

								if (pos1.equals(posP0) && pos2.equals(posP1)&& accP0P1.get(j) > grenze1){
									if (accP0P1.get(j) > accMax){
										accMax = accP0P1.get(j);
										ctagMax = ctag;
									}
								}
							}
							if (ctagMax == ""){
								//Algorithmus fuer Positionen -0
								for (int j = 0; j < P0.size(); j++){	// For-Schleife, die alle Regeln fuer die Positionen 0 durchlaeuft
									Rule rul1 = new Rule (P0.get(j));	// Auslesen der Regel an der Position j 
									String posr = rul1.getPostag()[0];	// Auslesen des POS-Tags aus der aktuellen Regeln an der Position 0
									String ctag = rul1.getChunktag();	// Auslesen des Chunk-Tags aus der aktuellen Regeln

									if (posr.equals(posP0) && accP0.get(j) > grenze1){
										if (accP0.get(j) > accMax){
											accMax = accP0.get(j);
											ctagMax = ctag;
										}
									}
								}
							}
						}
					}
				}
			}
			else {
			}
			if (ctagMax == ""){ // Ausgabe, wenn keine Regel gefunden wurde
				//Hinzufuegen des Tokens ohne Chunk-Tag
				output.add(input.get(i));
			}
			else {
				//Hinzufuegen des Tokens mit dem neu erworbenen Chunk-Tag
				output.add(input.get(i)+"_"+ctagMax);
			}
		}
		// ENDE CHUNKER

		// AUSGABE
		//gechunkter output wird in output.txt geschrieben
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(outputtext);

			for (int i=0;i<output.size();i++){
				printWriter.println(output.get(i)); // Zeilenweise Ausgabe in "output.txt"
			}
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			if ( printWriter != null ) {
				printWriter.close();
			}

		}
		// ENDE AUSGABE
		// zeitliche Messung des Chunkers fur input.size()- Tokens 
		final double timeEnd = System.currentTimeMillis(); 
		final double time = ((timeEnd-timeStart)/1000);
		System.out.println("Dauer des Chunkers fuer "+input.size()+" Tokens betraegt: "+time+" Sek.");
		System.out.println("END");
	}
}
