package com.example.bradley;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.ServerSocket;

public class ServerGui implements ActionListener {

    private ServerSocket serverSocket;
    private Thread serverThread;
    private int portNumber = 0;

    ///Swing UI
    private JFrame jFrame;
    private JPanel jPanel;
    private JLabel lbl_PortNumber;
    private JTextField tx_tPortNumber;
    private JButton btnStartServer;

    public ServerGui() {
        serverGUI();
    }

    public static void main(String[] args) {
         new ServerGui();

    }






    public  void serverGUI() {
        jFrame = new JFrame("Server configuration");
        jPanel = new JPanel();
        jPanel.setBackground(Color.PINK);
        jPanel.setLayout(null);

        lbl_PortNumber = new JLabel("Please enter port number");
        lbl_PortNumber.setBounds(60, 30, 400, 50);

        tx_tPortNumber = new JTextField();
        tx_tPortNumber.setBounds(60, 60, 400, 50);
        tx_tPortNumber.setText("3000");

        //start server btn
        btnStartServer = new JButton("Start server");
        btnStartServer.setBounds(60, 120, 400, 50);
        btnStartServer.addActionListener(this);


        jPanel.add(lbl_PortNumber);
        jPanel.add(tx_tPortNumber);
        jPanel.add(btnStartServer);


        jFrame.add(jPanel);


        jFrame.setResizable(true);
        jFrame.setSize(600, 300);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        centreWindow(jFrame);
        jFrame.setVisible(true);

    }


    //get port number
    @Override
    public void actionPerformed(ActionEvent e) {
        if ( tx_tPortNumber.getText().length()<= 0) {
            JOptionPane.showMessageDialog(null, "Default port number 3000 will be used");
            tx_tPortNumber.setText("3000");
            return;
        }

        portNumber = Integer.parseInt(tx_tPortNumber.getText());
        startServer(portNumber);
    }


    public void centreWindow(Window frame) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
    }

    /**
     *
     * @param portNumber
     * Given a port number,it starts a thread
     */
    private void startServer(int portNumber) {
        try {
            serverThread = new Server(portNumber);
            serverThread.start();
        } catch (Exception i) {
            System.out.println("Could not connect to that port " + i);
        }
    }
}
