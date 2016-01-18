import java.net.Socket;

public abstract class SimpleRunnable implements Runnable {

    private SimpleSocket simpleSocket = null;

    public void setSimpleSocket(Socket socket){
        simpleSocket = (SimpleSocket)socket;
    }

    public void setSimpleSocket(SimpleSocket simpleSocket){
        this.simpleSocket = simpleSocket;
    }



}
