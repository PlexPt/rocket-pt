package com.rocketpt.server.controller;

import com.rocketpt.server.common.Constants;
import com.rocketpt.server.common.base.I18nMessage;
import com.rocketpt.server.common.base.Result;
import com.rocketpt.server.dto.entity.InvitationEntity;
import com.rocketpt.server.dto.param.InviteParam;
import com.rocketpt.server.service.mail.MailService;
import com.rocketpt.server.service.sys.InvitationService;
import com.rocketpt.server.service.sys.UserService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author plexpt
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "邀请相关", description = Constants.FinishStatus.FINISHED)
@RequestMapping("/invite")
public class InviteController {


    private final UserService userService;
    private final MailService mailService;
    private final InvitationService invitationService;

    @Operation(summary = "发送邀请")
    @PostMapping("/send")
    public Result send(@RequestBody @Validated InviteParam param) {
        //发送邀请
        try{
            mailService.sendMail(param.getEmail(),
                    I18nMessage.getMessage("invitation_title"),
                    param.getContent(),
                    null);
        }catch (Exception e){
            return Result.failure();
        }
        return Result.ok();
    }

    @Operation(summary = "邀请码列表")
    @PostMapping("/list")
    public Result list() {
        List<InvitationEntity> invitations=invitationService.list();
        return Result.ok(invitations);
    }

}
