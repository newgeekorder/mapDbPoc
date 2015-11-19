package com.umg.storedprocedure.mule;

import org.mule.api.MuleEventContext;
import org.mule.api.MuleMessage;
import org.mule.api.lifecycle.Callable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.umg.db2mapdb.SPMessage;
import com.umg.storedprocedure.StoredProcedureMapper;

/**
 * Wrapper around {@link StoredProcedureMapper} which is coupled to Mule (implements Callable)
 */
public class StoredProcedureComponent implements Callable {

	private static final Logger LOG = LoggerFactory.getLogger(StoredProcedureComponent.class);

	protected StoredProcedureMapper mapper;

	public StoredProcedureMapper getMapper() {
		return mapper;
	}

	public void setMapper(StoredProcedureMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		MuleMessage message = eventContext.getMessage();
		SPMessage spMsg;
		try {
			spMsg = (SPMessage) message.getPayload();
		} catch (ClassCastException e) {
			LOG.error("I only know how to handle com.umg.storedprocedure.SPMessage payloads");
			throw e;
		}
		final String spName = spMsg.storedProcedureName;
		LOG.info("Stored procedure name: {}. About to process SPMessage: {}", spName, spMsg.toString());

		if (StoredProcedureMapper.COPA3_DERIVE_LABEL.equals(spName)) {
			String result = mapper.copa3DeriveLabel(spMsg.upc, spMsg.isrc, spMsg.sellingCountry, spMsg.globalIndicator, spMsg.interfaceType,
					spMsg.companyCodeIn, spMsg.globalRepertoire);
			LOG.info("Result of COPA3_DERIVE_LABEL stored procedure port: {}", result);
			return result;
		}

		LOG.info("Could not process SPMessage: {}", spMsg.toString());
		return "";
	}

}
