package br.com.unip.gamerating.model;

public enum Platform {
	
	PLAYSTATION("Playstation"),
    XBOX("Xbox"),
    WII("Wii"),
    SWITCH("Switch");
	
	private String platform;
	
	Platform(String platForm){
		this.platform = platForm;
	}
	
	@Override
	public String toString() {
		return platform;
	}
}