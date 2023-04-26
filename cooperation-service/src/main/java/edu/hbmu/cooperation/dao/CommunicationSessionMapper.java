package edu.hbmu.cooperation.dao;

import edu.hbmu.cooperation.domain.entity.CommunicationSession;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-04-19
 */
@Mapper
public interface CommunicationSessionMapper extends BaseMapper<CommunicationSession> {

}
