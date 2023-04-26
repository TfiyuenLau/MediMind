package edu.hbmu.outpatient.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
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
 * @since 2023-04-10
 */
@Getter
@Setter
@ApiModel(value = "Patient对象", description = "病人实体")
public class Patient implements Serializable {

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

    @ApiModelProperty("是否有效")
    @TableLogic
    private Byte isEffective;
}
