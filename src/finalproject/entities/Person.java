package finalproject.entities;


public class Person implements java.io.Serializable {

	private static final long serialVersionUID = 4190276780070819093L;
	private String firstName;
	private String lastName;
	private int age;
	private String city;
	private String id;
	private int sent;

	public Person(String firstName, String lastName, int age, String city, String id, int sent) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.city = city;
		this.id = id;
		this.sent = sent;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSent() {
		return sent;
	}

	public void setSent(int sent) {
		this.sent = sent;
	}

	// this is a person object that you will construct with data from the DB
	// table. The "sent" column is unnecessary. It's just a person with
	// a first name, last name, age, city, and ID.
	public static void main(String[] args) {

	}
}
