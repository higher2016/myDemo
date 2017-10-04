package state_demo.good_demo;

import java.util.HashMap;
import java.util.Map;

public enum AllMember {
	Admin(1, "管理员"), MaleGod(2, "男神"), FeMaleGod(3, "女神"), CuteGuy(4, "萌宠"), Normal(5, "普通人");
	public final int tpye;
	public final String name;
	private Map<Integer, AllPermission> permissions = new HashMap<Integer, AllPermission>();

	/**
	 * 将每种身份的人员所拥有的权限初始化的时候就赋予进去（也就是说，每一种身份的成员本身就有这样的属性，而不是通过外界判断的）
	 */
	static {
		for (AllPermission permission : AllPermission.values()) {
			Admin.addPerissions(permission);
		}
		MaleGod.addPerissions(AllPermission.Chat);
		MaleGod.addPerissions(AllPermission.Show);
		MaleGod.addPerissions(AllPermission.PrivateChatFeMale);

		FeMaleGod.addPerissions(AllPermission.Chat);
		FeMaleGod.addPerissions(AllPermission.Show);
		FeMaleGod.addPerissions(AllPermission.PrivateChatMale);

		CuteGuy.addPerissions(AllPermission.Chat);
		CuteGuy.addPerissions(AllPermission.Show);
		CuteGuy.addPerissions(AllPermission.SpecialEffects);

		Normal.addPerissions(AllPermission.Chat);
	}
	
	public static AllMember getMember(int type){
		for(AllMember member:AllMember.values()){
			if(member.tpye == type){
				return member;
			}
		}
		return null;
	} 

	public boolean canDo(AllPermission permission) {
		return permissions.containsKey(permission.type);
	}

	private AllMember(int type, String name) {
		this.tpye = type;
		this.name = name;
	}

	protected void addPerissions(AllPermission permission) {
		permissions.put(permission.type, permission);
	}
}
