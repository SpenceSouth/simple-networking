import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;


public class SimpleSocket {

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Socket socket;

    public SimpleSocket(String ip, int port) throws IOException {

        this.socket = new Socket(ip, port);
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());

    }

    public SimpleSocket(Socket socket) throws IOException {
      this.socket = socket;
      this.dataInputStream = new DataInputStream(socket.getInputStream());
      this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    public String readString(){

        int length;
        byte[] data = null;
        String result = null;

        try {
            length = dataInputStream.readInt();
            data = new byte[length];
            dataInputStream.readFully(data);
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }

        if(data == null){
            return null;
        }

        try{
            result = new String(data,"UTF-8");
        }
        catch(UnsupportedEncodingException uee){
            uee.printStackTrace();
        }

        return result;
    }

    public void writeString(String dataToWrite) {
        try {
            byte[] data = dataToWrite.getBytes("UTF-8");
            dataOutputStream.writeInt(data.length);
            dataOutputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
