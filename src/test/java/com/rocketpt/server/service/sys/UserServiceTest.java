package com.rocketpt.server.service.sys;

import com.rocketpt.server.dto.param.RegisterCodeParam;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @MockBean
    private CaptchaService captchaService;
    @MockBean
    private InvitationService invitationService;

    @Test
    @Transactional
    void sendRegCode() {
        RegisterCodeParam registerCodeParam=new RegisterCodeParam();
        registerCodeParam.setEmail("someone@example.com");
        BDDMockito.given(this.captchaService.verifyCaptcha(
                registerCodeParam.getUuid(),
                registerCodeParam.getCode()))
                .willReturn(true);
        BDDMockito.given(this.invitationService.check(
                registerCodeParam.getEmail(),
                registerCodeParam.getInvitationCode()))
                .willReturn(true);
        userService.sendRegCode(registerCodeParam);
    }
}