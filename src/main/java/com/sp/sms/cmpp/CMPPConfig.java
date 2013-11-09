/**
 * 
 */
package com.sp.sms.cmpp;


/**
 * @author sunpeng
 * 
 */
public class CMPPConfig {

	private static final CMPPConfig instance = new CMPPConfig();

	private CMPPConfig() {

	}

	public static CMPPConfig getInstance() {
		return instance;
	}

	private String user;
	private String secret;
	private String spId;
	private byte version;
	
	private byte tpPid;
	private byte tpUdhi;
	private String feeCode;
	private String serviceId;
	private byte registeredDelivery;
	

	public String getUser() {
		return user;
	}

	protected void setUser(String user) {
		this.user = user;
	}


	public String getSpId() {
		return spId;
	}

	protected void setSpId(String spId) {
		this.spId = spId;
	}

	public byte getVersion() {
		return version;
	}

	protected void setVersion(byte version) {
		this.version = version;
	}

	public String getSecret() {
		return secret;
	}

	protected void setSecret(String secret) {
		this.secret = secret;
	}

	public byte getRegisteredDelivery() {
		return registeredDelivery;
	}

	protected void setRegisteredDelivery(byte registeredDelivery) {
		this.registeredDelivery = registeredDelivery;
	}

	public String getServiceId() {
		return serviceId;
	}

	protected void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public byte getTpPid() {
		return tpPid;
	}

	protected void setTpPid(byte tpPid) {
		this.tpPid = tpPid;
	}

	public byte getTpUdhi() {
		return tpUdhi;
	}

	protected void setTpUdhi(byte tpUdhi) {
		this.tpUdhi = tpUdhi;
	}

	public String getFeeCode() {
		return feeCode;
	}

	protected void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}
	
	

}
