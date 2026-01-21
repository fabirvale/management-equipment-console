package service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import model.Equipment;
import model.EquipmentOperation;
import model.EquipmentState;
import model.EquipmentType;
import model.Firewall;
import model.Router;
import model.Server;
import model.Switch;

public class EquipmentService {

	List<Equipment> equipments = new ArrayList<>();
	
	public void clear() {
        equipments.clear();
    }

	public EquipmentService() {

	}

	public List<Equipment> getEquipments() {
		return equipments;
	}

	public void setEquipments(List<Equipment> equipments) {
		this.equipments = equipments;
	}
    
	
	//Register Equipment and add in the list
	public Boolean registerEquipment(EquipmentType type, String model, String ip, String manufacturer, EquipmentState state,
	        Double energyConsumption, Integer qtdHourConsumption, Boolean supportWifi, Integer mbps,
	        Double portCapacityGB, String opSystem, Integer ramCapacity, Integer diskCapacity,
	        Boolean statefullPacketInspection, Boolean blockDoS) {
	    	    
	    // Safety validations (non-interactive)
	    if (!validarIP(ip) || isDuplicateIp(ip)) {
	        return false;
	    }

	    if (!isValidEnergy(energyConsumption) || !isValidConsumptionHours(qtdHourConsumption)) {
	        return false;
	    }
  
	    // specific fields for validations
	    if (type == EquipmentType.ROUTER && (supportWifi == null || !isValidInteger(mbps))) {
	         return false;
	    } else if (type == EquipmentType.SWITCH && !isValidDouble(portCapacityGB)) {
	         return false;
	    } else if (type == EquipmentType.SERVER
	            && (!isRequiredFieldValid(opSystem) || !isValidInteger(ramCapacity) || !isValidInteger(diskCapacity))) {
	         return false;
	    } else if (type == EquipmentType.FIREWALL && (statefullPacketInspection == null || blockDoS == null)) {
	         return false;
	    }

	    // Create the equipment
	    Equipment e = null;
	    switch (type) {
	        case ROUTER:
	            e = new Router(type, model, ip, manufacturer, state, energyConsumption, qtdHourConsumption,
	                    supportWifi, mbps);
	            break;
	        case SWITCH:
	            e = new Switch(type, model, ip, manufacturer, state, energyConsumption, qtdHourConsumption,
	                    portCapacityGB);
	            break;
	        case SERVER:
	            e = new Server(type, model, ip, manufacturer, state, energyConsumption, qtdHourConsumption,
	                    opSystem, ramCapacity, diskCapacity);
	            break;
	        case FIREWALL:
	            e = new Firewall(type, model, ip, manufacturer, state, energyConsumption, qtdHourConsumption,
	                    statefullPacketInspection, blockDoS);
	            break;
	    }

	    // Add in the list
	    equipments.add(e);
	    return true;
	}
	


	// validate value != empty
	public boolean isRequiredFieldValid(String value) {
		return value != null && !value.trim().isEmpty();
	}

	// validate the IP
	public boolean validarIP(String ip) {
		String regex = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
		return ip.matches(regex);
	}

	public boolean isDuplicateIp(String ip) {
		for (Equipment e : equipments) {
			if (e.getIp().equals(ip)) {
				return true; // there is IP in list
			}
		}
		return false; // there is no IP in list
	}
	
	//valid Energy
	public boolean isValidEnergy(Double energy) {
		return energy != null && energy > 0;
	}
    
	//valid ConsumptionHours
	public boolean isValidConsumptionHours(int hours) {
		return hours > 0 && hours <= 24;
	}
 
	//valid integer value
	public boolean isValidInteger(Integer value) {
		return value != null && value > 0;
	}

	//valid double value
	public boolean isValidDouble(Double value) {
		return value != null && value > 0;
	}

	//valid yes or no
	public boolean isValidBoolean(String input) {
		return input.equalsIgnoreCase("yes") || input.equalsIgnoreCase("no");
	}

	
	//Search IP
	public Equipment ipSearch(String ip) {
		for (Equipment e : equipments) {
			if (e.getIp().equals(ip)) {
				return e;
			}
		}
		return null;
	}
	
// turn on or turn off or restart the equipment
	public void executeOperation(EquipmentOperation operation, Equipment equipment) {
		 EquipmentState currentState = equipment.getState();

		    switch (operation) {

		        case TURN_ON -> {
		            if (currentState == EquipmentState.ON) {
		                System.out.println("The equipment is already ON.");
		                return;
		            }
		            equipment.setState(EquipmentState.ON);
		            equipment.powerOn();
		        }

		        case TURN_OFF -> {
		            if (currentState == EquipmentState.OFF) {
		                System.out.println("The equipment is already OFF.");
		                return;
		            }
		            equipment.setState(EquipmentState.OFF);
		            equipment.powerOff();
		        }

		        case RESTART -> {
		            if (currentState == EquipmentState.OFF) {
		                System.out.println("The equipment is OFF. Turn it ON before restarting.");
		                return;
		            }
		            equipment.setState(EquipmentState.OFF);
			        equipment.powerOff();
			        equipment.setState(EquipmentState.ON);
			        equipment.powerOn();
		        }
		    }
  }
	
	
	//Report Consumption energy per day
	public void showEnergyReport(Equipment equipment) {
			
		double dailyConsumption = (equipment.getEnergyConsumption() * equipment.getQtdHourConsumption()) / 1000;

	    System.out.println("\n================ ENERGY CONSUMPTION REPORT ================\n");

	    System.out.println("Equipment Information");
	    System.out.println("-----------------------------------------------------------");
	    System.out.println("Type           : " + equipment.getType());
	    System.out.println("Model          : " + equipment.getModel());
	    System.out.println("IP             : " + equipment.getIp());
	    System.out.println("Manufacturer   : " + equipment.getManufacturer());
	    System.out.println("State          : " + equipment.getState());

	    System.out.println("\nEnergy Configuration");
	    System.out.println("-----------------------------------------------------------");
	    System.out.println("Power (Watts)  : " + equipment.getEnergyConsumption() + " W");
	    System.out.println("Usage per Day  : " + equipment.getQtdHourConsumption() + " hours");

	    System.out.println("\nCalculated Consumption");
	    System.out.println("-----------------------------------------------------------");
	    System.out.printf("Daily Consumption : %.2f kWh%n", dailyConsumption);

	    System.out.println("\nSpecific Information");
	    System.out.println("-----------------------------------------------------------");

	    if (equipment instanceof Switch s) {
	        System.out.println("Port Capacity     : " + s.getPortCapacityGB() + " GB");
	    } else if (equipment instanceof Router r) {
	        System.out.println("WiFi Supported    : " + r.getSuportWifi());
	        System.out.println("Speed             : " + r.getMbps() + " Mbps");
	    } else if (equipment instanceof Server s) {
	        System.out.println("OS                : " + s.getOpSystem());
	        System.out.println("RAM               : " + s.getRamCapacity() + " GB");
	        System.out.println("Disk              : " + s.getDiskCapacity() + " GB");
	    } else if (equipment instanceof Firewall f) {
	        System.out.println("SPI Enabled       : " + f.isStatefullPacketInspection());
	        System.out.println("Block DoS         : " + f.isBlockDoS());
	    }

	    System.out.println("\n===========================================================\n");
	}



	//Report state
	public void showStateReport(Equipment equipment) {
		    System.out.println();
		    System.out.println("==============================");
		    System.out.println(" EQUIPMENT STATE REPORT");
		    System.out.println("==============================");

		    System.out.println(equipment.toString());
		    
		    String details = equipment.getDetails();
		    if (details != null && !details.isBlank()) {
		        System.out.println(" Details: " + details);
		    }

		    System.out.println("=======================================================================================");
		    System.out.println();
	}

	//Remove the equipment
	public boolean removeEquipmentByIP(Integer index) {
		if (index >= 0 && index < equipments.size()) {
			equipments.remove((int) index);
			return true;
		}
		return false;
	}
	
	
  //Report	generate summary
 public void generateSummary(EquipmentService eqService) {
		
		Map<EquipmentType, Long> eqCount = generateEqCount();
		
        System.out.println();
		System.out.println("=== Number of equipment by type ===");
				
		for (Map.Entry<EquipmentType, Long> entry : eqCount.entrySet()) {
		    String typeFormatter =
		        entry.getKey().name().substring(0, 1).toUpperCase()
		      + entry.getKey().name().substring(1).toLowerCase();

		    System.out.println(typeFormatter + ": " + entry.getValue());
		}
		
		Map<EquipmentType, Double> eqAverage = generateAverageConsumption();
		
		System.out.println();
		System.out.println("=== Average daily energy consumption by type ===");
		
		for (Map.Entry<EquipmentType, Double> average : eqAverage.entrySet()) {
		    String typeFormatter =
		        average.getKey().name().substring(0, 1).toUpperCase()
		      + average.getKey().name().substring(1).toLowerCase();

		    System.out.printf("%s: %.2f kWh%n", typeFormatter, average.getValue());
		 }   
		
        Map<EquipmentState, Long> eqState = generateEqState();
		
        System.out.println();
		System.out.println("=== Number of devices per state (OFF/ON) ===");
		for (Map.Entry<EquipmentState, Long> entry : eqState.entrySet()) {
			 String typeFormatter = 
					 entry.getKey().name().substring(0, 1).toUpperCase()
				      + entry.getKey().name().substring(1).toLowerCase();
			 System.out.println(typeFormatter + ": " + entry.getValue());
		}
		System.out.println();
		System.out.println("=== Top 3 equipamentos com maior consumo ===");

		List<Equipment> top3 = eqService.getTop3Consumo();
		for (int i = 0; i < top3.size(); i++) {
		    Equipment eq = top3.get(i);
		    System.out.printf("%d. %s (%s) - %.2f kWh%n",
		        i + 1,
		        eq.getModel(),   
		        eq.getType(),
		        eq.getEnergyConsumption()
		    );
		}
    }
	//create the equipment from file
	public void createEquipmentFromLine(String[] vetEquipment, Integer line) {
		Integer qtdHourConsumption, mbps, ramCapacity, diskCapacity;
		String model, ip, manufacturer, opSystem;
		Double energyConsumption, portCapacityGB;
		EquipmentType type;
		EquipmentState state;
		Boolean supportWifi, statefullPacketInspection, blockDoS;

		Equipment eq = null;
		LogService log = new LogService();

		// Validate length of vector
		if (vetEquipment.length < 7) {
			log.saveLog("Line " + line + " ignored: missing basic fields (expected at least 7, found "
					+ vetEquipment.length + ").");
			return;
		}

		// validate Type

		try {
			type = EquipmentType.fromString(vetEquipment[0]);
		} catch (IllegalArgumentException e) {
			log.saveLog("Line " + line + " ignored: invalid equipment type.");
			return;
		}

		// validate Model
		if (isRequiredFieldValid(vetEquipment[1])) {
			model = vetEquipment[1];
		} else {
			log.saveLog("Line " + line + " ignored : invalid Model.");
			return;
		}

		// validate IP
		if (validarIP(vetEquipment[2])) {
			ip = vetEquipment[2];

			if (isDuplicateIp(ip)) {
				log.saveLog("Line " + line + " ignored : This " + ip + " already was registered!.");
				return;
			}
		} else {
			log.saveLog("Line " + line + " ignored : Invalid IP format.");
			return;
		}

		// validate manufacturer
		if (isRequiredFieldValid(vetEquipment[3])) {
			manufacturer = vetEquipment[3];
		} else {
			log.saveLog("Line " + line + " ignored : invalid Manufacturer.");
			return;
		}

		// validate State
		try {
			state = EquipmentState.fromString(vetEquipment[4]);
		} catch (IllegalArgumentException e) {
			log.saveLog("Line " + line + " ignored: invalid equipment state.");
			return;
		}

		// validate energyConsumption
		try {
			double energyValue = Double.parseDouble(vetEquipment[5]);

			if (isValidEnergy(energyValue)) {
				energyConsumption = energyValue;
			} else {
				log.saveLog("Line " + line + " ignored : invalid energy consumption.");
				return;
			}
		} catch (NumberFormatException e) {
			log.saveLog("Line " + line + " ignored : invalid energy value.");
			return;
		}

		// validate qtdHourConsumption
		try {
			int qtd = Integer.parseInt(vetEquipment[6]);
			if (isValidConsumptionHours(qtd)) {
				qtdHourConsumption = qtd;
			} else {
				log.saveLog("Line " + line + " ignored : Invalid the number of hours of use.");
				return;
			}
		} catch (NumberFormatException e) {
			log.saveLog("Line " + line + " ignored : invalid number of hours of use value.");
			return;
		}

		if (type == EquipmentType.ROUTER) {
			if (vetEquipment.length < 9) {
				log.saveLog("Line " + line + " ignored: missing fields for Router.");
				return;
			}

			// === Validate supportWifi ===
			String answer = vetEquipment[7].trim().toLowerCase();
			if (!answer.equals("true") && !answer.equals("false")) {
				log.saveLog("Line " + line + " ignored: Invalid boolean value");
				return;
			}
			supportWifi = Boolean.parseBoolean(answer);

			// === Validate Mbps ===
			try {
				mbps = Integer.parseInt(vetEquipment[8]);
				if (!isValidInteger(mbps)) {
					log.saveLog("Line " + line + " ignored: Mbps velocity must be positive.");
					return;
				}
			} catch (NumberFormatException e) {
				log.saveLog("Line " + line + " ignored: invalid Mbps velocity value.");
				return;
			}

			// === Create and add Router ===
			eq = new Router(type, model, ip, manufacturer, state, energyConsumption, qtdHourConsumption, supportWifi,
					mbps);
			equipments.add(eq);
		}

		else if (type == EquipmentType.SWITCH) {
			if (vetEquipment.length < 8) {
				log.saveLog("Line " + line + " ignored: missing fields for Switch.");
				return;
			}
			// validate portCapacityGB
			portCapacityGB = Double.parseDouble(vetEquipment[7]);
			try {
				if (!isValidDouble(portCapacityGB)) {
					log.saveLog("Line " + line + " ignored: portCapacityGB must be positive.");
					return;
				}
			} catch (NumberFormatException e) {
				log.saveLog("Line " + line + " ignored: portCapacityGB value.");
				return;
			}

			eq = new Switch(type, model, ip, manufacturer, state, energyConsumption, qtdHourConsumption,
					portCapacityGB);
			equipments.add(eq);
		}

		else if (type == EquipmentType.SERVER) {

			if (vetEquipment.length < 10) {
				log.saveLog("Line " + line + " ignored: missing fields for Server.");
				return;
			}

			// validate opSystem
			if (isRequiredFieldValid(vetEquipment[7])) {
				opSystem = vetEquipment[7];
			} else {
				log.saveLog("Line " + line + " ignored : Invalid Operating System.");
				return;
			}

			// validate ramCapacity
			ramCapacity = Integer.parseInt(vetEquipment[8]);
			try {
				if (!isValidInteger(ramCapacity)) {
					log.saveLog("Line " + line + " ignored: ramCapacity must be positive.");
					return;
				}
			} catch (NumberFormatException e) {
				log.saveLog("Line " + line + " ignored: ramCapacity value.");
				return;
			}
			// validate diskCapacity
			diskCapacity = Integer.parseInt(vetEquipment[9]);
			try {
				if (!isValidInteger(diskCapacity)) {
					System.out.println("Line " + line + " ignored: diskCapacity must be positive.");
					return;
				}
			} catch (NumberFormatException e) {
				log.saveLog("Line " + line + " ignored: diskCapacity value.");
				return;
			}
			eq = new Server(type, model, ip, manufacturer, state, energyConsumption, qtdHourConsumption, opSystem,
					ramCapacity, diskCapacity);
			equipments.add(eq);
		}

		else {
			if (vetEquipment.length < 8) {
				log.saveLog("Line " + line + " ignored: missing fields for Firewall.");
				return;
			}
			// validate statefullPacketInspection
			String answer = vetEquipment[7].trim().toLowerCase();
			if (!answer.equals("true") && !answer.equals("false")) {
				log.saveLog("Line " + line + " ignored: Invalid boolean value.");
				return;
			}
			statefullPacketInspection = Boolean.parseBoolean(answer);

			// validate blockDoS
			answer = vetEquipment[8].trim().toLowerCase();
			if (!answer.equals("true") && !answer.equals("false")) {
				log.saveLog("Line " + line + " ignored: Invalid boolean value.");
				return;
			}
			blockDoS = Boolean.parseBoolean(answer);

			eq = new Firewall(type, model, ip, manufacturer, state, energyConsumption, qtdHourConsumption,
					statefullPacketInspection, blockDoS);
			equipments.add(eq);
		}
	}
 //Filter by equipment type
	public Map<EquipmentType, Long> generateEqCount() {
		return equipments.stream().collect(Collectors.groupingBy(Equipment::getType, Collectors.counting()));
	}

	//calculate average energy consumption by type
	public Map<EquipmentType, Double> generateAverageConsumption() {
		return equipments.stream().collect(Collectors.groupingBy(Equipment::getType, // grouping by type
				Collectors.averagingDouble(Equipment::getEnergyConsumption) // calculate average
		));
	}


	//counting the equipment by state
	public Map<EquipmentState, Long> generateEqState() {
		return equipments.stream().collect(Collectors.groupingBy(Equipment::getState, // grouping by state
				Collectors.counting())); // counting the equipment by state
	}

	//Top 3 equipment that consume the most energy
	public List<Equipment> getTop3Consumo() {
		return equipments.stream().sorted(Comparator.comparingDouble(Equipment::getEnergyConsumption).reversed())
				.limit(3).collect(Collectors.toList());
	}

}
