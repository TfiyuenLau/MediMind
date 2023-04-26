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
 * @since 2023-03-19
 */
@Getter
@Setter
@ApiModel(value = "Department对象", description = "科室对象实体")
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("科室主键")
    @TableId(value = "department_id", type = IdType.AUTO)
    private Long departmentId;

    @ApiModelProperty("科室名称")
    private String departmentName;

    @ApiModelProperty("科室电话")
    private String departmentPhonenumber;

    @ApiModelProperty("科室地址")
    private String departmentAddress;

    @ApiModelProperty("是否有效")
    @TableLogic
    private Byte isEffective;
}
