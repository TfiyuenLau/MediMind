package edu.hbmu.cooperation.service;

import edu.hbmu.cooperation.domain.entity.GroupInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-04-19
 */
public interface IGroupInfoService extends IService<GroupInfo> {

    GroupInfo getGroupInfoById(Long id);

    int insertGroupInfo(GroupInfo groupInfo);
}
