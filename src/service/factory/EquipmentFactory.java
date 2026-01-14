package service.factory;

import model.Equipment;
import model.EquipmentState;
import model.EquipmentType;
import model.Firewall;
import model.Router;
import model.Server;
import model.Switch;

public class EquipmentFactory {
	
	public static Equipment create(EquipmentType  type, String model, String ip, String manufacturer, EquipmentState state, Double energyConsumption,
                                   Integer qtdHourConsumption, Boolean supportWifi, Integer mbps, Double portCapacityGB, String opSystem, 
                                   Integer ramCapacity, Integer diskCapacity, Boolean statefullPacketInspection, Boolean blockDoS) {

        switch (type) {
            case ROUTER:
                return new Router(type, model, ip, manufacturer, state,
                        energyConsumption, qtdHourConsumption, supportWifi, mbps);

            case SWITCH:
                return new Switch(type, model, ip, manufacturer, state,
                        energyConsumption, qtdHourConsumption, portCapacityGB);

            case SERVER:
                return new Server(type, model, ip, manufacturer, state,
                        energyConsumption, qtdHourConsumption, opSystem, ramCapacity, diskCapacity);

            case FIREWALL:
                return new Firewall(type, model, ip, manufacturer, state,
                        energyConsumption, qtdHourConsumption, statefullPacketInspection, blockDoS);

            default:
                throw new IllegalArgumentException("Unknown equipment type: " + type);
        }
    }
}
