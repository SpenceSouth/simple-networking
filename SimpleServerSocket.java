import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleServerSocket extends ServerSocket{

    private int port;
    private boolean verbose = true;
    private ExecutorService executorService;

    public SimpleServerSocket(int port) throws IOException{
        super(port);
        executorService = Executors.newFixedThreadPool(10);
    }

    public SimpleServerSocket(int port, int threadPoolSize) throws IOException{
        super(port);
        executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    public void setVerbose(boolean verbose){
        this.verbose = verbose;
    }

    public void listen(SimpleRunnable simpleRunnable){
        while(true){

            try{
                Socket socket = this.accept();
                simpleRunnable.setSimpleSocket(socket);
                if(verbose) System.out.println("Received socket connection.");
            }
            catch(SocketException se){
                se.printStackTrace();
                break;
            }
            catch(IOException ioe){
                ioe.printStackTrace();
                break;
            }

            executorService.submit(simpleRunnable);

        }
    }

}