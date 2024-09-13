import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileTransferServer {
    private int port;
    private String filePath;

    public FileTransferServer(int port, String filePath) {
        this.port = port;
        this.filePath = filePath;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server is listening on port " + port);
            while (true) { // For at understøtte flere klienter
                try (Socket socket = serverSocket.accept();
                     DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream())) {
                    System.out.println("New client connected");
                    sendFile(socket, dataOutputStream);
                    System.out.println("File sent successfully");
                } catch (IOException ex) {
                    handleError("Server error", ex);
                }
            }
        } catch (IOException ex) {
            handleError("Server error", ex);
        }
    }

    private void sendFile(Socket socket, DataOutputStream dataOutputStream) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }
        dataOutputStream.writeUTF(file.getName());
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] buffer = new byte[8192]; // Større buffer for bedre performance
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                dataOutputStream.write(buffer, 0, bytesRead);
            }
        }
    }
    private void handleError(String message, IOException ex) {
        System.err.println(message + ": " + ex.getMessage());
        ex.printStackTrace(); // Log, eller brug en logger til produktion
    }

    public static void main(String[] args) {
        FileTransferServer server = new FileTransferServer(5000, "src/Server/Danmark.txt");
        server.start();
    }
}
