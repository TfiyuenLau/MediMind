package edu.hbmu.outpatient.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.hbmu.fegin.client.OutpatientClient;
import edu.hbmu.outpatient.constants.MqConstants;
import edu.hbmu.outpatient.domain.entity.Patient;
import edu.hbmu.outpatient.domain.response.DiagnosisVO;
import edu.hbmu.outpatient.domain.response.PatientVO;
import edu.hbmu.outpatient.domain.response.ResultVO;
import edu.hbmu.outpatient.service.IDiagnosisService;
import edu.hbmu.outpatient.service.IPatientService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private OutpatientClient outpatientClient;

    @Autowired
    private ObjectMapper objectMapper;

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

        return ResultVO.ok(patientIPage);
    }

    @ApiOperation("获取部分病人信息列表")
    @GetMapping("/getPatientList")
    public ResultVO getPatientList() {
        // 获取数据库前15条Patient数据
        List<Patient> patientList = patientService.getPatientList().stream().limit(15).collect(Collectors.toList());

        // 封装病人视图对象集合
        ArrayList<PatientVO> patientVOS = new ArrayList<>();
        for (Patient patient : patientList) {
            PatientVO patientVO = new PatientVO();
            BeanUtils.copyProperties(patient, patientVO);
            patientVO.setDiagnosisList(objectMapper.convertValue(outpatientClient.getDiagnosisByPatientId(patientVO.getPatientId()).getData(), new TypeReference<List<DiagnosisVO>>() {
            }));
            patientVOS.add(patientVO);
        }

        return ResultVO.ok(patientVOS);
    }

    @ApiOperation("模糊查询病人姓名性别获取病人信息列表")
    @GetMapping("/getPatientByLikeName/{key}")
    public ResultVO getPatientByLikeName(@PathVariable("key") String key) {
        List<Patient> patientList = patientService.getPatientByLikeName(key);

        ArrayList<PatientVO> patientVOS = new ArrayList<>();
        for (Patient patient : patientList) {
            PatientVO patientVO = new PatientVO();
            BeanUtils.copyProperties(patient, patientVO);
            patientVO.setDiagnosisList(objectMapper.convertValue(outpatientClient.getDiagnosisByPatientId(patientVO.getPatientId()).getData(), new TypeReference<List<DiagnosisVO>>() {
            }));
            patientVOS.add(patientVO);
        }

        return ResultVO.ok(patientVOS);
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
        try {
            patientService.insertPatient(patient);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok();
    }

    @ApiOperation("更新指定patientId的患者信息")
    @PutMapping("/updatePatient")
    public ResultVO updatePatient(@RequestBody Patient patient) {
        try {
            patientService.updatePatient(patient);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok();
    }

    @ApiOperation("根据id删除患者信息")
    @SaCheckRole("admin")
    @PutMapping("/deletePatientById")
    public ResultVO deletePatientById(@RequestParam("id") Long id) {
        try {
            patientService.deletePatient(id);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok();
    }

}
