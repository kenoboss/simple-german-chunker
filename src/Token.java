
public class Token {

	private String token;
	private String tag;
	private String id;
	private String ctag;
	private String lemma;

	public Token(String text) {
	
		String [] wort = text.split("_");
//		if (wort[2].matches("[0-9]")){
			this.token = wort[0];
			this.tag = wort[1];
			this.id = wort[2];
			if (wort.length > 3){
				this.ctag = wort[3];
			}
			else{
				this.ctag = "";
			}
//		}
//		else  {
//			this.token = wort[0];
//			this.tag = wort[1];
//			this.lemma = wort[2];
//			this.id = wort[3];
//			if (wort.length > 4){
//				this.ctag = wort[4];
//			}
//			else{
//				this.ctag = "";
//			}
//		}
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
