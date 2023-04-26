package edu.hbmu.outpatient.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.hbmu.outpatient.domain.entity.Diagnosis;
import edu.hbmu.outpatient.domain.entity.Doctor;
import edu.hbmu.outpatient.domain.entity.Patient;
import edu.hbmu.outpatient.domain.request.DiagnosisParams;
import edu.hbmu.outpatient.domain.response.DiagnosisVO;
import edu.hbmu.outpatient.domain.response.ResultVO;
import edu.hbmu.outpatient.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-03-19
 */
@Api("Diagnosis控制器APi")
@RestController
@RequestMapping("/diagnosis")
public class DiagnosisController {

    @Autowired
    private IDiagnosisService diagnosisService;

    @Autowired
    private IDoctorService doctorService;

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private IDiseaseService diseaseService;

    @Autowired
    private IPatientService patientService;

    @Autowired
    private IMedicineService medicineService;

    @ApiOperation("通过页码获取全部接诊记录")
    @SaCheckRole("admin")
    @GetMapping("/getDiagnosisByPage/{page}")
    public ResultVO getDiagnosisByPage(@PathVariable("page") Long page) {
        IPage<Diagnosis> diagnosisIPage = diagnosisService.getDiagnosisByPage(page);

        return processDiagnosisPageToVO(page, diagnosisIPage);
    }

    @ApiOperation("通过page页码获取当前登录的医生相关的疗诊记录")
    @SaCheckLogin
    @GetMapping("/getDiagnosisByDoctor/{page}")
    public ResultVO getDiagnosisByDoctor(@PathVariable("page") Long page) {
        IPage<Diagnosis> diagnosisIPage = diagnosisService.getDiagnosisByDoctor(StpUtil.getLoginIdAsLong(), page);

        return processDiagnosisPageToVO(page, diagnosisIPage);
    }

    @ApiOperation("通过病人id获取就诊记录列表")
    @GetMapping("/getDiagnosisByPatientId/{page}/{patientId}")
    public ResultVO getDiagnosisByPatientId(@PathVariable("page") Long page, @PathVariable("patientId") Long patientId) {
        IPage<Diagnosis> diagnosisIPage = diagnosisService.getDiagnosisByPatientId(patientId, page);

        return processDiagnosisPageToVO(page, diagnosisIPage);
    }

    /**
     * 转换diagnosis与diagnosisVO封装诊疗记录json视图
     *
     * @param page
     * @param diagnosisIPage
     * @return
     */
    private ResultVO processDiagnosisPageToVO(Long page, IPage<Diagnosis> diagnosisIPage) {
        IPage<DiagnosisVO> diagnosisVOIPage = new Page<>(page, 20);// 封装前端视图
        BeanUtils.copyProperties(diagnosisIPage, diagnosisVOIPage);

        List<DiagnosisVO> diagnosisVOList = new ArrayList<>();
        for (Diagnosis diagnosis : diagnosisIPage.getRecords()) {
            DiagnosisVO diagnosisVO = new DiagnosisVO();
            BeanUtils.copyProperties(diagnosis, diagnosisVO);// 拷贝原diagnosis
            Patient patient = patientService.getPatientById(diagnosis.getPatientId());
            BeanUtils.copyProperties(patient, diagnosisVO);// 拷贝patient数据

            diagnosisVO.setDiagnosisResult(diseaseService.getDiseaseById(diagnosis.getDiagnosisResult()).getDiseaseName());
            diagnosisVOList.add(diagnosisVO);
        }
        diagnosisVOIPage.setRecords(diagnosisVOList);

        if (diagnosisVOIPage.getRecords().size() == 0) {
            long maxPage = diagnosisVOIPage.getTotal() / diagnosisVOIPage.getSize() + 1;
            return ResultVO.errorMsg("错误！页码最大为" + maxPage);
        }
        return ResultVO.ok(diagnosisVOIPage);
    }

    /**
     * 添加就诊记录至数据库
     *
     * @param diagnosisParams 就诊记录VO对象
     * @return ResultVO
     */
    @ApiOperation("添加就诊记录至数据库")
    @SaCheckLogin()
    @PostMapping("/insertDiagnosis")
    public ResultVO insertDiagnosis(@RequestBody DiagnosisParams diagnosisParams) {
        Diagnosis diagnosis = new Diagnosis();
        BeanUtils.copyProperties(diagnosisParams, diagnosis);// 拷贝VO至PO
        try {
            Doctor doctor = doctorService.getAccountInfo(StpUtil.getLoginIdAsLong());
            diagnosis.setDiagnosisResult(diseaseService.findDiseasesByName(diagnosisParams.getDiseaseName()).get(0).getDiseaseId());// 通过病症名称查询病症id
            diagnosis.setAttendingDoctor(doctor.getDoctorName());
            diagnosis.setTreatmentDepartment(departmentService.getDepartmentById(doctor.getBelongTo()).getDepartmentName());
            diagnosisService.insertDiagnosis(diagnosis);// 写入数据库
        } catch (RuntimeException e) {
            return ResultVO.errorMsg("发生错误：" + e.getMessage());
        }

        return ResultVO.ok();
    }

}
