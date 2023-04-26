package edu.hbmu.fegin.domain.entity;

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
@ApiModel(value = "Department对象", description = "科室对象实体")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("科室主键")
    private Long departmentId;

    @ApiModelProperty("科室名称")
    private String departmentName;

    @ApiModelProperty("科室电话")
    private String departmentPhonenumber;

    @ApiModelProperty("科室地址")
    private String departmentAddress;

    @ApiModelProperty("是否有效")
    private Byte isEffective;
}
