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
@TableName("communication_session")
@ApiModel(value = "CommunicationSession参数对象", description = "")
public class CommunicationSessionParams implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("接收人id(id/group_id)")
    private Long toId;

    @ApiModelProperty("会话类型(群组消息/个人聊天/系统消息:2/1/0)")
    private Integer chatType;
}
