package ru.sport.cardiomood.core.exceptions;

import ru.sport.cardiomood.core.constants.ResponseConstants;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class SportException extends Exception {

    public SportException() {
    }
    private Integer errorCode;

    public SportException(String msg) {
        super(msg);
        this.errorCode = ResponseConstants.NORMAL_ERROR_CODE;
    }

    public SportException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
