public class Authenticate{
	private String uid;
	private String pin;
	private int saldo = 200;
	private int pogingen = 3;
	private boolean blocked = false;
	
	public Authenticate(String uid_, String pin_){
		uid = uid_;
		pin = pin_;
	}
	
	public int getSaldo(){
		return saldo;
	}
	
	public int checkUID(String toCheck){
		if(uid.equals(toCheck)){
			if(blocked){
				return 2;
			} else {
				return 1;
			}
		} else {
			return 0;
		}
	}
	public boolean checkPIN(String toCheck){
		if(pin.equals(toCheck)){
			pogingen = 3;
			return true;
		} else {
			pogingen--;
			if(pogingen <= 0){
				blocked = true;
			}
			return false;
		}
	}
	public boolean setSaldo(int saldo_){
		if(saldo >= saldo_){
			saldo = saldo - saldo_;
			return true;
		} else {
			return false;
		}
	}
	
	public void setPin(String pin_){
		pin = pin_;
		System.out.println("PIN IS VERANDERD");
	}
	
	
}