package com.likontrotech.ejb;

class SumAsWords {
	public static class TriadeInfo {
		public String text;
		public boolean one;
	}

	public static synchronized String getSumAsWors(float val) {

		String sums = Float.toString(val);

		String cents = sums.substring(sums.indexOf('.') + 1, sums.length());
		cents = fillZeros2(cents, 2);

		String sums1 = getIntValue(sums);

		String loPart = "";
		String hiPart = "";
		if (3 < sums1.length())
			loPart = sums1.substring(sums1.length() - 3, sums1.length());

		else {
			loPart = sums1;
			loPart = fillZeros(loPart, 3);
		}

		TriadeInfo f1 = getDigits(loPart);

		// //////////////////////////////////////////

		TriadeInfo f2 = null;
		if (3 < sums1.length()) {
			hiPart = sums1.substring(0, sums1.length() - 3);
			hiPart = fillZeros(hiPart, 3);
			f2 = getDigits(hiPart);
		}

		String rez = (null != f2 ? (f2.text + (f2.one ? " tūkstantis "
				: "tūkstančiai ")) : "") + f1.text + "Lt " + cents + " ct.";
		return rez;
	}

	private static String getIntValue(String sums) {
		return sums.substring(0, sums.indexOf('.'));
	}

	public static String fillZeros(String val, int base) {
		while (val.length() < base)
			val = "0" + val;
		return val;
	}

	public static String fillZeros2(String val, int base) {
		while (val.length() < base)
			val += "0";
		return val;
	}
	
	private static TriadeInfo getDigits(String triada) {

		TriadeInfo ggg = new TriadeInfo();
		String valHundredString = triada.substring(0, 1);
		int valHundred = Integer.parseInt(valHundredString);

		String hundreds = "";
		switch (valHundred) {
		case 0:
			break;
		case 1:
			hundreds = "vienas šimtas ";
			break;
		case 2:
			hundreds = "du šimtai ";
			break;
		case 3:
			hundreds = "trys šimtai ";
			break;
		case 4:
			hundreds = "keturi šimtai ";
			break;
		case 5:
			hundreds = "penki šimtai ";
			break;
		case 6:
			hundreds = "šeši šimtai ";
			break;
		case 7:
			hundreds = "septyni šimtai ";
			break;
		case 8:
			hundreds = "aštuoni šimtai ";
			break;
		case 9:
			hundreds = "devyni šimtai ";
			break;
		}

		String decimalsString = triada.substring(1, 3);
		int valDecimals = Integer.parseInt(decimalsString);

		String decimals = "";
		if (20 > valDecimals && 10 < valDecimals) {
			switch (valDecimals) {
			case 11:
				decimals = "venuolika ";
				break;
			case 12:
				decimals = "dvylika ";
				break;
			case 13:
				decimals = "trylika ";
				break;
			case 14:
				decimals = "keturiolika ";
				break;
			case 15:
				decimals = "penkiolika ";
				break;
			case 16:
				decimals = "šešiolika ";
				break;
			case 17:
				decimals = "septiniolika ";
				break;
			case 18:
				decimals = "aštuoniolika ";
				break;
			case 19:
				decimals = "devyniolika ";
				break;
			}

		} else {
			switch (valDecimals / 10) {
			case 1:
				decimals = "dešimt ";
				break;
			case 2:
				decimals = "dvidešimt ";
				break;
			case 3:
				decimals = "trisdešimt ";
				break;
			case 4:
				decimals = "keturiasdešimt ";
				break;
			case 5:
				decimals = "penkiasdešimt ";
				break;
			case 6:
				decimals = "šešiasdešimt ";
				break;
			case 7:
				decimals = "septyniasdešimt ";
				break;
			case 8:
				decimals = "astuoniasdešimt ";
				break;
			case 9:
				decimals = "devyniasdešimt ";
				break;
			}
			ggg.one = false;
			switch (valDecimals % 10) {
			case 0:
				break;
			case 1:
				decimals += "vienas ";
				ggg.one = true;
				break;
			case 2:
				decimals += "du ";
				break;
			case 3:
				decimals += "trys ";
				break;
			case 4:
				decimals += "keturi ";
				break;
			case 5:
				decimals += "penki ";
				break;
			case 6:
				decimals += "šeši ";
				break;
			case 7:
				decimals += "septyni ";
				break;
			case 8:
				decimals += "aštuoni ";
				break;
			case 9:
				decimals += "devyni ";
				break;
			}

		}
		ggg.text = hundreds + decimals;

		return ggg;
	}
}
