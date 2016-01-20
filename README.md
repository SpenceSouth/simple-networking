## General purpose

This generic API provides a very simple and quick to implement solution for creating basic socket connections between two computers.  The API supports parallel processing, with an default of 10 threads.  This number can be altered by passing an integer as the second argument when creating a new SimpleServerSocket object.

### Future improvements include:
- Encryption support
- Cross compatible API's for Python, Ruby, and C


## Basic implementation of server side socket code
    public class ServerTest {
        public static void main(String args[]) throws Exception{
            SimpleServerSocket server = new SimpleServerSocket(8000);
            server.listen(new SocketThread());
        }
    }

    /** User created class that extends SimpleRunnable.  This will execute on each socket connection.
    * Developer has access to the SimpleSocket protected class from SimpleRunnable and can use it's
    * abstracted methods.*/
    class SocketThread extends SimpleRunnable{

        @Override
        public void run(){
        System.out.println(simpleSocket.readString());
        }
    }


--------------------------------------------------------------------------------


## Basic client side socket connection
    public class ClientTest {
        public static void main(String args[]) throws Exception{
            SimpleSocket simpleSocket = new SimpleSocket("10.0.1.2", 8000);
            simpleSocket.writeString("Hello world");
        }
    }
