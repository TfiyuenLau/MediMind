package edu.hbmu.cooperation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.hbmu.cooperation.domain.entity.GroupMember;
import edu.hbmu.cooperation.dao.GroupMemberMapper;
import edu.hbmu.cooperation.service.IGroupMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-04-19
 */
@Service
public class GroupMemberServiceImpl extends ServiceImpl<GroupMemberMapper, GroupMember> implements IGroupMemberService {

    @Autowired
    private GroupMemberMapper groupMemberMapper;

    /**
     * 通过GroupInfoId外键获取团队成员对象
     *
     * @param id GroupInfoId
     * @return
     */
    @Override
    public List<GroupMember> getGroupMembersByGroupInfoId(Long id) {
        LambdaQueryWrapper<GroupMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupMember::getGroupId, id);
        return groupMemberMapper.selectList(queryWrapper);
    }

    /**
     * 通过memberId获取集合
     *
     * @param memberId
     * @return
     */
    @Override
    public List<GroupMember> getGroupMembersByMemberId(Long memberId) {
        LambdaQueryWrapper<GroupMember> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GroupMember::getGroupMember, memberId);

        return groupMemberMapper.selectList(queryWrapper);
    }

    @Override
    public int insertGroupMember(GroupMember groupMember) {
        return groupMemberMapper.insert(groupMember);
    }

}
