package org.example;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class Server {
    private Integer port;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private CaesarCipher caesarCipher;

    private Server() {}

    public static Server create(Integer port) throws IOException {
        Server server = new Server();
        server.port = port;
        server.serverSocket = new ServerSocket(port);
        server.caesarCipher = new CaesarCipher();

        return server;
    }

    private byte[] extendArray(byte[] oldArray) {
        int oldSize = oldArray.length;
        byte[] newArray = new byte[oldSize * 2];
        System.arraycopy(oldArray, 0, newArray, 0, oldSize);

        return newArray;
    }

    private byte[] readInput(InputStream stream) throws IOException {
        int b;
        byte[] buffer = new byte[10];
        int counter = 0;

        while ((b = stream.read()) > -1) {
            buffer[counter++] = (byte) b;

            if (counter >= buffer.length) {
                buffer = extendArray(buffer);
            }

            if (counter > 1 && Packet.compareEOP(buffer, counter - 1)) {
                break;
            }
        }
        byte[] data = new byte[counter];
        System.arraycopy(buffer, 0, data, 0, counter);

        return data;
    }

    public void start() {
        try {
            clientSocket = serverSocket.accept();
            outputStream = clientSocket.getOutputStream();
            inputStream = new BufferedInputStream(clientSocket.getInputStream());

            byte[] data = readInput(inputStream);

            Packet packet = Packet.parse(data);

            String value = packet.getValue(1);
            String offset = packet.getValue(2);

            System.out.println("Encoded phrase from client: " + value);
            System.out.println("Offset: " + offset);

            Packet response = Packet.create(1);

            response.setValue(1, caesarCipher.decipher(value, Integer.parseInt(offset)));

            outputStream.write(response.toByteArray());
            outputStream.flush();

        } catch (IOException e) {
            System.out.println("Something went wrong");
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = Server.create(9999);
        server.start();
    }
}
