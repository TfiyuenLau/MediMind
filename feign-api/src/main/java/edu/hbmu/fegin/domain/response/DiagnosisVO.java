package edu.hbmu.fegin.domain.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-03-19
 */
@Getter
@Setter
@ApiModel(value = "Diagnosis视图对象", description = "诊断记录视图对象实体")
public class DiagnosisVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("诊断记录主键")
    private Long diagnosisId;

    @ApiModelProperty("病人id")
    private Long patientId;

    @ApiModelProperty("病人姓名")
    private String patientName;

    @ApiModelProperty("病人性别")
    private String patientGender;

    @ApiModelProperty("病人年龄")
    private String patientAge;

    @ApiModelProperty("病人联系方式")
    private String patientPhonenumber;

    @ApiModelProperty("病人症状")
    private String patientSymptom;

    @ApiModelProperty("诊断结果")
    private String diagnosisResult;

    @ApiModelProperty("治疗方案")
    private String treatmentPlan;

    @ApiModelProperty("治疗方案描述")
    private String treatmentPlanDescription;

    @ApiModelProperty("主治医生")
    private String attendingDoctor;

    @ApiModelProperty("就诊科室")
    private String treatmentDepartment;

    @ApiModelProperty("就诊时间")
    private LocalDateTime visitTime;

}
