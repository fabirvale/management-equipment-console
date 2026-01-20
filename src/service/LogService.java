package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogService {

	private static final String LOG_FILE_PATH = "C:\\temp\\out\\log_equipments.txt";

	public void saveLog(String msg) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
			// Gets the current date and time from the system
			LocalDateTime dateTime = LocalDateTime.now();

			// Defining the formatter (12/11/2025 14:35:20)
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
			String dateTimeFormatter = dateTime.format(formatter);

			// Set up the logging line
			String line = "[" + dateTimeFormatter + "] " + msg;

			// Writes the line to the file and moves on to the next one
			bw.write(line);
			bw.newLine();
			bw.flush();

		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}

	}

	public boolean printLog() {

		// Converts the file path String to a Path object
		Path path = Paths.get(LOG_FILE_PATH);

		if (!Files.exists(path)) {
			return false;
		}

		System.out.println("=== ERROR LOG ===");

		try (BufferedReader br = new BufferedReader(new FileReader(path.toFile()))) {
			String line = br.readLine();
			while (line != null) {
				System.out.println(line);
				line = br.readLine();
			}
			return true;

		} catch (IOException e) {
			System.out.println("Error reading log file: " + e.getMessage());
			return false;
		}

	}

}
