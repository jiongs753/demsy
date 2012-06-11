package com.kmetop.demsy.modules.webmail.enums;

public enum SmtpHostChoiceEnum {
	FREE("free"), DOMAIN("domain"), NONE("none");

	private static SmtpHostChoiceEnum[] allValues = SmtpHostChoiceEnum.values();

	private String id = null;

	private SmtpHostChoiceEnum(String id) {
		this.id = id;
	}

	public String getId() {

		return id;
	}

	public static SmtpHostChoiceEnum byId(String id) {
		for (int ii = 0; ii < allValues.length; ii++) {
			if (allValues[ii].id.equals(id)) {
				return (allValues[ii]);
			}
		}

		return (null);
	}

}
