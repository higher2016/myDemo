package state_demo.good_demo;

public class TestMain {
	public static void main(String[] args) {
		Person admin = new Person(1, AllMember.Admin.tpye, "admin");
		Person maleGod = new Person(2, AllMember.MaleGod.tpye, "maleGod");
		Person feMaleGod = new Person(3, AllMember.FeMaleGod.tpye, "feMaleGod");
		Person cuteGuy = new Person(4, AllMember.CuteGuy.tpye, "cuteGuy");
		Person normal = new Person(5, AllMember.Normal.tpye, "normal");
		
		System.out.println("Admin permission check: ");
		for(AllPermission permission : AllPermission.values()){
			System.out.println("Admin in " + permission.desc + " is " + admin.canDo(permission));
		}
		System.out.println("------------------------- ");

		System.out.println("MaleGod permission check: ");
		for(AllPermission permission : AllPermission.values()){
			System.out.println("MaleGod in " + permission.desc + " is " + maleGod.canDo(permission));
		}
		System.out.println("------------------------- ");
		
		System.out.println("FeMaleGod permission check: ");
		for(AllPermission permission : AllPermission.values()){
			System.out.println("FeMaleGod in " + permission.desc + " is " + feMaleGod.canDo(permission));
		}
		System.out.println("------------------------- ");
		
		System.out.println("CuteGuy permission check: ");
		for(AllPermission permission : AllPermission.values()){
			System.out.println("CuteGuy in " + permission.desc + " is " + cuteGuy.canDo(permission));
		}
		System.out.println("------------------------- ");
		
		System.out.println("Normal permission check: ");
		for(AllPermission permission : AllPermission.values()){
			System.out.println("Normal in " + permission.desc + " is " + normal.canDo(permission));
		}
		System.out.println("------------------------- ");
	}
}
