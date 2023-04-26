package edu.hbmu.outpatient.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

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
@ApiModel(value = "Diagnosis请求对象", description = "诊断记录参数封装")
public class DiagnosisParams implements Serializable {

    @ApiModelProperty("病人id")
    private Long patientId;

    @ApiModelProperty("病人症状")
    private String patientSymptom;

    @ApiModelProperty("诊断结果")
    @JsonProperty("diseaseName")
    private String diseaseName;

    @ApiModelProperty("治疗方案")
    @JsonProperty("treatmentPlan")
    private String treatmentPlan;
}
