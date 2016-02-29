import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleServerSocket<SimpleRunnableType extends SimpleRunnable> extends ServerSocket{

    private int port;
    private boolean verbose = false;
    private ExecutorService executorService;
    private Class<SimpleRunnableType> srt;

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

            if(verbose) System.out.println("Received socket connection.");

            executorService.execute(socketThread);

        }
    }

}
