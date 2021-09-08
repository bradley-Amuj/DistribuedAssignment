package com.example.bradley;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class Server extends Thread {
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final ServerSocket serverSocket;
    public int portNumber = 0;
    String message;
    String serverResponse;

    public Server(int portNumber) throws IOException {
        serverSocket = new ServerSocket(portNumber);
        this.portNumber = serverSocket.getLocalPort();
        System.out.println("Awaiting client connection on port " + serverSocket.getLocalPort());
    }


    @Override
    public void run() {
        try {
            Socket clientSocket = serverSocket.accept();
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            inputStream = new ObjectInputStream(clientSocket.getInputStream());

            System.out.println("connected " + clientSocket.getRemoteSocketAddress());

            ServerProtocol myProtocol = new ServerProtocol();
            serverResponse = myProtocol.processInput(null);
            while ((message = (String) inputStream.readObject()) != null) {

                serverResponse = myProtocol.processInput(message);
                String respString = message + "\n" + serverResponse;
                //give server response if the connection is still alive
                if (message.contains("connected")) {
                    respString = serverResponse;
                }
                outputStream.writeObject(respString);

                //close the client connection if the response contains the word "bye"
                if (serverResponse.contains("exit ")) {
                    clientSocket.close();
                    break;
                }
                //output client message
                System.out.println("Message received from client :" + message);
            }
        } catch (EOFException s) {
            System.out.println("Socket closed: " + s.getMessage());
        } catch (SocketException s) {
            System.out.println("Socket error: " + s.getMessage());
        } catch (SocketTimeoutException s) {
            System.out.println("socket connection timed out: " + s.getMessage());
        } catch (IOException | ClassNotFoundException ioEx) {
            ioEx.printStackTrace();
            System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(ioEx.getMessage());
        }
    }


}
