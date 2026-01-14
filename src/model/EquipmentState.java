package model;

public enum EquipmentState {
	
	    ON,
	    OFF;

	    public static EquipmentState fromString(String value) {
	        return EquipmentState.valueOf(value.trim().toUpperCase());
	    }
}
