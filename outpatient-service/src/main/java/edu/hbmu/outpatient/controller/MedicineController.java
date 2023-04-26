package edu.hbmu.outpatient.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import edu.hbmu.outpatient.domain.doc.MedicineDoc;
import edu.hbmu.outpatient.domain.entity.Medicine;
import edu.hbmu.outpatient.domain.response.ResultVO;
import edu.hbmu.outpatient.service.IMedicineService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author @TfiyuenLau
 * @since 2023-03-19
 */
@RestController
@RequestMapping("/medicine")
public class MedicineController {

    @Autowired
    private IMedicineService medicineService;

    @ApiOperation("通过id获取药物对象")
    @GetMapping("/getMedicineById/{id}")
    public ResultVO getMedicineById(@PathVariable("id") Long id) {
        Medicine medicine = medicineService.getMedicineById(id);
        if (medicine == null) {
            return ResultVO.errorMsg("没有找到id为" + id + "的Medicine对象");
        }

        return ResultVO.ok(medicine);
    }

    @ApiOperation("通过页码获取Medicine列表")
    @GetMapping("/getMedicineByPage/{page}")
    public ResultVO getMedicineByPage(@PathVariable("page") Long page) {
        IPage<Medicine> medicineIPage = medicineService.getMedicineByPage(page);
        if (medicineIPage.getRecords().size() == 0) {
            long maxPage = medicineIPage.getTotal() / medicineIPage.getSize() + 1;
            return ResultVO.errorMsg("失败！超出最大页码：" + maxPage);
        }

        return ResultVO.ok(medicineIPage);
    }

    @ApiOperation("通过药物名称查询药物对象列表")
    @GetMapping("/findMedicineByName/{medicineName}")
    public ResultVO findMedicineByName(@PathVariable("medicineName") String medicineName) throws IOException {
        List<Medicine> medicineList = medicineService.findMedicineByName(medicineName);
        if (medicineList == null) {
            return ResultVO.errorMsg("没有找到名称为" + medicineName + "对应的药物对象");
        }

        return ResultVO.ok(medicineList);
    }

    @ApiOperation("通过前缀获取药品自动补全建议(maxSize=10)")
    @GetMapping("/getMedicineSuggestion/{prefix}")
    public ResultVO getMedicineSuggestion(@PathVariable("prefix") String prefix) {
        List<String> medicineSuggestion = medicineService.getMedicineSuggestion(prefix);
        if (medicineSuggestion == null) {
            return ResultVO.errorMsg("无相关结果");
        }

        return ResultVO.ok(medicineSuggestion);
    }

}
