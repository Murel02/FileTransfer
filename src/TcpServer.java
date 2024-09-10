import javax.imageio.IIOException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    public static void main(String[] args){

        try(ServerSocket serverSocket = new ServerSocket(5000)){

            System.out.println("Server is listening on port 5000");

            Socket socket = serverSocket.accept();
            //socket.setSoTimeout(5000); //Timeout efter 5 sekunder.

            System.out.println("New client connected");

            File file = new File("src/Server/test.txt");
            if (!file.exists()){
                System.out.println("File does not exists");
                return;
            }

            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            FileInputStream fileInputStream = new FileInputStream(file);

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1){
                dataOutputStream.write(buffer, 0, bytesRead);
            }

            fileInputStream.close();
            dataOutputStream.close();
            socket.close();
            System.out.println("File sent successfully");


        } catch (IOException e){
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
