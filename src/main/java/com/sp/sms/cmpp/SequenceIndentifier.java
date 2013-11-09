/**
 * 
 */
package com.sp.sms.cmpp;

import java.util.concurrent.atomic.AtomicInteger;

import com.sp.sms.util.CTime;

/**
 * @author sunpeng
 * 
 */
public class SequenceIndentifier {

	private final AtomicInteger atomicInteger;

	private static final SequenceIndentifier instance = new SequenceIndentifier();

	private SequenceIndentifier() {
		Integer init = getInitSequence();
		atomicInteger = new AtomicInteger(init);
	}

	public static SequenceIndentifier getInstance() {
		return instance;
	}

	protected Integer getInitSequence() {
		String time = CTime.getTime(CTime.MMddHHmmss);
		return Integer.parseInt(time);
	}

	public void init(Integer value) {
		atomicInteger.set(value);
		atomicInteger.compareAndSet(Integer.MAX_VALUE + 1, 1);
	}

	public Integer nextSequence() {
		Integer value = atomicInteger.getAndIncrement();
		atomicInteger.compareAndSet(Integer.MAX_VALUE + 1, 1);
		return value;
	}

}
