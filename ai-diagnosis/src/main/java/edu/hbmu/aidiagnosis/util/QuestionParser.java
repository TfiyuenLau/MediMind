package edu.hbmu.aidiagnosis.util;

import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class QuestionParser {

    /**
     * 构建实体节点
     *
     * @param args
     * @return
     */
    private Map<String, List<String>> buildEntityMap(Map<String, List<String>> args) {
        Map<String, List<String>> entityMap = new HashMap<>();
        for (String key : args.keySet()) {// 迭代获取args的值，反转形成实体Map
            for (String type : args.get(key)) {
                List<String> value;
                if (!entityMap.containsKey(type)) {
                    value = new ArrayList<>();
                } else {
                    value = entityMap.get(type);
                }
                value.add(key);
                entityMap.put(type, value);
            }
        }
        return entityMap;
    }

    /**
     * 问题解析主函数
     *
     * @param resClassify 关键词分类器结果Map
     * @return
     */
    public List<Map<String, List<String>>> parserMain(Map<String, Map<String, List<String>>> resClassify) {
        Map<String, List<String>> args = resClassify.get("args");
        Map<String, List<String>> entityMap = buildEntityMap(args);
        List<String> questionTypes = resClassify.get("question_types").get("question_types");// 获取问题类型

        // 产生查询结果集合
        List<Map<String, List<String>>> queries = new ArrayList<>();
        for (String questionType : questionTypes) {
            Map<String, List<String>> queryMap = new HashMap<>();
            queryMap.put("question_type", new ArrayList<>(Collections.singleton(questionType)));

            List<String> query = new ArrayList<>();
            if (Objects.equals(questionType, "disease_symptom"))
                query = queryTransfer(questionType, entityMap.get("disease"));

            else if (Objects.equals(questionType, "symptom_disease"))
                query = queryTransfer(questionType, entityMap.get("symptom"));

            else if (Objects.equals(questionType, "disease_cause"))
                query = queryTransfer(questionType, entityMap.get("disease"));

            else if (Objects.equals(questionType, "disease_accompany"))
                query = queryTransfer(questionType, entityMap.get("disease"));

            else if (Objects.equals(questionType, "disease_not_food"))
                query = queryTransfer(questionType, entityMap.get("disease"));

            else if (Objects.equals(questionType, "disease_do_food"))
                query = queryTransfer(questionType, entityMap.get("disease"));

            else if (Objects.equals(questionType, "food_not_disease"))
                query = queryTransfer(questionType, entityMap.get("food"));

            else if (Objects.equals(questionType, "food_do_disease"))
                query = queryTransfer(questionType, entityMap.get("food"));

            else if (Objects.equals(questionType, "disease_drug"))
                query = queryTransfer(questionType, entityMap.get("disease"));

            else if (Objects.equals(questionType, "drug_disease"))
                query = queryTransfer(questionType, entityMap.get("drug"));

            else if (Objects.equals(questionType, "disease_check"))
                query = queryTransfer(questionType, entityMap.get("disease"));

            else if (Objects.equals(questionType, "check_disease"))
                query = queryTransfer(questionType, entityMap.get("check"));

            else if (Objects.equals(questionType, "disease_prevent"))
                query = queryTransfer(questionType, entityMap.get("disease"));

            else if (Objects.equals(questionType, "disease_lasttime"))
                query = queryTransfer(questionType, entityMap.get("disease"));

            else if (Objects.equals(questionType, "disease_cureway"))
                query = queryTransfer(questionType, entityMap.get("disease"));

            else if (Objects.equals(questionType, "disease_cureprob"))
                query = queryTransfer(questionType, entityMap.get("disease"));

            else if (Objects.equals(questionType, "disease_easyget"))
                query = queryTransfer(questionType, entityMap.get("disease"));

            else if (Objects.equals(questionType, "disease_desc"))
                query = queryTransfer(questionType, entityMap.get("disease"));

            if (query.size() != 0) {
                queryMap.put("query", query);
                queries.add(queryMap);
            }

        }
        return queries;
    }

    /**
     * 构建neo4j查询语句
     *
     * @param questionType 问题类型
     * @param entities     实体列表
     * @return
     */
    private List<String> queryTransfer(String questionType, List<String> entities) {
        if (entities == null || entities.size() == 0) {
            return null;
        }

        // 查询语句
        List<String> query = new ArrayList<>();
        switch (questionType) {
            // 查询疾病的原因
            case "disease_cause":
                for (String entity : entities) {
                    query.add(String.format("MATCH (m:Disease) where m.name = '%s' return m.name, m.cause", entity));
                }
                break;

            // 查询疾病的防御措施
            case "disease_prevent":
                for (String entity : entities) {
                    query.add(String.format("MATCH (m:Disease) where m.name = '%s' return m.name, m.prevent", entity));
                }
                break;

            // 查询疾病的持续时间
            case "disease_lasttime":
                for (String entity : entities) {
                    query.add(String.format("MATCH (m:Disease) where m.name = '%s' return m.name, m.cure_lasttime", entity));
                }
                break;

            // 查询疾病的治愈概率
            case "disease_cureprob":
                for (String entity : entities) {
                    query.add(String.format("MATCH (m:Disease) where m.name = '%s' return m.name, m.cured_prob", entity));
                }
                break;

            // 查询疾病的治疗方式
            case "disease_cureway":
                for (String entity : entities) {
                    query.add(String.format("MATCH (m:Disease) where m.name = '%s' return m.name, m.cure_way", entity));
                }
                break;

            // 查询疾病的易发人群
            case "disease_easyget":
                for (String entity : entities) {
                    query.add(String.format("MATCH (m:Disease) where m.name = '%s' return m.name, m.easy_get", entity));
                }
                break;

            // 查询疾病的相关介绍
            case "disease_desc":
                for (String entity : entities) {
                    query.add(String.format("MATCH (m:Disease) where m.name = '%s' return m.name, m.desc", entity));
                }
                break;

            // 查询疾病有哪些症状
            case "disease_symptom":
                for (String entity : entities) {
                    query.add(String.format("MATCH (m:Disease)-[r:has_symptom]->(n:Symptom) where m.name = '%s' return m.name, r.name, n.name", entity));
                }
                break;

            // 查询症状会导致哪些疾病
            case "symptom_disease":
                for (String entity : entities) {
                    query.add(String.format("MATCH (m:Disease)-[r:has_symptom]->(n:Symptom) where n.name = '%s' return m.name, r.name, n.name", entity));
                }
                break;

            // 查询疾病的并发症
            case "disease_accompany":
                for (String entity : entities) {
                    query.add(String.format("MATCH (m:Disease)-[r:acompany_with]->(n:Disease) where m.name = '%s' return m.name, r.name, n.name", entity));
                    query.add(String.format("MATCH (m:Disease)-[r:acompany_with]->(n:Disease) where n.name = '%s' return m.name, r.name, n.name", entity));
                }
                break;
            // 查询疾病的忌口
            case "disease_not_food":
                for (String entity : entities) {
                    query.add(String.format("MATCH (m:Disease)-[r:no_eat]->(n:Food) where m.name = '%s' return m.name, r.name, n.name", entity));
                }
                break;

            // 查询疾病建议吃的东西
            case "disease_do_food":
                for (String entity : entities) {
                    query.add(String.format("MATCH (m:Disease)-[r:do_eat]->(n:Food) where m.name = '%s' return m.name, r.name, n.name", entity));
                    query.add(String.format("MATCH (m:Disease)-[r:recommand_eat]->(n:Food) where m.name = '%s' return m.name, r.name, n.name", entity));
                }
                break;

            // 已知忌口查疾病
            case "food_not_disease":
                for (String entity : entities) {
                    query.add(String.format("MATCH (m:Disease)-[r:no_eat]->(n:Food) where n.name = '%s' return m.name, r.name, n.name", entity));
                }
                break;

            // 已知推荐查疾病
            case "food_do_disease":
                for (String entity : entities) {
                    query.add(String.format("MATCH (m:Disease)-[r:do_eat]->(n:Food) where n.name = '%s' return m.name, r.name, n.name", entity));
                    query.add(String.format("MATCH (m:Disease)-[r:recommand_eat]->(n:Food) where n.name = '%s' return m.name, r.name, n.name", entity));
                }
                break;

            // 查询疾病常用药品－药品别名记得扩充
            case "disease_drug":
                for (String entity : entities) {
                    query.add(String.format("MATCH (m:Disease)-[r:common_drug]->(n:Drug) where m.name = '%s' return m.name, r.name, n.name", entity));
                    query.add(String.format("MATCH (m:Disease)-[r:recommand_drug]->(n:Drug) where m.name = '%s' return m.name, r.name, n.name", entity));
                }
                break;

            // 已知药品查询能够治疗的疾病
            case "drug_disease":
                for (String entity : entities) {
                    query.add(String.format("MATCH (m:Disease)-[r:common_drug]->(n:Drug) where n.name = '%s' return m.name, r.name, n.name", entity));
                    query.add(String.format("MATCH (m:Disease)-[r:recommand_drug]->(n:Drug) where n.name = '%s' return m.name, r.name, n.name", entity));
                }
                break;

            // 查询疾病应该进行的检查
            case "disease_check":
                for (String entity : entities) {
                    query.add(String.format("MATCH (m:Disease)-[r:need_check]->(n:Check) where m.name = '%s' return m.name, r.name, n.name", entity));
                }
                break;

            // 已知检查查询疾病
            case "check_disease":
                for (String entity : entities) {
                    query.add(String.format("MATCH (m:Disease)-[r:need_check]->(n:Check) where n.name = '%s' return m.name, r.name, n.name", entity));
                }
                break;
        }

        return query;
    }

}
