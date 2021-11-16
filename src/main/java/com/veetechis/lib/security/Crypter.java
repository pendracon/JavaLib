package com.veetechis.lib.security;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * <p>
 * This class provides utility methods for common cryptographic handling needs.
 * </p>
 *
 * @author		pendraconx@gmail.com
 */
public class Crypter
{
	/**
	 * Default password-based symmetric key derivation algorithm.
	 */
	public static final String DEFAULT_KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA256";

	/**
	 * Crypto cipher strengths.
	 */
	public static enum CipherStrength {
		AES_WEAK( "AES", 128 ),
		AES_MEDIUM( "AES", 192 ),
		AES_STRONG( "AES", 256 ),
		AES_CBC_PKCS5( "AES/CBC/PKCS5Padding", -1 );

		/**
		 * Returns the cryptographic algorithm of this cipher.
		 */
		public String algorithm() { return algorithm; }

		/**
		 * Returns the key length of this cipher in bits.
		 */
		public int length() { return cipherLength; }

		private CipherStrength( String algo, int len )
		{
			this.algorithm = algo;
			this.cipherLength = len;
		}

		private final String algorithm;
		private final int cipherLength;
	}


	/**
	 * Returns the given data in encrypted form using the specified cipher
	 * strength, secret key, and initialization vector.
	 * 
	 * @param strength  the encryption strength.
	 * @param input  the data to decrypt.
	 * @param key  the secret key to use for decryption.
	 * @param iv  the initialization vector to use for decryption.
	 * @return  the data in unencrypted form.
	 * @throws CrypterException
	 */
	public static byte[] encrypt( CipherStrength strength, byte[] input, SecretKey key, IvParameterSpec iv )
	throws CrypterException
	{
		return dencrypt( Cipher.ENCRYPT_MODE, strength, input, key, iv );
	}

	/**
	 * Returns the given encrypted data in unencrypted form using the specified
	 * cipher strength, secret key, and initialization vector.
	 * 
	 * @param strength  the encryption strength.
	 * @param input  the data to decrypt.
	 * @param key  the secret key to use for decryption.
	 * @param iv  the initialization vector to use for decryption.
	 * @return  the data in unencrypted form.
	 * @throws CrypterException
	 */
	public static byte[] decrypt( CipherStrength strength, byte[] input, SecretKey key, IvParameterSpec iv )
	throws CrypterException
	{
		return dencrypt( Cipher.DECRYPT_MODE, strength, input, key, iv );
	}

	/**
	 * Returns a randomly generated symmetric key of the specified cipher
	 * strength.
	 * 
	 * @param strength  the strength of the generated key.
	 * @throws CrypterException
	 */
	public static SecretKey generateSymmetricKey( CipherStrength strength )
	throws CrypterException
	{
		validateKeyLength( strength );

		SecretKey key;
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance( strength.algorithm() );
			keyGenerator.init( strength.length() );
			key = keyGenerator.generateKey();
		}
		catch (Exception e) {
			throw new CrypterException( "Error while generating key.", e );
		}

		return key;
	}

	/**
	 * Returns a derived symmetric key of the specified cipher strength from
	 * the provided password and salt values. Key specification
	 * 'PBKDF2WithHmacSHA256' is used for the key-derivation algorithm.
	 * 
	 * @param strength  the strength of the generated key.
	 * @param password  the password from which to derive a key.
	 * @param salt  the salt for the key.
	 * @throws CrypterException
	 */
	public static SecretKey getSymmetricKeyFromPassword( CipherStrength strength, String password, String salt )
	throws CrypterException
	{
		validateKeyLength( strength );

		SecretKey key;
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance( DEFAULT_KEY_DERIVATION_ALGORITHM );
			KeySpec spec = new PBEKeySpec( password.toCharArray(), salt.getBytes(), 65536, strength.length() );
			key = new SecretKeySpec( factory.generateSecret(spec).getEncoded(), strength.algorithm() );
		}
		catch (Exception e) {
			throw new CrypterException( "Error while deriving key.", e );
		}

		return key;
	}

	/**
	 * Returns a randomly generated cryptographic initialization vector.
	 */
	public static IvParameterSpec generateIv()
	{
		byte[] iv = new byte[16];
		new SecureRandom().nextBytes( iv );
		return new IvParameterSpec( iv );
	}
	
	/**
	 * Returns the maximum key length supported for the specified cipher on the
	 * runtime platform.
	 * 
	 * @param  cipher  the cipher type.
	 * @throws CrypterException
	 */
	public static int maximumKeyLength( CipherStrength cipher )
	throws CrypterException
	{
		int max;
		try {
			max = Cipher.getMaxAllowedKeyLength( cipher.algorithm() );
		}
		catch (Exception e) {
			throw new CrypterException( "Error while checking maximum allowed key length.", e );
		}

		return max;
	}


	/**
	 * Returns the given input data in de/encrypted form, depending on the
	 * specified mode, using the specified cipher strength, secret key, and
	 * initialization vector.
	 * 
	 * @param mode  the cipher handling mode.
	 * @param strength  the encryption strength.
	 * @param input  the data to encrypt.
	 * @param key  the secret key to use for encryption.
	 * @param iv  the initialization vector to use for encryption.
	 * @return  the text in encrypted form.
	 * @throws CrypterException
	 */
	protected static byte[] dencrypt( int mode, CipherStrength strength, byte[] input, SecretKey key, IvParameterSpec iv )
	throws CrypterException
	{
		validateKeyLength( strength );

		byte[] output;
		try {
			Cipher cipher = Cipher.getInstance( strength.algorithm() );
			cipher.init( mode, key, iv );

			if (input.length > 1024) {
				ByteArrayInputStream in = new ByteArrayInputStream( input );
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int byteCnt;
				while ((byteCnt = in.read(buffer)) != -1) {
					output = cipher.update( buffer, 0, byteCnt );
					if (output != null) {
						out.write( output );
					}
				}
				output = cipher.doFinal();
				if (output != null) {
					out.write( output );
				}
				out.flush();
				out.close();
				in.close();
				output = out.toByteArray();
			}
			else {
				output = cipher.doFinal( input );
			}
		}
		catch (Exception e) {
			String direction = (mode == Cipher.ENCRYPT_MODE ? "encrypting" : "decrypting");
			throw new CrypterException( String.format("Error while %s data.", direction), e );
		}

		return output;
	}


	private static void validateKeyLength( CipherStrength cipher )
	throws CrypterException
	{
		int maxKeyLen = maximumKeyLength( cipher );
		if (cipher.length() > maxKeyLen) {
			throw new CrypterException(
				String.format("Key length of %s exceeds platform maximum of %d bits", cipher.name(), maxKeyLen) );
		}
	}


	public static final void main( String[] args )
	throws Exception
	{
		String password = null;
		String salt = null;
		String phrase = null;
		if (args.length > 0) {
			password = args[0];
			salt = Encoder.generateKey( password.length() );
		}
		if (args.length > 1) {
			phrase = args[1];
		}
		
		System.out.println(
			String.format("Maximum platform key length for %s keys is %d.\n",
				CipherStrength.AES_WEAK.algorithm(), maximumKeyLength(CipherStrength.AES_WEAK)) );
		
		SecretKey key = null;
		for (CipherStrength cipher: CipherStrength.values()) {
			if (cipher.length() != -1) {
				key = generateSymmetricKey( cipher );
				System.out.println( String.format("Generated key for %s:\n%s\n",
					cipher.name(), new String(key.getEncoded())) );

				if (password != null) {
					key = getSymmetricKeyFromPassword( cipher, password, salt );
					System.out.println( String.format("Derived %s key from password '%s' with salt '%s':\n%s\n",
						cipher.name(), password, salt, new String(key.getEncoded())) );
				}
			}
		}

		IvParameterSpec iv = generateIv();
		System.out.println( String.format("Generated IV: %s\n", new String(iv.getIV())) );

		if (phrase == null) {
			phrase = Encoder.generateKey( 1100 );
		}
		System.out.println( String.format("De/Encrypting phrase: %s...", phrase) );
		byte[] encrypted = encrypt( CipherStrength.AES_CBC_PKCS5, phrase.getBytes(), key, iv );
		String encryptedText = Base64.getEncoder().encodeToString( encrypted );
		System.out.println( String.format("Encrypted -> base64:\n%s", encryptedText) );

		String encryptedString = new String( encrypted );
		String unencodedString = new String( Base64.getDecoder().decode(encryptedText) );
		System.out.println( "Encrypted = " + encryptedString );
		System.out.println( "Unencoded = " + unencodedString );

		if (unencodedString.equals(encryptedString)) {
			String phraseBack = new String( decrypt(CipherStrength.AES_CBC_PKCS5, encrypted, key, iv) );
			System.out.println( String.format("Unencrypted <- base64:\n%s", phraseBack) );
		}
	}

} // End of class: +com.veetechis.lib.security.Crypter
