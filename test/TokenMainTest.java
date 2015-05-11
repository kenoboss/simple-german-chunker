import static org.junit.Assert.*;

import org.junit.Test;


public class TokenMainTest {

	@Test
	public void testMain() {
		String [] text = {"Trier_NN_0_BN", "ist_VFIN_1_BN"};
		Token tok = new Token(text[0]);
		tok.getToken();
		assertTrue ("Token: ", "Trier" == tok.getToken());
	}

}
