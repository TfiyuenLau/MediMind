package edu.hbmu.outpatient.domain.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-04-10
 */
@Getter
@Setter
@ApiModel(value = "Patient对象", description = "病人实体")
public class PatientVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("病人主键")
    @TableId(value = "patient_id", type = IdType.AUTO)
    private Long patientId;

    @ApiModelProperty("病人姓名")
    private String patientName;

    @ApiModelProperty("病人性别")
    private String patientGender;

    @ApiModelProperty("病人年龄")
    private String patientAge;

    @ApiModelProperty("病人联系方式")
    private String patientPhonenumber;

    @ApiModelProperty("病人所属的历史诊断记录")
    private List<DiagnosisVO> diagnosisList;
}
