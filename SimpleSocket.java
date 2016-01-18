import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;


public class SimpleSocket extends Socket {

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public SimpleSocket(String ip, int port) throws IOException{

        super(ip, port);
        dataInputStream = new DataInputStream(this.getInputStream());
        dataOutputStream = new DataOutputStream(this.getOutputStream());

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