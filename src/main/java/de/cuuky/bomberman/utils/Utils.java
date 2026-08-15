package de.cuuky.bomberman.utils;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.inventory.ItemStack;

public class Utils {

	public static Object getStringObject(String obj) {
		try {
			return Integer.parseInt(obj);
		} catch (NumberFormatException e) {
		}

		try {
			return Long.parseLong(obj);
		} catch (NumberFormatException e2) {
		}

		try {
			return Double.parseDouble(obj);
		} catch (NumberFormatException e2) {
		}

		if (obj.equalsIgnoreCase("true") || obj.equalsIgnoreCase("false"))
			return obj.equalsIgnoreCase("true") ? true : false;
		else
			return obj;
	}

	public static String getArgsToString(String[] args, String insertBewteen) {
		String command = "";
		for (String arg : args)
			if (command.equals(""))
				command = arg;
			else
				command = command + insertBewteen + arg;
		return command;
	}

	public static String getArgsToString(ArrayList<String> args, String insertBewteen) {
		String command = "";
		for (String arg : args)
			if (command.equals(""))
				command = arg;
			else
				command = command + insertBewteen + arg;
		return command;
	}

	public static String replaceAllColors(String s) {
		String newMessage = "";
		boolean lastPara = false;
		for (char c : s.toCharArray()) {
			if (lastPara) {
				lastPara = false;
				continue;
			}

			if (c == '§' || c == '&') {
				lastPara = true;
				continue;
			}

			newMessage = newMessage.isEmpty() ? String.valueOf(c) : newMessage + c;
		}

		return newMessage;
	}

	public static String[] removeString(String[] string, int loc) {
		String[] ret = new String[string.length - 1];
		int i = 0;
		boolean removed = false;
		for (String arg : string) {
			if (i == loc && !removed) {
				removed = true;
				continue;
			}

			ret[i] = arg;
			i++;
		}

		return ret;
	}

	@SuppressWarnings("deprecation")
	public static ItemStack getItemByString(String id) {
		int normalID = 0;
		try {
			normalID = (id.contains(":") ? Integer.valueOf(id.split(":")[0]) : Integer.valueOf(id));
		} catch (NumberFormatException e) {
			return null;
		}

		if (!id.contains(":"))
			return new ItemStack(normalID);
		else
			return new ItemStack(Integer.valueOf(id.split(":")[0]), 1, Byte.valueOf(id.split(":")[1]));
	}

	public static int getNextToNine(int to) {
		int temp = to;
		while (true) {
			temp++;
			if (temp == 53)
				return temp;

			if (temp % 9 == 0)
				return temp;
		}
	}
	
	public static int randomInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
}
