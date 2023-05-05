package edu.hbmu.fegin.client;

import edu.hbmu.fegin.domain.response.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("outpatient")//微服务名称
@RequestMapping("/outpatient")
public interface OutpatientClient {

    /**
     * 通过doctorId获取医生信息
     *
     * @param id
     * @return
     */
    @GetMapping("/doctor/getAccountById/{id}")
    ResultVO getAccountById(@PathVariable("id") Long id);

    /**
     * 获取用户权限
     *
     * @param id
     * @return String
     */
    @GetMapping("/doctor/getAuthorize/{id}")
    ResultVO getAuthorize(@PathVariable("id") Long id);

    /**
     * 通过医生id获取所属科室
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/department/getDepartmentById/{id}")
    ResultVO getDepartmentById(@PathVariable("id") Long id);

    /**
     * 按病人ID获取就诊病例视图集合
     *
     * @param patientId
     * @return
     */
    @GetMapping("/diagnosis/getDiagnosisByPatientId/{patientId}")
    ResultVO getDiagnosisByPatientId(@PathVariable("patientId") Long patientId);

}
