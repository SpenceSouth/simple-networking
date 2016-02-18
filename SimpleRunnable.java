import java.io.IOException;
import java.net.Socket;

public abstract class SimpleRunnable implements Runnable {

    protected SimpleSocket simpleSocket = null;

    public void setSimpleSocket(Socket socket) throws IOException {
        simpleSocket = new SimpleSocket(socket);
    }

    public void setSimpleSocket(SimpleSocket simpleSocket){
        this.simpleSocket = simpleSocket;
    }

    abstract public void run();
}
