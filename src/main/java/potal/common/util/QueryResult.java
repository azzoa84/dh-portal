/**
	@file	QueryResult.java
	@date	2017-01-01
	@author	UBCare CRM R&D TF
	@brief	
*/
package potal.common.util;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import potal.common.common.CommandMap;
import potal.common.common.PotalParamMap;
import potal.common.service.ComnConst;

public class QueryResult extends EgovMap
{
	private static final long serialVersionUID = 5073470731816469287L;

	public QueryResult(PotalParamMap statusCode, List<PotalParamMap> dataList)
	{
		super();
		this.put(ComnConst.ARGS_RETURN_CODE, statusCode.get(ComnConst.ARGS_RETURN_CODE));
		this.put(ComnConst.ARGS_RETURN_STRING, statusCode.get(ComnConst.ARGS_RETURN_STRING));
		this.put(ComnConst.RESULT_LIST, dataList);
	}
}