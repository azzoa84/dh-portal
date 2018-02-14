/**
	@file	UBSalesParamMap.java
	@date	2017-01-01
	@author	UBCare CRM R&D TF
	@brief	
*/
package potal.common.common;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public class PotalParamMap extends EgovMap implements Cloneable
{
	private static final long serialVersionUID = 3848851032730232522L;

	@Override
	public Object get(Object key)
	{
		if(super.containsKey(key))
		{
			Object obj = super.get(key) == null? "" : super.get(key);
			if(obj.getClass().getName().equals("java.sql.Date")) obj = obj.toString();
			return obj;
		}
		else
		{
			return "";
		}
	}
}