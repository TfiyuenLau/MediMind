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
@ApiModel(value = "Medicine对象", description = "药物对象实体")
public class Medicine implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("药品实体主键")
    @TableId(value = "medicine_id", type = IdType.AUTO)
    private Long medicineId;

    @ApiModelProperty("药品名称")
    private String medicineName;

    @ApiModelProperty("药品描述")
    private String medicineDescription;

    @ApiModelProperty("是否有效")
    @TableLogic
    private Byte isEffective;
}
