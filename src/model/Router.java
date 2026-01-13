package model;

public class Router extends Equipment {
	private Boolean supportWifi;
	private Integer Mbps;

	public Router(String type, String model, String ip, String manufacturer, String state, Double energyConsumption,
			Integer qtdHourConsumption, Boolean suportWifi, Integer mbps) {
		super(type, model, ip, manufacturer, state, energyConsumption, qtdHourConsumption);
		this.supportWifi = suportWifi;
		Mbps = mbps;
	}

	public Boolean getSuportWifi() {
		return supportWifi;
	}

	public void setSuportWifi(Boolean suportWifi) {
		this.supportWifi = suportWifi;
	}

	public Integer getMbps() {
		return Mbps;
	}

	public void setMbps(Integer mbps) {
		Mbps = mbps;
	}
	
	@Override
	public void powerOn() {
		System.out.println("Router turning on... Connecting to networks.");
	}
	
	@Override
	public  void powerOff() {
		System.out.println("Router turning off... Disconnecting to networks.");
	}
	
	@Override
	public  void restart() {
		System.out.println("Router restarting ...Please wait.");
		
	}
	
	@Override
	public Double calculateConsumption(Integer qtdHourConsumption) {
		
		return (super.getEnergyConsumption() * super.getQtdHourConsumption()) / 1000;
	}
    
	@Override
	public String getDetails() {
		    return String.format("WiFi: %s | Mbps: %d", getSuportWifi(), getMbps());
	}
		
	@Override
	public String toString() {
		return super.toString() 
				+"\n Support Wifi: " + supportWifi
				+ "\n Mbps: " + Mbps;
	}
	
	
}
