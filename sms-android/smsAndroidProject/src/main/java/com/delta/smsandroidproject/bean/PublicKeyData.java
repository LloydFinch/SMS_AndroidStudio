package com.delta.smsandroidproject.bean;

/**
 * 获取公钥-实体类
 * @author Jianzao.Zhang
 *
 */
public class PublicKeyData {
	private String publicKeyModulus;
	private String publicKeyExponent;
	private String publicKey;
	public String getPublicKeyModulus() {
		return publicKeyModulus;
	}
	public String getPublicKeyExponent() {
		return publicKeyExponent;
	}
	
	public String getPublicKey() {
		return publicKey;
	}
	@Override
	public String toString() {
		return "PublicKeyData [publicKeyModulus=" + publicKeyModulus
				+ ", publicKeyExponent=" + publicKeyExponent + ", publicKey="
				+ publicKey + "]";
	}
	
	
}
