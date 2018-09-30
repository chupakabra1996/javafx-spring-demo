package ru.kpfu.itis.exception;

import java.io.IOException;

public class JsonParseException extends IOException {

    public JsonParseException() {
        super();
    }

    public JsonParseException(String message) {
        super(message);
    }

    public JsonParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
