package SimpleSocketsJava;

import SimpleSocketsJava.Security.EncryptionUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;


public class SimpleSocket {

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Socket socket;
    private PublicKey publicKey = null;
    private PrivateKey privateKey = null;

    public SimpleSocket(String ip, int port) throws IOException {

        this.socket = new Socket(ip, port);
        this.socket.setSoTimeout(30000);
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());

    }

    public SimpleSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.socket.setSoTimeout(30000);
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    public void setPublicKey(String publicKeyPath) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        publicKey = EncryptionUtils.readPublicKey(publicKeyPath);
    }

    public void setPrivateKey(String pathToPrivateKey) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        privateKey = EncryptionUtils.readPrivateKey(pathToPrivateKey);
    }

    public void setPrivateKey(PrivateKey privateKey) throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        this.privateKey = privateKey;
    }

    public void setTimeOut(int ms) throws SocketException {
        this.socket.setSoTimeout(ms);
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

        try {
            result = new String(data, "UTF-8");

            if(privateKey != null) {
                try {
                    result = new String(EncryptionUtils.decrypt(privateKey, data), "UTF-8");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        }

        return result;
    }

    public void writeString(String message) {
        try {

            byte[] data;
            data = message.getBytes("UTF-8");

            if(publicKey != null){
                try {
                    data = EncryptionUtils.encrypt(publicKey, data);
                }
                catch (Exception nsae){
                    nsae.printStackTrace();
                }
            }

            dataOutputStream.writeInt(data.length);
            dataOutputStream.write(data);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
