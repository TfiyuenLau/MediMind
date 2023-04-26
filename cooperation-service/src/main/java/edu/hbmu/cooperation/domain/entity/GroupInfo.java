package edu.hbmu.cooperation.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2023-04-19
 */
@Getter
@Setter
@TableName("group_info")
@ApiModel(value = "GroupInfo对象", description = "")
public class GroupInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增长主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("团队名称")
    private String groupName;

    @ApiModelProperty("团队简介")
    private String groupIntroduce;

    @ApiModelProperty("团队人员数量")
    private Integer groupNum;

    @ApiModelProperty("团队创建者id")
    private Long groupCreateUid;

    @ApiModelProperty("团队创建时间")
    private LocalDateTime groupCreateTime;

    @ApiModelProperty("是否有效")
    @TableLogic
    private Byte isEffective;
}
