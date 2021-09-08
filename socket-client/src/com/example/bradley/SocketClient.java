package com.example.bradley;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketClient {
    public static void main(String[] args) {
        String[] myArgs = {"127.0.0.1", "4065"};

        String hostName = myArgs[0];
        int portNumber = Integer.parseInt(myArgs[1]);

        try {
            //Initialize the client socket with a default port number of 4065 and the localhost address as the host
            Socket socket = new Socket(hostName, portNumber);
            //output from the client
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            //input from the server
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromClient;



            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                //break the socket connection if the client types bye
                if (fromServer.contains("Bye.")) {
                    break;
                }

                //show the input from the client
                fromClient = stdIn.readLine();
                if (fromClient != null) {
                    System.out.println("Client: " + fromClient);
                    out.println(fromClient);
                }
            }
        }
        //report error if the host is unknown or can't be reached
        catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        }
        //report error if the input or output is null
        catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }
}
