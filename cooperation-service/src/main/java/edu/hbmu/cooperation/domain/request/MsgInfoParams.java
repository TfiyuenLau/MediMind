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
@TableName("msg_info")
@ApiModel(value = "MsgInfoParams请求对象", description = "")
public class MsgInfoParams implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("是否是自己发送的")
    private Integer isMe;

    @ApiModelProperty("发送者id")
    private Long fromId;

    @ApiModelProperty("接受者(id/group_id)")
    private Long toId;

    @ApiModelProperty("会话类型(群组消息/个人聊天/系统消息:2/1/0)")
    private Integer chatType;

    @ApiModelProperty("消息类型(文字/图片/文件/音乐等)")
    private Integer msgType;

    @ApiModelProperty("消息内容")
    private String msg;
}
