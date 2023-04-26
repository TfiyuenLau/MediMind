package edu.hbmu.aidiagnosis.controller;

import edu.hbmu.aidiagnosis.domain.node.Disease;
import edu.hbmu.aidiagnosis.domain.node.Drug;
import edu.hbmu.aidiagnosis.domain.node.Symptom;
import edu.hbmu.aidiagnosis.domain.request.DiseaseParams;
import edu.hbmu.aidiagnosis.domain.response.ResultVO;
import edu.hbmu.aidiagnosis.service.IDiseaseService;
import edu.hbmu.aidiagnosis.service.IDrugService;
import edu.hbmu.aidiagnosis.service.ISymptomService;
import edu.hbmu.aidiagnosis.util.AnswerSearcher;
import edu.hbmu.aidiagnosis.util.QuestionClassifier;
import edu.hbmu.aidiagnosis.util.QuestionParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Api("AiDiagnosis控制器Api")
@RestController
@RequestMapping("/diagnosis")
@ApiIgnore// 已过时
public class AiDiagnosisController {

    @Autowired
    private QuestionClassifier questionClassifier;

    @Autowired
    private QuestionParser questionParser;

    @Autowired
    private AnswerSearcher answerSearcher;

    @Autowired
    private IDiseaseService diseaseService;

    @Autowired
    private IDrugService drugService;

    @Autowired
    private ISymptomService symptomService;

    @ApiOperation("向智能助手提出医学问题")
    @GetMapping("/getChatBotAnswer/{question}")
    public ResultVO getChatBotAnswer(@PathVariable("question") String question) {
        String answer = "抱歉！我暂时无法回答您的问题...请咨询工作人员！";
        Map<String, Map<String, List<String>>> resClassify = questionClassifier.classify(question);
        if (resClassify == null || resClassify.size() == 0) {
            return ResultVO.ok(answer);
        }
        List<Map<String, List<String>>> resQuery = questionParser.parserMain(resClassify);
        List<String> finalAnswer = answerSearcher.searchMain(resQuery);
        if (finalAnswer == null || finalAnswer.size() == 0) {
            return ResultVO.ok(answer);
        } else {
            answer = String.join("\n", finalAnswer);
        }

        return ResultVO.ok(answer);
    }

    @ApiOperation("通过病症集合获取最有可能疾病列表(maxCount=5)")
    @PostMapping("/getDiseaseBySymptoms")
    public ResultVO getDiseaseBySymptoms(@RequestBody Set<String> symptomSet) {
        List<Map.Entry<Disease, Integer>> diseaseEntryList = diseaseService.findDiseaseBySymptoms(symptomSet)
                .stream().limit(5).collect(Collectors.toList());
        if (diseaseEntryList.size() == 0) {
            return ResultVO.errorMsg("没有找到改症状对应的疾病");
        }

        return ResultVO.ok(diseaseEntryList);
    }

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

    @ApiOperation("插入一个Disease节点")
    @PostMapping("/insertDisease")
    public ResultVO insertDisease(@RequestBody DiseaseParams diseaseParams) {
        Disease insertDisease = null;
        try {
            Disease disease = new Disease();
            BeanUtils.copyProperties(diseaseParams, disease);
            insertDisease = diseaseService.saveDisease(disease);
        } catch (BeansException e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok(insertDisease);
    }

    @ApiOperation("根据id删除疾病节点（仅需传id字段，其他为空：\"\"）")
    @DeleteMapping("/deleteDisease")
    public ResultVO deleteDisease(@RequestBody DiseaseParams diseaseParams) {
        try {
            Disease disease = new Disease();
            BeanUtils.copyProperties(diseaseParams, disease);
            diseaseService.deleteDisease(disease);
        } catch (BeansException e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok();
    }

}
