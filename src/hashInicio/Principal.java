package hashInicio;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

public class Principal {
    public static void main(String[] args) {
        String metodo = "MD5";
        try {
            MessageDigest hash = MessageDigest.getInstance(metodo);
            String cadena = "Cadena para el hast";

            byte[] cadenaBytes = cadena.getBytes();
            hash.update(cadenaBytes);

            byte[] resumen1 = hash.digest();

            String resumen1Hex = DatatypeConverter.printHexBinary(resumen1).toLowerCase(Locale.ROOT);
            System.out.println("hash: " + resumen1Hex);



        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
