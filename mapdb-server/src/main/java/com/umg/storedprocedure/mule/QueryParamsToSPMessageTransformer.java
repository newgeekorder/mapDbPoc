package com.umg.storedprocedure.mule;

import java.util.Map;

import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import com.umg.db2mapdb.SPMessage;

public class QueryParamsToSPMessageTransformer extends AbstractMessageTransformer {

	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
		SPMessage spMsg = new SPMessage();
		Map<String, String> httpQueryParams = message.getInboundProperty("http.query.params");
		spMsg.storedProcedureName = httpQueryParams.get("storedProcedureName");
		spMsg.upc = httpQueryParams.get("upc");
		spMsg.isrc = httpQueryParams.get("isrc");
		spMsg.sellingCountry = httpQueryParams.get("sellingCountry");
		spMsg.globalIndicator = httpQueryParams.get("globalIndicator");
		spMsg.interfaceType = httpQueryParams.get("interfaceType");
		spMsg.companyCodeIn = httpQueryParams.get("companyCodeIn");
		spMsg.globalRepertoire = httpQueryParams.get("globalRepertoire");

		return spMsg;
	}

}
