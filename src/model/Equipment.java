package model;

public abstract class Equipment {
	private String type;
	private String model;
	private String ip;
	private String manufacturer;
    private String state;
    private Double energyConsumption;
    private Integer qtdHourConsumption;
    
	public Equipment(String type, String model, String ip, String manufacturer, String state, Double energyConsumption, Integer qtdHourConsumption) {
		this.type = type;
		this.model = model;
		this.ip = ip;
		this.manufacturer = manufacturer;
		this.state = state;
		this.energyConsumption = energyConsumption;
		this.qtdHourConsumption = qtdHourConsumption;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Double getEnergyConsumption() {
		return energyConsumption;
	}
	public void setEnergyConsumption(Double energyConsumption) {
		this.energyConsumption = energyConsumption;
	}
	
	public Integer getQtdHourConsumption() {
		return qtdHourConsumption;
	}
	public void setQtdHourConsumption(Integer qtdHourConsumption) {
		this.qtdHourConsumption = qtdHourConsumption;
	}
	
	public abstract void powerOn();
	public abstract void powerOff();
	public abstract void restart();
	public abstract Double calculateConsumption(Integer qtdHourConsumption);
	public abstract String getDetails();
    
	@Override
	public String toString() {
		return " Model: " + model
				+ "\n IP: " + ip 
				+ "\n Manufacturer: " + manufacturer
				+ "\n State: " + state 
				+ "\n EnergyConsumption(Watts): " + energyConsumption
				+ "\n Consumption/Day (kWh): " + qtdHourConsumption;
				
	}
	
	
    
}
