package com.example.bradley;

//define the methods to be used in client communication with the server
public interface SocketConnector {
    void communicate();

    void closeConnections();
}
