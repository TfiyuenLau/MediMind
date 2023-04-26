package edu.hbmu.cooperation.service;

import edu.hbmu.cooperation.domain.entity.GroupMember;
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
public interface IGroupMemberService extends IService<GroupMember> {

    List<GroupMember> getGroupMembersByGroupInfoId(Long id);

    List<GroupMember> getGroupMembersByMemberId(Long memberId);

    int insertGroupMember(GroupMember groupMember);
}
