package llavesObjetos;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.*;

public class Cliente {

    public static void main(String[] args) {
        final String IP = "localhost";
        final int PUERTO = 9996;

        Socket socket;

        //EMPEZAMOS
        try {
            socket = new Socket(IP, PUERTO);

            //Creamos par de llaves
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            SecureRandom numero =  SecureRandom.getInstance("SHA1PRNG");
            generator.initialize(1024, numero);
            //Genero las dos llaves
            KeyPair llaves = generator.generateKeyPair();
            PrivateKey llavePrivada = llaves.getPrivate();
            PublicKey llavePublica = llaves.getPublic();

            //Enviamos llave publica al servidor
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(llavePublica);

            //Ahora podemos recibir la llave publica del servidor
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            PublicKey llaveServidor = (PublicKey) ois.readObject();

            if (llaveServidor!= null){
                System.out.println("Ha llegado la llave del servidor");
            }else {
                System.out.println("Problemas varios");
            }
            //CLIENTE PUEDE ENVIAR MENSAJES
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (socket.isConnected()) {
                            String frase = JOptionPane.showInputDialog("CLIENTE");
                            byte[] fraseServidor = frase.getBytes();
                            //Cifrado
                            Cipher cifrado = Cipher.getInstance(generator.getAlgorithm());
                            //Cifra con privada
                            cifrado.init(Cipher.ENCRYPT_MODE, llavePrivada);
                            byte[] fraseCif = cifrado.doFinal(fraseServidor);
                            //Mandamos mensaje cifrado
                            oos.writeObject(fraseCif);
                        }
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            while (socket.isConnected()){
                //Ahora vamos a  recibir y desencriptar el mensaje
                byte[] fraseServidor = (byte[]) ois.readObject();
                System.out.println("Mensaje cifrado con llave publica de cliente envidado desde el servidor:");
                System.out.println("FRASE: " + fraseServidor);
                // el mensaje del cliente con la llave publica del servidor
                //DESCIFRAMOS
                Cipher cifrado = Cipher.getInstance(generator.getAlgorithm());
                cifrado.init(Cipher.DECRYPT_MODE, llaveServidor);
                byte[] fraseDesc = cifrado.doFinal(fraseServidor);
                String cadena = new String(fraseDesc);
                System.out.println("Mensaje descifrado con llave privada de cliente envidado desde el servidor:");
                System.out.println(cadena);
            }

            //close
            ois.close();
            oos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
