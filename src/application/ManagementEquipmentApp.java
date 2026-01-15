package application;

import java.util.List;
import java.util.Scanner;

import model.Equipment;
import service.EquipmentService;

public class ManagementEquipmentApp {

	private final Scanner sc = new Scanner(System.in);
	private final EquipmentService equipmentService = new EquipmentService();

	public void start() {
		int option;

		do {
			showMenu();
			option = sc.nextInt();
			sc.nextLine(); // limpa buffer

			switch (option) {
			case 1 -> createEquipment(sc);
			case 2 -> printListEquipments(equipmentService.getEquipments());
			case 0 -> System.out.println("Application finished.");
			default -> System.out.println("Invalid option.");
			}

		} while (option != 0);

		sc.close();
	}

	public void createEquipment(Scanner sc) {
	    System.out.println("\n=== Register Equipment ===");

	    // === Type ===
	    String type;
	    while (true) {
	        System.out.print("Type (Router/Switch/Server/Firewall): ");
	        type = sc.nextLine();
	        if (!type.trim().isEmpty()) break;
	        System.out.println("Type cannot be empty.");
	    }

	    // === Model ===
	    String model;
	    while (true) {
	        System.out.print("Model: ");
	        model = sc.nextLine();
	        if (!model.trim().isEmpty()) break;
	        System.out.println("Model cannot be empty.");
	    }

	    // === Validate IP ===
	    String ip;
	    while (true) {
	        System.out.print("IP: ");
	        ip = sc.nextLine();
	        if (!ip.trim().isEmpty()) break;
	        System.out.println("IP cannot be empty.");
	    }
	    
	    while (true) {
	        if (!equipmentService.validarIP(ip)) {
	            System.out.println("Invalid IP format! Try again (e.g. 192.168.0.10)");
	            System.out.print("IP: ");
	            ip = sc.nextLine();
	            continue;
	        }

	        if (equipmentService.isDuplicateIp(ip)) {
	            System.out.println("This IP already was registered! Inform another IP.");
	            System.out.print("IP: ");
	            ip = sc.nextLine();
	            continue;
	        }

	        break;
	    }

	    // === Manufacturer ===
	    String manufacturer;
	    while (true) {
	        System.out.print("Manufacturer: ");
	        manufacturer = sc.nextLine();
	        if (!manufacturer.trim().isEmpty()) break;
	        System.out.println("Manufacturer cannot be empty.");
	    }

	    // === State ===
	    String state;
	    while (true) {
	        System.out.print("State (on/off): ");
	        state = sc.nextLine();
	        if (!state.trim().isEmpty()) break;
	        System.out.println("State cannot be empty.");
	    }

	 // === Validate Energy Consumption ===
	    Double energyConsumption = null;
	    while (true) {
	        try {
	            System.out.print("Energy Consumption (W): ");
	            energyConsumption = sc.nextDouble();
	            sc.nextLine(); // clear buffer
	            if (equipmentService.isValidEnergy(energyConsumption))
	                break;
	            System.out.println("Energy consumption must be positive!");
	        } catch (NumberFormatException e) {
	            System.out.println("Invalid number! Try again.");
	            sc.nextLine(); // clear buffer after error
	        }
	    }

	    // === Validate Consumption/Day ===
	    int qtdHourConsumption = 0;
	    while (true) {
	        try {
	            System.out.print("Consumption/Day (hours 1-24): ");
	            qtdHourConsumption = sc.nextInt();
	            sc.nextLine(); // clear buffer
	            if (equipmentService.isValidConsumptionHours(qtdHourConsumption))
	                break;
	            System.out.println("Consumption/Day must be 1-24!");
	        } catch (NumberFormatException e) {
	            System.out.println("Invalid number! Try again.");
	            sc.nextLine(); // clear buffer after error
	        }
	    }

	    // === Specific fields by type ===
	    Boolean supportWifi = null;
	    Integer mbps = null;
	    Double portCapacityGB = null;
	    String opSystem = null;
	    Integer ramCapacity = null;
	    Integer diskCapacity = null;
	    Boolean statefullPacketInspection = null;
	    Boolean blockDoS = null;

	    //Depending on the type, we request the specific fields
	    if (type.equalsIgnoreCase("Router")) {
	        System.out.print("Supports WiFi (yes/no): ");
	        String wifi = sc.nextLine();
	        supportWifi = wifi.equalsIgnoreCase("yes");

	        System.out.print("Mbps: ");
	        mbps = Integer.parseInt(sc.nextLine());
	    } else if (type.equalsIgnoreCase("Switch")) {
	        System.out.print("Port Capacity (GB): ");
	        portCapacityGB = Double.parseDouble(sc.nextLine());
	    } else if (type.equalsIgnoreCase("Server")) {
	        System.out.print("Operating System: ");
	        opSystem = sc.nextLine();

	        System.out.print("RAM Capacity (GB): ");
	        ramCapacity = Integer.parseInt(sc.nextLine());

	        System.out.print("Disk Capacity (GB): ");
	        diskCapacity = Integer.parseInt(sc.nextLine());
	    } else if (type.equalsIgnoreCase("Firewall")) {
	        System.out.print("Statefull Packet Inspection (yes/no): ");
	        statefullPacketInspection = sc.nextLine().equalsIgnoreCase("yes");

	        System.out.print("Block DoS attacks (yes/no): ");
	        blockDoS = sc.nextLine().equalsIgnoreCase("yes");
	    }

	    // === Registrar equipamento ===
	    boolean success = equipmentService.registerEquipment(
	        type, model, ip, manufacturer, state,
	        energyConsumption, qtdHourConsumption, supportWifi, mbps,
	        portCapacityGB, opSystem, ramCapacity, diskCapacity,
	        statefullPacketInspection, blockDoS
	    );

	    if (success) {
	        System.out.println("Equipment registered successfully!");
	    } else {
	        System.out.println("Error registering equipment. Check input values.");
	    }
	}

	
	public static void printListEquipments(List<Equipment> equipments) {
		System.out.println();
		System.out.println(
				"================================================================EQUIPMENTS LIST========================================================================================================");
		System.out.println();
		if (equipments.isEmpty()) {
			System.out.println("No equipment registered yet.");
		} else {
			// Cabeçalho da tabela
			System.out.printf("%-10s %-25s %-18s %-15s %-10s %-12s %-22s %-50s%n",
		            "Type", "Model", "IP", "Manufacturer", "State", "Energy(W)", "Consumption/Day(KWh)", "Specific Info");
		    System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------------------------");

			// lines of table
			for (Equipment e : equipments) {
				// Exibe uma linha formatada
				System.out.printf("%-10s %-25s %-18s %-15s %-10s %-12.2f %-22d %-50s%n", e.getType(), e.getModel(),
						e.getIp(), e.getManufacturer(), e.getState(), e.getEnergyConsumption(),
						e.getQtdHourConsumption(), e.getDetails());
			}
		}

		System.out.println(
				"--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
	}
    

	private void showMenu() {

		System.out.println("\n==============================");
		System.out.println(" MANAGEMENT EQUIPMENT SYSTEM ");
		System.out.println("==============================");
		System.out.println("1 - Register equipment");
		System.out.println("2 - List equipments");
		System.out.println("0 - Exit");
		System.out.print("Choose an option: ");
	}

}
