package socketseguros;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

/**
 *
 * @author oscar
 */
public class SocketSeguros {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int puerto = 5557;
        try 
        {
            // Creo el servidor
            new SSLservidor(puerto).start();
            // Creo un cliente
            new SSLcliente("localhost", puerto).start();
        } 
        catch (IOException | KeyManagementException | KeyStoreException
                | NoSuchAlgorithmException | UnrecoverableKeyException
                | CertificateException error) 
        {
            System.out.println("Error -> " + error.toString());
        }
    }
}
   
