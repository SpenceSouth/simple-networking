package SimpleSocketsJava;

import SimpleSocketsJava.Security.EncryptionUtils;

import javax.crypto.SecretKey;
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
import java.util.ArrayList;


public class SimpleSocket {

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Socket socket;
    private PublicKey publicKey = null;
    private PrivateKey privateKey = null;
    private SecretKey secretKey = null;

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

    public void setSecretKey(SecretKey key){
        this.secretKey = key;
    }

    public void setSecretKey(String key){
        this.secretKey = EncryptionUtils.aesStringToKey(key);
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
                    result = new String(EncryptionUtils.decryptRsa(privateKey, data), "UTF-8");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if(secretKey != null) {
                try {
                    result = EncryptionUtils.decryptAes(secretKey, new String(data, "UTF-8"));
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
                    data = EncryptionUtils.encryptRsa(publicKey, data);
                }
                catch (Exception nsae){
                    nsae.printStackTrace();
                }
            }

            if(secretKey != null){
                try {
                    data = EncryptionUtils.encryptAes(secretKey, message).getBytes("UTF-8");
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

    public int readInteger(){
        return Integer.parseInt(readString());
    }

    public void writeInteger(int value){
        writeString(String.valueOf(value));
    }

    public ArrayList<String> readArrayList(){

        ArrayList<String> message = new ArrayList<>();
        int size = readInteger();

        for(int i = 0; i < size; i++){
            message.add(readString());
        }

        return message;
    }

    public void writeArrayList(ArrayList<String> list){

        // Send number of items to expect
        writeInteger(list.size());

        // Send each item in the list
        for(String item : list){
            writeString(item);
        }

    }

    public String[] readArray(){

        int size = readInteger();
        String[] message = new String[size];

        for(int i = 0; i < size; i++){
            message[i] = readString();
        }

        return message;
    }

    public void writeArray(String[] list){

        // Send number of items to expect
        writeInteger(list.length);

        // Send each item in the list
        for(String item : list){
            writeString(item);
        }

    }

}
