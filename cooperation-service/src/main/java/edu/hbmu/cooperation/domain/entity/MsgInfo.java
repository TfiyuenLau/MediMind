package edu.hbmu.cooperation.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("msg_info")
@ApiModel(value = "MsgInfo对象", description = "")
public class MsgInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("自增长主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("所属会话uid")
    private Long uid;

    @ApiModelProperty("是否是自己发送的")
    private Integer isMe;

    @ApiModelProperty("发送者uid(可以是自己)")
    private Long fromId;

    @ApiModelProperty("发送者名称")
    private String fromName;

    @ApiModelProperty("接受者(uid/group_id)")
    private Long toId;

    @ApiModelProperty("会话类型(群组消息/个人聊天/系统消息:2/1/0)")
    private Integer chatType;

    @ApiModelProperty("消息类型(文字/图片/文件/音乐等)")
    private Integer msgType;

    @ApiModelProperty("消息内容")
    private String msg;

    @ApiModelProperty("消息发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime sendTime;

    @ApiModelProperty("是否有效")
    @TableLogic
    private Byte isEffective;
}
