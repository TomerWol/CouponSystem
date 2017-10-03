package core.exception;
/**
 * This class extends {@link Exception}.</br>
 * With this class we warp exceptions and throws a new one with a message depends on the cause.
 * */
public class CouponSystemException extends Exception {

	private static final long serialVersionUID = 1L;

	public CouponSystemException() {
		super();
	}

	public CouponSystemException(String message) {
		super(message);
	}

	public CouponSystemException(Throwable cause) {
		super(cause);
	}

	public CouponSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public CouponSystemException(String message, Throwable cause, boolean enableSuppression,
			boolean writabkeStackTrace) {
		super(message, cause, enableSuppression, writabkeStackTrace);
	}
}
