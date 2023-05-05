package edu.hbmu.outpatient.domain.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
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
@ApiModel(value = "Doctor密码请求对象", description = "")
public class PasswordParams implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("医生ID")
    private Long doctorId;

    @ApiModelProperty("旧密码")
    private String oldPassword;

    @ApiModelProperty("新密码")
    private String newPassword;
}
