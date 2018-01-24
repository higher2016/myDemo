package com.higherli.bits_store_boolean_demo.bit;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;

public class BitsTest {
	private List<Bit64> bit64List;
	private Bits bits;
	private Bits bitsEmpty;

	public static List<String> getBytesFromText() throws IOException {
		List<String> res = new ArrayList<String>();
		File path = new File("D:\\jmWorkSapce\\eclipse\\workSpace\\bits_store_boolean_demo\\src\\test\\resources\\bits.txt");
		InputStreamReader read = new InputStreamReader(new FileInputStream(path));
		BufferedReader bufferedReader = new BufferedReader(read);
		String lineTxt = null;
		while ((lineTxt = bufferedReader.readLine()) != null) {
			res.add(lineTxt);
		}
		bufferedReader.close();
		return res;
	}

	@Before
	public void setUp() throws Exception {
		bit64List = new ArrayList<Bit64>();
		List<String> binaryStringList = getBytesFromText();
		int i = 0;
		for (String binaryString : binaryStringList) {
			bit64List.add(Bit64.createBit64ByString(i, binaryString));
			i++;
		}
		bits = new Bits(bit64List.size() * 64, bit64List);
		bitsEmpty = new Bits(64);
	}

	@Test
	public void testReverse() {
		byte[] b1 = { 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1 };
		byte[] b2 = { 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 0, 1 };
		Bits.reverse(b1);
		for (int i = 0; i < b1.length; i++) {
			assertEquals(b1[i], b2[b1.length - i - 1]);
		}
	}

	@Test
	public void testGetBits() {
		byte[] t = bits.getBits();
		assertEquals(bit64List.size() * 8, t.length);
	}

	@Test
	public void testMarkIntAndIsMark() {
		int allNum = bitsEmpty.getBits().length * 8;
		for (int i = 0; i < allNum; i++) {
			assertFalse(bitsEmpty.isMark(i));
		}
		for (int i = 0; i < allNum; i++) {
			if (!bitsEmpty.isMark(i)) {
				bitsEmpty.mark(i);
			}
		}
		for (int i = 0; i < allNum; i++) {
			assertTrue(bitsEmpty.isMark(i));
		}
	}

	@Test
	public void testMarkCollectionOfInteger() {
		for (int i = 0; i < 7; i++) {
			assertFalse(bitsEmpty.isMark(i));
		}
		bitsEmpty.mark(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7));
		for (int i = 0; i < 7; i++) {
			assertTrue(bitsEmpty.isMark(i));
		}
	}

	@Test
	public void testUnMarkInt() {
		for (int i = 0; i < bitsEmpty.getBits().length * 8; i++) {
			bitsEmpty.mark(i);
			assertTrue(bitsEmpty.isMark(i));
			bitsEmpty.unMark(i);
			assertFalse(bitsEmpty.isMark(i));
		}
	}

	@Test
	public void testUnMarkCollectionOfInteger() {
		for (int i = 0; i < bitsEmpty.getBits().length * 8; i++) {
			bitsEmpty.mark(i);
		}
		List<Integer> indexList = new ArrayList<Integer>(RandomUtils.nextInt(1, 64));
		for (int i = 0; i < indexList.size(); i++) {
			indexList.add(RandomUtils.nextInt(0, 63));
		}
		for(int index : indexList){
			assertTrue(bitsEmpty.isMark(index));
		}
		bitsEmpty.unMark(indexList);
		for(int index : indexList){
			assertFalse(bitsEmpty.isMark(index));
		}
	}
}
