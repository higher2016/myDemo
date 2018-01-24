package com.higherli.bits_store_boolean_demo.bit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Bits implements Serializable {

	private static final long serialVersionUID = 1411830972255678072L;

	private byte bs[];

	public Bits(int bitNum) {
		bs = new byte[(int) Math.ceil(bitNum / 8)];
		Arrays.fill(bs, (byte) 0);
	}

	public Bits(int bitNum, List<Bit64> states) {
		this(bitNum);
		for (Bit64 state : states) {
			if (state.step >= bitNum / 64) {
				System.err.println(new RuntimeException(String.format("<Bits> step(%d) out of bound. length=%d", state.step, bitNum)));
				continue;
			}
			reverse(state.bs);
			System.arraycopy(state.bs, 0, bs, state.step * 8, 8);
		}
	}

	/**
	 * 反转数组
	 */
	public static void reverse(byte[] bs) {
		byte temp;
		for (int i = 0; i < bs.length / 2; i++) {
			int j = bs.length - 1 - i;
			temp = bs[i];
			bs[i] = bs[j];
			bs[j] = temp;
		}
	}

	/**
	 * 将index为标记位true
	 */
	public void mark(int index) {
		if (index / 8 >= bs.length) {
			System.err.println(new RuntimeException(String.format("<Bits> index(%d) out of bound. length=%d", index, bs.length)));
			return;
		}
		bs[index / 8] |= 0x01 << (index % 8);
	}

	public void mark(Collection<Integer> indexs) {
		for (int index : indexs) {
			if (index / 8 >= bs.length) {
				System.err.println(new RuntimeException(String.format("<Bits> index(%d) out of bound. length=%d", index, bs.length)));
				continue;
			}
			bs[index / 8] |= 0x01 << (index % 8);
		}
	}

	public void unMark(int index) {
		if (index / 8 >= bs.length) {
			System.err.println(String.format("<Bits> index(%d) out of bound. length=%d", index, bs.length));
		} else {
			bs[index / 8] &= ~(0x01 << (index % 8));
		}
	}

	public void unMark(Collection<Integer> indexs) {
		for (int index : indexs) {
			unMark(index);
		}
	}

	public boolean isMark(int index) {
		if (index / 8 >= bs.length) {
			System.err.println(new RuntimeException(String.format("<Bits> index(%d) out of bound. length=%d", index, bs.length)));
			return false;
		}
		return (bs[index / 8] & (0x01 << (index % 8))) != 0;
	}

	/** @return 低位在前高位在后 */
	public byte[] getBits() {
		return bs.clone();
	}

	public String toString() {
		ArrayList<Integer> list = new ArrayList<Integer>(bs.length * 8);
		for (int i = 0; i < bs.length * 8; i++) {
			if (isMark(i)) {
				list.add(i);
			}
		}
		return StringUtils.join(list, ",");
	}
}
