package model;

public class Switch extends Equipment {
	private Double portCapacityGB;

	public Switch(String type, String model, String ip, String manufacturer, String state, Double energyConsumption,
			Integer qtdHourConsumption, Double portCapacityGB) {
		super(type, model, ip, manufacturer, state, energyConsumption, qtdHourConsumption);
		this.portCapacityGB = portCapacityGB;
	}

	public Double getPortCapacityGB() {
		return portCapacityGB;
	}

	public void setPortCapacityGB(Double portCapacityGB) {
		this.portCapacityGB = portCapacityGB;
	}
	
	@Override
	public void powerOn() {
		System.out.println("Switch started. All ports active.");
	}
	
	@Override
	public  void powerOff() {
		System.out.println("Switch turned off... All ports down.");
	}
	
	@Override
	public  void restart() {
		System.out.println("Switch restarting... All ports activating.");
	}
	
	@Override
	public Double calculateConsumption(Integer qtdHourConsumption) {
		return (super.getEnergyConsumption() * super.getQtdHourConsumption()) / 1000;
	}
	
	@Override
	public String getDetails() {
	    return String.format("Capacity: %.2f GB", portCapacityGB);
	}
 
	@Override
	public String toString() {
		return super.toString() 
			   + "\n CapacityGB Port: " + portCapacityGB;
	}

}
