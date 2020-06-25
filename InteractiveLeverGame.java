package org.collins.kent.leverage;

import java.util.Collections;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InteractiveLeverGame extends LeverMachine {

	private Scanner s = new Scanner(System.in);
	private long jackpot = 0;
	private long earnings = 0;
	private boolean inProgress = true;

	public static final String QUIT = "q";
	public static final String RAISE = "r";
	public static final String LOWER = "l";
	public static final String STATUS = "s";
	public static final String CLAIM = "c";
	public static final String BITS = "b";
	public static final String MODIFY = "m";
	public static final String HINT = "h";

	public InteractiveLeverGame() {
		System.out.println(this.buildGameInstructionString());
		setRandomJackpot();
		System.out.println(this.buildStatusString());
		while (inProgress) {
			play();
		}
		System.out.println(this.buildFinalResultString());
	}

	public void play() {
		printCommandString();
		String command = s.next();
		if (QUIT.equalsIgnoreCase(command)) {
			inProgress = false;
		} else if (STATUS.equalsIgnoreCase(command)) {
			System.out.println(this.buildStatusString());
		} else if (RAISE.equalsIgnoreCase(command)) {
			System.out.println("Which lever to raise?");
			int lever;
			try {
				lever = s.nextInt();
				super.raiseLever(lever);
			} catch (NonExistentLeverException e) {
				System.out.println(e);
			} catch (Exception e) {
				System.out.println(
						"Not a lever.  " + super.getDescription());
			}

		} else if (LOWER.equalsIgnoreCase(command)) {
			System.out.println("Which lever to lower?");
			int lever;
			try {
				lever = s.nextInt();
				super.lowerLever(lever);
			} catch (NonExistentLeverException e) {
				System.out.println(e);
			} catch (Exception e) {
				System.out.println(
						"Not a lever.  " + super.getDescription());
			}
		} else if (MODIFY.equalsIgnoreCase(command)) {
			System.out.println(
					"How many levers should this machine have?");
			int numLevers = s.nextInt();
			this.setNumLevers(numLevers);
		} else if (BITS.equalsIgnoreCase(command)) {
			System.out.println(this.toString());
		} else if (CLAIM.equalsIgnoreCase(command)) {
			long value = super.getCurrentMachineValue();
			if (value == jackpot) {
				earnings += jackpot;
				System.out.println(this.buildCorrectJackpotString());
				setRandomJackpot();
				System.out.println(this.buildJackpotString());
				this.getRaisedLevers().clear();
				System.out.println(
						"All levers have been reset to their lowered positions.");
			} else {
				System.out.println(
						this.buildWrongJackpotString(value));
			}
		}

		else if (HINT.equalsIgnoreCase(command)) {
			System.out.println(this.buildHintString());
		}
	}

	private void printCommandString() {
		System.out.println(
				"(R)aise a lever, (L)ower a lever, (S)tatus report, \n(C)laim the jackpot, request a (H)int, or (Q)uit?: ");
	}

	private void setRandomJackpot() {
		jackpot = (long) (Math.random()
				* Math.pow(2, getNumLevers() - 1));
	}

	private int getNumLeversNeeded() {
		String jackpotString = Long.toBinaryString(jackpot);
		int numLevers = 0;
		for (char c : jackpotString.toCharArray()) {
			numLevers += c == '1' ? 1 : 0;
		}
		return numLevers;
	}

	private String buildGameInstructionString() {
		StringBuilder sb = new StringBuilder("");
		sb.append("======== INSTRUCTIONS ========\n");
		sb.append("Welcome to the Magical Mystery Machine. \n");
		sb.append(
				"Raise and lower levers to adjust the machine's secret value.\n");
		sb.append(
				"When you think the machine's value matches the jackpot value, claim the jackpot.");
		return sb.toString();
	}

	private String buildJackpotString() {
		return "The jackpot is set to $" + jackpot;
	}

	private String buildWrongJackpotString(long incorrectAmount) {
		StringBuilder sb = new StringBuilder();
		boolean plural = this.getRaisedLevers().size() != 1;
		if (plural) {
			sb.append(
					"Sorry -- the raised levers produce a value of ");
		} else {
			sb.append(
					"Sorry -- the raised lever produces a value of ");
		}
		sb.append(incorrectAmount + " but the jackpot amount is "
				+ jackpot + "\n");
		if (incorrectAmount > jackpot) {
			sb.append("You need to lower one or more levers.");
		} else {
			sb.append("You need to raise some more levers.");
		}
		return sb.toString();
	}

	private String buildCorrectJackpotString() {
		return "WINNER!  You won the jackpot of $" + jackpot
				+ " bringing your total winnings to $" + earnings;
	}

	private String buildFinalResultString() {
		return "You won a total of $" + earnings;
	}

	private String buildStatusString() {
		int numLifted = super.getRaisedLevers().size();
		StringBuilder sb = new StringBuilder();
		boolean plural = super.getRaisedLevers().size() != 1;
		sb.append("======== CURRENT STATUS\n");
		sb.append(this.getDescription());
		sb.append(".\nThe following ");
		sb.append(plural ? "levers are" : "lever is");
		sb.append(" raised: ");
		sb.append(numLifted == 0 ? "none"
				: super.getRaisedLevers().stream()
						.sorted(Collections.reverseOrder())
						.collect(Collectors.toList()));
		sb.append("\nThe jackpot is set to $");
		sb.append(jackpot);
		return sb.toString();
	}

	private String buildHintString() {
		StringBuilder sb = new StringBuilder("For a jackpot of ");
		sb.append("$");
		sb.append(jackpot);
		sb.append(" you will need to raise exactly ");
		int num = getNumLeversNeeded();
		sb.append(num == 1 ? "one lever." : num + " levers.");
		return sb.toString();
	}

	public static void main(String[] args) {
		InteractiveLeverGame game = new InteractiveLeverGame();
	}
}
