package zuulutils;

// General library of helpful methods used within the game
public class ZuulTools {
	public static int getIndex(String[] arr, String toIndex) {
		for (int i = 0, len = arr.length; i < len; i++) {
			if (arr[i].equals(toIndex)) {
				return i;
			}
		}
		return -1;
	}

	public static String capitalize(String toCapitalize) {
		return toCapitalize.substring(0, 1).toUpperCase()
				+ toCapitalize.substring(1, toCapitalize.length()).toLowerCase();
	}

}
