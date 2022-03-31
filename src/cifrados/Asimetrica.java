package cifrados;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;

public class Asimetrica {
    public static void main(String[] args) {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            SecureRandom numero =  SecureRandom.getInstance("SHA1PRNG");
            generator.initialize(1024, numero);
            //Genero las dos llaves
            KeyPair llaves = generator.generateKeyPair();
            PrivateKey llavePrivada = llaves.getPrivate();
            PublicKey llavePublica = llaves.getPublic();
            byte[] frase = "Espero que tambien funcione".getBytes();

            //Cifrador
            try {
                Cipher cifrado = Cipher.getInstance(generator.getAlgorithm()+"/ECB/PKCS1Padding");
                //Cifro con la privida
                 cifrado.init(Cipher.ENCRYPT_MODE, llavePrivada);
                 byte[] frasCif = cifrado.doFinal(frase);

                 //Desciframos
                cifrado.init(Cipher.DECRYPT_MODE, llavePublica);
                byte[] fraseDes = cifrado.doFinal(frasCif);
                String cadena = new String(fraseDes);
                //Mostramos
                System.out.println(cadena);
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
