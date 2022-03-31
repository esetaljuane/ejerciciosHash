package llavesObjetos;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;

public class Servidor {

    public static void main(String[] args) {
        final int PUERTO = 9996;
        ServerSocket ss;
        Socket so;
        try {
            ss = new ServerSocket(PUERTO);
            so = ss.accept();

            ObjectInputStream ois = new ObjectInputStream(so.getInputStream());
            PublicKey llaveCliente = (PublicKey) ois.readObject();
            // En el momento en el que recibo la llave publica,
            if (llaveCliente != null){
                System.out.println("Ha llegado la llave del cliente");
            }else {
                System.out.println("Problemas varios");
            }


            //Creamos par de llaves
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            SecureRandom numero =  SecureRandom.getInstance("SHA1PRNG");
            generator.initialize(1024, numero);
            //Genero las dos llaves
            KeyPair llaves = generator.generateKeyPair();
            PrivateKey llavePrivada = llaves.getPrivate();
            PublicKey llavePublica = llaves.getPublic();

            //MANDAMOS LLAVE PUBLICA AL CLIENTE
            ObjectOutputStream oos = new ObjectOutputStream(so.getOutputStream());
            oos.writeObject(llavePublica);

            // --- RECIBIR MENSAJES --- //
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!ss.isClosed()){
                        try {
                            //Ahora vamos a  recibir y desencriptar el mensaje
                            byte[] fraseServidor = (byte[]) ois.readObject();
                            System.out.println("Mensaje cifrado con llave publica de cliente envidado desde el servidor:");
                            System.out.println("FRASE: " + fraseServidor);
                            // el mensaje del cliente con la llave publica del servidor
                            //DESCIFRAMOS
                            Cipher cifrado = Cipher.getInstance(generator.getAlgorithm());
                            cifrado.init(Cipher.DECRYPT_MODE, llaveCliente);
                            byte[] fraseDesc = cifrado.doFinal(fraseServidor);
                            String cadena = new String(fraseDesc);
                            System.out.println("Mensaje descifrado con llave privada de cliente envidado desde el servidor:");
                            System.out.println(cadena);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
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
            }).start();


            //Una vez que he mandado la llave publica
            //Puedo mandar mensajes
            while (!ss.isClosed()){
                String frase = JOptionPane.showInputDialog("SERVIDOR");
                byte[] fraseServidor = frase.getBytes();
                //Cifrado
                Cipher cifrado = Cipher.getInstance(generator.getAlgorithm());
                //Cifra con privada
                cifrado.init(Cipher.ENCRYPT_MODE, llavePrivada);
                byte[] fraseCif = cifrado.doFinal(fraseServidor);
                //Mandamos mensaje cifrado
                oos.writeObject(fraseCif);
            }

            //close
            ois.close();
            oos.close();
            so.close();
            ss.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

    }
}
