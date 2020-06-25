package org.collins.kent.leverage;

import java.util.Arrays;

public class Levers {

	public static int pullLever(int index) {
		return (int) Math.pow(2, index);
	}

	public static int pullLevers(int[] indices) {
		int sum = 0;
		for (int i : indices) {
			sum += pullLever(i);
		}
		return sum;
	}
	
	

	public static void main(String[] args) {
		if (args.length!=0) {
			try {
			int[] collect = Arrays.stream(args).mapToInt(Integer::parseInt).toArray();
			System.out.println(pullLevers(collect));
			} catch (NumberFormatException e) {
				System.out.println("Encountered an error processing input values.");
			}
		} else {
			System.out.println("No arguments provided.  Indicate a sequence of levers to pull ...");
		}
	}

}
