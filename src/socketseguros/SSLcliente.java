package socketseguros;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Random;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * @author oscar
 */
public class SSLcliente {

    private final String CLIENTPASS = "clientpass";
    private final SSLSocket client;

    public SSLcliente(String direccionservidor, int puerto) throws
            KeyStoreException, FileNotFoundException, IOException,
            NoSuchAlgorithmException, CertificateException,
            UnrecoverableKeyException, KeyManagementException 
    {
        // Indico los certificados seguros del cliente
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream( "C:\\Program Files\\Java\\jdk-19\\bin\\clientKey.jks"),CLIENTPASS.toCharArray());
        
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, CLIENTPASS.toCharArray());
        KeyStore trustedStore = KeyStore.getInstance("JKS");
        // El fichero clientTrustedCerts.jks se crea en el sistema, al ejecutar el comando keytool -import
        trustedStore.load(new FileInputStream("C:\\Program Files\\Java\\jdk-19\\bin\\clientTrustedCerts.jks"), CLIENTPASS.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        
        tmf.init(trustedStore);
        SSLContext sc = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers = tmf.getTrustManagers();
        KeyManager[] keyManagers = kmf.getKeyManagers();
        sc.init(keyManagers, trustManagers, null);
        
        // Creo el socket seguro del cliente
        SSLSocketFactory ssf = sc.getSocketFactory();
        client = (SSLSocket) ssf.createSocket(direccionservidor,
                puerto);
        client.startHandshake();
    }

    public void start() {
        System.out.println("Inicio cliente");
        new Thread() {
            @Override
            public void run() {
                try {
                    Random aleatorio = new Random();
                    PrintWriter output = new PrintWriter(client.getOutputStream());
                    output.println("Cliente: " + aleatorio.nextInt(100));
                    output.flush();
                    BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String received = input.readLine();
                    System.out.println("Recibido: " + received);
                    client.close();
                } 
                catch (IOException e) {
                    System.out.println("Error cliente -> " + e.toString());
                }
            }
        }.start();
    }   
}
