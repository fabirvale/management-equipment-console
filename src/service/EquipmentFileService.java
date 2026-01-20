package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import model.Equipment;
import model.Firewall;
import model.Router;
import model.Server;
import model.Switch;

public class EquipmentFileService {
	
	private EquipmentService equipmentService;
	
	private static final String FILE_PATH = "C:\\temp\\out\\equipments.csv";
	private static final String BACKUP_PATH = "C:\\temp\\out\\equipments_backup.csv";
	
	public EquipmentFileService(EquipmentService equipmentService) {
		this.equipmentService = equipmentService;
	}

	public void loadFromFile() {

		// making a backup of the original file before uploading
		Path origem = Paths.get(FILE_PATH);
		Path destino = Paths.get(BACKUP_PATH);
		
		try {
			if (Files.exists(origem)) {
				Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			System.out.println("Error copying file: " + e.getMessage());
		}

		try (BufferedReader br = new BufferedReader(new FileReader(origem.toString()))) {

			System.out.println("Loading the equipment list file...");
			String[] vetEquipment;
			int i = 0;

			String line = br.readLine();

			while (line != null) {
				i++;
				vetEquipment = line.split(";");
				equipmentService.createEquipmentFromLine(vetEquipment, i);
				line = br.readLine();

			}

			System.out.println(i + " lines were loaded.");
			System.out.println();

		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	
	//Save the file .csv
	public void saveToFile() {
		
		File file = new File(FILE_PATH);
	    file.getParentFile().mkdirs();
	    
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {

			for (Equipment e : equipmentService.getEquipments()) {
				String line = e.getType().name() + ";" + e.getModel() + ";" + e.getIp() + ";" + e.getManufacturer()
						+ ";" + e.getState() + ";" + e.getEnergyConsumption() + ";" + e.getQtdHourConsumption();

				// specific fields by type
				if (e instanceof Router) {
					Router r = (Router) e;
					line += ";" + r.getSuportWifi() + ";" + r.getMbps();
				} else if (e instanceof Switch) {
					Switch s = (Switch) e;
					line += ";" + s.getPortCapacityGB();
				} else if (e instanceof Server) {
					Server s = (Server) e;
					line += ";" + s.getOpSystem() + ";" + s.getRamCapacity() + ";" + s.getDiskCapacity();
				} else if (e instanceof Firewall) {
					Firewall f = (Firewall) e;
					line += ";" + f.isStatefullPacketInspection() + ";" + f.isBlockDoS();
				}

				bw.write(line);
				bw.newLine();
			}

			System.out.println("Equipments successfully saved to file.");

		} catch (IOException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
 
}
