package edu.hbmu.aidiagnosis.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import edu.hbmu.aidiagnosis.domain.node.Symptom;
import edu.hbmu.aidiagnosis.domain.response.ResultVO;
import edu.hbmu.aidiagnosis.service.ISymptomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api("Symptom控制器Api")
@RestController
@RequestMapping("/symptom")
public class SymptomController {

    @Autowired
    private ISymptomService symptomService;

    @ApiOperation("以ID查询病症对象")
    @GetMapping("/getSymptomById/{id}")
    public ResultVO getSymptomById(@PathVariable("id") Long id) {
        Symptom symptom = symptomService.findSymptomById(id);

        return ResultVO.ok(symptom);
    }

    @ApiOperation("通过病症名称模糊查询病症Symptom对象")
    @GetMapping("/getSymptomByNameContaining/{name}")
    public ResultVO getSymptomByNameContaining(@PathVariable("name") String name) {
        List<Symptom> symptomList = symptomService.findSymptomsByNameContainingNoHavaSet(name)
                .stream().limit(5).collect(Collectors.toList());

        return ResultVO.ok(symptomList);
    }

    @ApiOperation("通过疾病名称智能推荐症状(maxCount=10)")
    @GetMapping("/findSymptomsByDisease/{diseaseName}")
    public ResultVO findSymptomsByDisease(@PathVariable("diseaseName") String diseaseName) {
        List<Symptom> symptomList = symptomService.findSymptomsByDisease(diseaseName)
                .stream().limit(10).collect(Collectors.toList());
        if (symptomList.size() == 0) {
            return ResultVO.errorMsg("未查寻到与" + diseaseName + "有关的症状");
        }

        return ResultVO.ok(symptomList);
    }

    @ApiOperation("修改症状节点信息")
    @SaCheckRole("admin")
    @PutMapping("/saveSymptom")
    public ResultVO saveSymptom(@RequestBody Symptom symptom) {
        Symptom newSymptom = new Symptom();
        Symptom oldSymptom = symptomService.findSymptomById(symptom.getId());
        if (oldSymptom != null) {
            BeanUtils.copyProperties(oldSymptom, newSymptom);
            if (symptom.getName() != null) {
                newSymptom.setName(symptom.getName());
            }
            if (symptom.getDiseaseSet() != null && newSymptom.getDiseaseSet() != null && newSymptom.getDiseaseSet().size() != 0) {
                newSymptom.getDiseaseSet().addAll(symptom.getDiseaseSet());
            }
        } else {
            BeanUtils.copyProperties(symptom, newSymptom);
            newSymptom.setDiseaseSet(symptom.getDiseaseSet());
        }

        return ResultVO.ok(symptomService.saveSymptom(newSymptom));
    }

    @ApiOperation("根据id删除疾病节点")
    @SaCheckRole("admin")
    @DeleteMapping("/deleteSymptom")
    public ResultVO deleteSymptom(@RequestParam("id") Long id) {
        try {
            symptomService.deleteSymptom(id);
        } catch (Exception e) {
            return ResultVO.errorException(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok();
    }

}
