package edu.hbmu.outpatient.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.hbmu.outpatient.domain.entity.Department;
import edu.hbmu.outpatient.domain.request.DepartmentParams;
import edu.hbmu.outpatient.service.IDepartmentService;
import edu.hbmu.outpatient.domain.response.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-03-19
 */
@Api("Department控制器Api")
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private IDepartmentService departmentService;

    @ApiOperation("通过科室id查询科室")
    @GetMapping(value = "/getDepartmentById/{id}")
    public ResultVO getDepartmentById(@PathVariable("id") Long id) {
        Department department = departmentService.getDepartmentById(id);
        if (department == null) {
            return ResultVO.errorMsg("抱歉，没有找到id为" + id + "的科室！");
        }

        return ResultVO.ok(department);
    }

    @ApiOperation("通过page页码获取科室列表")
    @GetMapping("/getDepartmentByPage/{page}")
    public ResultVO getDepartmentByPage(@PathVariable("page") Long page) {
        IPage<Department> departmentIPage = departmentService.getDepartmentByPage(page);
        if (departmentIPage.getRecords().size() == 0) {
            long maxPage = departmentIPage.getTotal() / departmentIPage.getSize() + 1;
            return ResultVO.errorMsg("失败！超出最大页码：" + maxPage);
        }

        return ResultVO.ok(departmentIPage);
    }

    @ApiOperation("新增科室")
    @PostMapping("/insertDepartment")
    public ResultVO insertDepartment(@RequestBody DepartmentParams departmentParams) {
        Department department = new Department();
        BeanUtils.copyProperties(departmentParams, department);
        try {
            departmentService.insertDepartment(department);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok();
    }

    @ApiOperation("更新科室信息")
    @PostMapping("/updateDepartment")
    public ResultVO updateDepartment(@RequestBody Department department) {
        try {
            departmentService.updateDepartment(department);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok();
    }

    @ApiOperation("按科室id删除科室")
    @DeleteMapping("/deleteDepartmentById")
    public ResultVO deleteDepartmentById(@RequestParam("id") Long id) {
        try {
            departmentService.deleteDepartmentById(id);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok();
    }

}
