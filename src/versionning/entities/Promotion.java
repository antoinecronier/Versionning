package versionning.entities;

import java.util.ArrayList;

public class Promotion {
	private int id;
	private String years;
	private String name;
	private String ou;
	private ArrayList<Student> students;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Student> getStudents() {
		return students;
	}

	public void setStudents(ArrayList<Student> users) {
		this.students = users;
	}

	public String getOu() {
		return ou;
	}

	public void setOu(String ou) {
		this.ou = ou;
	}

	public Promotion(int id, String years, String name) {
		super();
		this.id = id;
		this.years = years;
		this.name = name;
		this.students = new ArrayList<Student>();
	}

	public Promotion() {
		super();
	}
}
