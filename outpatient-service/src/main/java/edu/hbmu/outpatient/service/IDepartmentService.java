package edu.hbmu.outpatient.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.hbmu.outpatient.domain.entity.Department;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-03-19
 */
public interface IDepartmentService extends IService<Department> {

    Department getDepartmentById(Long id);

    IPage<Department> getDepartmentByPage(Long page);
}
