package com.higherli.bits_store_boolean_demo.bit;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

public class Bit64Test {
	private List<String> binaryStringList;

	@Before
	public void setUp() throws Exception {
		binaryStringList = BitsTest.getBytesFromText();
	}

	@Test
	public void testValueOf() {
		for (int q = 0; q < 1000; q++) {
			byte[] realBytes = new byte[8];
			Arrays.fill(realBytes, (byte) 0);
			String randomBinaryStr = randomCreateBinaryStr(realBytes);
			byte[] bit64Bytes = Bit64.valueOf(randomBinaryStr);
			assertEquals(bit64Bytes.length, realBytes.length);
			for (int i = 0; i < 8; i++) {
				assertEquals(realBytes[i], bit64Bytes[i]);
			}
		}
	}

	@Test
	public void testCreateBit64ByString() {
		List<Bit64> bit64List = new ArrayList<Bit64>();
		for (int i = 0; i < binaryStringList.size(); i++) {
			bit64List.add(Bit64.createBit64ByString(i, binaryStringList.get(i)));
		}
		for (Bit64 bit64 : bit64List) {
			assertEquals(8, bit64.bs.length);
		}
	}

	@Test
	public void testGetBinaryStr() {
		for (int q = 0; q < 100; q++) {
			for (int i = 0; i < binaryStringList.size(); i++) {
				String binaryStr = new String();
				for (int j = 0; j < 64; j++) {
					binaryStr += RandomUtils.nextInt(0, 2);
				}
				Bit64 bit64 = Bit64.createBit64ByString(i, binaryStr);
				assertEquals(binaryStr, bit64.getBinaryStr());
			}
		}
	}

	/**
	 * 随机生成长度为1~64的二进制数
	 * 
	 * @param realBytes
	 *            :存放每8位转为byte的数
	 * @return
	 */
	private String randomCreateBinaryStr(byte[] realBytes) {
		int bitNum = RandomUtils.nextInt(1, 65);
		StringBuilder sb = new StringBuilder();
		int arrayLength = (int) Math.ceil(bitNum / 8);
		for (int j = 0; j < arrayLength; j++) {
			StringBuilder sbs = new StringBuilder();
			int bitLength = j == arrayLength - 1 ? bitNum % 8 : 8;
			for (int i = 0; i < bitLength; i++) {
				int as = RandomUtils.nextInt(0, 2);
				sb.append(as);
				sbs.append(as);
			}
			String sss = sbs.toString();
			int binaryToDecimal = Integer.valueOf(StringUtils.isBlank(sss) ? "0" : sss, 2);
			realBytes[j] = (byte) (binaryToDecimal & 0xff);
		}
		return sb.toString();
	}
}
