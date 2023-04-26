package edu.hbmu.outpatient.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.ShearCaptcha;
import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.hbmu.outpatient.domain.entity.Doctor;
import edu.hbmu.outpatient.domain.request.DoctorParams;
import edu.hbmu.outpatient.domain.response.DoctorVO;
import edu.hbmu.outpatient.domain.response.ResultVO;
import edu.hbmu.outpatient.service.IDoctorService;
import edu.hbmu.outpatient.util.RedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-03-19
 */
@Api("Doctor控制器Api")
@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private IDoctorService doctorService;

    @Autowired
    private RedisUtils redisUtils;

    @ApiOperation("医生用户登录方法")
    @PostMapping("/doLogin")
    public SaResult doLogin(@RequestParam String account, @RequestParam String password, @RequestParam String code,
                            @CookieValue("imgCodeToken") String tokenId) {
        // 图形码验证
        String codeValue = redisUtils.get(tokenId);
        if (codeValue == null) {
            return SaResult.error("验证码已过期，请重新输入！");
        }
        System.err.println(account + ";" + password + ";" + code);
        if (!codeValue.equals(code)) {
            return SaResult.error("验证码错误！");
        }

        // 账号密码验证
        Doctor doctor = doctorService.doLogin(account, password);
        if (doctor == null) {
            return SaResult.error("登陆失败!账号或密码错误...");
        }

        StpUtil.login(doctor.getDoctorId());
        return SaResult.ok("登陆成功!");
    }

    /**
     * 生成图像验证码
     *
     * @param response response请求对象
     * @throws IOException
     */
    @ApiOperation(value = "生成图像验证码")
    @GetMapping("/generateValidateCode")
    public void generateValidateCode(HttpServletResponse response) throws IOException {
        //设置response响应
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        //把图形验证码凭证放入cookie中
        String tokenId = "CAPTCHA_TOKEN" + UUID.randomUUID().toString();
        Cookie cookie = new Cookie("imgCodeToken", tokenId);
        cookie.setPath("/");
        response.addCookie(cookie);

        //创建扭曲干扰验证码，定义图形验证码的长、宽、验证码字符数、干扰线宽度
        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 4);

        //把凭证对应的验证码信息保存到redis
        redisUtils.set(tokenId, captcha.getCode(), 60);

        //输出浏览器
        OutputStream stream = response.getOutputStream();
        captcha.write(stream);
        stream.flush();
        stream.close();
    }

    @ApiOperation("用户注销接口")
    @PostMapping("/logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }

    @ApiOperation("通过doctorId获取医生信息")
    @GetMapping("/getAccountById/{id}")
    public ResultVO getAccountById(@PathVariable("id") Long id) {
        Doctor accountInfo = doctorService.getAccountInfo(id);
        if (accountInfo == null) {
            return ResultVO.errorMsg("没有找到id为" + id + "的账号信息");
        }

        DoctorVO doctorVO = new DoctorVO();
        BeanUtils.copyProperties(accountInfo, doctorVO);

        return ResultVO.ok(doctorVO);
    }

    @ApiOperation("通过Cookie获取当前登录者信息")
    @GetMapping("/getAccountInfo")
    public ResultVO getAccountInfo() {
        Long loginId = null;
        try {
            loginId = StpUtil.getLoginIdAsLong();
        } catch (NotLoginException e) {
            return ResultVO.errorMsg(e.getMessage());
        }
        Doctor accountInfo = doctorService.getAccountInfo(loginId);

        DoctorVO doctorVO = new DoctorVO();
        BeanUtils.copyProperties(accountInfo, doctorVO);

        return ResultVO.ok(doctorVO);
    }

    @ApiOperation("通过id获取用户权限")
    @GetMapping("/getAuthorize/{id}")
    public ResultVO getAuthority(@PathVariable("id") Long id) {
        String authorize = doctorService.getAuthorize(id);
        return ResultVO.ok(authorize);
    }

    @ApiOperation("获取医生列表，仅admin权限")
    @SaCheckRole("admin")//鉴权操作
    @GetMapping("/getDoctorByPage/{page}")
    public ResultVO getDoctorByPage(@PathVariable("page") Long page) {
        IPage<Doctor> doctorIPage = doctorService.getDoctorByPage(page);
        if (doctorIPage.getRecords().size() == 0) {
            long maxPage = doctorIPage.getTotal() / doctorIPage.getSize() + 1;
            return ResultVO.errorMsg("失败！超出最大页码：" + maxPage);
        }

        return ResultVO.ok(doctorIPage);
    }

    @ApiOperation("注册一名医生")
    @PostMapping("/insertDoctor")
    public ResultVO insertDoctor(@RequestBody DoctorParams doctorParams) {
        Doctor doctor = new Doctor();
        BeanUtils.copyProperties(doctorParams, doctor);
        if (doctorService.insertDoctor(doctor) < 1) {
            return ResultVO.errorMsg("注册失败");
        }

        return ResultVO.ok();
    }

}
