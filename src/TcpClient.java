import java.io.*;
import java.net.Socket;

public class TcpClient {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 5000;

        Socket socket = null;
        DataInputStream dataInputStream = null;
        FileOutputStream fileOutputStream = null;

        try {
            socket = new Socket(hostname, port);
            System.out.println("Client connected to server");

            dataInputStream = new DataInputStream(socket.getInputStream());

            String fileName = dataInputStream.readUTF();

            //File destination directory
            String fileDirectory = "src/Client/";
            //Call method for unique file id
            File file = getUniqueFile(fileDirectory, fileName);

            // Initialize streams inside the try block
            fileOutputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int byteLength;

            // Reading from input stream and writing to output stream
            while ((byteLength = dataInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, byteLength);
            }

            System.out.println("File received successfully");

        } catch (FileNotFoundException e) {
            System.out.println("Error: The file was not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
            e.printStackTrace(); // for debugging purposes

        } finally {
            // Ensuring streams are closed in finally block to avoid resource leaks
            try {
                if (fileOutputStream != null) fileOutputStream.close();
            } catch (IOException e) {
                System.out.println("Error closing file output stream: " + e.getMessage());
            }

            try {
                if (dataInputStream != null) dataInputStream.close();
            } catch (IOException e) {
                System.out.println("Error closing data input stream: " + e.getMessage());
            }

            try {
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.out.println("Error closing socket: " + e.getMessage());
            }
        }

    }
    // Metode for at give et unikt fil navn, med nummer, hvis fillen allrede eksitere
    private static File getUniqueFile(String directory, String fileName) {
        File file = new File(directory + fileName);
        String baseName = fileName;
        String extension = "";

        //kontrolere om den har en extension
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != 1) {
            baseName = fileName.substring(0, dotIndex); //Uden extension
            extension = fileName.substring(dotIndex);   //Med extension
        }


        // et loop til at finde et gyldigt fil navn
        int count = 1;
        while (file.exists()) {
            String newFileName = baseName + "(" + count + ")" + extension;
            file = new File(directory + newFileName);
            count++;
        }
        return file;
    }


}