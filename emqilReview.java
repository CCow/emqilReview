import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
/**
*  Email Program.
*  Receives sender and recipient emails and names, subject line, and body,
*  stores them in separate variables.
*  Connects to a TCP Server.
*  Waits for a Welcome message from the server.
*  Sends the first sentence to the server.
*  Receives a response from the server and displays it.
*  Sends the second sentence to the server.
*  Receives a second response from the server and displays it.
*  Closes the socket and exits.
*  author: Sharon Chang
*  Email:  shchang@chapman.edu
*  Date:  2/23/2021
*  version: 3.1
*/

class Email {

  public static void main(String[] argv) throws Exception {
    // Get user input
    BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Type the sender's email address:\n");
    final String senderEmail = inFromUser.readLine();

    System.out.println("Type the recipient's email address:\n");
    final String receiverEmail = inFromUser.readLine();

    System.out.println("Type the sender's name:\n");
    final String senderName = inFromUser.readLine();

    System.out.println("Type the recipient's name:\n");
    final String receiverName = inFromUser.readLine();

    System.out.println("Type the subject of the email:\n");
    final String subject = inFromUser.readLine();

    System.out.println("Type the body of the email. Submit a single period when you are done.:\n");
    String bodyLine;
    String body = "";
    //While loop allows for multiple-line bodies
    while (true) {
      bodyLine = inFromUser.readLine();
      System.out.println(bodyLine);
      if (bodyLine.equals(".")) {
        body += ".";
        System.out.println("Message ended. Connecting to server...\n");
        break;
      }
      body += bodyLine + "\n";
    }
    // Finished getting user input

    // Connect to the server
    Socket clientSocket = null;

    try {
      clientSocket = new Socket("smtp.chapman.edu", 25);
    } catch (Exception e) {
      System.out.println("Failed to open socket connection");
      System.exit(0);
    }
    PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
    BufferedReader inFromServer =  new BufferedReader(
        new InputStreamReader(clientSocket.getInputStream()));

    // Exchange messages with the server
    // Recive and display the Welcome Message
    String welcomeMessage = inFromServer.readLine();
    System.out.println("FROM SERVER:\n" + welcomeMessage);

    //Sending the appropriate commands to send an email
    System.out.println("FROM CLIENT:\nHELO icd.chapman.edu");
    outToServer.println("HELO ice.chapman.edu");
    String serverResponse = inFromServer.readLine();
    System.out.println("FROM SERVER:\n" + serverResponse);

    System.out.println("FROM CLIENT:\nMAIL FROM: " + senderEmail);
    outToServer.println("MAIL FROM: " + senderEmail);
    serverResponse = inFromServer.readLine();
    System.out.println("FROM SERVER:\n" + serverResponse);

    System.out.println("FROM CLIENT:\nRCPT TO: " + receiverEmail);
    outToServer.println("RCPT TO: " + receiverEmail);
    serverResponse = inFromServer.readLine();
    System.out.println("FROM SERVER:\n" + serverResponse);

    System.out.println("FROM CLIENT:\nDATA");
    outToServer.println("DATA");
    serverResponse = inFromServer.readLine();
    System.out.println("FROM SERVER:\n" + serverResponse);

    System.out.println("FROM CLIENT:");
    System.out.println("From: " + senderName);
    System.out.println("To: " + receiverName);
    System.out.println("Subject: " + subject);
    System.out.println(body);

    outToServer.println("From: " + senderName);
    outToServer.println("To: " + receiverName);
    outToServer.println("Subject: " + subject);
    outToServer.println(body);
    serverResponse = inFromServer.readLine();
    System.out.println("FROM SERVER:\n" + serverResponse);

    System.out.println("FROM CLIENT:\nQUIT");
    outToServer.println("QUIT");
    serverResponse = inFromServer.readLine();
    System.out.println("FROM SERVER:\n" + serverResponse);

    // Close the socket connection
    clientSocket.close();
  }
}
