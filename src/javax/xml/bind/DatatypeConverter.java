package javax.xml.bind;

import java.util.Base64;

/**
 * Minimal Java 9+ compatibility shim for legacy dependencies which only use
 * JAXB's Base64 helpers.
 */
public final class DatatypeConverter {

	private DatatypeConverter() {
	}

	public static String printBase64Binary(byte[] val) {
		return Base64.getEncoder().encodeToString(val);
	}

	public static byte[] parseBase64Binary(String lexicalXSDBase64Binary) {
		return Base64.getDecoder().decode(lexicalXSDBase64Binary);
	}
}
