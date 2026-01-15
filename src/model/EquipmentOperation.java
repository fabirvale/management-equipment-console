package model;

public enum EquipmentOperation {
	TURN_ON,
	TURN_OFF,
    RESTART;

    public static EquipmentOperation fromString(String value) {
    	 if (value == null) {
             throw new IllegalArgumentException("Operation cannot be null");
         }

         return switch (value.trim().toLowerCase()) {
             case "on", "turn on", "turn_on" -> TURN_ON;
             case "off", "turn off", "turn_off" -> TURN_OFF;
             case "restart" -> RESTART;
             default -> throw new IllegalArgumentException("Invalid operation");
         };
     
    }

}
