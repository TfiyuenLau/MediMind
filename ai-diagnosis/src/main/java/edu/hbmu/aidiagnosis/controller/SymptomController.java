package edu.hbmu.aidiagnosis.controller;

import edu.hbmu.aidiagnosis.domain.node.Symptom;
import edu.hbmu.aidiagnosis.domain.response.ResultVO;
import edu.hbmu.aidiagnosis.service.ISymptomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation("通过病症名称模糊查询病症Symptom对象")
    @GetMapping("/getSymptomByNameContaining/{name};")
    public ResultVO getSymptomByNameContaining(@PathVariable("name") String name) {
        Symptom symptom = symptomService.findSymptomByNameContaining(name);

        return ResultVO.ok(symptom);
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

}
