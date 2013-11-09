/**
 * 
 */
package com.sp.sms.cmpp.message;

import com.sp.sms.cmpp.CMPPConfig;
import com.sp.sms.util.BytesWrapper;
import com.sp.sms.util.CTime;
import com.sp.sms.util.MD5;

/**
 * @author sunpeng
 * 
 */
public class ConnectMessage extends Header {

	public static final Integer COMMAND_CMPP_CONNECT = 0x00000001;
	public static final Integer COMMAND_CMPP_CONNECT_RESP = 0x80000001;
	
	public ConnectMessage() {
		super(0x00000001);
		this.sourceAddr = CMPPConfig.getInstance().getSpId();
		this.version = CMPPConfig.getInstance().getVersion();
		this.timestamp = CTime.getTime(CTime.MMddHHmmss);
		this.computeAuth();
	}

	private void computeAuth() {// 取得登录认证码（转换前）
		BytesWrapper wrapper = new BytesWrapper(100);
		wrapper.append(CMPPConfig.getInstance().getUser());
		wrapper.blank(9);
		wrapper.append(CMPPConfig.getInstance().getSecret());
		wrapper.append(this.timestamp);
		byte[] data = wrapper.getData();
		int length = wrapper.getLength();		
		this.authenticatorSource = (new MD5()).getMD5ofBytes(data,length);
	}

	@Override
	protected byte[] getBody() {
		BytesWrapper wrapper = new BytesWrapper(100);
		wrapper.append(this.sourceAddr);
		wrapper.append(this.authenticatorSource);
		wrapper.append(this.version);
		wrapper.append(Integer.parseInt(this.timestamp));
		return wrapper.getData();
	}

	/**
	 * 源地址，此处为SP_Id，即SP的企业代码。
	 */
	protected String sourceAddr;
	/**
	 * 用于鉴别源地址。其值通过单向MD5 hash计算得出，表示如下： AuthenticatorSource = MD5（Source_Addr+9
	 * 字节的0 +shared secret+timestamp） Shared secret
	 * 由中国移动与源地址实体事先商定，timestamp格式为：MMDDHHMMSS，即月日时分秒，10位。不足10位的左补零。
	 */
	protected byte[] authenticatorSource;
	/**
	 * 双方协商的版本号(高位4bit表示主版本号,低位4bit表示次版本号)
	 */
	protected byte version;
	/**
	 * 时间戳的明文,由客户端产生,格式为MMDDHHMMSS，即月日时分秒，10位数字的整型，右对齐
	 */
	protected String timestamp;

}
