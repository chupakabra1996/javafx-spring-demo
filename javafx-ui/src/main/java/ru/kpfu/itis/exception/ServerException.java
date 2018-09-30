package ru.kpfu.itis.exception;

public class ServerException extends Exception {

    private int statusCode = 200;

    public ServerException() {
        super();
    }

    public ServerException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public ServerException(String message, Throwable cause, int statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

}
