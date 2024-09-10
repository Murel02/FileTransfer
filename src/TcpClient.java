import java.io.*;
import java.net.Socket;

public class TcpClient {
    public static void main(String[] args){
        String hostname = "localhost";
        int port = 5000;

        //setting up at host port there fill output get send out if error catch Exception
        try(Socket socket = new Socket(hostname, port)){
            System.out.println("Client connected to server");

            File file = new File("src/Client/DownloadedFile.txt");

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            // Bad idea \/
            // String fileName = dataInputStream.readUTF();
            // long fileSize = dataInputStream.readLong();

            byte[] buffer = new byte[1024];
            int byteLength;

            while ((byteLength = dataInputStream.read(buffer)) != -1){
                fileOutputStream.write(buffer, 0, byteLength);
            }

            fileOutputStream.close();
            dataInputStream.close();
            socket.close();

            System.out.println("File received successfully");

        }catch (IOException e){
            System.out.println("IO error:  " + e.getMessage());
        }
    }
}
