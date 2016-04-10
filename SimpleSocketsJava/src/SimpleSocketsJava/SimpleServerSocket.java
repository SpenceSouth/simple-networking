package SimpleSocketsJava;

import SimpleSocketsJava.Security.EncryptionUtils;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleServerSocket<SimpleRunnableType extends SimpleRunnable> extends ServerSocket{

    private int port;
    private boolean verbose = false;
    private ExecutorService executorService;
    private Class<SimpleRunnableType> srt;
    private PrivateKey privateKey = null;
    private SecretKey secretKey = null;

    public SimpleServerSocket(int port, Class<SimpleRunnableType> srt) throws IOException {
        super(port);
        int numberOfThreads = 1 + Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(numberOfThreads);
        this.srt = srt;
    }

    public SimpleServerSocket(int port, Class<SimpleRunnableType> srt, int threadPoolSize) throws IOException {
        super(port);
        executorService = Executors.newFixedThreadPool(threadPoolSize);
        this.srt = srt;
    }

    public void setPrivateKey(String privateKeyPath) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException{
        privateKey = EncryptionUtils.readPrivateKey(privateKeyPath);
    }

    public void setSecretKey(SecretKey key) {
        this.secretKey = key;
    }

    public void setSecretKey(String key){
        this.secretKey = EncryptionUtils.aesStringToKey(key);
    }

    private SimpleRunnableType createThread() throws InstantiationException, IllegalAccessException {
        return srt.newInstance();
    }

    public void setVerbose(boolean verbose){
        this.verbose = verbose;
    }

    public void listen() throws IOException, IllegalAccessException, InstantiationException{

        while(true){

            SimpleRunnableType socketThread = null;

            Socket socket = this.accept();
            socket.setSoTimeout(30000);
            socketThread = createThread();
            socketThread.setSimpleSocket(socket);

            if(privateKey != null){
                try {
                    socketThread.setPrivateKey(privateKey);
                }
                catch(Exception ex){
                    System.out.println("Could not encrypt message with private key");
                    ex.printStackTrace();
                }
            }

            if(secretKey != null){
                try {
                    socketThread.setSecretKey(secretKey);
                }
                catch(Exception ex){
                    System.out.println("Could not encrypt message with secret key");
                    ex.printStackTrace();
                }
            }

            if(verbose) System.out.println("Received socket connection.");

            executorService.execute(socketThread);

        }
    }

}
