package edu.hbmu.cooperation.domain.request;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2023-04-19
 */
@Getter
@Setter
@TableName("group_info")
@ApiModel(value = "GroupInfo对象", description = "")
public class GroupInfoParams implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增长主键")
    private Long id;

    @ApiModelProperty("团队名称")
    private String groupName;

    @ApiModelProperty("团队简介")
    private String groupIntroduce;
}
