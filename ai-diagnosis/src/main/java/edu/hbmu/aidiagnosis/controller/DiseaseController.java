package edu.hbmu.aidiagnosis.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import edu.hbmu.aidiagnosis.domain.node.Disease;
import edu.hbmu.aidiagnosis.domain.request.DiseaseParams;
import edu.hbmu.aidiagnosis.domain.response.ResultVO;
import edu.hbmu.aidiagnosis.service.IDiseaseService;
import edu.hbmu.aidiagnosis.util.AnswerSearcher;
import edu.hbmu.aidiagnosis.util.QuestionClassifier;
import edu.hbmu.aidiagnosis.util.QuestionParser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Api("Disease控制器Api")
@RestController
@RequestMapping("/disease")
public class DiseaseController {

    @Autowired
    private QuestionClassifier questionClassifier;

    @Autowired
    private QuestionParser questionParser;

    @Autowired
    private AnswerSearcher answerSearcher;

    @Autowired
    private IDiseaseService diseaseService;

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

    @ApiOperation("通过疾病id获取疾病封装对象")
    @GetMapping("/getDiseaseById/{id}")
    public ResultVO getDiseaseById(@PathVariable("id") Long id) {
        Disease disease = diseaseService.findDiseaseById(id);

        return ResultVO.ok(disease);
    }

    @ApiOperation("通过疾病名称精确查询疾病封装对象")
    @GetMapping("/getDiseaseByName/{name}")
    public ResultVO getDiseaseByName(@PathVariable("name") String name) {
        Disease disease = diseaseService.findDiseaseByName(name);

        return ResultVO.ok(disease);
    }

    @ApiOperation("通过疾病名称模糊查询疾病封装对象")
    @GetMapping("/findDiseaseByNameContaining/{name}")
    public ResultVO findDiseaseByNameContaining(@PathVariable("name") String name) {
        List<Disease> diseaseList = diseaseService.findDiseaseByNameContaining(name);

        return ResultVO.ok(diseaseList);
    }

    @ApiOperation("通过病症集合获取最有可能疾病列表(maxCount=5)")
    @PostMapping("/getDiseaseBySymptoms")
    public ResultVO getDiseaseBySymptoms(@RequestBody Set<String> symptomSet) {
        List<Map.Entry<Disease, Integer>> diseaseEntryList = diseaseService.findDiseaseBySymptoms(symptomSet);
        if (diseaseEntryList.size() == 0) {
            return ResultVO.errorMsg("没有找到改症状对应的疾病");
        }

        ArrayList<Disease> diseases = new ArrayList<>();
        for (Map.Entry<Disease, Integer> entry : diseaseEntryList) {
            diseases.add(entry.getKey());
        }
        return ResultVO.ok(diseases.stream().limit(5).collect(Collectors.toList()));
    }

    @ApiOperation("插入一个不带关系的节点（请遵循以下规则：新增id为null、修改时带id参数）")
    @SaCheckRole("admin")
    @PostMapping("/insertDisease")
    public ResultVO insertDisease(@RequestBody DiseaseParams diseaseParams) {
        Disease disease = new Disease();
        BeanUtils.copyProperties(diseaseParams, disease);

        return ResultVO.ok(diseaseService.saveDisease(disease));
    }

    @ApiOperation("更新一个Disease节点信息，当属性不为null时更新属性，当关系集合不为null时添加该关系")
    @SaCheckRole("admin")
    @PostMapping("/saveDisease")
    public ResultVO saveDisease(@RequestBody Disease newDisease) {
        Disease disease = new Disease();
        try {
            Disease oldDisease = diseaseService.findDiseaseById(newDisease.getId());
            if (oldDisease != null) {// 更新操作
                BeanUtils.copyProperties(oldDisease, disease);
                // 更新节点信息
                if (newDisease.getName() != null) {
                    disease.setName(newDisease.getName());
                }
                if (newDisease.getCause() != null) {
                    disease.setCause(newDisease.getCause());
                }
                if (newDisease.getDescription() != null) {
                    disease.setDescription(newDisease.getDescription());
                }
                if (newDisease.getCureWay() != null) {
                    disease.setCureWay(newDisease.getCureWay());
                }
                if (newDisease.getCuredProb() != null) {
                    disease.setCuredProb(newDisease.getCuredProb());
                }
                if (newDisease.getCureLastTime() != null) {
                    disease.setCureLastTime(newDisease.getCureLastTime());
                }
                if (newDisease.getPrevent() != null) {
                    disease.setPrevent(newDisease.getPrevent());
                }
                if (newDisease.getEasyGet() != null) {
                    disease.setEasyGet(newDisease.getEasyGet());
                }

                // 添加关系节点
                if (newDisease.getAccompanyDiseaseSet() != null) {
                    disease.getAccompanyDiseaseSet().addAll(newDisease.getAccompanyDiseaseSet());
                }
                if (newDisease.getSymptomSet() != null) {
                    disease.getSymptomSet().addAll(newDisease.getSymptomSet());
                }
                if (newDisease.getRecommendEatSet() != null) {
                    disease.getRecommendEatSet().addAll(newDisease.getRecommendEatSet());
                }
                if (newDisease.getDoEatSet() != null) {
                    disease.getDoEatSet().addAll(newDisease.getDoEatSet());
                }
                if (newDisease.getNoEatSet() != null) {
                    disease.getNoEatSet().addAll(newDisease.getNoEatSet());
                }
                if (newDisease.getCheckSet() != null) {
                    disease.getCheckSet().addAll(newDisease.getCheckSet());
                }
                if (newDisease.getCommonDrugSet() != null) {
                    disease.getCommonDrugSet().addAll(newDisease.getCommonDrugSet());
                }
                if (newDisease.getRecommendDrugSet() != null) {
                    disease.getRecommendDrugSet().addAll(newDisease.getRecommendDrugSet());
                }
            } else {// 新增一个节点
                BeanUtils.copyProperties(newDisease, disease);
            }

            // 保存disease节点
            return ResultVO.ok(diseaseService.saveDisease(disease));
        } catch (BeansException e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }
    }

    @ApiOperation("根据id删除疾病节点")
    @SaCheckRole("admin")
    @DeleteMapping("/deleteDiseaseById")
    public ResultVO deleteDiseaseById(@RequestParam("id") Long id) {
        try {
            diseaseService.deleteDiseaseById(id);
        } catch (BeansException e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok();
    }

    @ApiOperation("按照type删除Disease实体节点的一个关系")
    @SaCheckRole("admin")
    @DeleteMapping("/deleteDiseaseRelationship")
    public ResultVO deleteDiseaseRelationship(@ApiParam("出节点的疾病ID，如：8808") @RequestParam("diseaseId") Long diseaseId,
                                              @ApiParam("入节点的实体节点ID，如：38118") @RequestParam("otherId") Long otherId,
                                              @ApiParam("待删除的关系类型，如：has_symptom") @RequestParam("type") String type) {
        try {
            diseaseService.deleteDiseaseRelationship(diseaseId, otherId, type);
        } catch (Exception e) {
            return ResultVO.errorMsg(new RuntimeException(e).getMessage());
        }

        return ResultVO.ok();
    }

}
