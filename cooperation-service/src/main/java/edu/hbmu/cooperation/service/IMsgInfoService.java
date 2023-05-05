package edu.hbmu.cooperation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.hbmu.cooperation.domain.entity.MsgInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-04-19
 */
public interface IMsgInfoService extends IService<MsgInfo> {

    int insertMsg(MsgInfo msgInfo);

    MsgInfo getMsgInfoById(Long id);

    List<MsgInfo> getMsgInfoList(Long uid);

    List<MsgInfo> getGroupMsgInfoList(Long uid);

    List<MsgInfo> getUnreadMsgInfo(Long doctorId, Long fromId);

    IPage<MsgInfo> getMsgInfoPage(Long page, Long uid);

    File uploadingFile(MultipartFile file, String filePath) throws IOException;

    int updateMsgIsReadByUid(MsgInfo msgInfo);
}
