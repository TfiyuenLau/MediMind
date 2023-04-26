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
@TableName("communication_session")
@ApiModel(value = "CommunicationSession对象", description = "")
public class CommunicationSession implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增长主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("发送人id(自己发送就是自己的uid，不然就是别人的uid)")
    private Long fromId;

    @ApiModelProperty("接收人id(uid/group_id)")
    private Long toId;

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

    @ApiModelProperty("是否有效")
    @TableLogic
    private Byte isEffective;
}
