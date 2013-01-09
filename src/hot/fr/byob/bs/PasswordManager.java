package fr.byob.bs;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Random;

import org.jboss.seam.Component;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Transactional;
import org.jboss.seam.util.Base64;
import org.jboss.seam.util.Hex;

import fr.byob.bs.debug.MeasureCalls;

@Name("BSPasswordManager")
@MeasureCalls
public class PasswordManager implements Serializable {

	private static final long serialVersionUID = 1L;
	private String digestAlgorithm;
	private String charset;

	public PasswordManager() {
		this.setCharset("UTF-8");
		this.setDigestAlgorithm("MD5");
		this.setEncoding(Encoding.base64);
	}

	public enum Encoding {
		base64, hex
	}

	private Encoding encoding = Encoding.hex;

	public String getDigestAlgorithm() {
		return this.digestAlgorithm;
	}

	public void setDigestAlgorithm(String algorithm) {
		this.digestAlgorithm = algorithm;
	}

	public String getCharset() {
		return this.charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Encoding getEncoding() {
		return this.encoding;
	}

	public void setEncoding(Encoding encoding) {
		this.encoding = encoding;
	}

	@Transactional
	public String hash(String plainTextPassword) {
		try {
			MessageDigest digest = MessageDigest.getInstance(digestAlgorithm);
			digest.update(plainTextPassword.getBytes(charset));
			byte[] rawHash = digest.digest();
			if (encoding != null && encoding.equals(Encoding.base64)) {
				return Base64.encodeBytes(rawHash);
			} else {
				return new String(Hex.encodeHex(rawHash));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private final static String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

	public String getRandomString(int lenght) {
		Random rand = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lenght; i++) {
			sb.append(alphabet.charAt(rand.nextInt(alphabet.length())));
		}
		return sb.toString();
	}

	public static PasswordManager instance() {
		return (PasswordManager) Component.getInstance(PasswordManager.class,
				ScopeType.EVENT);
	}

	public static void main(String[] args) {
		PasswordManager manager = new PasswordManager();
		manager.setCharset("UTF-8");
		manager.setDigestAlgorithm("MD5");
		manager.setEncoding(Encoding.base64);
		System.out.println(manager.hash("emilie"));
	}

}