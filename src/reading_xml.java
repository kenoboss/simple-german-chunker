import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import java.io.File;
import java.io.PrintWriter;


/**
 * Diese Datei ist Teil des Projektes "Simple-German-Chunker".
 * Dieses Programm durchlaeft den Corpus "Tuebda-DZ 9.0" und 
 * liest aus allen .xml - Dateien, die Tokens aus.
 * Es dient zur Beschaffung eines geeigneten Formates des Corpus, 
 * welches fuer das  Training und Testen des Chunkers benoetigt 
 * wird. 
 *
 */
public class reading_xml {

	public static void main(String argv[]) {
		
		final long timeStart = System.currentTimeMillis();

		try {

			//reading the folder 
			File folder = new File("corpora");
			File[] listOfFiles = folder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {				
				if (listOfFiles[i].isFile()) {

					//reading the single xml file from the corpus		
					File fXmlFile = new File("corpora/"+listOfFiles[i].getName());					
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();					
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();					
					Document doc = dBuilder.parse(fXmlFile);
					doc.getDocumentElement().normalize();
					
					NodeList nList = doc.getElementsByTagName("t");
					
					PrintWriter writer = new PrintWriter("justTexts/"+listOfFiles[i].getName()+".txt", "UTF-8");					
					System.out.println("Writining File # "+i);
					
					//reading the word attribute from xml file
					for (int temp = 0; temp < nList.getLength(); temp++) {
						Node nNode = nList.item(temp);
						if (nNode.getNodeType() == Node.ELEMENT_NODE) {
							Element eElement = (Element) nNode;					
							//writing the word into a new .txt file
							writer.println(eElement.getAttribute("word"));
						}
					}
					writer.close();
				}
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		final long timeEnd = System.currentTimeMillis(); 
		final long time = (timeEnd - timeStart)/1000;
		System.out.println("Dauer des Programms: " + time + " Sek."); 
	}

}
