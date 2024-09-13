import java.io.*;
import java.net.Socket;

public class FileTransferClient {

       private String hostname;
       private int port;
       private String directory;

    public FileTransferClient(String hostname, int port, String directory) {
        this.hostname = hostname;
        this.port = port;
        this.directory = directory;
    }

    public void start(){
        try(Socket socket = new Socket(hostname, port)) {

            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

            String fileName = dataInputStream.readUTF();
            File file = getUniqueFile(directory, fileName);

            receiveFile(dataInputStream, file);
            System.out.println("File received successfully");

        }catch(IOException ex){
            handleError("Client error", ex);
        }
    }

    private void receiveFile(DataInputStream dataInputStream, File file) throws IOException{
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)){
            byte[] buffer = new byte[8192]; //Større buffer for bedre performance
            int byteLength;

            while ((byteLength = dataInputStream.read(buffer)) != -1){
                fileOutputStream.write(buffer, 0, byteLength);
            }
        }
    }

    // Metode for at give et unikt fil navn, med nummer, hvis fillen allrede eksitere
    private File getUniqueFile(String directory, String fileName) {
        File file = new File(directory + fileName);
        String baseName = fileName;
        String extension = "";

        //kontrolere om den har en extension
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            baseName = fileName.substring(0, dotIndex); //Uden extension
            extension = fileName.substring(dotIndex);   //Med extension
        }


        // et loop til at finde et gyldigt fil navn
        int count = 1;
        while (file.exists()) {
           file = new File(directory + baseName + "(" + count + ")" + extension);
           count++;
        }
        return file;
    }

    private void handleError(String message, IOException ex){
        System.err.println(message + ":" + ex.getMessage());
        ex.printStackTrace();
    }

    public static void main(String[] args){
        FileTransferClient client = new FileTransferClient("localhost", 5000, "src/Client/");
        client.start();
    }
}