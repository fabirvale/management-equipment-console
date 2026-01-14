package service.factory;

import model.Equipment;
import model.Firewall;
import model.Router;
import model.Server;
import model.Switch;

public class EquipmentFactory {
	
	public static Equipment create(String type, String model, String ip, String manufacturer, String state, Double energyConsumption,
                                   Integer qtdHourConsumption, Boolean supportWifi, Integer mbps, Double portCapacityGB, String opSystem, 
                                   Integer ramCapacity, Integer diskCapacity, Boolean statefullPacketInspection, Boolean blockDoS) {

        switch (type.toLowerCase()) {
            case "router":
                return new Router(type, model, ip, manufacturer, state,
                        energyConsumption, qtdHourConsumption, supportWifi, mbps);

            case "switch":
                return new Switch(type, model, ip, manufacturer, state,
                        energyConsumption, qtdHourConsumption, portCapacityGB);

            case "server":
                return new Server(type, model, ip, manufacturer, state,
                        energyConsumption, qtdHourConsumption, opSystem, ramCapacity, diskCapacity);

            case "firewall":
                return new Firewall(type, model, ip, manufacturer, state,
                        energyConsumption, qtdHourConsumption, statefullPacketInspection, blockDoS);

            default:
                throw new IllegalArgumentException("Unknown equipment type: " + type);
        }
    }
}
