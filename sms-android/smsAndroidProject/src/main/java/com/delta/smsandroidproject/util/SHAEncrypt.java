package com.delta.smsandroidproject.util;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class SHAEncrypt {
	/**
	 * 生成SALT的数组(86)
	 */
	private final String[] SALT_ARR = { "a", "b", "c", "d", "e", "f", "g", "h",
			"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
			"v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "0", ".", "-", "*", "/", "'", ":", ";", ">", "<", "~", "!",
			"@", "#", "$", "%", "^", "&", "(", ")", "{", "}", "[", "]", "|" };
	/**
	 * 循环加密次数
	 */
	private final int ENCODE_COUNT = 1000;
	/**
	 * 加密算法类型
	 */
	private final String ENCODE_TYPE = "SHA-256";
	/**
	 * SALT长度
	 */
	private final int SALT_LENGTH = 16;
	/**
	 * getBytes时指定使用的字符集
	 */
	private final String CHAR_SET = "UTF-8";

	/**
	 * 对明文密码进行SSHA-256加密，加密步骤如下： <br>
	 * 1. 生成随机salt值 <br>
	 * 2. 明文密码变形：前后各补4位0 <br>
	 * 3. 连接salt值: 将salt分成前8位temp1和剩余部分temp2,以temp2+transformedPwd+temp1的形式连接 <br>
	 * 4. 对连接后的字串进行多次SHA-256加密<br>
	 * 5. 对加密后的密文右移6位<br>
	 * 6. 将salt值转换成字节数组(utf-8)再转换成16进制字符串<br>
	 * 7. 将转换后的salt值作左移4位变形<br>
	 * 8. 连接移位后的密文和移位后的变形salt值字串<br>
	 * 9. 对连接字串右移8位作为最终加密结果<br>
	 * 
	 * @param pwd
	 *            密码明文
	 * @return 加密后的密码
	 */
	public String encryptPwd(String pwd) {
		String ret = "";
		String salt = this.genSalt();
		ret = this.encryptPwdWithSalt(pwd, salt);
		return ret;
	}

	/**
	 * 对明文密码进行SSHA-256加密，加密步骤如下： <br>
	 * 1. 明文密码变形：前后各补4位0 <br>
	 * 2. 连接salt值: 将salt分成前8位temp1和剩余部分temp2,以temp2+transformedPwd+temp1的形式连接 <br>
	 * 3. 对连接后的字串进行多次SHA-256加密<br>
	 * 4. 对加密后的密文右移6位<br>
	 * 5. 将salt值转换成字节数组(utf-8)再转换成16进制字符串<br>
	 * 6. 将转换后的salt值作左移4位变形<br>
	 * 7. 连接移位后的密文和移位后的变形salt值字串<br>
	 * 8. 对连接字串右移8位作为最终加密结果<br>
	 * 
	 * @param pwd
	 *            明文密码
	 * @param salt
	 *            用于加密的salt值
	 * @return
	 */
	private String encryptPwdWithSalt(String pwd, String salt) {
		String ret = "";
		try {
			String transformedPwd = this.transform(pwd, 4);
			// 将salt分成前8位temp1和剩余部分temp2
			// 以temp2+transformedPwd+temp1的形式连接
			String joinStr = this.joinSaltAndPwd(salt, transformedPwd);
			String encodePwd = this.encodeJoinStr(joinStr);
			String rightMovedPwd = this.rightMove(encodePwd, 6);
			String hexStrSalt = this.bytes2Hex(salt.getBytes(CHAR_SET));
			String leftMovedHexStrSalt = this.rightMove(hexStrSalt, -4);
			ret = this.rightMove(rightMovedPwd + leftMovedHexStrSalt, 8);
		} catch (Exception e) {
			ret = "";
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 对明文/密文进行验证，检查是否一致 验证步骤如下： <br>
	 * 1. 从原密文中获取通过逆运算获取salt字串 <br>
	 * 2. 对明文密码使用salt字串再次进行加密 <br>
	 * 3. 两个密文是否一致 <br>
	 * 
	 * @param pwd
	 *            明文密码
	 * @param encPwd
	 *            密文密码
	 * @return 校验结果
	 */
	public boolean validatePwd(String pwd, String encPwd) {
		if (null == encPwd) {
			return false;
		}
		boolean ret = false;
		String salt = this.getSaltFromEncryptedPwd(encPwd);
		ret = encPwd.equals(encryptPwdWithSalt(pwd, salt));
		return ret;
	}

	/**
	 * 从密文密码中通过逆运算获取加密时使用到的salt字串 <br>
	 * 获取步骤如下： <br>
	 * 1. 对密文左移8位 <br>
	 * 2. 从尾部截取变形后的salt字串 <br>
	 * 长度为2倍原salt字串长度（因为转为16进制字节数组字串时长度会加倍） <br>
	 * 3. 对salt值的16进制字节数组字符串右移4位 <br>
	 * 4. 将变形后的salt值从16进制字节数组字符串还原成原来的字符串形式 <br>
	 * 
	 * @param encPwd
	 *            密文密码
	 * @return 加密时使用的salt字串，获取出错则返回""
	 */
	private String getSaltFromEncryptedPwd(String encPwd) {
		String ret = "";
		try {
			String leftMoved = this.rightMove(encPwd, -8);
			int hexSaltLength = SALT_LENGTH * 2;
			if(encPwd.length()<hexSaltLength){
				// 加密密码长度比16进制字节数组字串的salt值短，不可能获取得到正确的salt值
				return "";
			}
			String hexSalt = this.rightMove(leftMoved.substring(encPwd.length()
					- hexSaltLength, encPwd.length()), 4);
			ret = new String(this.hex2Bytes(hexSalt), CHAR_SET);
		} catch (Exception e) {
			ret = "";
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 
	 * 获得随机SALT值
	 * 
	 * @return 返回一个包含字母、数字、特殊字符的16位随机数
	 */
	private String genSalt() {

		StringBuffer result = new StringBuffer();
		Random r = new Random();
		int temp = 0;
		for (int i = 0; i < this.SALT_LENGTH; i++) {
			temp = r.nextInt(this.SALT_ARR.length);
			result.append(this.SALT_ARR[temp]);
		}
		return result.toString();
	}

	/**
	 * 密码变形（前后补N位0）
	 * 
	 * @param pwd
	 *            密码原文
	 * @param n
	 *            补0的个数
	 * @return 返回一个密码前后补N位0的字符串
	 */
	private String transform(String pwd, int n) {
		String toTransform = null == pwd ? "" : pwd;
		StringBuffer temp = new StringBuffer();
		for (int i = 0; i < n; i++)
			temp.append("0");
		return temp.toString() + toTransform + temp.toString();
	}

	/**
	 * 
	 * 把指定字符串右移N位,若N<0则为左移
	 * 
	 * @param src
	 *            源字符串
	 * @param n
	 *            右移的位数
	 * @return 返回一个右移N位的字符串
	 */
	private String rightMove(String src, int n) {
		if (src == null || src.length() == 0) {
			return src;
		}
		boolean isRight = true;
		int absN = n;
		if (n < 0) {
			isRight = false;
			absN = -n;
		}
		int cnt = absN > src.length() ? absN % src.length() : absN;
		if (0 == cnt) {
			return src;
		}
		if (isRight) {
			// 右移cnt位
			String temp1 = src.substring(src.length() - cnt, src.length());
			String temp2 = src.substring(0, src.length() - cnt);
			return temp1 + temp2;
		} else {
			// 左移cnt位
			String temp1 = src.substring(0, cnt);
			String temp2 = src.substring(cnt, src.length());
			return temp2 + temp1;
		}
	}

	/**
	 * 
	 * 把对应的随机数分成2部分，前半部分8个字符，调换顺序后把变形密码加到中间
	 * 
	 * @param pwd
	 *            变形密码
	 * @param salt
	 *            对应的随机数
	 * @return 返回一个随机数与变形密码连接的字符串
	 */
	private String joinSaltAndPwd(String salt, String pwd) {
		if(salt.length()!=SALT_LENGTH){
			// salt值的长度与给定的不符（校验的时候可能会出现）
			// 返回pwd+salt
			return pwd+salt;
		}
		String temp1 = salt.substring(0, 8);
		String temp2 = salt.substring(8, salt.length());
		return temp2 + pwd + temp1;
	}

	/**
	 * 
	 * 使用加密算法进行多次加密
	 * 
	 * @param joinStr
	 *            连接字符串
	 * @return 返回一个多次加密后的字符串
	 */
	private String encodeJoinStr(String joinStr) {

		if (joinStr == null)
			return null;

		String temp = joinStr;

		for (int i = 0; i < this.ENCODE_COUNT; i++) {

			temp = this.encrypt(temp);
		}
		return temp;
	}

	/**
	 * 把字节数组输出成16进制字符串
	 * 
	 * @param bts
	 * @return
	 */
	private String bytes2Hex(byte[] bts) {
		StringBuffer des = new StringBuffer();
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des.append("0");
			}
			des.append(tmp);
		}
		return des.toString();
	}

	/**
	 * 把16进制字符串输出成字节数组
	 * 
	 * @param hexStr
	 * @return
	 */
	private byte[] hex2Bytes(String hexStr) {
		if ((hexStr.length() % 2) != 0) {
			return new byte[] {};
		}
		byte[] ret = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {

			ret[i] = (byte) (Long.decode("0x"
					+ hexStr.substring(i * 2, i * 2 + 2)) & 0xFF);
		}
		return ret;
	}

	/**
	 * 
	 * 利用SHA-256对源字符串进行加密
	 * 
	 * @param src
	 * @return 返回加密串，注：本例中，空串表示加密失败
	 */
	private String encrypt(String src) {

		// 源字符串转换成byte数组
		try {
			byte[] btSource = src.getBytes(CHAR_SET);
			// 此处选择了SHA-256加密算法,实际可选的算法有"MD2、MD5、SHA-1、SHA-256、SHA-384、SHA-512"
			MessageDigest md = MessageDigest.getInstance(this.ENCODE_TYPE);
			md.reset();
			md.update(btSource);
			String result = bytes2Hex(md.digest()); // to HexString
			return result;
		} catch (NoSuchAlgorithmException e) {
			return "";
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	public static void main(String args[]) throws Exception {
		SHAEncrypt demo = new SHAEncrypt();
		String str = "12test中文濲騃";
		long startTime = System.currentTimeMillis();
		String encPwd = demo.encryptPwd(str);
		System.out.println("encrypt time costs:"
				+ (System.currentTimeMillis() - startTime));
		System.out.println("encrypted pwd:" + encPwd + ",length:"
				+ encPwd.length());
		System.out.println(demo.validatePwd(str, encPwd));
		System.out.println(new String(demo.hex2Bytes(encPwd), "UTF-8"));
	}
}