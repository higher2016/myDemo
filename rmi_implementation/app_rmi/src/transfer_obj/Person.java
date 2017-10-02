package transfer_obj;

public class Person implements java.io.Serializable {

	private static final long serialVersionUID = -9156411713118961501L;

	private String name;
	private int userId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Person(String name, int userId) {
		super();
		this.name = name;
		this.userId = userId;
	}

	public String toString() {
		return "Person [name=" + name + ", userId=" + userId + "]";
	}
}
