public class AccountData {
	private String customer_id; // Dan weet de client met wie hij bezig is
	private String pas_id; // Lekker handig ofzo
	private String name; // voor client functionaliteit
	private String surName; // voor client functionaliteit
	private int rekeningAmount; // Hiermee kiest de client een rekening
	private String[] rekeningen;// Hiermee kiest de client een rekening

	public AccountData(String customer, String pas, String nm, String srnm, int rekeningamt, String[] rekeningArray) {
		customer_id = customer;
		pas_id = pas;
		name = nm;
		surName = srnm;
		rekeningAmount = rekeningamt;
		rekeningen = rekeningArray;
	}

	public String getCustomerID() {
		return customer_id;
	}

	public String getCardID() {
		return pas_id;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surName;
	}

	public int getRekeningAmount() {
		return rekeningAmount;
	}

	public String getRekening(int i) {
		return rekeningen[i];
	}

	public String toString() {
		return customer_id + ";" + pas_id + ";" + name + ";" + surName + ";" + rekeningAmount + ";"
				+ String.join(",", rekeningen);
	}

	public static AccountData fromString(String ac) {

		String[] columns = ac.split(";");
		String[] rekeningen = columns[5].split(",");

		return new AccountData(columns[0], columns[1], columns[2], columns[3], Integer.parseInt(columns[4]),
				rekeningen);

	}

	public String[] getRekeningen() {
		return rekeningen;
	}
}