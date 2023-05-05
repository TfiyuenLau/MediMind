package edu.hbmu.aidiagnosis.util;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class AnswerSearcher {

    @Autowired
    private SessionFactory sessionFactory;

    public Session getSession() {
        return sessionFactory.openSession();
    }

    /**
     * 执行cypher查询，并返回相应结果
     *
     * @param queries
     * @return
     */
    public List<String> searchMain(List<Map<String, List<String>>> queries) {
        List<String> finalAnswers = new ArrayList<>();
        for (Map<String, List<String>> query : queries) {
            String questionType = query.get("question_type").get(0);
            List<String> queryList = query.get("query");
            List<Map<String, Object>> answers = new ArrayList<>();
            for (String q : queryList) {
                Result result = getSession().query(q, new HashMap<>());
                for (Map<String, Object> queryResult : result.queryResults()) {
                    answers.add(queryResult);
                }
            }
            List<String> finalAnswer = answerPrettify(questionType, answers);
            if (finalAnswer != null && finalAnswer.size() != 0) {
                finalAnswers.addAll(finalAnswer);
            }
        }

        return finalAnswers;
    }

    /**
     * 根据question_type调用回复模板
     *
     * @param questionType
     * @param answers
     * @return
     */
    private List<String> answerPrettify(String questionType, List<Map<String, Object>> answers) {
        List<String> finalAnswer = new ArrayList<>();
        if (answers.size() == 0) {
            return null;
        }

        if (Objects.equals(questionType, "disease_symptom")) {
            List<String> desc = new ArrayList<>();
            for (Map<String, Object> answer : answers) {
                desc.add((String) answer.get("n.name"));
            }
            String subject = (String) answers.get(0).get("m.name");
            finalAnswer.add(String.format("%s的症状包括：%s", subject, desc.stream().limit(20).collect(Collectors.joining("；"))));
        } else if (Objects.equals(questionType, "symptom_disease")) {
            List<String> desc = new ArrayList<>();
            for (Map<String, Object> answer : answers) {
                desc.add((String) answer.get("m.name"));
            }
            String subject = (String) answers.get(0).get("n.name");
            finalAnswer.add(String.format("症状%s可能染上的疾病有：%s", subject, desc.stream().limit(20).collect(Collectors.joining("；"))));
        } else if (Objects.equals(questionType, "disease_cause")) {
            List<String> desc = new ArrayList<>();
            for (Map<String, Object> answer : answers) {
                desc.add((String) answer.get("m.cause"));
            }
            String subject = (String) answers.get(0).get("m.name");
            finalAnswer.add(String.format("%s可能的成因有：%s", subject, desc.stream().limit(20).collect(Collectors.joining("；"))));
        } else if (Objects.equals(questionType, "disease_prevent")) {
            List<String> desc = new ArrayList<>();
            for (Map<String, Object> answer : answers) {
                desc.add((String) answer.get("m.prevent"));
            }
            String subject = (String) answers.get(0).get("m.name");
            finalAnswer.add(String.format("%s的预防措施包括：%s", subject, desc.stream().limit(20).collect(Collectors.joining("；"))));
        } else if (Objects.equals(questionType, "disease_lasttime")) {
            List<String> desc = new ArrayList<>();
            for (Map<String, Object> answer : answers) {
                desc.add((String) answer.get("m.cure_lasttime"));
            }
            String subject = (String) answers.get(0).get("m.name");
            finalAnswer.add(String.format("治疗%s可能持续的周期是：%s", subject, desc.stream().limit(20).collect(Collectors.joining("；"))));
        } else if (Objects.equals(questionType, "disease_cureway")) {
            List<String> desc = new ArrayList<>();
            for (Map<String, Object> answer : answers) {
                // 该查询将获取一个String[]数组，需将其封装成String
                String[] cureWays = (String[]) answer.get("m.cure_way");
                String cureWay = String.join("、", cureWays);
                desc.add(cureWay);
            }
            String subject = (String) answers.get(0).get("m.name");
            finalAnswer.add(String.format("%s可以尝试如下治疗：%s", subject, desc.stream().limit(20).collect(Collectors.joining("；"))));
        } else if (Objects.equals(questionType, "disease_cureprob")) {
            List<String> desc = new ArrayList<>();
            for (Map<String, Object> answer : answers) {
                desc.add((String) answer.get("m.cured_prob"));
            }
            String subject = (String) answers.get(0).get("m.name");
            finalAnswer.add(String.format("%s治愈的概率为（仅供参考）：%s", subject, desc.stream().limit(20).collect(Collectors.joining("；"))));
        } else if (Objects.equals(questionType, "disease_esayget")) {
            List<String> desc = new ArrayList<>();
            for (Map<String, Object> answer : answers) {
                desc.add((String) answer.get("m.easy_get"));
            }
            String subject = (String) answers.get(0).get("m.name");
            finalAnswer.add(String.format("%s的易感人群包括：%s", subject, desc.stream().limit(20).collect(Collectors.joining("；"))));
        } else if (Objects.equals(questionType, "disease_desc")) {
            List<String> desc = new ArrayList<>();
            for (Map<String, Object> answer : answers) {
                desc.add((String) answer.get("m.desc"));
            }
            String subject = (String) answers.get(0).get("m.name");
            finalAnswer.add(String.format("%s定义：%s", subject, desc.stream().limit(20).collect(Collectors.joining("；"))));
        } else if (Objects.equals(questionType, "disease_accompany")) {
            List<String> desc = new ArrayList<>();
            for (Map<String, Object> answer : answers) {
                desc.add((String) answer.get("n.name"));
                desc.add((String) answer.get("m.name"));
            }
            String subject = (String) answers.get(0).get("m.name");
            finalAnswer.add(String.format("%s的并发症包括：%s", subject, desc.stream().limit(20).collect(Collectors.joining("；"))));
        } else if (Objects.equals(questionType, "disease_not_food")) {
            List<String> desc = new ArrayList<>();
            for (Map<String, Object> answer : answers) {
                desc.add((String) answer.get("n.name"));
            }
            String subject = (String) answers.get(0).get("m.name");
            finalAnswer.add(String.format("%s忌食以下食物：%s", subject, desc.stream().limit(20).collect(Collectors.joining("；"))));
        } else if (Objects.equals(questionType, "disease_do_food")) {
            List<String> doDesc = new ArrayList<>();
            List<String> recommendDesc = new ArrayList<>();
            for (Map<String, Object> answer : answers) {
                if (Objects.equals((String) answer.get("r.name"), "宜吃")) {
                    doDesc.add((String) answer.get("n.name"));
                } else if (Objects.equals((String) answer.get("r.name"), "推荐食谱")) {
                    recommendDesc.add((String) answer.get("n.name"));
                }
            }
            String subject = (String) answers.get(0).get("m.name");
            finalAnswer.add(String.format("%s宜食的食物包括有：%s\n另推荐以下食谱：%s", subject,
                    doDesc.stream().limit(20).collect(Collectors.joining("；")),
                    recommendDesc.stream().limit(20).collect(Collectors.joining("；"))));
        } else if (Objects.equals(questionType, "food_not_disease")) {
            List<String> desc = new ArrayList<>();
            for (Map<String, Object> answer : answers) {
                desc.add((String) answer.get("m.name"));
            }
            String subject = (String) answers.get(0).get("n.name");
            finalAnswer.add(String.format("注意：患有%s的人最好不要吃%s", subject, desc.stream().limit(20).collect(Collectors.joining("；"))));
        } else if (Objects.equals(questionType, "food_do_disease")) {
            List<String> desc = new ArrayList<>();
            for (Map<String, Object> answer : answers) {
                desc.add((String) answer.get("m.name"));
            }
            String subject = (String) answers.get(0).get("n.name");
            finalAnswer.add(String.format("注意：患有%s的人建议尝试%s", subject, desc.stream().limit(20).collect(Collectors.joining("；"))));
        } else if (Objects.equals(questionType, "disease_drug")) {
            List<String> desc = new ArrayList<>();
            for (Map<String, Object> answer : answers) {
                desc.add((String) answer.get("n.name"));
            }
            String subject = (String) answers.get(0).get("m.name");
            finalAnswer.add(String.format("%s通常的使用的药物包括：%s", subject, desc.stream().limit(20).collect(Collectors.joining("；"))));
        } else if (Objects.equals(questionType, "drug_disease")) {
            List<String> desc = new ArrayList<>();
            for (Map<String, Object> answer : answers) {
                desc.add((String) answer.get("m.name"));
            }
            String subject = (String) answers.get(0).get("n.name");
            finalAnswer.add(String.format("%s主治的疾病包括%s，可以试试", subject, desc.stream().limit(20).collect(Collectors.joining("；"))));
        } else if (Objects.equals(questionType, "disease_check")) {
            List<String> desc = new ArrayList<>();
            for (Map<String, Object> answer : answers) {
                desc.add((String) answer.get("n.name"));
            }
            String subject = (String) answers.get(0).get("m.name");
            finalAnswer.add(String.format("%s通常可以通过以下方式检查出来：%s", subject, desc.stream().limit(20).collect(Collectors.joining("；"))));
        } else if (Objects.equals(questionType, "check_disease")) {
            List<String> desc = new ArrayList<>();
            for (Map<String, Object> answer : answers) {
                desc.add((String) answer.get("m.name"));
            }
            String subject = (String) answers.get(0).get("n.name");
            finalAnswer.add(String.format("通过%s可以检查出来的疾病有：%s", subject, desc.stream().limit(20).collect(Collectors.joining("；"))));
        }

        return finalAnswer;
    }

}
