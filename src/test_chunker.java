/*
 * Diese File ist Inhalt des Programms "Simple-German-Chunker"
 * Hier werden die trainierten Regeln aus train_chunker auf das 
 * letzte 1/3 des Corpus angewendet. 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class test_chunker {

	public static void main(String[] args) {

		Date date = new Date();
		System.out.println(date.toString());
		final double timeStart = System.currentTimeMillis();

		//Alle Pfadangaben
		File auswertung = new File("results/regel_auswertung.txt");
		File rules_tested = new File ("results/rules_tested.txt");

		File chunked_corpus = new File("ressources/chunked_corpus.txt");
		File tagged_corpus = new File("ressources/tagged_corpus.txt");
		File corpus_tested = new File("ressources/corpus_tested.txt");


		List<String> corpus_chunked = new ArrayList<String>();
		List<String> corpus_tagged = new ArrayList<String>();
		List<String> rules = new ArrayList<String>();
		List<String> tested_rules = new ArrayList<String>();

		// START EINLESEN
		final long timeStartReading = System.currentTimeMillis();
		try {
			// Einlesen des chunk-getaggten Korpus
			BufferedReader incc  = new BufferedReader(new InputStreamReader(new FileInputStream (chunked_corpus), "UTF8"));			
			String line = null;

			while (( line = incc.readLine()) != null) {
				corpus_chunked.add(line);
			}
			incc.close();

			// Einlesen des POS-getaggten Korpus
			BufferedReader intc  = new BufferedReader(new InputStreamReader(new FileInputStream (tagged_corpus), "UTF8"));
			while (( line = intc.readLine()) != null) {
				corpus_tagged.add(line);
			}
			intc.close();

			// Einlesen der trainierten Regeln
			BufferedReader intr = new BufferedReader (new InputStreamReader (new FileInputStream (auswertung),"UTF8"));
			while ((line = intr.readLine()) != null){
				rules.add(line);
			}
			intr.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();  
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Dauer des Einlesens
		final long timeEndReading = System.currentTimeMillis(); 
		final long timeReading = (timeEndReading - timeStartReading)/1000;
		System.out.println("Dauer des Einlesens: " + timeReading + " Sek."); 
		// ENDE EINLESEN

		// Groeße zum Testen: 1074818 + 500000

		List<String> P0 = new ArrayList <String>();
		List<String> P0M1 = new ArrayList <String>();
		List<String> P0P1 = new ArrayList <String>();
		List<String> P0M2M1 = new ArrayList <String>();
		List<String> P0M1P1 = new ArrayList <String>();
		List<String> P0P1P2 = new ArrayList <String>();

		List<Double> accP0 = new ArrayList <Double>();
		List<Double> accP0M1 = new ArrayList <Double>();
		List<Double> accP0P1 = new ArrayList <Double>();
		List<Double> accP0M2M1 = new ArrayList <Double>();
		List<Double> accP0M1P1 = new ArrayList <Double>();
		List<Double> accP0P1P2 = new ArrayList <Double>();

		List<String> corpus_tagged_new = new ArrayList<String>();

		double grenze1 = 80.0;
		// Auslesen der trainierten ACC und Regeln aus rules.txt
		for (int i = 0; i < rules.size(); i++){
			Rule rul1 = new Rule (rules.get(i));
			String accs = rul1.getArruracy();
			double acc = Double.parseDouble(accs); 
			//Regeln P0
			if (i < 330 && acc > grenze1){
				P0.add(rules.get(i));
				accP0.add(acc);
				tested_rules.add(rules.get(i));
			}
			//Regeln P0M1
			if (i > 330 && i <= 18480 && acc > grenze1){
				P0M1.add(rules.get(i));
				accP0M1.add(acc);
				tested_rules.add(rules.get(i));
			}
			//Regeln P0P1
			if (i > 18480 && i <= 36630 && acc > grenze1){
				P0P1.add(rules.get(i));
				accP0P1.add(acc);
				tested_rules.add(rules.get(i));
			}
			//Regeln P0M2M1 
			if (i > 36630 && i <= 1034880 && acc > grenze1){
				P0M2M1.add(rules.get(i));
				accP0M2M1.add(acc);
				tested_rules.add(rules.get(i));
			}
			//Regeln P0M1P1 
			if (i > 1034880 && i <= 2033130 && acc > grenze1){
				P0M1P1.add(rules.get(i));
				accP0M1P1.add(acc);
				tested_rules.add(rules.get(i));
			}
			//Regeln P0P1P2 
			if (i > 2033130 && acc > grenze1){
				P0P1P2.add(rules.get(i));
				accP0P1P2.add(acc);
				tested_rules.add(rules.get(i));
			}
		}

		//Ausgabe der Anzahl der Regeln
		System.out.println("Anzahl der Regeln an der Position 0:\t\t"+P0.size());
		System.out.println("Anzahl der Regeln an den Positionen -1,0:\t"+P0M1.size());
		System.out.println("Anzahl der Regeln an den Positionen 0,1:\t"+P0P1.size());
		System.out.println("Anzahl der Regeln an den Positionen -2,-1,0:\t"+P0M2M1.size());
		System.out.println("Anzahl der Regeln an den Positionen -1,0,1:\t"+P0M1P1.size());
		System.out.println("Anzahl der Regeln an den Positionen 0,1,2:\t"+P0P1P2.size());
		System.out.print("Summe aller Regeln: \t\t\t\t");
		System.out.println(P0.size()+P0M1.size()+P0P1.size()+P0M2M1.size()+P0M1P1.size()+P0P1P2.size());

		// getestete Regeln werden in rules_tested.txt geschrieben
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(rules_tested);

			for (int i=0;i<tested_rules.size();i++){
				printWriter.println(tested_rules.get(i));
			}
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			if ( printWriter != null ) {
				printWriter.close();
			}
		}



		//Chunking-Algorithmus
		/*
		 * Regel-Reihenfolge:
		 * (Zahlen sind die betrachteten Positionen, um das aktuelle Wort an der Position 0)
		 * 1. -2,-1,0
		 * 2. -1,0,1
		 * 3. 0,1,2
		 * 4. -1,0
		 * 5. 0,1
		 * 6. 0
		 */
		System.out.println("_______START CHUNKING_______");
		int start = 1574418;
//		int start = 1074818;
		final double timeChunkingStart = System.currentTimeMillis(); 
		for (int i = start; i < corpus_tagged.size(); i++) {
			//Erstellung der POS-Tags für die aktuelle Position im Korpus
			Token tokM2 = new Token (corpus_tagged.get(i-2));
			String posM2 = tokM2.getTag();
			Token tokM1 = new Token (corpus_tagged.get(i-1));
			String posM1 = tokM1.getTag();
			Token tokP0 = new Token (corpus_tagged.get(i));
			String posP0 = tokP0.getTag();
			String posP1 = "";
			String posP2 = "";

			if (i >= corpus_tagged.size()-1) {
			}
			else {
				Token tokP1 = new Token (corpus_tagged.get(i+1));
				posP1 = tokP1.getTag();
			}
			if (i >= corpus_tagged.size()-2) {
			}
			else {
				Token tokP2 = new Token (corpus_tagged.get(i+2));
				posP2 = tokP2.getTag();
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
				corpus_tagged_new.add(corpus_tagged.get(i));
			}
			else {
				//Hinzufügen des Tokens mit dem neu erworbennen CTag
				corpus_tagged_new.add(corpus_tagged.get(i)+"_"+ctagF);
			}
		}
		System.out.println("Groesse von dem gechunkten Corpus: "+corpus_tagged_new.size());

		//getesteter Corpus wird in tested_corpus.txt geschrieben
		PrintWriter printWriter2 = null;
		try {
			printWriter2 = new PrintWriter(corpus_tested);

			for (int i=0;i<corpus_tagged_new.size();i++){
				printWriter2.println(corpus_tagged_new.get(i));
			}
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			if ( printWriter2 != null ) {
				printWriter2.close();
			}
		}

		//Auswertung
		// fehlerhalft
		double succ = 0 ;
		double freq = 0;

		for (int i = 0; i < corpus_tagged_new.size(); i++){
			Token tok1 = new Token (corpus_tagged_new.get(i));
			String ctagt = tok1.getCtag();

			Token tok2 = new Token (corpus_chunked.get(start+i));
			String ctagc = tok2.getCtag();
			freq++;
			if (ctagt.equals(ctagc)){
				succ++;
			}
		}

		final double timeChunkingEnd = System.currentTimeMillis(); 
		final double timeChunking = ((timeChunkingEnd-timeChunkingStart)/1000)/60;
		System.out.println("Dauer des Chunkings: "+timeChunking+" Min.");
		System.out.println("________ENDE CHUNKING_______");

		
		double pres = (succ/freq)*100;
		System.out.println("Genauigkeit: "+pres);



		final double timeEnd = System.currentTimeMillis();
		final double time = ((timeEnd-timeStart)/1000)/60;
		System.out.println("Dauer des Programms: "+time+" Min.");
		Date date1 = new Date();
		System.out.println(date1.toString());

	}

}
