package SimpleSocketsJava;

import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

public abstract class SimpleRunnable implements Runnable {

    protected SimpleSocket simpleSocket = null;

    public void setSimpleSocket(Socket socket) throws IOException {
        simpleSocket = new SimpleSocket(socket);
    }

    public void setSimpleSocket(SimpleSocket simpleSocket){
        this.simpleSocket = simpleSocket;
    }

    public void setPrivateKey(String pathToprivateKey) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        this.simpleSocket.setPrivateKey(pathToprivateKey);
    }

    public void setPrivateKey(PrivateKey privateKey) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        this.simpleSocket.setPrivateKey(privateKey);
    }

    abstract public void run();
}
