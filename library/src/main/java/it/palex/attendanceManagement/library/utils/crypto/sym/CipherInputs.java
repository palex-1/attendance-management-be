package it.palex.attendanceManagement.library.utils.crypto.sym;

import java.io.Serializable; 

public class CipherInputs implements Serializable {

	private static final long serialVersionUID = -9103224174475516850L;
	
	private Bytes salt;
	private Bytes initializationVector;
	private int keySize;
	private int pwdIteration;
	private String secretKeyAlgorithm;
	private String algorithm;
	private String cipherAlgorithm;
	
	
	public CipherInputs() {
		
	}

	public Bytes getSalt() {
		return salt;
	}

	public void setSalt(Bytes salt) {
		this.salt = salt;
	}
	
	public Bytes getInitializationVector() {
		return initializationVector;
	}

	public void setInitializationVector(Bytes initializationVector) {
		this.initializationVector = initializationVector;
	}

	public int getKeySize() {
		return keySize;
	}

	public void setKeySize(int keySize) {
		if(keySize<=0) {
			throw new IllegalArgumentException("keySize is invalid");
		}
		this.keySize = keySize;
	}

	public int getPwdIteration() {
		return pwdIteration;
	}

	public void setPwdIteration(int pwdIteration) {
		if(pwdIteration<=0) {
			throw new IllegalArgumentException("pwdIteration is invalid");
		}
		this.pwdIteration = pwdIteration;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getSecretKeyAlgorithm() {
		return secretKeyAlgorithm;
	}

	public void setSecretKeyAlgorithm(String secretKeyAlgorithm) {
		this.secretKeyAlgorithm = secretKeyAlgorithm;
	}

	public String getCipherAlgorithm() {
		return cipherAlgorithm;
	}

	public void setCipherAlgorithm(String cipherAlgorithm) {
		this.cipherAlgorithm = cipherAlgorithm;
	}
	
}
