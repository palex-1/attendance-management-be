package it.palex.attendanceManagement.library.utils.crypto.sym;

import java.io.BufferedReader;  
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.IOUtils;
import org.springframework.data.util.StreamUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StreamsEncryptionUtil {

	public static final String CIPHER_ALG = "AES/CBC/PKCS5Padding ";
	public static final int PSWD_ITERATIONS = 65536;
	public static final int KEY_SIZE = 256;
	public static final String ALGORITHM = "AES";
	public static final String SECRET_KEY_ALGORITHM = "PBKDF2WithHmacSHA256";
    
	
    public static CipherInputs buildStandardCipher() {
    	byte[] salt = new byte[8];
        new SecureRandom().nextBytes(salt);
        
        byte[] initializationVector = new byte[16];
        new SecureRandom().nextBytes(initializationVector);
        
    	CipherInputs cipherInput = new CipherInputs();
    	cipherInput.setAlgorithm(ALGORITHM);
    	Bytes iv = new Bytes(initializationVector);
    	cipherInput.setInitializationVector(iv);
    	Bytes s = new Bytes(salt);
    	cipherInput.setSalt(s);
    	cipherInput.setKeySize(KEY_SIZE);
    	cipherInput.setPwdIteration(PSWD_ITERATIONS);
    	cipherInput.setSecretKeyAlgorithm(SECRET_KEY_ALGORITHM);
    	cipherInput.setCipherAlgorithm(CIPHER_ALG);
    	
    	return cipherInput;
    }
    
	
	public static void writeAlgorithmDataFile(OutputStream fileOutput, CipherInputs cipherInput) 
						throws JsonProcessingException, FileNotFoundException {
		if(fileOutput==null || cipherInput==null) {
			throw new NullPointerException();
		}
		ObjectMapper objectMapper = new ObjectMapper();
		String cipherStr = objectMapper.writeValueAsString(cipherInput);
		
        final PrintWriter pw1 = new PrintWriter(fileOutput);
		pw1.println(cipherStr);
		pw1.flush();
		pw1.close();
	}
	
	
	public static CipherInputs writeFile(InputStream plainTextInput, 
			char[] psw, String fileOutputPath, String algorithmDataFile)  
					throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, IOException {
		CipherInputs cipherInput = buildStandardCipher();
		
		FileOutputStream fileOutput = new FileOutputStream(fileOutputPath);
		FileOutputStream algorithmDataOutput = new FileOutputStream(algorithmDataFile);
		
		writeFile(plainTextInput, cipherInput, psw, 
				fileOutput, algorithmDataOutput);
		
		return cipherInput;
	}
	
	public static CipherInputs writeFile(String plainTextInputPath, 
			char[] psw, String fileOutputPath, String algorithmDataFile)  
					throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, IOException {
		CipherInputs cipherInput = buildStandardCipher();
		
		FileInputStream plainTextInput = new FileInputStream(fileOutputPath);
		
		FileOutputStream fileOutput = new FileOutputStream(fileOutputPath);
		FileOutputStream algorithmDataOutput = new FileOutputStream(algorithmDataFile);
		
		writeFile(plainTextInput, cipherInput, psw, 
				fileOutput, algorithmDataOutput);
		
		return cipherInput;
	}
	
	public static CipherInputs writeFile(InputStream plainTextInputStream, 
			char[] psw, OutputStream fileOutput, OutputStream algorithmDataOutput)  
					throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, IOException {
		
		CipherInputs cipherInput = buildStandardCipher();
		writeFile(plainTextInputStream, cipherInput, psw, 
				fileOutput, algorithmDataOutput);
		
		return cipherInput;
	}
	
	public static void writeFile(InputStream plainTextInputStream, CipherInputs cipherInput, 
									char[] psw, OutputStream fileOutput, OutputStream algorithmDataOutput) 
											throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException, IOException {
		if(plainTextInputStream==null || cipherInput==null || psw==null || fileOutput==null || algorithmDataOutput==null) {
			throw new NullPointerException();
		}
		
		writeAlgorithmDataFile(algorithmDataOutput, cipherInput);
		
		IvParameterSpec ivspec = new IvParameterSpec(cipherInput.getInitializationVector().getBytes());
		
		CipherOutputStream output = encryptStream(fileOutput, cipherInput.getSalt().getBytes(), ivspec, psw,
				cipherInput.getSecretKeyAlgorithm(), cipherInput.getAlgorithm(), 
				cipherInput.getCipherAlgorithm(), cipherInput.getPwdIteration(), cipherInput.getKeySize());
		
		IOUtils.copy(plainTextInputStream, output);
		
		output.close();
	}
	
	
	public static InputStream decryptFile(char[] psw, String inputFile, String algorithmDataFile) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException {
		if(psw==null || inputFile==null || algorithmDataFile==null) {
			throw new NullPointerException();
		}
		CipherInputStream cipherInput = initDecryptionStream(psw, inputFile, algorithmDataFile);
		return cipherInput;
	}
	
	public static CipherInputStream initDecryptionStream(char[] psw, String inputFile, 
			String algorithmDataFile) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException {
		if(psw==null || inputFile==null || algorithmDataFile==null) {
			throw new NullPointerException();
		}
		InputStream fileInput = new FileInputStream(inputFile);
		CipherInputs cipherInput = readAlgorithmDataFile(algorithmDataFile);
		
		IvParameterSpec ivspec = new IvParameterSpec(cipherInput.getInitializationVector().getBytes());
		CipherInputStream input = decryptStream(fileInput, cipherInput.getSalt().getBytes(), ivspec, psw,
				cipherInput.getSecretKeyAlgorithm(), cipherInput.getAlgorithm(), 
				cipherInput.getCipherAlgorithm(), cipherInput.getPwdIteration(), cipherInput.getKeySize());
		
		return input;
	}
		
	public static CipherInputs readAlgorithmDataFile(String filePath) throws IOException {
		if(filePath==null) {
			throw new NullPointerException();
		}
		InputStream fileInput = new FileInputStream(filePath);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(fileInput));
		String json = "";
		String strLine;
		while ((strLine = br.readLine()) != null){
			json+=strLine;
		}
		br.close();
		
		ObjectMapper mapper = new ObjectMapper();

		return mapper.readValue(json, CipherInputs.class);
	}
	
	public static CipherOutputStream encryptStream(OutputStream stream, byte[] salt, 
			IvParameterSpec ivspec, char[] psw, String secretKeyAlgorithm, String algorithm,
			String cipherAlgorithm, int pswIteration, int keySize) 
					throws NoSuchAlgorithmException, InvalidKeyException,
						   NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException {
		if(stream==null || ivspec==null || salt==null || psw==null || secretKeyAlgorithm==null 
				|| algorithm==null || cipherAlgorithm==null) {
			throw new NullPointerException();
		}
		if(pswIteration<=0 || keySize<=0) {
			throw new IllegalArgumentException();
		}
		
		SecretKeyFactory factory = SecretKeyFactory.getInstance(secretKeyAlgorithm);
		KeySpec spec = new PBEKeySpec(psw, salt, pswIteration, keySize);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), algorithm);
		
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
        
        
        CipherOutputStream output = new CipherOutputStream(stream, cipher);
        
        return output;
	}
	
	public static CipherInputStream decryptStream(InputStream stream, byte[] salt, 
			IvParameterSpec ivspec, char[] psw, String secretKeyAlgorithm, String algorithm,
			String cipherAlgorithm, int pswIteration, int keySize) throws NoSuchAlgorithmException, InvalidKeyException,
										NoSuchPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException {
		
		if(stream==null || ivspec==null || salt==null || psw==null || secretKeyAlgorithm==null 
				|| algorithm==null || cipherAlgorithm==null) {
			throw new NullPointerException();
		}
		if(pswIteration<=0 || keySize<=0) {
			throw new IllegalArgumentException();
		}
        
		SecretKeyFactory factory = SecretKeyFactory.getInstance(secretKeyAlgorithm);
		KeySpec spec = new PBEKeySpec(psw, salt, pswIteration, keySize);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey secretKey = new SecretKeySpec(tmp.getEncoded(), algorithm);
		
		Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
        
        CipherInputStream input = new CipherInputStream(stream, cipher);
        
        return input;
	}
	
}
