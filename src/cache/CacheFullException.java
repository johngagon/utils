package cache;

public class CacheFullException extends Exception {


	private static final long serialVersionUID = 1L;

	public CacheFullException() {
		super();
	}

	public CacheFullException(String message) {
		super(message);
	}

	public CacheFullException(Throwable cause) {
		super(cause);
	}

	public CacheFullException(String message, Throwable cause) {
		super(message, cause);
	}

	public CacheFullException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
