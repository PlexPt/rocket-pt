package com.rocketpt.server.common.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 返回值实体类
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Res<T> {

    private int code;

    private String msg;

    @JsonProperty
    private T data;

    public Res(Status status) {
        this.code = status.getCode();
        this.msg = status.getMsg();
        this.data = null;
    }

    public Res(Status status, T data) {
        this.code = status.getCode();
        this.msg = status.getMsg();
        this.data = data;
    }

    public Res(Status status, String msg) {
        this.code = status.getCode();
        this.msg = msg;
        this.data = null;
    }

    public Res(Status status, int msgCode) {
        this.code = status.getCode();
        this.msg = I18nMessage.getMessage(String.valueOf(msgCode));
        this.data = null;
    }

    public Res(Status status, String msg, T data) {
        this.code = status.getCode();
        this.msg = msg;
        this.data = data;
    }

    public Res(Status status, int msgCode, T data) {
        this.code = status.getCode();
        this.msg = I18nMessage.getMessage(String.valueOf(msgCode));
        this.data = data;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.code == Status.SUCCESS.getCode();
    }

    @JsonIgnore
    public boolean nonSuccess() {
        return this.code != Status.SUCCESS.getCode();
    }

    public static <T> Res<T> success() {
        return new Res<T>(Status.SUCCESS);
    }

    public static <T> Res<T> ok() {
        return new Res<T>(Status.SUCCESS);
    }

    public static <T> Res<T> ok(T data) {
        return new Res<T>(Status.SUCCESS, data);
    }

    public static <T> Res<T> illegal() {
        return new Res<T>(Status.BAD_REQUEST);
    }

    public static <T> Res<T> unauthorized() {
        return new Res<T>(Status.UNAUTHORIZED);
    }

    public static <T> Res<T> forbidden() {
        return new Res<T>(Status.FORBIDDEN);
    }

    public static <T> Res<T> notFound() {
        return new Res<T>(Status.NOT_FOUND);
    }

    public static <T> Res<T> failure() {
        return new Res<T>(Status.FAILURE);
    }

    public static <T> Res<T> failure(String msg) {
        return new Res<T>(Status.FAILURE, msg);
    }

    public static <T> Res<T> error(String msg) {
        return new Res<T>(Status.FAILURE, msg);
    }

    public static <T> Res<T> conflict() {
        return new Res<T>(Status.CONFLICT);
    }

    public static <T> Res<T> build(Status status, T data) {
        return new Res<T>(status, data);
    }

    public static <T> Res<T> build(Status status, String msg) {
        return new Res<T>(status, msg);
    }

    public static <T> Res<T> build(Status status, int msgCode) {
        return new Res<T>(status, msgCode);
    }

    public static <T> Res<T> build(Status status, String msg, T data) {
        return new Res<T>(status, msg, data);
    }

    public static <T> Res<T> build(Status status, int msgCode, T data) {
        return new Res<T>(status, msgCode, data);
    }
}
