package pruebas;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;

public class Main {
    public static void main(String[] args) {

        //CREO LLAVES
        try {
            //Creamos par de llaves
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            //SecureRandom numero =  SecureRandom.getInstance("SHA1PRNG");
            generator.initialize(1024);
            //Genero las dos llaves
            KeyPair llaves = generator.generateKeyPair();
            PrivateKey llavePrivada = llaves.getPrivate();
            PublicKey llavePublica = llaves.getPublic();

            //CREO frase encriptada
            String frase = "Hola, Soy una frase";
            byte[] fraseServidor = frase.getBytes();
            //Cifrado
            Cipher cifrado = Cipher.getInstance(generator.getAlgorithm());
            //Cifra con privada
            cifrado.init(Cipher.ENCRYPT_MODE, llavePrivada);
            byte[] fraseCif = cifrado.doFinal(fraseServidor);

            //DOblamos el procedimiento
            KeyPairGenerator generator1 = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            //
            KeyPair llavesAux = generator1.generateKeyPair();
            PublicKey llavePublica1 = llavesAux.getPublic();
            //
            Cipher cipher = Cipher.getInstance(generator.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, llavePublica1);
            byte[] fraseDesc = cipher.doFinal(fraseCif);
            String cadena = new String(fraseDesc);
            System.out.println(cadena);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }
}
