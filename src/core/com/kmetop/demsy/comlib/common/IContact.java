package com.kmetop.demsy.comlib.common;

public interface IContact {
	public static final String SYS_CODE = "_Contact";

	String getProvince();

	String getCity();

	String getArea();

	String getStreet();

	String getPostcode();

	String getPerson();

	String getTelcode();

	void setProvince(String province);

	void setCity(String city);

	void setArea(String area);

	void setStreet(String street);

	void setPostcode(String postcode);

	void setPerson(String person);

	void setTelcode(String telcode);

	void setSoftID(Long id);

}
