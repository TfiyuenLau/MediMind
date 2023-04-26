package edu.hbmu.outpatient.controller;

import edu.hbmu.outpatient.domain.entity.Disease;
import edu.hbmu.outpatient.domain.response.ResultVO;
import edu.hbmu.outpatient.service.IDiseaseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/disease")
public class DiseaseController {

    @Autowired
    private IDiseaseService diseaseService;

    @ApiOperation("通过疾病id获取疾病对象")
    @GetMapping("/getDiseaseById/{id}")
    public ResultVO getDiseaseById(@PathVariable("id") Long id) {
        Disease disease = diseaseService.getDiseaseById(id);
        if (disease == null) {
            return ResultVO.errorMsg("没有找到id为" + id + "的Disease对象");
        }

        return ResultVO.ok(disease);
    }

    @ApiOperation("通过疾病名称查找所有符合条件的Disease对象集合")
    @GetMapping("/findDiseasesByName/{diseaseName}")
    public ResultVO findDiseasesByName(@PathVariable("diseaseName") String diseaseName) {
        List<Disease> diseaseList = diseaseService.findDiseasesByName(diseaseName);
        if (diseaseList == null) {
            return ResultVO.errorMsg("未找到名称为" + diseaseName + "对应的疾病");
        }

        return ResultVO.ok();
    }

    @ApiOperation("通过前缀获取疾病自动补全建议(maxSize=10)")
    @GetMapping("/getDiseaseSuggestion/{prefix}")
    public ResultVO getDiseaseSuggestion(@PathVariable("prefix") String prefix) {
        List<String> diseaseSuggestion = diseaseService.getDiseaseSuggestion(prefix);
        if (diseaseSuggestion == null) {
            return ResultVO.errorMsg("无相关结果");
        }

        return ResultVO.ok(diseaseSuggestion);
    }

}
