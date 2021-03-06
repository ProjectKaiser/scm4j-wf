package org.scm4j.releaser.exceptions;

public class EDelayingDelayed extends EReleaserException {

	private static final long serialVersionUID = 1L;
	private final String url;

	public EDelayingDelayed(String url) {
		super("Delayed tag already exists for product url " + url + "\r\nTag it or clean Delayed Tags file manually");
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
