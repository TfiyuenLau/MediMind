package edu.hbmu.aidiagnosis.controller;

import edu.hbmu.aidiagnosis.domain.node.Drug;
import edu.hbmu.aidiagnosis.domain.response.ResultVO;
import edu.hbmu.aidiagnosis.service.IDrugService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api("Drug控制器Api")
@RestController
@RequestMapping("/drug")
public class DrugController {

    @Autowired
    private IDrugService drugService;

    @ApiOperation("通过疾病名称智能推常用药物列表(maxCount=10)")
    @GetMapping("/getDrugByDisease/{diseaseName}")
    public ResultVO getDrugByDisease(@PathVariable("diseaseName") String diseaseName) {
        List<Drug> drugList = drugService.findRecommendDrugByDisease(diseaseName)
                .stream().limit(10).collect(Collectors.toList());
        if (drugList.size() == 0) {
            return ResultVO.errorMsg("没有找到" + diseaseName + "对应的药品结果");
        }

        return ResultVO.ok(drugList);
    }

}
