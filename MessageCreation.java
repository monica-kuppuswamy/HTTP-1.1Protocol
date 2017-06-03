package http;

import java.net.*;
import java.io.*;

/**
 * @author Monica Padmanabhan Kuppuswamy
 * @author Prayas Rode
 */
public class MessageCreation {
	
	public void processRequest(String requestString, Socket clientSocket, BufferedReader requestFromClient, 
			PrintWriter responseToClient) throws IOException {
		if (requestString != null) {
			String fileLine = null;
			String filePath = null;
			String[] request = requestString.split(" ");
			String fileName = request[1].substring(1, request[1].length());
			String command = request[0].toLowerCase();
			switch(command) {
			
			// Process GET request from Client
			case "get":
				filePath = (System.getProperty("user.dir")).toString() + "http\\Sample File\\" + fileName;
				File f = new File(filePath);
				if (f.exists()) {
			        responseToClient.println("200 OK");
			        responseToClient.flush();
					BufferedReader reader = new BufferedReader(new FileReader(f));
					while((fileLine = reader.readLine()) != null) {
						responseToClient.println(fileLine);
						responseToClient.flush();
					}
					reader.close();
				} else {
					responseToClient.println("404 Not Found\n");
					responseToClient.flush();
				}
			break;
			
			// Process PUT request from Client
			case "put":
				filePath = (System.getProperty("user.dir")).toString() + "http\\Received Files\\" + fileName;
				BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
				while((fileLine = requestFromClient.readLine()) != null) {
					if(fileLine.equals("End")) {
						break;
					} else {
						writer.write(fileLine.toString());
					    writer.newLine();
						writer.flush();
					}
				}
				File fp = new File(filePath);
				if (fp.exists()) {
					responseToClient.println("200 OK File Created");
					responseToClient.flush();
				} else {
					responseToClient.println("500 Internal Server Error");
					responseToClient.flush();
				}
				writer.close();
				break;
		
			default:
				responseToClient.println("Invalid request command. Try again");
				responseToClient.println("Usage: ServerName PortNumber GET/PUT FileName");
				responseToClient.flush();
			}
		} else {
			responseToClient.println("Request not valid.");
			responseToClient.flush();
		}
	}
}