package versionning.ldap;

import java.io.File;
import java.util.ArrayList;

import javajackson.json.manager.JsonManager;
import javajsoncrud.manager.CRUDManager;
import javaldap.ldap.entities.LdapItem;
import javaldap.ldap.manager.LdapConfiguration;
import javaldap.ldap.manager.LdapManager;
import saisi.SaisiManager;
import versionning.entities.Promotion;
import versionning.entities.Student;
import versionning.entities.Teacher;

public class LdapRetriever {

	private static final String STUDENT_FILE = "Student.json";
	private static final String PROMOTION_FILE = "Promotion.json";
	private static final String TEACHER_FILE = "Teacher.json";
	private static LdapRetriever INSTANCE = null;

	public static synchronized LdapRetriever getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new LdapRetriever();
		}
		return INSTANCE;
	}

	private LdapRetriever() {
		createBaseJSONFiles();
	}

	private void createBaseJSONFiles() {

		String school = SaisiManager.question("Which school you have to use?");

		File file = new File(CRUDManager.DB_PATH + this.STUDENT_FILE);
		if (!file.exists()) {

			JsonManager.getInstance().clear();
			JsonManager.getInstance().addItems(
					getAllStudentsWithPromotion(school));
			JsonManager.getInstance().sendToFile(this.STUDENT_FILE,
					CRUDManager.DB_PATH);
		}

		file = new File(CRUDManager.DB_PATH + this.PROMOTION_FILE);
		if (!file.exists()) {
			JsonManager.getInstance().clear();
			JsonManager.getInstance().addItems(getAllPromotions(school));
			JsonManager.getInstance().sendToFile(this.PROMOTION_FILE,
					CRUDManager.DB_PATH);
		}

		file = new File(CRUDManager.DB_PATH + this.TEACHER_FILE);
		if (!file.exists()) {
			JsonManager.getInstance().clear();
			JsonManager.getInstance().addItems(getTeachers(school));
			JsonManager.getInstance().sendToFile(this.TEACHER_FILE,
					CRUDManager.DB_PATH);
		}
	}

	public ArrayList<Promotion> getAllStudentsWithPromotion(String school) {
		ArrayList<Promotion> promos = this.getAllPromotions(school);
		for (Promotion promo : promos) {
			this.getStudentsByPromotion(promo);
		}
		return promos;
	}

	public ArrayList<Student> getStudentsByPromotion(Promotion promotion) {
		ArrayList<LdapItem> items = LdapManager.getInstance().request(
				promotion.getOu(), false);

		int i = 0;
		for (LdapItem user_ldapitem : items) {
			promotion.getStudents().add(
					new Student(i, user_ldapitem.getName(), user_ldapitem
							.getGivenName(), "", "", user_ldapitem
							.getObjectGUID(), promotion.getId()));
			i++;
		}

		return promotion.getStudents();
	}

	public ArrayList<Teacher> getTeachers(String school) {
		String requestBase = "OU=Formateurs_Externes,OU=Utilisateurs,OU=Formation,OU="
				+ school + ",OU=Sites";
		ArrayList<LdapItem> items = LdapManager.getInstance().request(
				requestBase, false);

		ArrayList<Teacher> teachers = new ArrayList<Teacher>();

		int i = 0;
		for (LdapItem ldapItem : items) {
			teachers.add(new Teacher(i, ldapItem.getName(), ldapItem
					.getGivenName(), "", "", ldapItem.getObjectGUID()));
			i++;
		}

		return teachers;
	}

	public ArrayList<Promotion> getAllPromotions(String school) {
		String requestBase = "OU=Eleves,OU=Utilisateurs,OU=Formation,OU="
				+ school + ",OU=Sites";
		ArrayList<LdapItem> items = LdapManager.getInstance().request(
				requestBase, false);

		ArrayList<Promotion> promotions = new ArrayList<Promotion>();

		int i = 0;
		for (LdapItem ldapItem : items) {
			String name = ldapItem.getName();
			String sub_request = "OU=" + ldapItem.getOu() + "," + requestBase;
			ArrayList<LdapItem> sub_items = LdapManager.getInstance().request(
					sub_request, false);
			for (LdapItem sub_ldapItem : sub_items) {
				String years = sub_ldapItem.getName();
				Promotion promo = new Promotion(i, years, name);
				promo.setOu("OU=" + sub_ldapItem.getName() + "," + sub_request);
				promotions.add(promo);
				i++;
			}
		}

		return promotions;
	}

	public ArrayList<Promotion> getPromotions(String school, String years,
			String promotion) {
		String requestBase = "OU=Eleves,OU=Utilisateurs,OU=Formation,OU="
				+ school + ",OU=Sites";
		ArrayList<LdapItem> items = LdapManager.getInstance().request(
				requestBase, false);

		ArrayList<Promotion> promotions = new ArrayList<Promotion>();

		if (promotion.equals("") || promotion == null) {
			for (LdapItem ldapItem : items) {
				String name = ldapItem.getName();
				String sub_request = "OU=" + ldapItem.getOu() + ","
						+ requestBase;
				ArrayList<LdapItem> sub_items = LdapManager.getInstance()
						.request(sub_request, false);

				if (years.equals("") || years == null) {
					for (LdapItem sub_ldapItem : sub_items) {
						Promotion promo = new Promotion(0, years, name);
						promo.setOu("OU=" + sub_ldapItem.getName() + ","
								+ sub_request);
						promotions.add(promo);
					}
				} else {
					for (LdapItem sub_ldapItem : sub_items) {
						if (sub_ldapItem.getName().contains(years)) {
							Promotion promo = new Promotion(0, years, name);
							promo.setOu("OU=" + sub_ldapItem.getName() + ","
									+ sub_request);
							promotions.add(promo);
						}
					}
				}
			}
		} else {
			for (LdapItem ldapItem : items) {
				if (ldapItem.getName().contains(promotion)) {
					String name = ldapItem.getName();
					String sub_request = "OU=" + ldapItem.getOu() + ","
							+ requestBase;
					ArrayList<LdapItem> sub_items = LdapManager.getInstance()
							.request(sub_request, false);

					if (years.equals("") || years == null) {
						for (LdapItem sub_ldapItem : sub_items) {
							Promotion promo = new Promotion(0, years, name);
							promo.setOu("OU=" + sub_ldapItem.getName() + ","
									+ sub_request);
							promotions.add(promo);
						}
					} else {
						for (LdapItem sub_ldapItem : sub_items) {
							if (sub_ldapItem.getName().contains(years)) {
								Promotion promo = new Promotion(0, years, name);
								promo.setOu("OU=" + sub_ldapItem.getName()
										+ "," + sub_request);
								promotions.add(promo);
							}
						}
					}
				}
			}
		}

		return promotions;
	}
}