import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class chunker {

	public static void main(String[] args) {

		final long timeStart = System.currentTimeMillis();

		String [] corpus_chunked = new String [1574818];
		String [] corpus_tagged = new String [1574818];
		String [] postag = new String[50];

		try {
			// Reading the diffrent corpora
			// Creating an array for the chunked corpus
			File chunked_corpus = new File("C:/Users/Kenobi/workspace/Chunker_POS_Tagger_Test/newCorporus/chunked_corpus.txt");
			BufferedReader incc  = new BufferedReader(new InputStreamReader(new FileInputStream (chunked_corpus), "UTF8"));			
			String line = null;

			int indexcc = 0;
			while (( line = incc.readLine()) != null) {
				corpus_chunked[indexcc] = line;
				indexcc++;
			}
			incc.close();

			// Creating an array for the tagged corpus (POS-Tag)
			File tagged_corpus = new File("C:/Users/Kenobi/workspace/Chunker_POS_Tagger_Test/newCorporus/tagged_corpus.txt");
			BufferedReader intc  = new BufferedReader(new InputStreamReader(new FileInputStream (tagged_corpus), "UTF8"));

			int indextc = 0;
			while (( line = intc.readLine()) != null) {
				corpus_tagged[indextc] = line;
				indextc++;
			}
			intc.close();


			// Creating an array for POS Tag
			File tagPos = new File("C:/Users/Kenobi/workspace/Chunker_POS_Tagger_Test/tagPos.txt");
			BufferedReader inpt  = new BufferedReader(new InputStreamReader(new FileInputStream (tagPos), "UTF8"));			

			int indexpt = 0;
			while (( line = inpt.readLine()) != null) {
				postag[indexpt] = line;
				indexpt++;
			}
			inpt.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();  
		} catch (IOException e) {
			e.printStackTrace();
		}


		String [] ctags = {"B-NC", "I-NC", "B-VC", "I-VC", "B-PC", "I-PC"};
		//String [] postag = {"ADJA", "ADV", "APPR", "ART", "CARD", "NN", "NE", "PPER", "VVFIN", "VVIMP"};
		String [] rules = new String [ctags.length*postag.length];

		// Creating the first rules
		File file = new File("C:/Users/Kenobi/workspace/Chunker_POS_Tagger_Test/newCorporus/rules.txt");
		PrintWriter printWriter = null;
		try{
			printWriter = new PrintWriter(file);

			int l = 0;
			for (int j = 0; j < postag.length; j++){
				for (int k = 0; k < ctags.length; k++){
					printWriter.println("0="+postag[j]+"=>"+ctags[k]);
					rules[l]="0="+postag[j]+"=>"+ctags[k];
					l++;
				}
			}
		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			if ( printWriter != null ) {
				printWriter.close();
			}
		}

		//		Rule regel  = new Rule (rules[0]);
		//		System.out.println(regel.getChunktag());
		//		System.out.println(regel.getPostag()[0]);
		//		System.out.println(regel.getPosition()[0]);7


		int [] freq = new int [rules.length];
		int [] succ = new int [rules.length];
		for (int n = 0; n < rules.length; n++){
			freq[n] = 0;
			succ[n] = 0;
		}
		File anwendung = new File("C:/Users/Kenobi/workspace/Chunker_POS_Tagger_Test/newCorporus/anwendung.txt");
		PrintWriter printWriter2 = null;
		try{
			printWriter2 = new PrintWriter(anwendung);

			for (int i = 0; i< corpus_tagged.length; i++){
				//for (int i = 0; i< 16; i++){
				Token tok1 = new Token (corpus_tagged[i]);
				String pos = tok1.getTag();

				Token tok2 = new Token (corpus_chunked[i]);
				String chunk = tok2.getCtag();

				for (int m = 0; m < rules.length; m++){
					Rule rul1 = new Rule(rules[m]);
					String rulpos = rul1.getPostag()[0];
					String rulchunk = rul1.getChunktag();



					if (pos.equals(rulpos)){
						freq[m]++;
						if (chunk.equals(rulchunk)){
							printWriter2.println(corpus_tagged[i]+"_"+rulchunk);
							succ[m]++;
						}
					}
					else{
						printWriter2.println(corpus_tagged[i]);
					}
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


		File auswertung = new File("C:/Users/Kenobi/workspace/Chunker_POS_Tagger_Test/newCorporus/regel_auswertung.txt");
		PrintWriter printWriter3 = null;
		try{
			printWriter3 = new PrintWriter(auswertung);
			for (int o = 0; o<rules.length;o++){
				printWriter3.println("Regel: "+rules[o]+"Freq:"+freq[o]+" Succ:"+succ[o]);
			}

		}catch (FileNotFoundException e){
			e.printStackTrace();
		}
		finally{
			if ( printWriter3 != null ) {
				printWriter3.close();
			}
		}







		final long timeEnd = System.currentTimeMillis(); 
		final long time = (timeEnd - timeStart)/1000;
		System.out.println("Dauer des Programms: " + time + " Sek."); 

	}

}
