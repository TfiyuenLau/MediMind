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
import java.time.LocalDateTime;

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
@TableName("communication_session")
@ApiModel(value = "CommunicationSession对象", description = "")
public class CommunicationSessionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增长主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("发送人id")
    private Long fromId;

    @ApiModelProperty("发送人名字")
    private String fromName;

    @ApiModelProperty("接收人id(uid/group_id)")
    private Long toId;

    @ApiModelProperty("接收人名字")
    private String toName;

    @ApiModelProperty("最后的一条消息内容")
    private String lastMsg;

    @ApiModelProperty("最后的发送者名称")
    private String lastDoctorName;

    @ApiModelProperty("最后消息发送时间")
    private LocalDateTime lastTime;

    @ApiModelProperty("会话类型(群组消息/个人聊天/系统消息:2/1/0)")
    private Integer chatType;

    @ApiModelProperty("消息类型(文字/图片/文件/音乐等)")
    private Integer msgType;

    @ApiModelProperty("该会话未读数目")
    private Integer unreadCount;
}
