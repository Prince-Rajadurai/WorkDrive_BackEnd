package hashing;

import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class PasswordHashing {

	 private static final int ITERATIONS = 65536;
	 private static final int KEY_LENGTH = 256;
	 private static String salt = "A0+SwdP46VuOQ4J2qKbZrA==";

     public String passwordHashing(String plaintext) throws Exception{

         byte[] saltArray = Base64.getDecoder().decode(salt);

         KeySpec spec = new PBEKeySpec(plaintext.toCharArray(),saltArray,ITERATIONS,KEY_LENGTH);

         SecretKeyFactory factor = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

         byte[] value = factor.generateSecret(spec).getEncoded();

         return Base64.getEncoder().encodeToString(value);

     }
	
}
