package edu.hbmu.outpatient.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.hbmu.outpatient.constants.MqConstants;
import edu.hbmu.outpatient.domain.entity.Patient;
import edu.hbmu.outpatient.domain.response.ResultVO;
import edu.hbmu.outpatient.service.IPatientService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-04-10
 */
@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private IPatientService patientService;

    private Patient curPatient;// 当前挂号的病人

    @ApiOperation("通过id获取病人信息")
    @GetMapping("/getPatientById/{id}")
    public ResultVO getPatientById(@PathVariable("id") Long id) {
        Patient patient = patientService.getPatientById(id);
        if (patient == null) {
            return ResultVO.errorMsg("没有id：" + id + "对应的病人信息");
        }

        return ResultVO.ok(patient);
    }

    @ApiOperation("通过页码获取病人信息")
    @GetMapping("/getPatientByPage/{page}")
    public ResultVO getPatientByPage(@PathVariable() Long page) {
        IPage<Patient> patientIPage = patientService.getPatientByPage(page);
        if (patientIPage.getRecords().size() == 0) {
            long maxPage = patientIPage.getTotal() / patientIPage.getSize() + 1;
            return ResultVO.errorMsg("失败！超出最大页码：" + maxPage);
        }

        return ResultVO.ok();
    }

    @ApiOperation("向队列随机添加挂号数据以模拟病人挂号")
    @PostMapping("/publishRegisterPatient")
    public ResultVO publishRegisterPatient() {
        return ResultVO.ok(patientService.publishRegisterPatientList());
    }

    /**
     * 从消息队列中获取一条挂号诊断的病人消息
     *
     * @param flag 该消息是否已被选择消费
     * @return ResultVO
     */
    @ApiOperation("从消息队列中获取一条挂号诊断的病人消息")
    @GetMapping("/getRegisterPatient/{flag}")
    public ResultVO getRegisterPatient(@PathVariable("flag") Boolean flag) {
        if (!flag) {
            if (curPatient == null) {// 初始化curPatient消息变量
                curPatient = patientService.processPatientQueue(MqConstants.PATIENT_QUEUE);
            }
        } else {
            curPatient = patientService.processPatientQueue(MqConstants.PATIENT_QUEUE);// 消费这条消息
        }

        if (curPatient == null) {
            return ResultVO.errorMap("暂无病人挂号...");
        }
        return ResultVO.ok(curPatient);
    }

    @ApiOperation("新增病人信息")
    @PostMapping("/insertPatient")
    public ResultVO insertPatient(@RequestBody Patient patient) {
        patientService.insertPatient(patient);

        return ResultVO.ok();
    }

}
