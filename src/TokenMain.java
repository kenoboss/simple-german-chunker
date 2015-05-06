
public class TokenMain {

	public static void main(String[] args) {

		String [] text = {"Trier_NN_0_BN", "ist_VFIN_1_BN"};
		Token tok = new Token(text[0]);
		System.out.println(tok.getToken());
	}

}
