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

		  String type;
		   while (true) {
		        System.out.print("Type (Router/Switch/Server/Firewall): ");
		        type = sc.nextLine();
		        if (!type.trim().isEmpty()) {
		            break;
		        }
		        System.out.println("Type cannot be empty.");
		    }

		    String model;
		    while (true) {
		        System.out.print("Model: ");
		        model = sc.nextLine();
		        if (!model.trim().isEmpty()) {
		            break;
		        }
		        System.out.println("Model cannot be empty.");
		    }

		    String ip;
		    while (true) {
		        System.out.print("IP: ");
		        ip = sc.nextLine();
		        if (!ip.trim().isEmpty()) {
		            break;
		        }
		        System.out.println("IP cannot be empty.");
		    }

		    String manufacturer;
		    while (true) {
		        System.out.print("Manufacturer: ");
		        manufacturer = sc.nextLine();
		        if (!manufacturer.trim().isEmpty()) {
		            break;
		        }
		        System.out.println("Manufacturer cannot be empty.");
		    }

		    String state;
		    while (true) {
		        System.out.print("State (on/off): ");
		        state = sc.nextLine();
		        if (state.equalsIgnoreCase("on") || state.equalsIgnoreCase("off")) {
		            break;
		        }
		        System.out.println("Invalid state. Use on or off.");
		    }

		    Double energy;
		    while (true) {
		        try {
		            System.out.print("Energy consumption (W): ");
		            energy = Double.parseDouble(sc.nextLine());
		            break;
		        } catch (NumberFormatException e) {
		            System.out.println("Invalid number.");
		        }
		    }

		    Integer hours;
		    while (true) {
		        try {
		            System.out.print("Hours per day: ");
		            hours = Integer.parseInt(sc.nextLine());
		            break;
		        } catch (NumberFormatException e) {
		            System.out.println("Invalid number.");
		        }
		    }

		boolean success = equipmentService.registerEquipment(type, model, ip, manufacturer, state, energy, hours, null,
				null, null, null, null, null, null, null);

		if (success) {
			System.out.println("Equipment registered successfully!");
		} else {
			System.out.println("Error registering equipment.");
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
