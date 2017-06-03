package http;
import java.io.*;
import java.net.*;

/**
 * @author Monica Padmanabhan Kuppuswamy
 * @author Prayas Rode
 */
public class HttpServer implements Runnable {
	
	Socket clientSocket;
	
	HttpServer(Socket client) {
		this.clientSocket = client;
	}
	
	public static void main(String[] args) throws IOException {
		int portNumber = Integer.parseInt(args[0]);
		ServerSocket serverSocket = new ServerSocket(portNumber);
		
		while(true) {
			Socket clientSoc = serverSocket.accept();
			new Thread(new HttpServer(clientSoc)).start();
		}	
	}
	
	public void run() {
		try {
			BufferedReader requestFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			PrintWriter responseToClient = new PrintWriter(clientSocket.getOutputStream());
			String requestString;
			
			MessageCreation m = new MessageCreation();
			if ((requestString = requestFromClient.readLine()) != null) {
				m.processRequest(requestString, clientSocket, requestFromClient, responseToClient);
			}
			requestFromClient.close();
			responseToClient.close();
			clientSocket.close();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}