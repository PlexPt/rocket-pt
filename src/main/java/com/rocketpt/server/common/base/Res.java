package com.rocketpt.server.common.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class Res {

    private int code;

    private String msg;

    @JsonProperty
    private Object data;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private ResPage page;


    public Res(Status status) {
        this.code = status.getCode();
        this.msg = status.getMsg();
        this.data = new Object();
    }

    public Res(Status status, Object data) {
        this.code = status.getCode();
        this.msg = status.getMsg();
        this.data = data;
    }

    public Res(Status status, Object data, ResPage page) {
        this.code = status.getCode();
        this.msg = status.getMsg();
        this.data = data;
        this.page = page;
    }

    public Res(Status status, String msg) {
        this.code = status.getCode();
        this.msg = msg;
        this.data = new Object();
    }

    public Res(Status status, int msgCode) {
        this.code = status.getCode();
        this.msg = I18nMessage.getMessage(String.valueOf(msgCode));
        this.data = new Object();
    }

    public Res(Status status, String msg, Object data) {
        this.code = status.getCode();
        this.msg = msg;
        this.data = data;
    }

    public Res(Status status, int msgCode, Object data) {
        this.code = status.getCode();
        this.msg = I18nMessage.getMessage(String.valueOf(msgCode));
        this.data = data;
    }

    public Res(Status status, String msg, Object data, ResPage page) {
        this.code = status.getCode();
        this.msg = msg;
        this.data = data;
        this.page = page;
    }

    public Res(Status status, int msgCode, Object data, ResPage page) {
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

    public static Res success() {
        return new Res(Status.SUCCESS);
    }

    public static Res ok() {
        return new Res(Status.SUCCESS);
    }

    public static Res ok(  Object data) {
        return new Res(Status.SUCCESS, data);
    }

    public static Res ok(Object data, ResPage page) {
        return new Res(Status.SUCCESS, data, page);
    }

    public static Res illegal() {
        return new Res(Status.BAD_REQUEST);
    }

    public static Res unauthorized() {
        return new Res(Status.UNAUTHORIZED);
    }

    public static Res forbidden() {
        return new Res(Status.FORBIDDEN);
    }

    public static Res notFound() {
        return new Res(Status.NOT_FOUND);
    }

    public static Res failure() {
        return new Res(Status.FAILURE);
    }

    public static Res failure(String msg) {
        return new Res(Status.FAILURE, msg);
    }

    public static Res error(String msg) {
        return new Res(Status.FAILURE, msg);
    }

    public static Res conflict() {
        return new Res(Status.CONFLICT);
    }

    public static Res build(Status status, Object data) {
        return new Res(status, data);
    }

    public static Res build(Status status, Object data, ResPage page) {
        return new Res(status, data, page);
    }

    public static Res build(Status status, String msg) {
        return new Res(status, msg);
    }

    public static Res build(Status status, int msgCode) {
        return new Res(status, msgCode);
    }

    public static Res build(Status status, String msg, Object data) {
        return new Res(status, msg, data);
    }

    public static Res build(Status status, int msgCode, Object data) {
        return new Res(status, msgCode, data);
    }

    public static Res build(Status status, String msg, Object data, ResPage page) {
        return new Res(status, msg, data, page);
    }

    public static Res build(Status status, int msgCode, Object data, ResPage page) {
        return new Res(status, msgCode, data, page);
    }
}
