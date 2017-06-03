package http;
import java.io.*;
import java.net.*;

/**
 * @author Monica Padmanabhan Kuppuswamy
 * @author Prayas Rode
 */
public class HttpClient {
	
	public static void main(String[] args) {
		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);
		String requestString = null;
		String responseString = null;
		try {
			
			Socket clientSocket = new Socket(hostName, portNumber);
			PrintWriter requestToServer = new PrintWriter(clientSocket.getOutputStream());
			BufferedReader responseFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String requestCommand = args[2].toLowerCase();
			 
			// Request to the server
			switch(requestCommand) {
				case "get":
					requestString = args[2] + " /" + args[3] + " HTTP/1.1";
					System.out.println("Client: " + requestString);
					requestToServer.println(requestString);
					requestToServer.flush();
				break;
				
				case "put":
					File f = new File((System.getProperty("user.dir")).toString() + "http\\Sample File\\" + args[3]);
					if (f.exists() == false){
						System.out.println("Given file doesn't exist. Please try again with valid file name.");
						clientSocket.close();
						System.exit(1);
					}
				requestString = args[2] + " /" + args[3] + " HTTP/1.1";
					System.out.println("Client: " + requestString);
					requestToServer.println(requestString);
					BufferedReader br = new BufferedReader(new FileReader(f));
					String line;
					while((line = br.readLine()) != null) {
						requestToServer.println(line);
						requestToServer.flush();
					}
					requestToServer.println("End");
					requestToServer.flush();
				break;
				
				default:
					requestString = args[2] + " /" + args[3] + " HTTP/1.1";
					requestToServer.println(requestString);
					requestToServer.flush();
			}
			
			// Response from the server
			System.out.print("Server: ");
			while((responseString = responseFromServer.readLine()) != null) {
				System.out.println(responseString);
			}
			clientSocket.close();
			requestToServer.close();
			responseFromServer.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}