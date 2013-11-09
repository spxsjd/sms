/**
 * 
 */
package com.sp.sms.cmpp.message;

import com.sp.sms.cmpp.CMPPConfig;
import com.sp.sms.util.BytesWrapper;

/**
 * @author sunpeng
 * 
 */
public class SubmitMessage extends Header {

	public static final Integer CMPP_SUBMIT = 0x00000004;
	public static final Integer CMPP_SUBMIT_RESP = 0x80000004;

	
	public SubmitMessage() {
		super(0x00000004);
		this.msgId = 0;
		this.msgLevel = 1;
		this.registeredDelivery = CMPPConfig.getInstance()
				.getRegisteredDelivery();
		this.serviceId = CMPPConfig.getInstance().getServiceId();
		this.feeUserType = 2;
		this.tpPid = CMPPConfig.getInstance().getTpPid();
		this.tpUdhi = CMPPConfig.getInstance().getTpUdhi();
		this.feeCode = CMPPConfig.getInstance().getFeeCode();
		this.msgSrc =  CMPPConfig.getInstance().getSpId();
		this.feeType = "00";
	}

	@Override
	protected byte[] getBody() {
		BytesWrapper wrapper = new BytesWrapper(1024 * 3);
		wrapper.blank(8);
		wrapper.append(this.pkTotal);
		wrapper.append(this.pkNumber);
		wrapper.append(this.registeredDelivery);
		wrapper.append(this.msgLevel);
		if (this.serviceId != null) {
			wrapper.append(this.serviceId);
		} else {
			wrapper.blank(10);
		}
		wrapper.append(this.feeUserType);
		if (this.feeTerminalId != null) {
			wrapper.append(this.feeTerminalId);
		} else {
			wrapper.blank(21);
		}
		wrapper.append(this.tpPid);
		wrapper.append(this.tpUdhi);
		wrapper.append(this.msgFmt);
		wrapper.append(this.msgSrc);
		wrapper.append(this.feeType);
		wrapper.append(this.feeCode);
		if (this.valIdTime != null) {
			wrapper.append(this.valIdTime.getBytes());
		} else {
			wrapper.blank(17);
		}
		if (this.atTime != null) {
			wrapper.append(this.atTime.getBytes());
		} else {
			wrapper.blank(17);
		}
		if (this.srcId != null) {
			wrapper.append(this.srcId);
		} else {
			wrapper.blank(21);
		}
		wrapper.append(this.destUsrTl);
		wrapper.append(this.getDestTerminalIdBytes());
		wrapper.append(this.msgLength);
		wrapper.append(this.msgContent);
		if (this.reserve != null) {
			wrapper.append(this.reserve);
		} else {
			wrapper.blank(8);
		}
		return wrapper.getData();
	}

	public byte[] getDestTerminalIdBytes() {
		byte[] desc = new byte[this.destUsrTl * 21];
		for (byte i = 0; i < this.destUsrTl; i++) {
			System.arraycopy(this.destTerminalId[i].getBytes(), 0, desc,
					i * 21, this.destTerminalId[i].getBytes().length);
		}
		return desc;
	}

	/**
	 * 信息标识，由SP侧短信网关本身产生，本处填空。
	 */
	protected Integer msgId;
	/**
	 * 相同Msg_Id的信息总条数，从1开始
	 */
	protected byte pkTotal;
	/**
	 * 相同Msg_Id的信息序号，从1开始
	 */
	protected byte pkNumber;
	/**
	 * 是否要求返回状态确认报告： 0：不需要 1：需要 2：产生SMC话单 （该类型短信仅供网关计费使用，不发送给目的终端)
	 */
	protected byte registeredDelivery;
	/**
	 * 信息级别
	 */
	protected byte msgLevel;
	/**
	 * 业务类型，是数字、字母和符号的组合。
	 */
	protected String serviceId;
	/**
	 * 计费用户类型字段 0：对目的终端MSISDN计费； 1：对源终端MSISDN计费； 2：对SP计费;
	 * 3：表示本字段无效，对谁计费参见Fee_terminal_Id字段。
	 */
	protected byte feeUserType;
	/**
	 * 被计费用户的号码（如本字节填空，则表示本字段无效，对谁计费参见Fee_UserType字段，本字段与Fee_UserType字段互斥）
	 */
	protected String feeTerminalId;
	/**
	 * GSM协议类型。详细是解释请参考GSM03.40中的9.2.3.9
	 */
	protected byte tpPid;
	/**
	 * GSM协议类型。详细是解释请参考GSM03.40中的9.2.3.23,仅使用1位，右对齐
	 */
	protected byte tpUdhi;
	/**
	 * 信息格式 0：ASCII串 3：短信写卡操作 4：二进制信息 8：UCS2编码 15：含GB汉字 。。。。。。
	 */
	protected byte msgFmt;
	/**
	 * 信息内容来源(SP_Id)
	 */
	protected String msgSrc;
	/**
	 * 资费类别 01：对“计费用户号码”免费 02：对“计费用户号码”按条计信息费 03：对“计费用户号码”按包月收取信息费
	 * 04：对“计费用户号码”的信息费封顶 05：对“计费用户号码”的收费是由SP实现
	 */
	protected String feeType;
	/**
	 * 资费代码（以分为单位）
	 */
	protected String feeCode;
	/**
	 * 存活有效期，格式遵循SMPP3.3协议
	 */
	protected String valIdTime;
	/**
	 * 定时发送时间，YYMMDDhhmmsstnnp YY--年份的最后两位00-99 MM—月份01-12 DD—日01-31 Hh—小时00-23
	 * Mm—分00-59 Ss—秒00-59 T—十分之一秒0-9 Nn—与UTS时间超前或落后的差距00-48 P—“+”超前 “-”落后
	 */
	protected String atTime;
	/**
	 * 源号码 SP的服务代码或前缀为服务代码的长号码,
	 * 网关将该号码完整的填到SMPP协议Submit_SM消息相应的source_addr字段，该号码最终在用户手机上显示为短消息的主叫号码
	 */
	protected String srcId;
	/**
	 * 接收信息的用户数量(小于100个用户)
	 */
	protected byte destUsrTl;
	/**
	 * 接收短信的MSISDN号码
	 */
	protected String[] destTerminalId;
	/**
	 * 信息长度(Msg_Fmt值为0时：<=160个字节；其它<=140个字节)
	 */
	protected byte msgLength;
	/**
	 * 信息内容
	 */
	protected byte[] msgContent;
	/**
	 * 保留
	 */
	protected String reserve;

	public Integer getMsgId() {
		return msgId;
	}

	public void setMsgId(Integer msgId) {
		this.msgId = msgId;
	}

	public byte getPkTotal() {
		return pkTotal;
	}

	public void setPkTotal(byte pkTotal) {
		this.pkTotal = pkTotal;
	}

	public byte getPkNumber() {
		return pkNumber;
	}

	public void setPkNumber(byte pkNumber) {
		this.pkNumber = pkNumber;
	}

	public byte getRegisteredDelivery() {
		return registeredDelivery;
	}

	public void setRegisteredDelivery(byte registeredDelivery) {
		this.registeredDelivery = registeredDelivery;
	}

	public byte getMsgLevel() {
		return msgLevel;
	}

	public void setMsgLevel(byte msgLevel) {
		this.msgLevel = msgLevel;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public byte getFeeUserType() {
		return feeUserType;
	}

	public void setFeeUserType(byte feeUserType) {
		this.feeUserType = feeUserType;
	}

	public String getFeeTerminalId() {
		return feeTerminalId;
	}

	public void setFeeTerminalId(String feeTerminalId) {
		this.feeTerminalId = feeTerminalId;
	}

	public byte getTpPid() {
		return tpPid;
	}

	public void setTpPid(byte tpPid) {
		this.tpPid = tpPid;
	}

	public byte getTpUdhi() {
		return tpUdhi;
	}

	public void setTpUdhi(byte tpUdhi) {
		this.tpUdhi = tpUdhi;
	}

	public byte getMsgFmt() {
		return msgFmt;
	}

	public void setMsgFmt(byte msgFmt) {
		this.msgFmt = msgFmt;
	}

	public String getMsgSrc() {
		return msgSrc;
	}

	public void setMsgSrc(String msgSrc) {
		this.msgSrc = msgSrc;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getFeeCode() {
		return feeCode;
	}

	public void setFeeCode(String feeCode) {
		this.feeCode = feeCode;
	}

	public String getValIdTime() {
		return valIdTime;
	}

	public void setValIdTime(String valIdTime) {
		this.valIdTime = valIdTime;
	}

	public String getAtTime() {
		return atTime;
	}

	public void setAtTime(String atTime) {
		this.atTime = atTime;
	}

	public String getSrcId() {
		return srcId;
	}

	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}

	public byte getDestUsrTl() {
		return destUsrTl;
	}

	public String[] getDestTerminalId() {
		return destTerminalId;
	}

	public void setDestTerminalId(String[] destTerminalId) {
		this.destTerminalId = destTerminalId;
		this.destUsrTl = Integer.valueOf(this.destTerminalId.length)
				.byteValue();
	}

	public byte getMsgLength() {
		return msgLength;
	}

	public void setMsgLength(byte msgLength) {
		this.msgLength = msgLength;
	}

	public byte[] getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(byte[] msgContent) {
		this.msgContent = msgContent;
	}

	public String getReserve() {
		return reserve;
	}

	public void setReserve(String reserve) {
		this.reserve = reserve;
	}

}
