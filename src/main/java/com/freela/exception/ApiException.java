package com.freela.exception;

public class ApiException extends RuntimeException {
	private Source source;

	public ApiException() {
	}

	public ApiException(String message) {
		super(message);
	}

	public ApiException(String message, Source source) {
		super(message);
		this.source = source;
	}

	public ApiException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApiException(String message, Throwable cause, Source source) {
		super(message, cause);
		this.source = source;
	}

	public ApiException(Throwable cause) {
		super(cause);
	}

	public ApiException(Throwable cause, Source source) {
		super(cause);
		this.source = source;
	}

	public ApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Source source) {
		super(message, cause, enableSuppression, writableStackTrace);
		this.source = source;
	}

	public Source getSource() {
		return source;
	}

	public void setSource(Source source) {
		this.source = source;
	}

	public enum Location {
		BODY,
		HEADER,
		PATH,
		QUERY
	}

	public static class Source {
		private Location location;
		private String resource;
		private String property;
		private String value;
		private String expected;

		public Source() { }

		public Source(Location location, String property) {
			this.location = location;
			this.property = property;
		}

		public Source(Location location, String property, String value, String expected) {
			this.location = location;
			this.property = property;
			this.value = value;
			this.expected = expected;
		}

		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}

		public String getResource() {
			return resource;
		}

		public void setResource(String resource) {
			this.resource = resource;
		}

		public String getProperty() {
			return property;
		}

		public void setProperty(String property) {
			this.property = property;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getExpected() {
			return expected;
		}

		public void setExpected(String expected) {
			this.expected = expected;
		}

		@Override
		public String toString() {
			return "Source{" +
					"location=" + location +
					", resource='" + resource + '\'' +
					", property='" + property + '\'' +
					", value='" + value + '\'' +
					", expected='" + expected + '\'' +
					'}';
		}
	}
}
