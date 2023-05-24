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
public class Result<T> {

    private int code;

    private String msg;

    @JsonProperty
    private T data;

    private ResPage page;


    public Result(Status status) {
        this.code = status.getCode();
        this.msg = status.getMsg();
        this.data = null;
    }

    public Result(Status status, T data) {
        this.code = status.getCode();
        this.msg = status.getMsg();
        this.data = data;
    }

    public Result(Status status, String msg) {
        this.code = status.getCode();
        this.msg = msg;
        this.data = null;
    }

    public Result(Status status, int msgCode) {
        this.code = status.getCode();
        this.msg = I18nMessage.getMessage(String.valueOf(msgCode));
        this.data = null;
    }

    public Result(Status status, String msg, T data) {
        this.code = status.getCode();
        this.msg = msg;
        this.data = data;
    }

    public Result(Status status, int msgCode, T data) {
        this.code = status.getCode();
        this.msg = I18nMessage.getMessage(String.valueOf(msgCode));
        this.data = data;
    }

    public Result(Status status, T data, ResPage page) {
        this.code = status.getCode();
        this.msg = status.getMsg();
        this.data = data;
        this.page = page;
    }

    public Result(Status status, String msg, T data, ResPage page) {
        this.code = status.getCode();
        this.msg = msg;
        this.data = data;
        this.page = page;
    }

    public Result(Status status, int msgCode, T data, ResPage page) {
        this.code = status.getCode();
        this.msg = I18nMessage.getMessage(String.valueOf(msgCode));
        this.data = data;
        this.page = page;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return this.code == Status.SUCCESS.getCode();
    }

    @JsonIgnore
    public boolean nonSuccess() {
        return this.code != Status.SUCCESS.getCode();
    }

    public static <T> Result<T> success() {
        return new Result<T>(Status.SUCCESS);
    }

    public static <T> Result<T> ok() {
        return new Result<T>(Status.SUCCESS);
    }

    public static <T> Result<T> ok(T data) {
        return new Result<T>(Status.SUCCESS, data);
    }

    public static <T> Result<T> illegal() {
        return new Result<T>(Status.BAD_REQUEST);
    }

    public static <T> Result<T> unauthorized() {
        return new Result<T>(Status.UNAUTHORIZED);
    }

    public static <T> Result<T> forbidden() {
        return new Result<T>(Status.FORBIDDEN);
    }

    public static <T> Result<T> notFound() {
        return new Result<T>(Status.NOT_FOUND);
    }

    public static <T> Result<T> failure() {
        return new Result<T>(Status.FAILURE);
    }

    public static <T> Result<T> failure(String msg) {
        return new Result<T>(Status.FAILURE, msg);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<T>(Status.FAILURE, msg);
    }

    public static <T> Result<T> conflict() {
        return new Result<T>(Status.CONFLICT);
    }

    public static <T> Result<T> build(Status status, T data) {
        return new Result<T>(status, data);
    }

    public static <T> Result<T> build(Status status, String msg) {
        return new Result<T>(status, msg);
    }

    public static <T> Result<T> build(Status status, int msgCode) {
        return new Result<T>(status, msgCode);
    }

    public static <T> Result<T> build(Status status, String msg, T data) {
        return new Result<T>(status, msg, data);
    }

    public static <T> Result<T> build(Status status, int msgCode, T data) {
        return new Result<T>(status, msgCode, data);
    }

    public static Result ok(Object data, ResPage page) {
        return new Result(Status.SUCCESS, data, page);
    }

    public static Result build(Status status, Object data, ResPage page) {
        return new Result(status, data, page);
    }

    public static Result build(Status status, String msg, Object data, ResPage page) {
        return new Result(status, msg, data, page);
    }

    public static Result build(Status status, int msgCode, Object data, ResPage page) {
        return new Result(status, msgCode, data, page);
    }

}
