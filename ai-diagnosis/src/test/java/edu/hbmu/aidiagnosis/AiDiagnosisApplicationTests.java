package edu.hbmu.aidiagnosis;

import edu.hbmu.aidiagnosis.dao.DiseaseRepository;
import edu.hbmu.aidiagnosis.domain.node.Disease;
import edu.hbmu.aidiagnosis.domain.node.Drug;
import edu.hbmu.aidiagnosis.service.IDiseaseService;
import edu.hbmu.aidiagnosis.service.IDrugService;
import edu.hbmu.aidiagnosis.util.AnswerSearcher;
import edu.hbmu.aidiagnosis.util.QuestionClassifier;
import edu.hbmu.aidiagnosis.util.QuestionParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class AiDiagnosisApplicationTests {

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Autowired
    private IDiseaseService diseaseService;

    @Autowired
    private IDrugService drugService;

    @Autowired
    private QuestionClassifier questionClassifier;

    @Autowired
    private QuestionParser questionParser;

    @Autowired
    private AnswerSearcher answerSearcher;

    @Test
    void contextLoads() {
    }

    @Test
    void testNeo4jMatch() {
        Optional<Disease> optionalDisease = diseaseRepository.findById(1L);
        Disease disease = optionalDisease.get();
        System.out.println(disease);
    }

    @Test
    void testQuestionClassifier() {
        ArrayList<String> testList = new ArrayList<>(Arrays.asList("乳腺癌的症状有哪些？", "最近老流鼻涕怎么办？","为什么有的人会失眠？"));
        for (String question : testList) {
            Map<String, Map<String, List<String>>> res = questionClassifier.classify(question);
            for (String key : res.keySet()) {
                Map<String, List<String>> map = res.get(key);
                for (String args : map.keySet()) {
                    for (String output : map.get(args)) {
                        System.out.println(args + "——" + output);
                    }
                }
            }
        }
    }

    @Test
    void testQuestionParser() {
        ArrayList<String> testList = new ArrayList<>(Arrays.asList("乳腺癌的症状有哪些？", "最近老流鼻涕怎么办？","为什么有的人会失眠？失眠有哪些并发症？"));
        for (String question : testList) {
            Map<String, Map<String, List<String>>> resClassify = questionClassifier.classify(question);
            List<Map<String, List<String>>> resQueries = questionParser.parserMain(resClassify);
            for (Map<String, List<String>> resQuery : resQueries) {
                for (String questionType : resQuery.keySet()) {
                    System.out.print("questionType为" + questionType + "——Query语句:");
                    Collection<String> queries = resQuery.get(questionType);
                    for (String query : queries) {
                        System.out.print(query + ";");
                    }
                    System.out.println();
                }
            }
            for (String result : answerSearcher.searchMain(resQueries)) {
                    System.err.println(result);
            }
        }
    }

    @Test
    void testAnswerSearcher() {
        String question = "为什么有的人会失眠？";

        String answer = "您好！我是基于kg的AI辅助诊断助手。我暂时无法回答您的问题...请咨询工作人员！";
        Map<String, Map<String, List<String>>> resClassify = questionClassifier.classify(question);
        if (resClassify.size() == 0) {
            System.out.println(answer);
            return;
        }
        List<Map<String, List<String>>> resQuery = questionParser.parserMain(resClassify);

        List<String> finalAnswer = answerSearcher.searchMain(resQuery);
        if (finalAnswer.size() == 0) {
            System.out.println(answer);
        } else {
            System.out.println(String.join("\n", finalAnswer));
        }
    }

    @Test
    void testFindDiseaseBySymptom() {
        List<Disease> diseaseList = diseaseService.findDiseaseBySymptom("唇干舌燥");
        for (Disease disease : diseaseList) {
            System.out.println(disease.getId() + "." + disease.getName() + ":" + disease.getSymptomSet());
        }
    }

    @Test
    void testFindDiseaseBySymptoms() {
        List<Map.Entry<Disease, Integer>> diseaseList = diseaseService.findDiseaseBySymptoms(Set.of("唇干舌燥", "食欲不振"));
        for (Map.Entry<Disease, Integer> diseaseEntry : diseaseList) {
            System.out.println(diseaseEntry.getKey().getName() + "-" + diseaseEntry.getValue());
        }
    }

    @Test
    void testFindDrugByDisease() {
        List<Drug> drugList1 = drugService.findCommonDrugByDisease("苍耳中毒");
        for (Drug drug : drugList1) {
            System.out.println(drug.getId() + "." + drug.getName());
        }
        System.out.println("-----------");
        List<Drug> drugList2 = drugService.findRecommendDrugByDisease("苍耳中毒");
        for (Drug drug : drugList2) {
            System.out.println(drug.getId() + "." + drug.getName());
        }
    }

    @Test
    void testFindDiseaseById() {
        Disease disease = diseaseRepository.findDiseaseById(1L);
        System.out.println(disease);
    }
}
