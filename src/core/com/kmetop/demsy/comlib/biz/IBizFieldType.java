package com.kmetop.demsy.comlib.biz;

import com.kmetop.demsy.comlib.entity.IBizComponent;

public interface IBizFieldType extends IBizComponent {
	public boolean isBoolean();

	public boolean isV1Dic();

	public boolean isV1GEO();

	public boolean isNumber();

	public boolean isInteger();

	public boolean isRichText();

	public boolean isDate();

	public boolean isString();

	public boolean isUpload();

	public boolean isSystem();

	public boolean isManyToMany();

	public String getType();

	public String getUiTemplate();

}
