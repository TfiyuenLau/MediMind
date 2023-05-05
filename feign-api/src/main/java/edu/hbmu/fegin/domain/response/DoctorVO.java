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
@ApiModel(value = "Doctor视图对象", description = "医生对象实体")
public class DoctorVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("医生主键")
    private Long doctorId;

    @ApiModelProperty("医生姓名")
    private String doctorName;

    @ApiModelProperty("医生性别")
    private String doctorGender;

    @ApiModelProperty("医生联系方式")
    private String doctorPhonenumber;

    @ApiModelProperty("医生职称")
    private String professionalTitle;

    @ApiModelProperty("外键映射至department_id（科室）")
    private String belongTo;

    @ApiModelProperty("医生用户账号")
    private String account;

    @ApiModelProperty("用户权限")
    private String authority;

    @ApiModelProperty("入职时间")
    private LocalDateTime hiredate;
}
