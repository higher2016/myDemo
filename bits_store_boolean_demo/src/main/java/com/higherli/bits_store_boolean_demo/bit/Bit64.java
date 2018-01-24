package com.higherli.bits_store_boolean_demo.bit;

import org.apache.commons.lang3.StringUtils;

/**
 * 一个8*8位存储单元
 */
public class Bit64 {
	public final int step; // bit存储单元的序号
	public final byte[] bs; // 存储信息（一位存8字节，共8位）

	public Bit64(int step, byte[] bs) {
		this.step = step;
		this.bs = bs;
	}

	public static Bit64 createBit64ByString(int step, String binaryStr) {
		return new Bit64(step, valueOf(binaryStr));
	}

	public String getBinaryStr() {
		StringBuilder sb = new StringBuilder();
		for (byte b : bs) {
			sb.append(Integer.toBinaryString((b & 0xFF) + 0x100).substring(1));
		}
		return sb.toString();
	}

	/**
	 * 将二进制的字符串，每八位二进制数转为一位byte（无符号数），返回字符串所包含的byte[]
	 */
	public final static byte[] valueOf(String binaryStr) {
		String[] as = splitStringToArrayEightLength(binaryStr);
		byte[] res = new byte[as.length];
		for (int i = 0; i < as.length; i++) {
			int binaryToDecimal = Integer.valueOf(as[i], 2);
			res[i] = (byte) (binaryToDecimal & 0xff);
		}
		return res;
	}

	/**
	 * 以八位为单位长度，切割字符串（长度不足的补0）,返回的String[]长度一定为8
	 * 
	 * @param binaryStr
	 * @return
	 */
	private static String[] splitStringToArrayEightLength(String binaryStr) {
		int byteNum = (int) Math.ceil(binaryStr.length() / 8);
		if (byteNum > 8) {
			throw new RuntimeException("Binary string length exceeds 64 bits.");
		} else {
			byteNum = 8;
		}
		String[] res = new String[byteNum];
		for (int i = 0; i < byteNum; i++) {
			if (i < byteNum - 1) {
				res[i] = StringUtils.substring(binaryStr, i * 8, (i + 1) * 8);
			} else {
				res[i] = StringUtils.substring(binaryStr, i * 8, binaryStr.length());
			}
		}
		for (int i = 0; i < byteNum; i++) {
			if (StringUtils.isBlank(res[i])) {
				res[i] = "0";
			}
		}
		return res;
	}
}
