import javax.imageio.IIOException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    public static void main(String[] args) {

        ServerSocket serverSocket = null;
        Socket socket = null;
        DataOutputStream dataOutputStream = null;
        FileInputStream fileInputStream = null;

        try {
            serverSocket = new ServerSocket(5000);
            System.out.println("Server is listening on port 5000");

            socket = serverSocket.accept();
            System.out.println("New client connected");

            File file = new File("src/Server/Danmark.txt");
            if (!file.exists()) {
                System.out.println("File does not exist");
                return;
            }

            // Initialize streams
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            fileInputStream = new FileInputStream(file);

            dataOutputStream.writeUTF(file.getName());

            byte[] buffer = new byte[1024];
            int bytesRead;

            // Read from the file and send data to the client
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, bytesRead);
            }

            System.out.println("File sent successfully");

        } catch (FileNotFoundException e) {
            System.out.println("Error: The file was not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace(); // For debugging purposes

        } finally {
            // Ensure resources are closed in the finally block to prevent resource leaks
            try {
                if (fileInputStream != null) fileInputStream.close();
            } catch (IOException e) {
                System.out.println("Error closing file input stream: " + e.getMessage());
            }

            try {
                if (dataOutputStream != null) dataOutputStream.close();
            } catch (IOException e) {
                System.out.println("Error closing data output stream: " + e.getMessage());
            }

            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }

            try {
                if (serverSocket != null) serverSocket.close();
            } catch (IOException e) {
                System.out.println("Error closing server socket: " + e.getMessage());
            }
        }
    }
}
