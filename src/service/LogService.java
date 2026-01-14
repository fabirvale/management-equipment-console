package service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogService {

	public void saveLog(String msg) {
		 try (BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\temp\\out\\log_equipments.txt", true))) {
			    // Gets the current date and time from the system
		        LocalDateTime dateTime = LocalDateTime.now();
		        
		        // Defining the formatter (12/11/2025 14:35:20)
		        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		        String dateTimeFormatter = dateTime.format(formatter);
		        
		     // Set up the logging line
		        String line = "[" + dateTimeFormatter + "] " + msg;
		        
		     //Writes the line to the file and moves on to the next one
		        bw.write(line);
		        bw.newLine();
		        bw.flush();
		       			        
		 } catch (IOException e) {
		        System.out.println("Error: " + e.getMessage());
		 }		 
			 
	}
	
}
