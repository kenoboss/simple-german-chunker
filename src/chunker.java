/*
 * Diese File ist Inhalt des Programms "Simple-German-Chunker"
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

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class chunker  {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		System.out.println("START");
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
		List<String> output = new ArrayList<String>();	// Ausgabe des Chunkers wird zunächst als ArrayList erzeugt

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
		
		/* die trainierten Regeln wurden in einer bestimmten Reihenfolge gespeichert und können nun auch in dieser gleichen Reihenfolge
		 * wieder ausgelsen werden, damit zum einen die Regel selbst und deren trainierte Genauigkeit bestimmt werden kann. 
		 */

		for (int i = 0; i < rules.size(); i++){		// Einlesen der Regeln 
			Rule rul1 = new Rule (rules.get(i));
			String accs = rul1.getArruracy();		// Auslesen der Genauigkeit aus den einzelnen Regeln
			double acc = Double.parseDouble(accs); 	// Speicherung der Genauigkeit als double
			
			// Regeln fuer die Position 0 befinden sich in "rules.txt" in den Zeilen 0-330
			if (i < 330 && acc > grenze1) {	// Abfrage der Regeln in der Zeile i unter der Bedingung, dass die trainierte Genauigkeit der Regel den oberen Grenzwert übersteigt	
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
		int start = 0;
		for (int i = start; i < input.size(); i++) {
			String posM2 = "";
			String posM1 = "";
			if (i < 2){
				
			}
			else {
				Token tokM2 = new Token (input.get(i-2));
				posM2 = tokM2.getTag();
			}
			if (i < 1){
				
			}
			else {
				Token tokM1 = new Token (input.get(i-1));
				posM1 = tokM1.getTag();
			}

			Token tokP0 = new Token (input.get(i));
			String posP0 = tokP0.getTag();
			String posP1 = "";
			String posP2 = "";
			if (i >= input.size()-2) {
			}
			else {
				Token tokP2 = new Token (input.get(i+2));
				posP2 = tokP2.getTag();
			}
			if (i >= input.size()-1) {
			}
			else {
				Token tokP1 = new Token (input.get(i+1));
				posP1 = tokP1.getTag();
			}
			
			String ctagF = "";
			double accF = 0.0;
			double grenze2 = grenze1;
			//Beginn des eigentlichen Algorithmuses
			//Algorithmus fuer Positionen -2,-1,0
			for (int j = 0; j < P0M2M1.size(); j++){
				Rule rul1 = new Rule (P0M2M1.get(j));
				String pos1 = rul1.getPostag()[0];
				String pos2 = rul1.getPostag()[1];
				String pos3 = rul1.getPostag()[2];
				String ctag = rul1.getChunktag();

				if (pos1.equals(posM2) && pos2.equals(posM1) && pos3.equals(posP0) && accP0M2M1.get(j) > grenze2){
					if (accP0M2M1.get(j) > accF){
						accF = accP0M2M1.get(j);
						ctagF = ctag;
					}
				}
			}
			if (ctagF == "") {
				//Algorithmus fuer Positionen -1,0,1
				for (int j = 0; j < P0M1P1.size(); j++){
					Rule rul1 = new Rule (P0M1P1.get(j));
					String pos1 = rul1.getPostag()[0];
					String pos2 = rul1.getPostag()[1];
					String pos3 = rul1.getPostag()[2];
					String ctag = rul1.getChunktag();

					if (pos1.equals(posM1) && pos2.equals(posP0) && pos3.equals(posP1) && accP0M1P1.get(j) > grenze2){
						if (accP0M1P1.get(j) > accF){
							accF = accP0M1P1.get(j);
							ctagF = ctag;
						}
					}
				}
				if (ctagF == "") {			
					//Algorithmus fuer Positionen 0,1,2
					for (int j = 0; j < P0P1P2.size(); j++){
						Rule rul1 = new Rule (P0P1P2.get(j));
						String pos1 = rul1.getPostag()[0];
						String pos2 = rul1.getPostag()[1];
						String pos3 = rul1.getPostag()[2];
						String ctag = rul1.getChunktag();

						if (pos1.equals(posP0) && pos2.equals(posP1) && pos3.equals(posP2) && accP0P1P2.get(j) > grenze2){
							if (accP0P1P2.get(j) > accF){
								accF = accP0P1P2.get(j);
								ctagF = ctag;
							}
						}
					}
					if (ctagF == ""){
						//Algorithmus fuer Positionen -1,0
						for (int j = 0; j < P0M1.size(); j++){
							Rule rul1 = new Rule (P0M1.get(j));
							String pos1 = rul1.getPostag()[0];
							String pos2 = rul1.getPostag()[1];
							String ctag = rul1.getChunktag();

							if (pos1.equals(posM1) && pos2.equals(posP0)&& accP0M1.get(j) > grenze2){
								if (accP0M1.get(j) > accF){
									accF = accP0M1.get(j);
									ctagF = ctag;
								}
							}
						}
						if (ctagF == "") {
							//Algorithmus fuer Positionen 0,1
							for (int j = 0; j < P0P1.size(); j++){
								Rule rul1 = new Rule (P0P1.get(j));
								String pos1 = rul1.getPostag()[0];
								String pos2 = rul1.getPostag()[1];
								String ctag = rul1.getChunktag();

								if (pos1.equals(posP0) && pos2.equals(posP1)&& accP0P1.get(j) > grenze2){
									if (accP0P1.get(j) > accF){
										accF = accP0P1.get(j);
										ctagF = ctag;
									}
								}
							}
							if (ctagF == ""){
								//Algorithmus fuer Positionen -0
								for (int j = 0; j < P0.size(); j++){
									Rule rul1 = new Rule (P0.get(j));
									String posr = rul1.getPostag()[0];
									String ctag = rul1.getChunktag();

									if (posr.equals(posP0) && accP0.get(j) > grenze2){
										if (accP0.get(j) > accF){
											accF = accP0.get(j);
											ctagF = ctag;
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
			if (ctagF == ""){
				//Hinzufügen des Tokens ohne CTag
				output.add(input.get(i));
			}
			else {
				//Hinzufügen des Tokens mit dem neu erworbennen CTag
				output.add(input.get(i)+"_"+ctagF);
			}
		}

		//gechunkter output wird in output.txt geschrieben
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(outputtext);

			for (int i=0;i<output.size();i++){
				printWriter.println(output.get(i));
			}
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			if ( printWriter != null ) {
				printWriter.close();
			}

		}
		System.out.println("END");
	}
}