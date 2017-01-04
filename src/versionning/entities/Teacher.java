package versionning.entities;

public class Teacher extends User{

	public Teacher() {
		super();
	}

	public Teacher(int id, String firstname, String lastname, String login,
			String password, byte[] guid) {
		super(id, firstname, lastname, login, password, guid);
	}

}
