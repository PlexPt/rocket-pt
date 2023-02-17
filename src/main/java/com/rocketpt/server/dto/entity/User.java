package com.rocketpt.server.dto.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.RequiredArgsConstructor;


/**
 * 用户
 *
 * @author plexpt
 */
@Data
@TableName("user")
public class User extends EntityBase {

    //    @Column(nullable = false, unique = true)
    private String username;

    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String fullName;

    private String avatar;

    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private Gender gender;

    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private State state;

    @TableField(insertStrategy = FieldStrategy.NOT_EMPTY, updateStrategy = FieldStrategy.NOT_EMPTY)
    private String email;

    private LocalDateTime createdTime;

    private Long organizationId;

    private String checkCode;

    //    @PrePersist
    protected void onCreate() {
        this.createdTime = LocalDateTime.now();
    }

//    /**
//     * 获取用户权限列表
//     *
//     * @return
//     */
//    public Set<String> findPermissions() {
//        return roles.stream()
//                .map(Role::getResources)
//                .flatMap(Collection::stream)
//                .map(Resource::getPermission)
//                .collect(Collectors.toSet());
//    }


    @RequiredArgsConstructor
    public enum Gender {
        MALE(0),
        FEMALE(1),
        OTHER(2);

        @EnumValue
        private final int code;
    }

    @RequiredArgsConstructor
    public enum State {
        NORMAL(0),
        LOCKED(1),
        INACTIVATED(2)

        ;
        @EnumValue
        private final int code;
    }

    public boolean userLocked() {
        return this.state == State.LOCKED;
    }


}
