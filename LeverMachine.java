package org.collins.kent.leverage;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class LeverMachine {

	private int numLevers = 8;

	Set<Integer> raisedLevers = new HashSet<>();

	public void setRaisedLevers(Set<Integer> levers) {
		this.raisedLevers = levers;
	}

	public Set<Integer> getRaisedLevers() {
		return raisedLevers;
	}

	public void raiseLever(int lever)
			throws NonExistentLeverException {
		if (isValid(lever))
			raisedLevers.add(lever);
		else
			throw new NonExistentLeverException();
	}

	public void lowerLever(int lever)
			throws NonExistentLeverException {
		if (isValid(lever))
			raisedLevers.remove(lever);
		else
			throw new NonExistentLeverException();
	}

	public long getCurrentMachineValue() {
		long sum = 0;
		for (Integer i : raisedLevers) {
			sum += Levers.pullLever(i);
		}
		return sum;
	}

	public String toString() {
		// binary string representation of the lifted levers
		StringBuilder sb = new StringBuilder("");
		for (int i = numLevers - 1; i >= 0; i--) {
			sb.append(raisedLevers.contains(i) ? "1" : "0");
		}
		return sb.toString();
	}

	private boolean isValid(int lever) {
		return lever >= 0 && lever < numLevers;
	}

	public String getDescription() {
		return "This machine consists of " + numLevers
				+ " levers numbered from 0 to " + (numLevers - 1);
	}

	public int getNumLevers() {
		return numLevers;
	}

	public void setNumLevers(int numLevers) {
		// let's not get too crazy
		this.numLevers = numLevers > 64 ? 64 : numLevers;
		raisedLevers = raisedLevers.stream().filter(s -> isValid(s))
				.collect(Collectors.toSet());
	}

	// for testing
	public static void main(String[] args)
			throws NonExistentLeverException {
		LeverMachine m = new LeverMachine();
		System.out.println(m);
		m.raiseLever(1);
		m.raiseLever(2);
		System.out.println(m);
		m.lowerLever(2);
		m.lowerLever(3);
		m.raiseLever(4);
		m.raiseLever(6);
		m.raiseLever(7);
		m.setNumLevers(6);
		System.out.println(m.getDescription());
		System.out.println(m.getCurrentMachineValue());
		System.out.println(m);
	}

	@SuppressWarnings("serial")
	class NonExistentLeverException extends Exception {
		NonExistentLeverException() {
			super("The lever you requested does not exist in this machine.");
		}
	}

}
