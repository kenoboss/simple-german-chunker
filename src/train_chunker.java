
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
/**
 * Diese File ist Inhalt des Programms "Simple-German-Chunker"

 * Hier werden die verschiedenen Regeln f�r den Chunker erzeugt und 
 * mit Hilfe des Korpus werden diese Regeln trainiert.
 * Hier werden folgende Dateien benoetigt:
 * <ul>
 * <li>Rule.java</li>
 * <li>Token.java</li>
 * <li>tagPos.txt</li>
 * <li>chunked_corpus.txt</li>
 * <li>tagged_corpus.txt</li>
 * </ul>
 * 
 * Folgende Dateien werden hier erzeugt:
 * <ul>
 * <li>rules.txt - Dabei handelt es sich um alle erzeugten Regeln, ohne Training</li>
 * <li>regel_auswertung.txt - Dabei handelt es sich um die trainierten Regeln</li>
 * </ul>
 */

public class train_chunker {

	/**
	 * Main-Methode des f�r das Training verwendeten Chunkers.
	 * <br>
	 * Erstellt die Regeln fuer den Chunker, wendet sie auf tagged_corpus.txt 
	 * an und trainiert sie durch Vergleich mit chunked_corpus.txt.
	 * <br>
	 * Die Bewertung der Regeln sieht wie folgt aus:
	 * <ul>
	 * <li>Haeufigkeit (Freq): Haeufigkeit der Anwendung einer Regel</li>
	 * <li>Erfolg (Succ): Haeufigkeit der Korrekten Anwendung einer Regel</li>
	 * <li>Genauigkeit (Acc): Erfolg geteilt durch Haeufigkeit</li>
	 * </ul>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Date date = new Date();		// Beginng der Zeitmessung des Programms
		System.out.println(date.toString());
		final double timeStart = System.currentTimeMillis();

		//Alle Pfadangaben
		File rule_file = new File("results/rules.txt");						// Speicherort fuer die hier erzeugten Regeln
		File auswertung = new File("results/regel_auswertung.txt");			// Speicherort fuer die trainierten Regeln

		File chunked_corpus = new File("ressources/chunked_corpus.txt");	// Speicherort fuer das Trainingscorpus	
		File tagged_corpus = new File("ressources/tagged_corpus.txt");		// Speicherort fuer das POS-getaggte Corpus
		File tagPos = new File("ressources/tagPos.txt");					// Speicherort fuer die List der POS-Tags

		/*
		 *  Fuer die weitere Verarbeitung von chunked_corpus.txt, tagged_corpus.txt und tagPos.txt 
		 * werden ArrayLists erstellt, in denen diese Dokumente fuer den Programmablauf gespeichert werden
		 */
		List<String> corpus_chunked = new ArrayList<String>();
		List<String> corpus_tagged = new ArrayList<String>();
		List<String> postag = new ArrayList<String>();




		// START EINLESEN
		final long timeStartReading = System.currentTimeMillis();
		try {
			String line = null; //Zeilenweises Einlesen der Eingabe
			// Einlesen des chunk-getaggten Korpus
			BufferedReader incc  = new BufferedReader(new InputStreamReader(new FileInputStream (chunked_corpus), "UTF8"));	// incc = Input des gechunkten Corpus		
			while (( line = incc.readLine()) != null) {
				corpus_chunked.add(line);
			}
			incc.close();

			// Einlesen des POS-getaggten Korpus
			BufferedReader intc  = new BufferedReader(new InputStreamReader(new FileInputStream (tagged_corpus), "UTF8"));	// intc = Input des POS-getaggten Corpus	
			while (( line = intc.readLine()) != null) {
				corpus_tagged.add(line);
			}
			intc.close();

			// Einlesen der POS-Tags in ArrayList
			BufferedReader inpt  = new BufferedReader(new InputStreamReader(new FileInputStream (tagPos), "UTF8"));			// inpt = Input der Liste von POS-Tags	
			while (( line = inpt.readLine()) != null) {
				postag.add(line);
			}
			inpt.close();

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

		// Groesse des Trainingscorpus
		long trainsize = corpus_tagged.size()-500000;	// Beschraenkung des Trainings auf ca. 2/3 des Corpus 
		// Laenge des gesamten Corpus. 1.574.817 Tokens

		// START REGELERSTELLUNG
		final long timeStartRules = System.currentTimeMillis(); 
		List<String> rulesP0 = new ArrayList<String>();			// Regeln an der Stelle: 0 				
		List<String> rulesP0m1 = new ArrayList<String>();		// Regeln an der Stelle: -1,0			
		List<String> rulesP0m1m2 = new ArrayList<String>();		// Regeln an der Stelle: -2,-1,0		
		List<String> rulesP0p1 = new ArrayList<String>();		// Regeln an der Stelle: 0,1			
		List<String> rulesP0p1m1 = new ArrayList<String>();		// Regeln an der Stelle: -1,0,1			
		List<String> rulesP0p1p2 = new ArrayList<String>();		// Regeln an der Stelle: 0,1,2			


		// Chunk-Tag, die verwendet werden: Nominal-,Verbal- und Pr�positionalchunk
		List<String> ctags = new ArrayList<String>();	// Chunk-Tags werden in einer ArrayList gespeichert
		ctags.add("B-NC");	// Beginn eines Nominalchunks
		ctags.add("I-NC");	// weiterer Eintrag eines Nominalchunks
		ctags.add("B-VC");	// Beginn eines Verbalchunks
		ctags.add("I-VC");	// weiterer Eintrag eines Verbalchunks
		ctags.add("B-PC");	// Beginn eines Praepositionalchunks
		ctags.add("I-PC");	// weiterer Eintrag eines Praepositionalchunks

		// Erstellung der Regeln an der Stelle: 0
		/*
		 * Fuer jedes POS-Tag werden ctags.size() (6) viele Regeln fuer die Position 0 erzeugt 
		 */
		for (int i = 0; i < postag.size(); i++){		// For-Schleife, welche alle POS-Tags durchlaeuft
			for (int j = 0; j < ctags.size(); j++){		// For-Schleife, welche alle Chunk-Tags durchlaeuft
				rulesP0.add("0="+postag.get(i)+"=>"+ctags.get(j));	// Speicherung der Regeln in der ArrayList 
			}
		}
		System.out.println("Anzahl der Regeln 0: \t\t"+rulesP0.size());

		// Erstellung der Regeln an der Stelle: -1,0
		/* 
		 * Fuer jede erzeugte Regel fuer die Position 0 werden nun wieder ctags.size() viele 
		 * neue Regeln erstellt
		 */
		for (int i=0; i< rulesP0.size();i++){		// For-Schleife, welche alle Regel fuer die Position 0 durchlaeuft
			//			Rule rul1 = new Rule (rulesP0.get(i)); 
			//			String chunk = rul1.getChunktag();		
			for (int j=0; j<postag.size();j++){		// For-Schleife, welche alle Chunk-Tags durchlaeuft
				rulesP0m1.add("-1="+postag.get(j)+";"+rulesP0.get(i));	// Speicherung der Regeln in der ArrayList
			}
		}
		System.out.println("Anzahl der Regeln -1,0: \t"+rulesP0m1.size());

		/*
		 * Die anderen Regeln, werden auf die gleiche Weise produziert wie die bisherigen.
		 * spezifische Unterschiede bei der Regelerzeugung werden in Kommentaren dargestellt
		 */

		// Erstellung der Regeln an der Stelle: 0,1
		for (int i=0; i< rulesP0.size();i++){
			//			Rule rul1 = new Rule (rulesP0.get(i));
			//			String chunk = rul1.getChunktag();
			String[] rulesP0split = new String [rulesP0.size()];	
			rulesP0split=rulesP0.get(i).split("=>");
			/*
			 * Damit diese Regeln erzeugt werden, wird hier zunaechst die Regeln fuer die Positionen 0
			 * an "=>" getrennt, damit ein weiterer Regelteil hinzugefuegt werden kann
			 */
			for (int j=0; j<postag.size();j++){
				rulesP0p1.add(rulesP0split[0]+";1="+postag.get(j)+"=>"+rulesP0split[1]);
			}
		}
		System.out.println("Anzahl der Regeln 0,1: \t\t"+rulesP0p1.size());

		// Erstellung der Regeln an der Stelle: -2,-1,0
		for (int i=0; i<rulesP0m1.size();i++){
			for (int j=0; j<postag.size();j++){
				rulesP0m1m2.add("-2="+postag.get(j)+";"+rulesP0m1.get(i));
			}
		}
		System.out.println("Anzahl der Regeln -2,-1,0: \t"+rulesP0m1m2.size());

		// Erstellung der Regeln an der Stelle: -1,0,1
		for (int i=0; i<rulesP0p1.size();i++){
			for (int j=0; j<postag.size();j++){
				rulesP0p1m1.add("-1="+postag.get(j)+";"+rulesP0p1.get(i));
			}
		}
		System.out.println("Anzahl der Regeln -1,0,1: \t"+rulesP0p1m1.size());

		// Erstellung der Regeln an der Stelle: 0,1,2
		for (int i=0; i< rulesP0p1.size();i++){
			String[] rulesP0p1split = new String [rulesP0p1.size()];	// siehe Regelerzeung fuer die Regeln fuer die Positionen 0,1
			rulesP0p1split=rulesP0p1.get(i).split("=>");
			for (int j=0; j<postag.size();j++){
				rulesP0p1p2.add(rulesP0p1split[0]+";2="+postag.get(j)+"=>"+rulesP0p1split[1]);
			}
		}
		System.out.println("Anzahl der Regeln 0,1,2: \t"+rulesP0p1p2.size());


		// Summe aller Regeln
		long anzahlrules = rulesP0.size()+rulesP0m1.size()+rulesP0m1m2.size()+rulesP0p1.size()+
				rulesP0p1m1.size()+rulesP0p1p2.size();
		System.out.println("________________________________________");
		System.out.println("Anzahl aller Regeln: \t\t"+anzahlrules);

		// Regeln werden in die Datei 'rules.txt' geschrieben
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(rule_file);
			// Die Regeln werden in der gleichen Reihenfolge in die Datei geschrieben, wie sie erzeugt wurden
			for (int i=0;i<rulesP0.size();i++){		// For-Schleife durchlaeuft alle erzeugten Regeln, die in der ArrayList sind
				printWriter.println(rulesP0.get(i));	// Regeln werden in die Datei geschrieben
			}
			for (int i=0;i<rulesP0m1.size();i++){
				printWriter.println(rulesP0m1.get(i));
			}
			for (int i=0;i<rulesP0p1.size();i++){
				printWriter.println(rulesP0p1.get(i));
			}
			for (int i=0;i<rulesP0m1m2.size();i++){
				printWriter.println(rulesP0m1m2.get(i));
			}
			for (int i=0;i<rulesP0p1m1.size();i++){
				printWriter.println(rulesP0p1m1.get(i));
			}
			for (int i=0;i<rulesP0p1p2.size();i++){
				printWriter.println(rulesP0p1p2.get(i));
			}
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			if ( printWriter != null ) {
				printWriter.close();
			}
		}

		final long timeEndRules = System.currentTimeMillis(); 
		final long timeRules = (timeEndRules - timeStartRules)/1000;
		System.out.println("Dauer der Regelerstellung " + timeRules + " Sek."); 
		// ENDE REGELERSTELLUNG

		System.out.println("________________________________________");
		System.out.println("START TEST REGELN AUF DAS CORPUS");
		// START TEST REGELN AUF CORPUS

		/*
		 * Bereitsstellung von Variablen zur Bewertung der Regeln, wenn sie auf
		 * das Corpus trainiert werden.
		 * Bewertung wird spaeter erlaeutert
		 */
		// Eval f�r Regeln der Positionen 0
		float [] frequP0 = new float [rulesP0.size()];
		/*
		 * Erstellung eines Arrays fuer die Frequenzen.
		 * Laenge des Arrays ist die Anzahl der Regeln fuer die Position 0
		 * Hier werden die Frequenzen bzw. Haeufigkeiten des Trainings 
		 * einer Regel festgehalten
		 */
		float [] succP0 = new float [rulesP0.size()];
		/*
		 * Erstellung eines Arrays fuer die Erfolge.
		 * Laenge des Arrays ist die Anzahl der Regeln fuer die Position 0
		 * Hier werden die Erfolge des Trainings 
		 * einer Regel festgehalten
		 */
		for (int i = 0; i < rulesP0.size(); i++){
			// Besetzung der Bewertungsarrays mit der 0, als Inistialwert
			frequP0[i] = 0; 
			succP0[i] = 0;
		}

		/*
		 * Fuer die folgenden Regeln findet die gleiche Erstellung von Arrays statt
		 */

		// Eval f�r Regeln der Positionen -1,0
		float [] frequP0M1 = new float [rulesP0m1.size()];
		float [] succP0M1 = new float [rulesP0m1.size()];
		for (int i = 0; i < rulesP0m1.size(); i++){
			frequP0M1[i] = 0;
			succP0M1[i] = 0;
		}

		// Eval f�r Regeln der Positionen 0,1
		float [] frequP0P1 = new float [rulesP0p1.size()];
		float [] succP0P1 = new float [rulesP0p1.size()];
		for (int i = 0; i < rulesP0p1.size(); i++){
			frequP0P1[i] = 0;
			succP0P1[i] = 0;
		}

		// Eval f�r Regeln der Positionen -2,-1,0
		float [] frequP0M1M2 = new float [rulesP0m1m2.size()];
		float [] succP0M1M2 = new float [rulesP0m1m2.size()];
		for (int i = 0; i < rulesP0m1m2.size(); i++){
			frequP0M1M2[i] = 0;
			succP0M1M2[i] = 0;
		}

		// Eval f�r Regeln der Positionen -1,0,1
		float [] frequP0P1M1 = new float [rulesP0p1m1.size()];
		float [] succP0P1M1 = new float [rulesP0p1m1.size()];
		for (int i = 0; i < rulesP0p1m1.size(); i++){
			frequP0P1M1[i] = 0;
			succP0P1M1[i] = 0;
		}

		// Eval f�r Regeln der Positionen 0,1,2
		float [] frequP0P1P2 = new float [rulesP0p1p2.size()];
		float [] succP0P1P2 = new float [rulesP0p1p2.size()];
		for (int i = 0; i < rulesP0p1p2.size(); i++){
			frequP0P1P2[i] = 0;
			succP0P1P2[i] = 0;
		}



		/* 
		 * Erstellung von IDs fur das Training
		 * Erstellt fuer jeden Postag eine ID in den Regeln, die markiert wo die Regeln fuer diesen Postag beginnen.
		 * Die Zahl mit der temp multipliziert wird entspricht der Anzahl der Regeln fuer den Postag,
		 * 6 fur einstelligen und 330 fuer zweistelligen Regeln und 18150 fuer die dreistelligen Regeln.
		 * 
		 * Diese Identifizierung fuer das Training ist moeglich, da die Regeln alle an der Position 0 nach
		 * POS-Tags sortiert sind
		 */
		int [] id1 = new int [postag.size()];
		for (int temp = 0; temp < postag.size(); temp++) {
			id1[temp] = 6*temp;
		}
		int [] id2 = new int [postag.size()];
		for (int temp = 0; temp < postag.size(); temp++) {
			id2 [temp] = 330*temp;
		}
		int [] id3 = new int [postag.size()];
		for (int temp = 0; temp < postag.size(); temp++) {
			id3 [temp] = 18150*temp;
		}

		System.out.println("Corpusgroesse: \t\t"+corpus_tagged.size());
		System.out.println("Trainingscorpus: \t\t"+trainsize);

		// TRAINING
		final long timeStartTraining = System.currentTimeMillis(); 

		// Algorithmus zum Training der erzeugten Regeln Position = 0
		final double timeStartRunOne = System.currentTimeMillis();	// Messung der Zeit fuer den Durchlauf der Regeln
		for (int i = 0; i< trainsize; i++){ 					// For-Schleife fuer den Durchlauf des Trainingscorpus

			token tok1 = new token (corpus_tagged.get(i));		// Auslesen des Tokens aus dem POS-getaggten Corpus
			String pos = tok1.getTag();							// Auslesen des POS-Tags des Tokens (POS Corpus)

			token tok2 = new token (corpus_chunked.get(i));		// Auslesen des Tokens aus dem Chunk-getaggten Corpus
			String chunk = tok2.getCtag();						// Auslesen des POS-Tags des Tokens (Chunk Corpus)

			int k = id1[postag.indexOf(pos)];
			for (int j = k; j < k+6; j++){						// For-Schleife fuer den Durchlauf der Regeln fuer die Position 0
				rule rul1 = new rule(rulesP0.get(j));			// Auslesen der Regel aus den Regeln fuer die Position 0 an der Stelle j 
				String rulpos = rul1.getPostag()[0];			// Auslesen des POS-Tags aus der Regel
				String rulchunk = rul1.getChunktag();			// Auslesen des Chunk-Tags aus der Regel

				if (pos.equals(rulpos)){	
					/*
					 * Ueberpruefung, ob das POS-Tag aus dem POS-getaggten Corpus mit 
					 * dem POS-Tag aus der aktuellen Regel uebereinstimmt
					 */
					frequP0[j]++;	// Frequenzzaehler wird um 1 erhoeht, wenn die Bedingung zutrifft
					if (chunk.equals(rulchunk)){
						/*
						 * Ueberpruefung, ob das Ergebnis(Chunk-Tag) der Regel aus das Token aus dem POS Corpus
						 * mit dem Chunk-Tag aus dem Chunk-getaggten Corpus uebereinstimmt
						 */
						succP0[j]++; // Zaehler wird um 1 erhoeht, wenn die Bedinung zutrifft
					}
				}
			}

		}
		// Messung der Zeit fuer den Durchlauf der Regeln
		final double timeEndRunOne = System.currentTimeMillis();	
		final double timeRunOne = ((timeEndRunOne - timeStartRunOne)/1000)/60;	
		System.out.println("Dauer Run One: \t\t" + timeRunOne + " Min."); 

		/*
		 * Das Training der Regeln, findet bei den laengeren Regeln auf die gleiche
		 * Weise statt, wie das Training der Regeln fuer die Position 0. 
		 * 
		 * Unterschiede zu dem Training der anderen Regeln werden im Training dieser
		 * kommentiert
		 */


		// Algorithmus zum Training der erzeugten Regeln Position = -1,0
		final double timeStartRunTwo = System.currentTimeMillis();
		for (int i = 1; i< trainsize; i++){ 
			/*
			 * For-Schleife beginnt nicht bei 0, sondern bei 1, also trainsize + 1, da ansonsten 
			 * die Regeln bei -1 (OutOfBounds) starten wuerden
			 * 
			 * eine Alternative waere auch, dass man es ueber eine If-Bedinung zunaechst abfragt
			 */

			token tokp1 = new token (corpus_tagged.get(i-1));	// Auslesen des Tokens aus dem POS-getaggten Corpus fuer die Position -1
			String pos1 = tokp1.getTag();						// Auslesen des POS-Tags des Tokens (POS Corpus) fuer die Position -1
			token tokp2 = new token (corpus_tagged.get(i));		// Auslesen des Tokens aus dem POS-getaggten Corpus fuer die Position 0
			String pos2 = tokp2.getTag();						// Auslesen des POS-Tags des Tokens (POS Corpus) fuer die Position 0


			token tokc1 = new token (corpus_chunked.get(i));
			String chunk = tokc1.getCtag();

			int k = id2[postag.indexOf(pos2)];
			for (int j = k; j < k+330; j++){
				rule rul1 = new rule(rulesP0m1.get(j));
				String rulpos1 = rul1.getPostag()[0];			// Auslesen des POS-Tags fuer die Postion -1 aus der Regel
				String rulpos2 = rul1.getPostag()[1];			// Auslesen des POS-Tags fuer die Postion 0 aus der Regel

				String rulchunk = rul1.getChunktag();

				if (pos1.equals(rulpos1) && pos2.equals(rulpos2)){	
					/*
					 * Hier werden nun beide POS-Tags aus dem Corpus 
					 * mit dem POS-Tag aus den Regeln verglichen
					 */
					frequP0M1[j]++;
					if (chunk.equals(rulchunk)){
						succP0M1[j]++;
					}
				}
				else{
				}
			}
		}
		final double timeEndRunTwo = System.currentTimeMillis();
		final double timeRunTwo =((timeEndRunTwo - timeStartRunTwo)/1000)/60;
		System.out.println("Dauer Run Two: \t\t" + timeRunTwo + " Min."); 

		// Algorithmus zum Training der erzeugten Regeln Position = -2,-1,0
		final double timeStartRunThree = System.currentTimeMillis();
		for (int i = 2; i< trainsize; i++){ 	
			/*
			 * For-Schleife beginnt nicht bei 0, sondern bei 2, also trainsize + 2, da ansonsten 
			 * die Regeln bei -2 (OutOfBounds) starten wuerden
			 * 
			 * eine Alternative waere auch, dass man es ueber eine If-Bedinung zunaechst abfragt
			 */

			token tokp1 = new token (corpus_tagged.get(i-2));		// Auslesen des Tokens aus dem POS-getaggten Corpus fuer die Position -2
			String pos1 = tokp1.getTag();							// Auslesen des POS-Tags des Tokens (POS Corpus) fuer die Position -2
			token tokp2 = new token (corpus_tagged.get(i-1));		// Auslesen des Tokens aus dem POS-getaggten Corpus fuer die Position -1
			String pos2 = tokp2.getTag();							// Auslesen des POS-Tags des Tokens (POS Corpus) fuer die Position -1
			token tokp3 = new token (corpus_tagged.get(i));			// Auslesen des Tokens aus dem POS-getaggten Corpus fuer die Position 0
			String pos3 = tokp3.getTag();							// Auslesen des POS-Tags des Tokens (POS Corpus) fuer die Position 0


			token tokc1 = new token (corpus_chunked.get(i));
			String chunk = tokc1.getCtag();

			int k = id3[postag.indexOf(pos3)];
			for (int j = k; j < k+18150; j++){
				rule rul1 = new rule(rulesP0m1m2.get(j));
				String rulpos1 = rul1.getPostag()[0];				// Auslesen des POS-Tags fuer die Postion -2 aus der Regel
				String rulpos2 = rul1.getPostag()[1];				// Auslesen des POS-Tags fuer die Postion -1 aus der Regel
				String rulpos3 = rul1.getPostag()[2];				// Auslesen des POS-Tags fuer die Postion 0 aus der Regel

				String rulchunk = rul1.getChunktag();

				if (pos1.equals(rulpos1) && pos2.equals(rulpos2) && pos3.equals(rulpos3)){
					/*
					 * Hier werden nun alle drei POS-Tags aus dem Corpus 
					 * mit dem POS-Tag aus den Regeln verglichen
					 */
					frequP0M1M2[j]++;
					if (chunk.equals(rulchunk)){
						succP0M1M2[j]++;
					}
				}
				else{
				}
			}
		}
		final double timeEndRunThree = System.currentTimeMillis();
		final double timeRunThree = ((timeEndRunThree - timeStartRunThree)/1000)/60;
		System.out.println("Dauer Run Three: \t" + timeRunThree + " Min."); 

		// Algorithmus zum Training der erzeugten Regeln Position = 0,1
		final double timeStartRunFour = System.currentTimeMillis();
		for (int i = 0; i< trainsize; i++){ 	

			token tokp1 = new token (corpus_tagged.get(i));			// Auslesen des Tokens aus dem POS-getaggten Corpus fuer die Position 0
			String pos1 = tokp1.getTag();							// Auslesen des POS-Tags des Tokens (POS Corpus) fuer die Position 0
			token tokp2 = new token (corpus_tagged.get(i+1));		// Auslesen des Tokens aus dem POS-getaggten Corpus fuer die Position 1
			String pos2 = tokp2.getTag();							// Auslesen des POS-Tags des Tokens (POS Corpus) fuer die Position 1


			token tokc1 = new token (corpus_chunked.get(i));
			String chunk = tokc1.getCtag();

			int k = id2[postag.indexOf(pos1)];
			for (int j = k; j < k+330; j++){
				rule rul1 = new rule(rulesP0p1.get(j));
				String rulpos1 = rul1.getPostag()[0];				// Auslesen des POS-Tags fuer die Postion 0 aus der Regel
				String rulpos2 = rul1.getPostag()[1];				// Auslesen des POS-Tags fuer die Postion 1 aus der Regel

				String rulchunk = rul1.getChunktag();

				if (pos1.equals(rulpos1) && pos2.equals(rulpos2)){
					frequP0P1[j]++;
					if (chunk.equals(rulchunk)){
						succP0P1[j]++;
					}
				}
				else{
				}
			}
		}
		final double timeEndRunFour = System.currentTimeMillis();
		final double timeRunFour = ((timeEndRunFour - timeStartRunFour)/1000)/60;
		System.out.println("Dauer Run Four: \t" + timeRunFour + " Min."); 

		// Algorithmus zum Training der erzeugten Regeln Position = -1,0,1
		final double timeStartRunFive = System.currentTimeMillis();
		for (int i = 1; i< trainsize; i++){ 	
			/*
			 * For-Schleife beginnt nicht bei 0, sondern bei 1, also trainsize + 1, da ansonsten 
			 * die Regeln bei -1 (OutOfBounds) starten wuerden
			 * 
			 * eine Alternative waere auch, dass man es ueber eine If-Bedinung zunaechst abfragt
			 */

			token tokp1 = new token (corpus_tagged.get(i-1));		// Auslesen des Tokens aus dem POS-getaggten Corpus fuer die Position -1
			String pos1 = tokp1.getTag();							// Auslesen des POS-Tags des Tokens (POS Corpus) fuer die Position -1
			token tokp2 = new token (corpus_tagged.get(i));			// Auslesen des Tokens aus dem POS-getaggten Corpus fuer die Position 0
			String pos2 = tokp2.getTag();							// Auslesen des POS-Tags des Tokens (POS Corpus) fuer die Position 0
			token tokp3 = new token (corpus_tagged.get(i+1));		// Auslesen des Tokens aus dem POS-getaggten Corpus fuer die Position 1
			String pos3 = tokp3.getTag();							// Auslesen des POS-Tags des Tokens (POS Corpus) fuer die Position 1


			token tokc1 = new token (corpus_chunked.get(i));
			String chunk = tokc1.getCtag();

			int k = id3[postag.indexOf(pos2)];
			for (int j = k; j < k+18150; j++){
				rule rul1 = new rule(rulesP0p1m1.get(j));
				String rulpos1 = rul1.getPostag()[0];				// Auslesen des POS-Tags fuer die Postion -1 aus der Regel
				String rulpos2 = rul1.getPostag()[1];				// Auslesen des POS-Tags fuer die Postion 0 aus der Regel
				String rulpos3 = rul1.getPostag()[2];				// Auslesen des POS-Tags fuer die Postion 1 aus der Regel

				String rulchunk = rul1.getChunktag();

				if (pos1.equals(rulpos1) && pos2.equals(rulpos2) && pos3.equals(rulpos3)){
					frequP0P1M1[j]++;
					if (chunk.equals(rulchunk)){
						succP0P1M1[j]++;
					}
				}
				else{
				}
			}
		}
		final double timeEndRunFive = System.currentTimeMillis();
		final double timeRunFive = ((timeEndRunFive - timeStartRunFive)/1000)/60;
		System.out.println("Dauer Run Five: \t" + timeRunFive + " Min."); 

		// Algorithmus zum Training der erzeugten Regeln Position = 0,1,2
		final double timeStartRunSix = System.currentTimeMillis();
		for (int i = 0; i< trainsize; i++){ 	

			token tokp1 = new token (corpus_tagged.get(i));			// Auslesen des Tokens aus dem POS-getaggten Corpus fuer die Position 0
			String pos1 = tokp1.getTag();							// Auslesen des POS-Tags des Tokens (POS Corpus) fuer die Position 0
			token tokp2 = new token (corpus_tagged.get(i+1));		// Auslesen des Tokens aus dem POS-getaggten Corpus fuer die Position 1
			String pos2 = tokp2.getTag();							// Auslesen des POS-Tags des Tokens (POS Corpus) fuer die Position 1
			token tokp3 = new token (corpus_tagged.get(i+2));		// Auslesen des Tokens aus dem POS-getaggten Corpus fuer die Position 2
			String pos3 = tokp3.getTag();							// Auslesen des POS-Tags des Tokens (POS Corpus) fuer die Position 2


			token tokc1 = new token (corpus_chunked.get(i));
			String chunk = tokc1.getCtag();

			int k = id3[postag.indexOf(pos1)];
			for (int j = k; j < k+18150; j++){
				rule rul1 = new rule(rulesP0p1p2.get(j));
				String rulpos1 = rul1.getPostag()[0];				// Auslesen des POS-Tags fuer die Postion 0 aus der Regel
				String rulpos2 = rul1.getPostag()[1];				// Auslesen des POS-Tags fuer die Postion 1 aus der Regel
				String rulpos3 = rul1.getPostag()[2];				// Auslesen des POS-Tags fuer die Postion 2 aus der Regel

				String rulchunk = rul1.getChunktag();

				if (pos1.equals(rulpos1) && pos2.equals(rulpos2) && pos3.equals(rulpos3)){
					frequP0P1P2[j]++;
					if (chunk.equals(rulchunk)){
						succP0P1P2[j]++;
					}
				}
				else{
				}
			}
		}
		final double timeEndRunSix = System.currentTimeMillis();
		final double timeRunSix = ((timeEndRunSix - timeStartRunSix)/1000)/60;
		System.out.println("Dauer Run Six: \t\t" + timeRunSix + " Min."); 

		// Dauer des Training der Regeln
		final long timeEndTraining = System.currentTimeMillis(); 
		final long timeTraining = (timeEndTraining - timeStartTraining)/1000;
		System.out.println("Dauer des Trainings der Regeln: \t" + timeTraining + " Sek."); 


		// Auswertung der Regeln auf ihre Frequenz, Erfolge und Genauigkeit (Erfolg/Frequenz)
		
		/*
		 * Die Regeln werden zusammen mit ihrer zuhehoerigen Frequenzen und Erfolgen, sowie 
		 * einem Genauigkeitswert, der sich aus den beiden anderen Werten zusammensetzt
		 * in die Text-Datei "regel_auswertung.txt" geschrieben
		 * 
		 * Wenn die Regel gar nicht trainiert worden ist, also wenn ihre Frequenz 0 betraegt
		 * kann auch keine Genauigkeit errechnet werden. So wird als default-Wert 0.0 eingetragen
		 */
		final long timeStartTesting = System.currentTimeMillis();
		PrintWriter printWriter2 = null;
		try{
			printWriter2 = new PrintWriter(auswertung);
			// Eval der Regeln an den Position 0
			for (int i = 0; i<rulesP0.size();i++){
				if (frequP0[i]>0){
					printWriter2.println(rulesP0.get(i)+"=> Freq: "+frequP0[i]+" Succ: "+succP0[i]+" Acc: "+(succP0[i]/frequP0[i])*100);
				}
				else{
					printWriter2.println(rulesP0.get(i)+"=> Freq: "+frequP0[i]+" Succ: "+succP0[i]+" Acc: 0.0");
				}
			}
			// Eval der Regeln an den Positionen -1,0
			for (int i = 0; i<rulesP0m1.size();i++){
				if (frequP0M1[i]>0){
					printWriter2.println(rulesP0m1.get(i)+"=> Freq: "+frequP0M1[i]+" Succ: "+succP0M1[i]+" Acc: "+(succP0M1[i]/frequP0M1[i])*100);
				}
				else{
					printWriter2.println(rulesP0m1.get(i)+"=> Freq: "+frequP0M1[i]+" Succ: "+succP0M1[i]+" Acc: 0.0");
				}
			}
			// Eval f�r Regeln der Positionen 0,1
			for (int i = 0; i<rulesP0p1.size();i++){
				if (frequP0P1[i]>0){
					printWriter2.println(rulesP0p1.get(i)+"=> Freq: "+frequP0P1[i]+" Succ: "+succP0P1[i]+" Acc: "+(succP0P1[i]/frequP0P1[i])*100);
				}
				else{
					printWriter2.println(rulesP0p1.get(i)+"=> Freq: "+frequP0P1[i]+" Succ: "+succP0P1[i]+" Acc: 0.0");
				}
			}
			// Eval f�r Regeln der Positionen -2,-1,0
			for (int i = 0; i<rulesP0m1m2.size();i++){
				if (frequP0M1M2[i]>0){
					printWriter2.println(rulesP0m1m2.get(i)+"=> Freq: "+frequP0M1M2[i]+" Succ: "+succP0M1M2[i]+" Acc: "+(succP0M1M2[i]/frequP0M1M2[i])*100);
				}
				else{
					printWriter2.println(rulesP0m1m2.get(i)+"=> Freq: "+frequP0M1M2[i]+" Succ: "+succP0M1M2[i]+" Acc: 0.0");
				}
			}
			// Eval f�r Regeln der Positionen -1,0,1
			for (int i = 0; i<rulesP0p1m1.size();i++){
				if (frequP0P1M1[i]>0){
					printWriter2.println(rulesP0p1m1.get(i)+"=> Freq: "+frequP0P1M1[i]+" Succ: "+succP0P1M1[i]+" Acc: "+(succP0P1M1[i]/frequP0P1M1[i])*100);
				}
				else{
					printWriter2.println(rulesP0p1m1.get(i)+"=> Freq: "+frequP0P1M1[i]+" Succ: "+succP0P1M1[i]+" Acc: 0.0");
				}
			}
			// Eval f�r Regeln der Positionen 0,1,2
			for (int i = 0; i<rulesP0p1p2.size();i++){
				if (frequP0P1P2[i]>0){
					printWriter2.println(rulesP0p1p2.get(i)+"=> Freq: "+frequP0P1P2[i]+" Succ: "+succP0P1P2[i]+" Acc: "+(succP0P1P2[i]/frequP0P1P2[i])*100);
				}
				else{
					printWriter2.println(rulesP0p1p2.get(i)+"=> Freq: "+frequP0P1P2[i]+" Succ: "+succP0P1P2[i]+" Acc: 0.0");
				}
			}
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			if ( printWriter2 != null ) {
				printWriter2.close();
			}
		}

		// Dauer der Evaluation der Regeln
		final long timeEndTesting = System.currentTimeMillis(); 
		final long timeTesting = (timeEndTesting - timeStartTesting)/1000;
		System.out.println("Dauer der Bewertung der Regeln: \t"+ timeTesting + " Sek."); 

		// Berechnung der Genauigkeit aller Regeln
		/*
		 * Hier werden die Frequenzen und Erfolge aller Regeln summiert und 
		 * also gemeinsame Genauigkeit, wie bei den einzelnen Regeln ausgegeben
		 * 
		 * Diese Auswertung wird bei allen verschiedenen "Regeltypen" auf die gleiche 
		 * Weise durchgefuehrt
		 */
		// Regeln mit der Position 0 
		float summeFreqP0=0;	// Summe der Frequenzen der Regeln fuer die Position 0
		float summeSuccP0=0;	// Summe der Erfolge der Regeln fuer die Position 0

		for (int p = 0; p< frequP0.length;p++){	// For-Schleife zum Durchlaufen aller Regelfrequenzen
			summeFreqP0=summeFreqP0+frequP0[p]; // Berechnung der Summe der Frequenzen
			summeSuccP0=summeSuccP0+succP0[p];	// Berechnung der Summe der Erfolge
		}
		float summeAccP0=(summeSuccP0/summeFreqP0)*100; // Ausgabe findet also Prozentangabe statt
		System.out.println("Regeln an der Position 0\t Genauigkeit: "+summeAccP0+" %");

		// Regeln mit der Position -1,0
		float summeFreqP0M1=0;
		float summeSuccP0M1=0;

		for (int p = 0; p< frequP0M1.length;p++){
			summeFreqP0M1=summeFreqP0M1+frequP0M1[p];
			summeSuccP0M1=summeSuccP0M1+succP0M1[p];
		}
		float summeAccP0M1=(summeSuccP0M1/summeFreqP0M1)*100;
		System.out.println("Regeln an der Position -1,0\t Genauigkeit: "+summeAccP0M1+" %");

		// Regeln mit der Position 0,1
		float summeFreqP0P1=0;
		float summeSuccP0P1=0;

		for (int p = 0; p< frequP0P1.length;p++){
			summeFreqP0P1=summeFreqP0P1+frequP0P1[p];
			summeSuccP0P1=summeSuccP0P1+succP0P1[p];
		}
		float summeAccP0P1=(summeSuccP0P1/summeFreqP0P1)*100;
		System.out.println("Regeln an der Position 0,1\t Genauigkeit: "+summeAccP0P1+" %");

		// Regeln mit der Position -2,-1,0
		float summeFreqP0M1M2=0;
		float summeSuccP0M1M2=0;

		for (int p = 0; p< frequP0M1M2.length;p++){
			summeFreqP0M1M2=summeFreqP0M1M2+frequP0M1M2[p];
			summeSuccP0M1M2=summeSuccP0M1M2+succP0M1M2[p];
		}
		float summeAccP0M1M2=(summeSuccP0M1M2/summeFreqP0M1M2)*100;
		System.out.println("Regeln an der Position -2,-1,0\t Genauigkeit: "+summeAccP0M1M2+" %");

		// Regeln mit der Position -1,0,1
		float summeFreqP0P1M1=0;
		float summeSuccP0P1M1=0;

		for (int p = 0; p< frequP0P1M1.length;p++){
			summeFreqP0P1M1=summeFreqP0P1M1+frequP0P1M1[p];
			summeSuccP0P1M1=summeSuccP0P1M1+succP0P1M1[p];
		}
		float summeAccP0P1M1=(summeSuccP0P1M1/summeFreqP0P1M1)*100;
		System.out.println("Regeln an der Position -1,0,1\t Genauigkeit: "+summeAccP0P1M1+" %");

		// Regeln mit der Position 0,1,2
		float summeFreqP0P1P2=0;
		float summeSuccP0P1P2=0;

		for (int p = 0; p< frequP0P1P2.length;p++){
			summeFreqP0P1P2=summeFreqP0P1P2+frequP0P1P2[p];
			summeSuccP0P1P2=summeSuccP0P1P2+succP0P1P2[p];
		}
		float summeAccP0P1P2=(summeSuccP0P1P2/summeFreqP0P1P2)*100;
		System.out.println("Regeln an der Position 0,1,2\t Genauigkeit: "+summeAccP0P1P2+" %");

		// ENDE TRAINING


		final double timeEnd = System.currentTimeMillis(); // Messung der Zeit fuer das komplette Regeltraining
		final double time = ((timeEnd - timeStart)/1000)/60;
		System.out.println("________________________________________");
		System.out.println("Dauer des Programms: " + time + " Min."); 
		Date date2 = new Date();
		System.out.println(date2.toString());
	}
}