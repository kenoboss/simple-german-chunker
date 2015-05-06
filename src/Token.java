
public class Token {

	private String token;
	private String tag;
	private String id;
	private String ctag;

	public Token(String text) {
		String [] wort = text.split("_");
		this.token = wort[0];
		this.tag = wort[1];
		this.id = wort[2];
		this.ctag = wort[3];
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

	public String getCtag() {
		return ctag;
	}
}
