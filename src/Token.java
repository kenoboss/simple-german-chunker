
public class Token {
	/*
	 * Beschreibt den Aufbau eines Tokens
	 * Beispiel: Woerter_NN_Wort_1_I-NC
	 */
	private String token;	/* Wort (Token)*/
	private String tag;		/* POS-Tag des Tokens*/	
	private String id;		/* ID des Tokens*/
	private String ctag;	/* Chunk-Tag des Tokens*/
	private String lemma;	/* Lemma des Tokens*/

	public Token(String text) {
	
		String [] wort = text.split("_"); //Zerlegung des Wortes (Tokens) in seine verschiedenen Teile
		if (wort[2].matches("[0-9]*")){
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

	public String getToken() {
		
		return token;
	}

	public String getTag() {
		return tag;
	}

	public String getId() {
		return id;
	}
	
	public String getLemma() {
		return lemma;
	}

	public String getCtag() {
		return ctag;
	}
}
