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
@ApiModel(value = "Disease对象", description = "病症实体")
public class Disease implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("病症主键")
    @TableId(value = "disease_id", type = IdType.AUTO)
    private Long diseaseId;

    @ApiModelProperty("病症名称")
    private String diseaseName;

    @ApiModelProperty("病症描述")
    private String diseaseDescription;

    @ApiModelProperty("是否有效")
    @TableLogic
    private Byte isEffective;
}
