package edu.hbmu.cooperation.domain.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
@TableName("group_member")
@ApiModel(value = "GroupMember对象", description = "")
public class GroupMemberParams implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("所属团队id")
    private Long groupId;

    @ApiModelProperty("团队成员id")
    private Long groupMember;
}
