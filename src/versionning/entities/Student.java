package versionning.entities;

public class Student extends User{
	private int promotionId;

	public int getPromotion() {
		return promotionId;
	}

	public void setPromotion(int promotion) {
		this.promotionId = promotion;
	}

	public Student() {
		super();
	}

	public Student(int id, String firstname, String lastname, String login,
			String password, byte[] guid, int promotion) {
		super(id, firstname, lastname, login, password, guid);
		this.promotionId = promotion;
	}
}
