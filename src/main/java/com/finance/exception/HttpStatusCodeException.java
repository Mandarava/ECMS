package com.finance.exception;

public class HttpStatusCodeException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int statusCode;
    private String reson;

    public HttpStatusCodeException(int statusCode) {
        super();
        this.statusCode = statusCode;

    }

    public HttpStatusCodeException(int statusCode, String reson) {
        super();
        this.statusCode = statusCode;
        this.reson = reson;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getReson() {
        return reson;
    }

    public void setReson(String reson) {
        this.reson = reson;
    }

}
