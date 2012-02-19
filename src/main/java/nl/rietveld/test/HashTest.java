package nl.rietveld.test;

import static java.lang.System.out;

import java.security.MessageDigest;
import java.util.UUID;

public class HashTest {
	public HashTest() {}

	/**
	 * Testen van de SHA-256 hash met random UUID. 
	 * @throws Exception Als er iets mis gaat. 
	 */
	public void testSHA2() throws Exception {
		String uuid = UUID.randomUUID().toString();
		byte[] hashBytes = getHash(uuid);
		String hash = bytesToString(hashBytes);

		out.println( "UUID: " + uuid);
		out.println( "Hash [" + hash.length() + "]: " + hash );
	}

	/**
	 * Hierin berekenen wij de hash van iets. 
	 * @param message Waarop wij de hash berekenen. 
	 * @return hash De berekende hash. 
	 * @throws Exception Als er iets misgaat. 
	 */
	public byte[] getHash(String message) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		digest.reset();
		byte[] input = digest.digest(message.getBytes("UTF-8"));
		return input;
	}

	/**
	 * Conversie functie: byte array naar een string. 
	 * @param array een byte array. 
	 * @return de string. 
	 */
	public static String bytesToString(byte[] array) {
		StringBuffer out = new StringBuffer();
		for (int k = 0; k < array.length; k++) {
			out.append(byteToHex(array[k]));
		}
		return out.toString();
	}

	 static public String byteToHex(byte b) {
	      // Returns hex String representation of byte b
	      char hexDigit[] = {
	         '0', '1', '2', '3', '4', '5', '6', '7',
	         '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
	      };
	      char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
	      return new String(array);
	   }

}
