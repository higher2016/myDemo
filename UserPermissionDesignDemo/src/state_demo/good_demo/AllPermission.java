package state_demo.good_demo;

public enum AllPermission {
	Chat(1, "群聊权限"), ForbiddenSpeak(2, "禁言权限"), Show(3, "说话高亮及展示身份权限"), PrivateChatAll(4,
			"和任意人员私聊权限"), PrivateChatFeMale(5, "和女性人员私聊权限"), PrivateChatMale(6, "和男性人员私聊权限"), SpecialEffects(7,
					"发特效权限"),;
	public final int type;
	public final String desc;

	private AllPermission(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
}
