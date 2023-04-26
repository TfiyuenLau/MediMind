package edu.hbmu.outpatient.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.hbmu.outpatient.domain.entity.Department;
import edu.hbmu.outpatient.dao.DepartmentMapper;
import edu.hbmu.outpatient.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-03-19
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public Department getDepartmentById(Long id) {
        return departmentMapper.selectById(id);
    }

    /**
     * 根据page页码获取科室分页结果
     *
     * @param page 页码
     * @return
     */
    @Override
    public IPage<Department> getDepartmentByPage(Long page) {
        Page<Department> departmentPage = new Page<>(page, 20);

        return departmentMapper.selectPage(departmentPage, null);
    }

}
