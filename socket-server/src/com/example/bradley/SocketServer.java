package com.example.bradley;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
//initialize the socket connection
public class SocketServer extends Thread {
    private ServerSocket serverSocket;

    public SocketServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(10000);
    }

    //initialize thread for the server to run awaiting client connection and requests
    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("Waiting for client connection on port " + serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();

                System.out.println("Just connected to " + server.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(server.getInputStream());

                System.out.println(in.readUTF());
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress() + "\nGoodbye!");
                server.close();
            } catch (SocketTimeoutException s) {
                System.out.println("socket connection timed out");
                break;
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
                break;
            }
        }
    }
}
