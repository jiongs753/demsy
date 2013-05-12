package soom.entity;

import java.util.Map;

/**
 * <b>动态实体:</b>
 * 
 * @author yongshan.ji
 */
public interface IDataOptions {

	
	Object get(String optionName);

	void set(String optionName, Object optionValue);

	Map getDataOptions();

	boolean is(byte optionIndex);
}
