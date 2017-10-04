package state_demo.good_demo;

public class Person {
	private int id;
	private String name;
	private AllMember member;

	public Person(int id, int memberType, String name) {
		super();
		this.id = id;
		this.name = name;
		this.member = AllMember.getMember(memberType);
	}

	public boolean canDo(AllPermission permission) {
		return member.canDo(permission);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMemberType() {
		return member.tpye;
	}

	public void changeMemberType(AllMember member) {
		this.member = member;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
