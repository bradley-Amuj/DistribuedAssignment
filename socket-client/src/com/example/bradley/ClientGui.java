package com.example.bradley;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientGui implements SocketConnector, ActionListener {
    //initialize the client socket, input and output stream objects
    protected Socket socket;
    protected ObjectInputStream inputStream;
    protected ObjectOutputStream outputStream;

    private JPanel switchPanels; // a panel that uses CardLayout
    private JFrame window;

    private JPanel messagePanel;
    private JPanel main;
    private JTextField txtHostName;
    private JTextField txtPortNumber;
    private JButton btnConnectServer;
    private JLabel lblHostName[];


    private JTextArea chatMessages;
    private JTextField chatInput;
    private JScrollPane scrollBar;

    public ClientGui() {

    }


    //build the client swing GUI for entering the host and port number
    public void buildGui() {

        window = new JFrame("Socket Client");
        lblHostName = new JLabel[6];
        switchPanels = new JPanel(new CardLayout());

        messagePanel = new JPanel();
        messagePanel.setLayout(null); // Setting to layout to null, so it becomes absolute position
        messagePanel.setSize(300, 400);

        //request hostname from the client
        txtHostName = new JTextField();
        txtHostName.setBounds(60, 120, 400, 25);
        txtHostName.setText("localhost");
        txtHostName.addActionListener(this);
        lblHostName[1] = new JLabel("Please enter your host?");
        lblHostName[1].setBounds(60, 90, 400, 25);
        messagePanel.add(lblHostName[1]);
        messagePanel.add(txtHostName);

        //request port number (default is 4065) from the client

        txtPortNumber = new JTextField();
        txtPortNumber.setBounds(60, 180, 400, 25);
        txtPortNumber.addActionListener(this);
        txtPortNumber.setText("4065");
        lblHostName[2] = new JLabel("Please enter your port number?");
        lblHostName[2].setBounds(60, 150, 300, 25);
        messagePanel.add(lblHostName[2]);
        messagePanel.add(txtPortNumber);

        btnConnectServer = new JButton("Connect");
        btnConnectServer.setBounds(200, 250, 100, 35);

        btnConnectServer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    //get port number and hostname from the text inputs
                    int port_number = Integer.parseInt(txtPortNumber.getText());
                    socket = new Socket(txtHostName.getText(), port_number);
                    // send port number and host name to the client socket
                    outputStream = new ObjectOutputStream(socket.getOutputStream());
                    // read message inputs from the server socket
                    inputStream = new ObjectInputStream(socket.getInputStream());

                    new ServerReader().start();


                    CardLayout changePages = (CardLayout) (switchPanels.getLayout());
                    changePages.show(switchPanels, "main");
                    window.setSize(500, 350);

                    outputStream.writeObject("Client connected from " + socket.getRemoteSocketAddress());


                } catch (IOException u) {
                    u.printStackTrace();
                }
            }
        });
        messagePanel.add(btnConnectServer);

        switchPanels.add(messagePanel, "cover");

        main = new JPanel();
        main.setLayout(null); // Setting to layout to null, so it becomes absolute position
        main.setSize(300, 400);

        //start the messaging interface to request for student's details
        startMainInterface();
        window.setSize(400, 350);

        switchPanels.add(main, "main");
        window.add(switchPanels);

        window.setResizable(false); // size remain the same, not changeable
        window.setSize(500, 350); // size for the client
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true); // display frame
        centreWindow(window);

        //add close handler
        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                closeConnections(); //close socket connection
                System.exit(0);
            }
        });
    }

    //build the messaging interface where the student details are given
    public void startMainInterface() {
        lblHostName[3] = new JLabel("Display Messages:");
        lblHostName[3].setBounds(20, 0, 300, 40);
        main.add(lblHostName[3]);

        chatMessages = new JTextArea();
        chatMessages.setEditable(false);
        scrollBar = new JScrollPane(chatMessages);
        scrollBar.setBounds(20, 30, 450, 150);
        main.add(scrollBar);

        lblHostName[5] = new JLabel("Enter message here");
        lblHostName[5].setBounds(20, 250, 300, 25);
        main.add(lblHostName[5]);

        chatInput = new JTextField();
        chatInput.setBounds(20, 280, 450, 35);
        chatInput.addActionListener(this);
        main.add(chatInput);
    }

    //Ensure the server runs perpetually awaiting connections and requests
    class ServerReader extends Thread {
        public void run() {
            try {
                String p;
                String message = "";
                while ((p = (String) inputStream.readObject()) != null) {
                    message = message + p.toString() + "\n";
                    chatMessages.setText(message);
                }

            } catch (Exception ignored) {
            }

        }
    }

    //////
    //get the client's input and provide it as output
    @Override
    public void communicate() {
        try {
            String message = chatInput.getText();
            outputStream.writeObject(message);
            outputStream.flush();

        } catch (IOException i) {
            System.out.println("Error " + i);
        }

        chatInput.setText("");
    }


    //close the client connection
    @Override
    public void closeConnections() {
        try {
            inputStream.close();
            outputStream.close();
            this.socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!chatInput.getText().equals("")) {
            communicate();
        }
    }

    public void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    public static void main(String[] args) {
        new ClientGui();
    }

}
