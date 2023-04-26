package edu.hbmu.cooperation.domain.request;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @since 2023-04-23
 */
@Getter
@Setter
@ApiModel(value = "Schedule请求参数对象", description = "日程安排对象")
public class ScheduleParams implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("日程的归属医生ID")
    private Long doctorId;

    @ApiModelProperty("日程标题")
    private String scheduleTitle;

    @ApiModelProperty("日程描述")
    private String scheduleDescription;

    @ApiModelProperty("日程紧急程度(0<1<2<3)")
    private Integer urgency;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
}
