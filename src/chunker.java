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

		} catch (FileNotFoundException e) {
			e.printStackTrace();  
		} catch (IOException e) {
			e.printStackTrace();
		}


		String [] ctags = {"B-NC", "I-NC", "B-VC", "I-VC", "B-PC", "I-PC"};
		String [] postag = {"ADJA", "ADV", "APPR", "ART", "CARD", "NN", "NE", "PPER", "VVFIN", "VVIMP"};
		String [] rules = new String [ctags.length*postag.length];

		// Creating the first rules
		File file = new File("C:/Users/Kenobi/workspace/Chunker_POS_Tagger_Test/newCorporus/rules.txt");
		PrintWriter printWriter = null;
		try{
			printWriter = new PrintWriter(file);


			for (int j = 0; j < postag.length; j++){
				for (int k = 0; k < ctags.length; k++){
					printWriter.println("[POS, 0="+postag[j]+"] = "+ctags[k]);
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


		//			for (int i= 0; i<corpus_tagged.length; i++){
		//				Token tok = new Token (corpus_tagged[i]);
		//				System.out.println(tok.getTag());
		//			}




		final long timeEnd = System.currentTimeMillis(); 
		final long time = (timeEnd - timeStart)/1000;
		System.out.println("Dauer des Programms: " + time + " Sek."); 

	}

}
