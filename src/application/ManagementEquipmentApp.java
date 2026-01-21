package application;

import java.util.List;
import java.util.Scanner;

import model.Equipment;
import model.EquipmentOperation;
import model.EquipmentState;
import model.EquipmentType;
import service.EquipmentFileService;
import service.EquipmentService;
import service.LogService;

public class ManagementEquipmentApp {

	private final Scanner sc = new Scanner(System.in);
	private final EquipmentService equipmentService = new EquipmentService();
	private LogService logService = new LogService();
	
	EquipmentFileService fileService = new EquipmentFileService(equipmentService);
				
	public ManagementEquipmentApp() {
		 equipmentService.clear();
		 fileService.loadFromFile(); // loading equipments.csv
	}

	public void start() {
		int option;
		EquipmentOperation op = null;

		do {
			showMenu();
			option = sc.nextInt();
			sc.nextLine(); // limpa buffer

			switch (option) {
			case 1 -> createEquipment(sc);
			case 2 -> printListEquipments(equipmentService.getEquipments());
			case 3 ->
			{
			    // === Operation ===
			    while (true) {
			        System.out.print("Operation (turn on/turn off/restart): ");
			        String operation = sc.nextLine();

			        try {
			            op = EquipmentOperation.fromString(operation);
			            break; //valid operation
			        } catch (IllegalArgumentException e) {
			            System.out.println("Invalid operation. Use: turn on, turn off or restart.");
			        }
			    }
                
			    Equipment equipment = validateAndGetEquipment(sc, equipmentService);
			
			    // === Execute ===
			    equipmentService.executeOperation(op, equipment);
			}
			case 4 ->
			{
			  // === Execute ===       
			  Equipment equipment = validateAndGetEquipment(sc, equipmentService);	   
			  if (equipment == null) {
			        break; // break exits menu loop and returns to main menu
			    }
			  equipmentService.showEnergyReport(equipment);

			}
			case 5 ->
			{
			  // === Execute ===       
			  Equipment equipment = validateAndGetEquipment(sc, equipmentService);	   
			  if (equipment == null) {
			        break; // break exits menu loop and returns to main menu
			    }
			  equipmentService.showStateReport(equipment);

			}
			case 6 ->
			{
			  // === Execute ===       
			  Equipment equipment = validateAndGetEquipment(sc, equipmentService);	   
			  if (equipment == null) {
			        break; // break exits menu loop and returns to main menu
			    }
			  showSearchResult(equipment);

			}
			case 7 ->
			{
			  // === Execute ===       
			  Equipment equipment = validateAndGetEquipment(sc, equipmentService);	   
			  if (equipment == null) {
			        break; // break exits menu loop and returns to main menu
			    }
			  int index = equipmentService.getEquipments().indexOf(equipment);

			  if (equipmentService.removeEquipmentByIP(index)) {
					System.out.println("Equipment with IP " + equipment.getIp() + " removed successfully.");
					break;
				} 
			  else {
					System.out.println("Error removing equipment with IP " + equipment.getIp());
					break;
				}

			}
			case 8 -> equipmentService.generateSummary(equipmentService);
			case 9 ->
			{
				if (!logService.printLog()) {
			        System.out.println("No log file found.");
			    }
			}
			case 0 ->
			{
				System.out.println("Saving data before exit...");
				fileService.saveToFile();
				System.out.println("Application finished.");
			}
			default -> System.out.println("Invalid option.");
		  }

		} while (option != 0);

		sc.close();
	}

	public void createEquipment(Scanner sc) {
	    System.out.println("\n=== Register Equipment ===");

	    // === Type ===
	    EquipmentType type;
	    while (true) {
	        System.out.print("Type (Router/Switch/Server/Firewall): ");
	        String input = sc.nextLine().trim();

	        if (input.isEmpty()) {
	            System.out.println("Type cannot be empty! Try again.");
	            continue;
	        }

	        try {
	            type = EquipmentType.fromString(input);
	            break; // valid, exit the loop
	        } catch (IllegalArgumentException e) {
	            System.out.println("Invalid type! Please enter Router, Switch, Server, or Firewall.");
	        }
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
	    EquipmentState state;
	    while (true) {
	        System.out.print("State (on/off): ");
	        String input = sc.nextLine().trim();
	        if (input.isEmpty()) {
	            System.out.println("State cannot be empty.");
	            continue;
	        }

	        try {
	            state = EquipmentState.fromString(input);
	            break;
	        } catch (IllegalArgumentException e) {
	            System.out.println("Invalid state! Please enter 'on' or 'off'.");
	        }
	    }

	 // === Validate Energy Consumption ===
	    Double energyConsumption = null;
	    while (true) {
	        System.out.print("Energy Consumption (W): ");
	        String input = sc.nextLine().trim(); 

	        if (input.isEmpty()) {
	            System.out.println("Energy Consumption cannot be empty! Try again.");
	            continue;
	        }

	        try {
	            energyConsumption = Double.parseDouble(input);

	            if (energyConsumption <= 0) {
	                System.out.println("Energy consumption must be positive!");
	                continue;
	            }

	            break; // valid
	        } catch (NumberFormatException e) {
	            System.out.println("Invalid number! Enter a numeric value (e.g. 450).");
	        }
	    }



	    // === Validate Consumption/Day ===
	    int qtdHourConsumption = 0;
	    while (true) {
	            System.out.print("Consumption/Day (hours 1-24): ");
	            String input = sc.nextLine().trim(); 
	            
	            if (input.isEmpty()) {
		            System.out.println("Consumption/Day cannot be empty! Try again.");
		            continue;
		        }

		        try {
		        	qtdHourConsumption = Integer.parseInt(input);
		        	if (qtdHourConsumption < 1 || qtdHourConsumption > 24) {
		                System.out.println("Invalid value! Enter a number between 1 and 24.");
		                continue;
		            }
		            break; // valid value
		        } catch (NumberFormatException e) {
		        	 System.out.println("Invalid number! Try again.");
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
	    if (type == EquipmentType.ROUTER) {
	        
	        String wifiInput;
	        while (true) {
	            System.out.print("Supports WiFi (yes/no): ");
	            wifiInput = sc.nextLine().trim().toLowerCase();
	            if (wifiInput.equals("yes") || wifiInput.equals("no")) {
	                break;
	            }
	            System.out.println("Invalid input! Please enter 'yes' or 'no'.");
	        }
	        supportWifi = wifiInput.equals("yes"); // true if 'yes', false if 'no'
	
	       while (true) {
	            System.out.print("Mbps: ");
	            String input = sc.nextLine().trim();

	            if (input.isEmpty()) {
	                System.out.println("Mbps cannot be empty! Try again.");
	                continue;
	            }

	            try {
	                mbps = Integer.parseInt(input);
	                if (mbps <= 0) {
	                    System.out.println("Mbps must be positive!");
	                    continue;
	                }
	                break; // valid value
	            } catch (NumberFormatException e) {
	                System.out.println("Invalid number! Enter a positive integer.");
	            }
	        }

	        
	    } else if (type == EquipmentType.SWITCH) {
	       	        
	        while (true) {
	        	System.out.print("Port Capacity (GB): ");
	            String input = sc.nextLine().trim();

	            if (input.isEmpty()) {
	                System.out.println("Port Capacity cannot be empty! Try again.");
	                continue;
	            }

	            try {
	            	portCapacityGB = Double.parseDouble(input);
	                if (portCapacityGB <= 0) {
	                    System.out.println("Port Capacity must be positive!");
	                    continue;
	                }
	                break; // valid value
	            } catch (NumberFormatException e) {
	                System.out.println("Invalid number! Try again.");
	            }
	        }
        
	        
	    } else if (type == EquipmentType.SERVER) {
	       
		    while (true) {
		    	 System.out.print("Operating System: ");
			     opSystem = sc.nextLine();
			       	       
		         if (!opSystem.trim().isEmpty()) break;
		         System.out.println("Operation System cannot be empty.");
		    }

	        while (true) {
	            System.out.print("RAM Capacity (GB): ");
	            String input = sc.nextLine().trim();

	            if (input.isEmpty()) {
	                System.out.println("RAM Capacity cannot be empty! Try again.");
	                continue;
	            }

	            try {
	                ramCapacity = Integer.parseInt(input);
	                if (ramCapacity <= 0) {
	                    System.out.println("RAM Capacity must be positive!");
	                    continue;
	                }
	                break; // valor válido
	            } catch (NumberFormatException e) {
	                System.out.println("Invalid number! Enter a positive integer.");
	            }
	        }


	        while (true) {
	            System.out.print("Disk Capacity (GB): ");
	            String input = sc.nextLine().trim();

	            if (input.isEmpty()) {
	                System.out.println("Disk Capacity cannot be empty! Try again.");
	                continue;
	            }

	            try {
	                diskCapacity = Integer.parseInt(input);
	                if (diskCapacity <= 0) {
	                    System.out.println("Disk Capacity must be positive!");
	                    continue;
	                }
	                break; // valor válido
	            } catch (NumberFormatException e) {
	                System.out.println("Invalid number! Enter a positive integer.");
	            }
	        }

	    } else if (type == EquipmentType.FIREWALL) {
	      
	        String sfpiInput;
	        while (true) {
	        	System.out.print("Statefull Packet Inspection (yes/no): ");
	            sfpiInput = sc.nextLine().trim().toLowerCase();
	            if (sfpiInput.equals("yes") || sfpiInput.equals("no")) {
	                break;
	            }
	            System.out.println("Invalid input! Please enter 'yes' or 'no'.");
	        }
	        statefullPacketInspection = sfpiInput.equals("yes"); // true if 'yes', false if 'no'
		        
	        String blockDoSInput;
	        while (true) {
	            System.out.print("Block DoS (yes/no): ");
	            blockDoSInput = sc.nextLine().trim().toLowerCase();
	            if (blockDoSInput.equals("yes") || blockDoSInput.equals("no")) {
	                break;
	            }
	            System.out.println("Invalid input! Please enter 'yes' or 'no'.");
	        }
	        blockDoS = blockDoSInput.equals("yes"); // true if 'yes', false if 'no'
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
	
	private Equipment validateAndGetEquipment(Scanner sc, EquipmentService service) {

	    if (service.getEquipments().isEmpty()) {
	        System.out.println("No equipment registered yet.");
	        return null;
	    }

	    while (true) {
	        System.out.print("Inform the IP: ");
	        String ip = sc.nextLine().trim();

	        if (!service.validarIP(ip)) {
	            System.out.println("Invalid IP format! Try again.");
	            continue;
	        }

	        Equipment equipment = service.ipSearch(ip);

	        if (equipment == null) {
	            System.out.println("No equipment found with this IP. Try again.");
	            continue;
	        }

	        return equipment;
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
	
	private void showSearchResult(Equipment equipment) {
	    System.out.println();
	    System.out.println("==============================");
	    System.out.println(" EQUIPMENT FOUND");
	    System.out.println("==============================");
	    System.out.println(equipment);
	    System.out.println(" Details: " + equipment.getDetails());
	    System.out.println("=======================================================================================");
	    System.out.println();
	}
    

	private void showMenu() {

		System.out.println("\n==============================");
		System.out.println(" MANAGEMENT EQUIPMENT SYSTEM ");
		System.out.println("==============================");
		System.out.println("1 - Register equipment");
		System.out.println("2 - List equipments");
		System.out.println("3 - Turn On / Turn Off / Restart Equipment");
		System.out.println("4 - Calculate Energy Consumption");
		System.out.println("5 - State Report");
		System.out.println("6 - Search Equipment by IP");
		System.out.println("7 - Remove Equipment by IP");
		System.out.println("8 - Generic Summary Report");
		System.out.println("9 - List error log ");
		System.out.println("0 - Exit");
		System.out.print("Choose an option: ");
	}

}
