package ru.sport.cardiomood.json.entity;

import ru.sport.cardiomood.core.constants.ResponseConstants;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class JsonResponse<T> {

    private Integer responseCode;
    private JsonError error;
    private T data;

    public JsonResponse(Integer responseCode, JsonError error, T object) {
        this.responseCode = responseCode;
        this.error = error;
        this.data = object;
    }

    public JsonResponse(T data) {
        this.data = data;
        this.error = null;
        this.responseCode = ResponseConstants.OK;
    }

    public JsonResponse() {
    }

    public JsonError getError() {
        return error;
    }

    public void setError(JsonError error) {
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }
}
