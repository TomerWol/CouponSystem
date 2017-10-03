package core;

import core.exception.CouponSystemException;
import core.facade.AdminFacade;
import core.facade.CompanyFacade;
import core.facade.CouponClientFacade;
import core.facade.CustomerFacade;
import core.facade.clientType;
import core.thread.DailyCouponSystemExpirationTask;
import db.connection_pool.ConnectionPool;

/**
 * A <b>singleton</b> class that function as the top layer of this
 * application.</br>
 * In here you got all the methods for starting this application & shutting it
 * down and the Login that each user uses to login and get a Facade.</br>
 * See also : {@link AdminFacade}, {@link CompanyFacade}, {@link CustomerFacade}
 */
public class CouponSystem {
	// Attributes
	private static CouponSystem instance = null;
	private static ConnectionPool con = null;
	private DailyCouponSystemExpirationTask task = null;
	private Thread dailyTask = null;

	// Private CTOR
	private CouponSystem() {
		con = ConnectionPool.getInstance();
		task = new DailyCouponSystemExpirationTask();
		dailyTask = new Thread(task);
		dailyTask.start();
	}

	public static CouponSystem getInstance() {
		if (instance == null) {
			instance = new CouponSystem();
		}
		return instance;
	}

	/**
	 * With this methods the user login.</br>
	 * If the user name & password are correct you will get a Facade depends
	 * your {@link ClientType}
	 **/
	public CouponClientFacade login(String name, String password, clientType type) throws CouponSystemException {
		switch (type) {
		case CUSTOMER:
			return CustomerFacade.login(name, password);
		case COMPANY:
			return CompanyFacade.login(name, password);
		case ADMIN:
			return AdminFacade.login(name, password);
		default:
			throw new CouponSystemException("no client type was given");
		}
	}

	/**
	 * Shuts down all this application connection & Daily Task.
	 */
	public void shutDown() throws InterruptedException {
		dailyTask.interrupt();
		dailyTask.join();
		con.closeAllConnections();
		System.out.println("Successfully shutdown");
	}
}
