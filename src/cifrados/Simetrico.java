package cifrados;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Simetrico {

    public static void main(String[] args) {
        //Generamos la llave
        try {
            KeyGenerator generador = KeyGenerator.getInstance("AES");
            generador.init(256);
            SecretKey llave = generador.generateKey();
            //String frase
            String frase = "Espero que funcione";
            byte[] fraseByte = frase.getBytes();
            //Creamos cifrador
            Cipher cifrado = Cipher.getInstance(generador.getAlgorithm());
            cifrado.init(Cipher.ENCRYPT_MODE, llave);
            //Creamos frase encriptada
            byte[] fraseCifrada = cifrado.doFinal(fraseByte);
            System.out.println(fraseCifrada.hashCode());

            //Desencriptamos
            cifrado.init(Cipher.DECRYPT_MODE, llave);
            byte[] fraseDes = cifrado.doFinal(fraseCifrada);
            String st = new String(fraseDes);
            System.out.println(st);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

    }
}
