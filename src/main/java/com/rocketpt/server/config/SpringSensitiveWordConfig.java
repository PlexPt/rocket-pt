//package com.rocketpt.server.config;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
//
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import jakarta.validation.constraints.NotNull;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@Configuration
//@Slf4j
//@RequiredArgsConstructor
//public class SpringSensitiveWordConfig {
//
//    final SensitiveWordService sensitiveWordService;
//
//
//    /**
//     * 初始化引导类
//     *
//     * @return 初始化引导类
//     * @since 1.0.0
//     */
//    @Bean
//    @Primary
//    public SensitiveWordBs sensitiveWordBs() {
//        SensitiveWordBs sensitiveWordBs = SensitiveWordBs.newInstance()
//                .wordAllow(() -> Arrays.asList(""))
//                .wordDeny(() -> getDenyList())
//                // 各种其他配置
//                .ignoreCase(true)
//                .ignoreWidth(true)
//                .ignoreNumStyle(true)
//                .ignoreChineseStyle(true)
//                .ignoreEnglishStyle(true)
//                .ignoreRepeat(false)
//                .enableNumCheck(false)
//                .enableEmailCheck(false)
//                .enableUrlCheck(false)
////                .numCheckLen(8)
//                .init();
////        run(sensitiveWordBs);
//        return sensitiveWordBs;
//    }
//
//
//    @NotNull
//    private List<String> getDenyList() {
//
//        List<SensitiveWordEntity> list =
//                sensitiveWordService.list(new QueryWrapper<SensitiveWordEntity>()
//                        .lambda().select(SensitiveWordEntity::getWord)
//                        .eq(SensitiveWordEntity::getEnable, 1)
//                );
//
//        if (list == null) {
//            return new ArrayList<>();
//        }
//
//        List<String> results = list.stream()
//                .map(SensitiveWordEntity::getWord)
//                .filter(StringUtils::isNotEmpty)
//                .toList();
//
//        return results;
//    }
//    /**
//     * 更新词库 5min
//     * <p>
//     * 每次数据库的信息发生变化之后，首先调用更新数据库敏感词库的方法。
//     * 如果需要生效，则调用这个方法。
//     * <p>
//     * 说明：重新初始化不影响旧的方法使用。初始化完成后，会以新的为准。
//     */
//    public void refresh() {
//        // 每次数据库的信息发生变化之后，首先调用更新数据库敏感词库的方法，然后调用这个方法。
//        sensitiveWordBs().init();
//    }
//}
