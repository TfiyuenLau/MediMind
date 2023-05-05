package edu.hbmu.aidiagnosis.util;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
public class QuestionClassifier {
    BufferedReader br = null;

    List<String> diseaseWds = new ArrayList<>();
    List<String> departmentWds = new ArrayList<>();
    List<String> checkWds = new ArrayList<>();
    List<String> drugWds = new ArrayList<>();
    List<String> foodWds = new ArrayList<>();
    List<String> producerWds = new ArrayList<>();
    List<String> symptomWds = new ArrayList<>();

    HashSet<String> regionWords = new HashSet<>();
    List<String> denyWds = new ArrayList<>();
    Trie regionTree = null;
    Map<String, List<String>> wordTypeDict = null;

    List<String> symptomQuestionWds = null;
    List<String> causeQuestionWds = null;
    List<String> accompanyQuestionWds = null;
    List<String> foodQuestionWds = null;
    List<String> drugQuestionWds = null;
    List<String> preventQuestionWds = null;
    List<String> lasttimeQuestionWds = null;
    List<String> cureWayQuestionWds = null;
    List<String> cureProbQuestionWds = null;
    List<String> easyGetQuestionWds = null;
    List<String> checkQuestionWds = null;
    List<String> belongQuestionWds = null;
    List<String> cureQuestionWds = null;

    {
        try {
            // 加载特征词
            ClassPathResource diseasePath = new ClassPathResource("/dict/disease.txt");
            File diseaseFile = diseasePath.getFile();
            br = new BufferedReader(new FileReader(diseaseFile));
            String disease = null;
            while ((disease = br.readLine()) != null) {
                if (disease.strip().length() != 0) {
                    diseaseWds.add(disease.strip());
                }
            }
            br.close();

            ClassPathResource departmentPath = new ClassPathResource("/dict/department.txt");
            File departmentFile = departmentPath.getFile();
            br = new BufferedReader(new FileReader(departmentFile));
            String department = null;
            while ((department = br.readLine()) != null) {
                if (department.strip().length() != 0) {
                    departmentWds.add(department.strip());
                }
            }
            br.close();

            ClassPathResource checkPath = new ClassPathResource("/dict/check.txt");
            File checkFile = checkPath.getFile();
            br = new BufferedReader(new FileReader(checkFile));
            String check = null;
            while ((check = br.readLine()) != null) {
                if (check.strip().length() != 0) {
                    checkWds.add(check.strip());
                }
            }
            br.close();

            ClassPathResource drugPath = new ClassPathResource("/dict/drug.txt");
            File drugFile = drugPath.getFile();
            br = new BufferedReader(new FileReader(drugFile));
            String drug = null;
            while ((drug = br.readLine()) != null) {
                if (drug.strip().length() != 0) {
                    drugWds.add(drug.strip());
                }
            }
            br.close();

            ClassPathResource foodPath = new ClassPathResource("/dict/food.txt");
            File foodFile = foodPath.getFile();
            br = new BufferedReader(new FileReader(foodFile));
            String food = null;
            while ((food = br.readLine()) != null) {
                if (food.strip().length() != 0) {
                    foodWds.add(food.strip());
                }
            }
            br.close();

            ClassPathResource producerPath = new ClassPathResource("/dict/producer.txt");
            File producerFile = producerPath.getFile();
            br = new BufferedReader(new FileReader(producerFile));
            String producer = null;
            while ((producer = br.readLine()) != null) {
                if (producer.strip().length() != 0) {
                    producerWds.add(producer.strip());
                }
            }
            br.close();

            ClassPathResource symptomPath = new ClassPathResource("/dict/symptom.txt");
            File symptomFile = symptomPath.getFile();
            br = new BufferedReader(new FileReader(symptomFile));
            String symptom = null;
            while ((symptom = br.readLine()) != null) {
                if (symptom.strip().length() != 0) {
                    symptomWds.add(symptom.strip());
                }
            }
            br.close();

            regionWords.addAll(departmentWds);
            regionWords.addAll(diseaseWds);
            regionWords.addAll(checkWds);
            regionWords.addAll(drugWds);
            regionWords.addAll(foodWds);
            regionWords.addAll(producerWds);
            regionWords.addAll(symptomWds);

            ClassPathResource denyPath = new ClassPathResource("/dict/deny.txt");
            File denyFile = denyPath.getFile();
            br = new BufferedReader(new FileReader(denyFile));
            String deny = null;
            while ((deny = br.readLine()) != null) {
                if (deny.strip().length() != 0) {
                    denyWds.add(deny.strip());
                }
            }
            br.close();

            // 构造领域AcTree
            regionTree = buildAcTree(regionWords);
            // 构造字典
            wordTypeDict = buildWordTypeDict();

            // 初始化问句疑问词
            symptomQuestionWds = Arrays.asList("症状", "表征", "现象", "症候", "表现");
            causeQuestionWds = Arrays.asList("原因", "成因", "为什么", "怎么会", "怎样才", "咋样才", "怎样会", "如何会", "为啥",
                    "为何", "如何才会", "怎么才会", "会导致", "会造成");
            accompanyQuestionWds = Arrays.asList("并发症", "并发", "一起发生", "一并发生", "一起出现", "一并出现", "一同发生", "一同出现", "伴随发生", "伴随", "共现");
            foodQuestionWds = Arrays.asList("饮食", "饮用", "吃", "食", "伙食", "膳食", "喝", "菜", "忌口", "补品", "保健品", "食谱", "菜谱", "食用", "食物", "补品");
            drugQuestionWds = Arrays.asList("药", "药品", "用药", "胶囊", "口服液", "炎片");
            preventQuestionWds = Arrays.asList("预防", "防范", "抵制", "抵御", "防止", "躲避", "逃避", "避开", "免得", "逃开",
                    "避开", "避掉", "躲开", "躲掉", "绕开", "怎样才能不", "怎么才能不", "咋样才能不", "咋才能不", "如何才能不",
                    "怎样才不", "怎么才不", "咋样才不", "咋才不", "如何才不", "怎样才可以不", "怎么才可以不", "咋样才可以不",
                    "咋才可以不", "如何可以不", "怎样才可不", "怎么才可不", "咋样才可不", "咋才可不", "如何可不");
            lasttimeQuestionWds = Arrays.asList("周期", "多久", "多长时间", "多少时间", "几天", "几年", "多少天", "多少小时", "几个小时", "多少年");
            cureWayQuestionWds = Arrays.asList("怎么治疗", "如何医治", "怎么医治", "怎么治", "怎么医", "如何治", "医治方式", "疗法", "咋治", "怎么办", "咋办", "咋治");
            cureProbQuestionWds = Arrays.asList("多大概率能治好", "多大几率能治好", "治好希望大么", "几率", "几成", "比例", "可能性", "能治", "可治", "可以治", "可以医");
            easyGetQuestionWds = Arrays.asList("易感人群", "容易感染", "易发人群", "什么人", "哪些人", "感染", "染上", "得上");
            checkQuestionWds = Arrays.asList("检查", "检查项目", "查出", "检查", "测出", "试出");
            belongQuestionWds = Arrays.asList("属于什么科", "属于", "什么科", "科室");
            cureQuestionWds = Arrays.asList("治疗什么", "治啥", "治疗啥", "医治啥", "治愈啥", "主治啥", "主治什么", "有什么用", "有何用",
                    "用处", "用途", "有什么好处", "有什么益处", "有何益处", "用来", "用来做啥", "用来作甚", "需要", "要");

            System.out.println("Model init finished...");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 分类器
     *
     * @param question
     * @return
     */
    public Map<String, Map<String, List<String>>> classify(String question) {
        Map<String, Map<String, List<String>>> data = new HashMap<>();// 分类结果

        Map<String, List<String>> medicalMap = checkMedical(question);// 获取医学关键词
        if (medicalMap.size() == 0) {
            return null;
        }
        data.put("args", medicalMap);

        // 收集问句当中涉及到的实体类型
        Set<String> types = new HashSet<>();
        for (List<String> type : medicalMap.values()) {
            types.addAll(type);
        }
        String questionType = "others";
        List<String> questionTypes = new ArrayList<>();

        // 症状
        if (checkWords(symptomQuestionWds, question) && types.contains("disease")) {
            questionType = "disease_symptom";
            questionTypes.add(questionType);
        }
        if (checkWords(symptomQuestionWds, question) && types.contains("symptom")) {
            questionType = "symptom_disease";
            questionTypes.add(questionType);
        }

        // 原因
        if (checkWords(causeQuestionWds, question) && types.contains("disease")) {
            questionType = "disease_cause";
            questionTypes.add(questionType);
        }
        // 并发症
        if (checkWords(accompanyQuestionWds, question) && types.contains("disease")) {
            questionType = "disease_accompany";
            questionTypes.add(questionType);
        }

        // 推荐食物
        if (checkWords(foodQuestionWds, question) && types.contains("disease")) {
            boolean denyStatus = checkWords(denyWds, question);
            if (denyStatus) {
                questionType = "disease_not_food";
            } else {
                questionType = "disease_do_food";
            }
            questionTypes.add(questionType);
        }

        // 已知食物找疾病
        if (checkWords(foodWds, question) && types.contains("food")) {
            boolean denyStatus = checkWords(denyWds, question);
            if (denyStatus) {
                questionType = "food_not_disease";
            } else {
                questionType = "food_do_disease";
            }
            questionTypes.add(questionType);
        }

        // 推荐药品
        if (checkWords(drugQuestionWds, question) && types.contains("disease")) {
            questionType = "disease_drug";
            questionTypes.add(questionType);
        }

        // 药品治疗的疾病
        if (checkWords(cureQuestionWds, question) && types.contains("drug")) {
            questionType = "drug_disease";
            questionTypes.add(questionType);
        }

        // 疾病对应的检查
        if (checkWords(checkQuestionWds, question) && types.contains("disease")) {
            questionType = "disease_check";
            questionTypes.add(questionType);
        }

        // 已知检查项目查疾病
        if (checkWords(checkQuestionWds, question) && types.contains("check")) {
            questionType = "check_disease";
            questionTypes.add(questionType);
        }

        // 疾病防治方法
        if (checkWords(preventQuestionWds, question) && types.contains("disease")) {
            questionType = "disease_prevent";
            questionTypes.add(questionType);
        }

        // 疾病医疗周期
        if (checkWords(lasttimeQuestionWds, question) && types.contains("disease")) {
            questionType = "disease_lasttime";
            questionTypes.add(questionType);
        }

        // 疾病治疗方式
        if (checkWords(cureWayQuestionWds, question) && types.contains("disease")) {
            questionType = "disease_cureway";
            questionTypes.add(questionType);
        }

        // 疾病治疗可能
        if (checkWords(cureProbQuestionWds, question) && types.contains("disease")) {
            questionType = "disease_cureprob";
            questionTypes.add(questionType);
        }

        // 疾病易感染人群
        if (checkWords(easyGetQuestionWds, question) && types.contains("disease")) {
            questionType = "disease_easyget";
            questionTypes.add(questionType);
        }

        // 若没有查到相关的消息，则返回疾病描述
        if (questionTypes.size() == 0 && types.contains("disease")) {
            questionTypes.add("disease_desc");
        }
        if (questionTypes.size() == 0 && types.contains("symptom")) {
            questionTypes.add("symptom_disease");
        }

        // 组合成字典Map
        HashMap<String, List<String>> newQuestionTypes = new HashMap<String, List<String>>();
        newQuestionTypes.put("question_types", questionTypes);
        data.put("question_types", newQuestionTypes);

        return data;
    }

    /**
     * 构造一个词对应其类型的字典
     *
     * @return
     */
    private Map<String, List<String>> buildWordTypeDict() {
        Map<String, List<String>> wordMap = new HashMap<>();
        for (String region : regionWords) {
            wordMap.put(region, new ArrayList<String>());
            if (diseaseWds.contains(region)) {
                List<String> value = wordMap.get(region);
                value.add("disease");
                wordMap.put(region, value);
            }
            if (departmentWds.contains(region)) {
                List<String> value = wordMap.get(region);
                value.add("department");
                wordMap.put(region, value);
            }
            if (checkWds.contains(region)) {
                List<String> value = wordMap.get(region);
                value.add("check");
                wordMap.put(region, value);
            }
            if (drugWds.contains(region)) {
                List<String> value = wordMap.get(region);
                value.add("drug");
                wordMap.put(region, value);
            }
            if (foodWds.contains(region)) {
                List<String> value = wordMap.get(region);
                value.add("food");
                wordMap.put(region, value);
            }
            if (symptomWds.contains(region)) {
                List<String> value = wordMap.get(region);
                value.add("symptom");
                wordMap.put(region, value);
            }
            if (producerWds.contains(region)) {
                List<String> value = wordMap.get(region);
                value.add("producer");
                wordMap.put(region, value);
            }
        }

        return wordMap;
    }

    /**
     * AC自动机构造AcTree，加速过滤
     *
     * @param wordList
     * @return
     */
    private Trie buildAcTree(Collection<String> wordList) {
        Trie.TrieBuilder trie = Trie.builder();
        trie.addKeywords(wordList);
        return trie.build();
    }

    /**
     * 问句过滤
     *
     * @param question
     * @return
     */
    private Map<String, List<String>> checkMedical(String question) {
        // 获取关键词并处理
        List<String> regionWds = new ArrayList<>();
        for (Emit emit : regionTree.parseText(question)) {
            regionWds.add(emit.getKeyword());
        }
        List<String> stopWds = new ArrayList<>();
        for (String word1 : regionWds) {
            for (String word2 : regionWds) {
                if (word2.contains(word1) && !word1.equals(word2)) {
                    stopWds.add(word1);
                }
            }
        }

        List<String> finalWds = new ArrayList<>();
        for (String word : regionWds) {
            if (!stopWds.contains(word)) {
                finalWds.add(word);
            }
        }

        Map<String, List<String>> finalMap = new HashMap<>();
        for (String word : finalWds) {
            finalMap.put(word, wordTypeDict.get(word));
        }
        return finalMap;// 最终关键词
    }

    /**
     * 基于特征词进行分类
     *
     * @param words
     * @param sent
     * @return
     */
    private boolean checkWords(List<String> words, String sent) {
        for (String word : words) {
            if (sent.contains(word)) {
                return true;
            }
        }
        return false;
    }

}
