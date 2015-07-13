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
import java.util.Date;
import java.util.List;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class chunker  {

	public static void main(String[] args) {

		System.out.println("START");
		//POS-TAGGER
		//Pfadangabe
		File text = new File ("text.txt"); 										//unbearbeitete Eingabe
		MaxentTagger tagger = new MaxentTagger("taggers/german-fast.tagger");	//verwendeter POS-Tagger

		List<String> unbearbeitet = new ArrayList<String>();
		List<String> input = new ArrayList<String>();
		
		try {
			String line = null;

			BufferedReader inut  = new BufferedReader(new InputStreamReader(new FileInputStream (text), "UTF8"));			
			while (( line = inut.readLine()) != null) {
				unbearbeitet.add(line);
			}
			inut.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();  
		} catch (IOException e) {
			e.printStackTrace();
		}

		String text_unbearbeitet = "";
		for (int i = 0; i < unbearbeitet.size(); i++){
			text_unbearbeitet = text_unbearbeitet.concat(unbearbeitet.get(i));
		}
		String tagged = tagger.tagString(text_unbearbeitet);
		String [] split_tagged = tagged.split(" ");
		
		for (int i = 0; i < split_tagged.length; i++) {
			input.add(split_tagged[i]+"_"+i);
		}


		// CHUNKER
		// Pfadangaben
		File rules_file = new File ("results/regel_auswertung.txt"); 	// Regeln
		File outputtext = new File("results/output.txt");				// Ausgabetext mit Chunks


		List<String> rules = new ArrayList<String>();
		List<String> output = new ArrayList<String>();

		//Einlesen der Regeln und des Eingabe
		try {
			String line = null;

			// Einlesen der Regeln
			BufferedReader inrf  = new BufferedReader(new InputStreamReader(new FileInputStream (rules_file), "UTF8"));			
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

		//Setzen des Grenzwertes fuer die Acc der trainierten Regeln
		double grenze1 = 50.0;
		// Auslesen der trainierten ACC und Regeln aus rules.txt
		for (int i = 0; i < rules.size(); i++){
			Rule rul1 = new Rule (rules.get(i));
			String accs = rul1.getArruracy();
			double acc = Double.parseDouble(accs); 
			//Regeln P0
			if (i < 330 && acc > grenze1){
				P0.add(rules.get(i));
				accP0.add(acc);
			}
			//Regeln P0M1
			if (i > 330 && i <= 18480 && acc > grenze1){
				P0M1.add(rules.get(i));
				accP0M1.add(acc);
			}
			//Regeln P0P1
			if (i > 18480 && i <= 36630 && acc > grenze1){
				P0P1.add(rules.get(i));
				accP0P1.add(acc);
			}
			//Regeln P0M2M1 
			if (i > 36630 && i <= 1034880 && acc > grenze1){
				P0M2M1.add(rules.get(i));
				accP0M2M1.add(acc);
			}
			//Regeln P0M1P1 
			if (i > 1034880 && i <= 2033130 && acc > grenze1){
				P0M1P1.add(rules.get(i));
				accP0M1P1.add(acc);
			}
			//Regeln P0P1P2 
			if (i > 2033130 && acc > grenze1){
				P0P1P2.add(rules.get(i));
				accP0P1P2.add(acc);
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

			if (i >= input.size()-1) {
			}
			else {
				Token tokP1 = new Token (input.get(i+1));
				posP1 = tokP1.getTag();
			}
			if (i >= input.size()-2) {
			}
			else {
				Token tokP2 = new Token (input.get(i+2));
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