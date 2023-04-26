package edu.hbmu.outpatient.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

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
@ApiModel(value = "Diagnosis对象", description = "诊断记录实体")
public class Diagnosis implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("诊断记录主键")
    @TableId(value = "diagnosis_id", type = IdType.AUTO)
    private Long diagnosisId;

    @ApiModelProperty("外键映射病人id")
    private Long patientId;

    @ApiModelProperty("病人症状")
    private String patientSymptom;

    @ApiModelProperty("诊断结果，外键映射至disease_id（疾病实体）")
    private Long diagnosisResult;

    @ApiModelProperty("治疗方案")
    private String treatmentPlan;

    @ApiModelProperty("主治医生")
    private String attendingDoctor;

    @ApiModelProperty("就诊科室")
    private String treatmentDepartment;

    @ApiModelProperty("就诊时间")
    private LocalDateTime visitTime;

    @ApiModelProperty("是否有效")
    @TableLogic
    private Byte isEffective;

}
