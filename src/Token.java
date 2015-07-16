/**
 * Beschreibt den Aufbau eines Tokens
 * Beispiel: Woerter_NN_Wort_1_I-NC
 */
public class Token {

	private String token;	/* Wort (Token)*/
	private String tag;		/* POS-Tag des Tokens*/	
	private String id;		/* ID des Tokens*/
	private String ctag;	/* Chunk-Tag des Tokens*/
	private String lemma;	/* Lemma des Tokens*/

	/**
	 * Das Wort wird hier in seine verschiedenen Bestandteile 
	 * zerlegt: 
	 * Token
	 * POS-Tag des Tokens
	 * ID des Tokens
	 * Lemma des Tokens
	 * Chunk-Tag des Tokens
	 * 
	 * @param einzelenen Wort z.B. token_tag_id_ctag oder token_tag_lemma_id_ctag
	 */
	public Token(String text) {
	
		String [] wort = text.split("_"); 	//Zerlegung des Wortes (Tokens) in seine verschiedenen Teile
		if (wort[2].matches("[0-9]*")){		// Ueberpruefung, ob das dritte Element von wort eine ID oder ein Lemma ist
			this.token = wort[0];
			this.tag = wort[1];
			this.id = wort[2];
			if (wort.length > 3){
				this.ctag = wort[3];
			}
			else{
				this.ctag = "";
			}
		}
		else  {
			this.token = wort[0];
			this.tag = wort[1];
			this.lemma = wort[2];
			this.id = wort[3];
			if (wort.length > 4){
				this.ctag = wort[4];
			}
			else{
				this.ctag = "";
			}
		}
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setLemma (String lemma){
		this.lemma = lemma;
	}

	public void setCtag(String ctag) {
		this.ctag = ctag;
	}

	/**
	 * Gibt das Token selbst zurueck
	 * @return token
	 */
	public String getToken() {
		return token;
	}
	
	/**
	 * Gibt das POS-Tag des Tokens zurueck
	 * @return tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Gibt die ID des Tokens zurueck
	 * @return id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Gibt das Lemma des Tokens zurueck
	 * @return lemma
	 */
	public String getLemma() {
		return lemma;
	}

	/**
	 * Gibt das Chunk-Tag des Tokens zurueck
	 * @return ctag
	 */
	public String getCtag() {
		return ctag;
	}
}
