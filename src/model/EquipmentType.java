package model;

public enum EquipmentType {
	
    ROUTER,
	    SWITCH,
	    SERVER,
	    FIREWALL;

	    public static EquipmentType fromString(String value)
	    {
	        return EquipmentType.valueOf(value.trim().toUpperCase());
	    }


}
