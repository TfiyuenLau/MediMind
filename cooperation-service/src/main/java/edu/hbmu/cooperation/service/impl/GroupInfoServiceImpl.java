package edu.hbmu.cooperation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.hbmu.cooperation.domain.entity.GroupInfo;
import edu.hbmu.cooperation.dao.GroupInfoMapper;
import edu.hbmu.cooperation.service.IGroupInfoService;
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
public class GroupInfoServiceImpl extends ServiceImpl<GroupInfoMapper, GroupInfo> implements IGroupInfoService {

    @Autowired
    private GroupInfoMapper groupInfoMapper;

    @Override
    public GroupInfo getGroupInfoById(Long id) {
        return groupInfoMapper.selectById(id);
    }

    @Override
    public List<GroupInfo> getGroupInfoList() {
        return groupInfoMapper.selectList(null);
    }

    @Override
    public List<GroupInfo> getGroupByName(String name) {
        LambdaQueryWrapper<GroupInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(GroupInfo::getGroupName, name);

        return groupInfoMapper.selectList(queryWrapper);
    }

    @Override
    public int insertGroupInfo(GroupInfo groupInfo) {
        return groupInfoMapper.insert(groupInfo);
    }

    @Override
    public int updateGroupInfo(GroupInfo groupInfo) {
        return groupInfoMapper.updateById(groupInfo);
    }
}
