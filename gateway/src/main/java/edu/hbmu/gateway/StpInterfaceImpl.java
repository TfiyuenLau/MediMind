package edu.hbmu.gateway;

import cn.dev33.satoken.stp.StpInterface;
import edu.hbmu.fegin.client.OutpatientClient;
import edu.hbmu.fegin.domain.response.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限验证接口扩展 
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private OutpatientClient outpatientClient;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的权限列表
        ArrayList<String> permissionList = new ArrayList<>();
        ResultVO authorizeVO = outpatientClient.getAuthorize(Long.valueOf((String) loginId));
        permissionList.add((String) authorizeVO.getData());//按登录用户id获取权限列表

        return permissionList;
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 返回此 loginId 拥有的角色列表
        ArrayList<String> roleList = new ArrayList<>();
        ResultVO authorizeVO = outpatientClient.getAuthorize(Long.valueOf((String) loginId));
        roleList.add((String) authorizeVO.getData());

        return roleList;
    }

}