package com.example.bradley;
//implement the knock-knock protocol
public class ServerProtocol {
    //define the states
    private static final int WAITING = 0;
    private static final int SENDADM = 1;
    private static final int SENDNAME = 2;
    private static final int SENDFACCRSDEG = 3;
    private static final int PERSONALCODE = 4;
    private static final int SENDALL = 5;
    private static final int SESSIONRENEWAL = 7;
    private int state = -1;

    //define server inputs for the various states
    String processInput(String theInput) {
        String theOutput = null;
        switch (state) {
            default:
                state = WAITING;
                break;
            case WAITING:
                theOutput = "Enter admission number";
                state = SENDADM;
                break;
            case SENDADM:
                theOutput = "Enter name";
                state = SENDNAME;
                break;
            case SENDNAME:
                theOutput = "What's your faculty /course / degree (faculty, course, degree)?";
                state = SENDFACCRSDEG;
                break;
//            case SENDFACCRSDEG:
//                theOutput = "Nice, what's your personal code? (firstname-admission number)";
//                state = PERSONALCODE;
//                break;
            case PERSONALCODE:
                theOutput = "Could you resend everything?  (admission number, name, faculty, course, degree)";
                state = SENDALL;
                break;
            case SENDALL:
                theOutput = "Nice that's all ^ ^. Do you want to restart? (Y/N)";
                state = SESSIONRENEWAL;
                break;
            case SESSIONRENEWAL:
                if (theInput.equalsIgnoreCase("y")) {
                    theOutput = "Welcome! What's your Admission Number";
                    state = SENDADM;
                } else {
                    theOutput = "Bye. Connection Terminating";
                    state = WAITING;
                }
                break;
        }

        System.out.println("Current state is :" + state + " with output " + theOutput);
        return theOutput;
    }
}
