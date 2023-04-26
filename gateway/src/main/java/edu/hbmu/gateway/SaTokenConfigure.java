package edu.hbmu.gateway;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * [Sa-Token 权限认证] 配置类
 *
 * @author TfiyuenLau
 */
@Configuration
public class SaTokenConfigure {
    // 注册 Sa-Token 全局过滤器
    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                .addInclude("/**")// 拦截地址
                .addExclude("/outpatient/doctor/doLogin", "/outpatient/doctor/generateValidateCode")// 开放地址
                .setAuth(obj -> {// 鉴权方法：每次访问进入
                    // 登录校验 -- 拦截所有路由，并排除/doctor/doLogin 用于开放登录
                    SaRouter.match("/**", "/outpatient/doctor/doLogin", r -> StpUtil.checkLogin());

                    // 权限认证 -- 不同模块, 校验不同权限
                    SaRouter.match("/outpatient/**", "/outpatient/doctor/doLogin", r -> {
                        StpUtil.checkPermissionOr("admin", "doctor");
                    });
                    SaRouter.match("/aidiagnosis/**", "/outpatient/doctor/doLogin", r -> {
                        StpUtil.checkPermissionOr("admin", "doctor");
                    });
                    SaRouter.match("/cooperation/**", "/outpatient/doctor/doLogin", r -> {
                        StpUtil.checkPermissionOr("admin", "doctor");
                    });
                }).setError(e -> {
                    return SaResult.error(e.getMessage());
                });
    }
}
