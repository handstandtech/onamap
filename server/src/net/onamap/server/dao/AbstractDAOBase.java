package net.onamap.server.dao;

import com.google.appengine.api.datastore.TransactionOptions;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyOpts;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.util.DAOBase;

public abstract class AbstractDAOBase extends DAOBase {

	private ObjectifyOpts transOpts() {
		return new ObjectifyOpts()
				.setTransactionOptions(TransactionOptions.Builder
						.withDefaults());
	}

	protected Objectify ofyTrans() {
		return ObjectifyService.factory().begin(transOpts());
	}

}
