package edu.hbmu.cooperation.service;

import edu.hbmu.cooperation.domain.entity.GroupInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    List<GroupInfo> getGroupInfoList();

    List<GroupInfo> getGroupByName(String name);

    int insertGroupInfo(GroupInfo groupInfo);

    int updateGroupInfo(GroupInfo groupInfo);
}
