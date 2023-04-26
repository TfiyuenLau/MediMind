package edu.hbmu.cooperation.domain.response;

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
public class GroupMemberVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增长主键")
    private Long doctorId;

    @ApiModelProperty("所属团队id")
    private Long groupId;

    @ApiModelProperty("医生姓名")
    private String doctorName;

    @ApiModelProperty("医生性别")
    private String doctorGender;

    @ApiModelProperty("所属科室")
    private String department;

    @ApiModelProperty("医生联系方式")
    private String doctorPhonenumber;

    @ApiModelProperty("医生职称")
    private String professionalTitle;
}
