package it.polimi.tiw.projects.beans;

public class Campaign {
	
	private String name;
	private String client;
	private String manager;
	private CampaignStatus status;
	
	public String getName() {
		return name;
	}
	
	public String getClient() {
		return client;
	}
	
	public String getManager() {
		return manager;
	}
	
	public CampaignStatus getStatus() {
		return status;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setClient(String name) {
		client = name;
	}
	
  	public void setManager(String manager) {
		manager = name;
	} 
  	
  	public void setStatus(CampaignStatus status) {
  		this.status = status;
  	}
  	
  	public void setStatus(int value) {
  		status = CampaignStatus.getCampaignStatusFromInt(value);
  	}


}
