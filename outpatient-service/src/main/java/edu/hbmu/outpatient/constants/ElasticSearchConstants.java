package edu.hbmu.outpatient.constants;

public class ElasticSearchConstants {

    public static final String MEDICINE_MAPPING = "{\n" +
            "\t\"settings\": {\n" +
            "\t\t\"analysis\": {\n" +
            "\t\t\t\"analyzer\": {\n" +
            "\t\t\t\t\"my_analyzer\": {\n" +
            "\t\t\t\t\t\"tokenizer\": \"ik_max_word\",\n" +
            "\t\t\t\t\t\"filter\": \"py\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"completion_analyzer\": {\n" +
            "\t\t\t\t\t\"tokenizer\": \"keyword\",\n" +
            "\t\t\t\t\t\"filter\": \"py\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t},\n" +
            "\t\t\t\"filter\": {\n" +
            "\t\t\t\t\"py\": {\n" +
            "\t\t\t\t\t\"type\": \"pinyin\",\n" +
            "\t\t\t\t\t\"keep_full_pinyin\": false,\n" +
            "\t\t\t\t\t\"keep_joined_full_pinyin\": true,\n" +
            "\t\t\t\t\t\"keep_original\": true,\n" +
            "\t\t\t\t\t\"limit_first_letter_length\": 16,\n" +
            "\t\t\t\t\t\"remove_duplicated_term\": true,\n" +
            "\t\t\t\t\t\"none_chinese_pinyin_tokenize\": false\n" +
            "\t\t\t\t}\n" +
            "\t\t\t}\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t\"mappings\": {\n" +
            "\t\t\"properties\": {\n" +
            "\t\t\t\"medicineName\": {\n" +
            "\t\t\t\t\"type\": \"text\",\n" +
            "\t\t\t\t\"analyzer\": \"my_analyzer\",\n" +
            "\t\t\t\t\"search_analyzer\": \"ik_smart\",\n" +
            "\t\t\t\t\"copy_to\": \"all\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"medicineDescription\": {\n" +
            "\t\t\t\t\"type\": \"text\",\n" +
            "\t\t\t\t\"analyzer\": \"my_analyzer\",\n" +
            "\t\t\t\t\"search_analyzer\": \"ik_smart\",\n" +
            "\t\t\t\t\"copy_to\": \"all\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"all\": {\n" +
            "\t\t\t\t\"type\": \"text\",\n" +
            "\t\t\t\t\"analyzer\": \"my_analyzer\",\n" +
            "\t\t\t\t\"search_analyzer\": \"ik_smart\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"suggestion\": {\n" +
            "\t\t\t\t\"type\": \"completion\",\n" +
            "\t\t\t\t\"analyzer\": \"completion_analyzer\"\n" +
            "\t\t\t}\n" +
            "\t\t}\n" +
            "\t}\n" +
            "}";

    public static final String DISEASE_MAPPING = "{\n" +
            "\t\"settings\": {\n" +
            "\t\t\"analysis\": {\n" +
            "\t\t\t\"analyzer\": {\n" +
            "\t\t\t\t\"my_analyzer\": {\n" +
            "\t\t\t\t\t\"tokenizer\": \"ik_max_word\",\n" +
            "\t\t\t\t\t\"filter\": \"py\"\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"completion_analyzer\": {\n" +
            "\t\t\t\t\t\"tokenizer\": \"keyword\",\n" +
            "\t\t\t\t\t\"filter\": \"py\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t},\n" +
            "\t\t\t\"filter\": {\n" +
            "\t\t\t\t\"py\": {\n" +
            "\t\t\t\t\t\"type\": \"pinyin\",\n" +
            "\t\t\t\t\t\"keep_full_pinyin\": false,\n" +
            "\t\t\t\t\t\"keep_joined_full_pinyin\": true,\n" +
            "\t\t\t\t\t\"keep_original\": true,\n" +
            "\t\t\t\t\t\"limit_first_letter_length\": 16,\n" +
            "\t\t\t\t\t\"remove_duplicated_term\": true,\n" +
            "\t\t\t\t\t\"none_chinese_pinyin_tokenize\": false\n" +
            "\t\t\t\t}\n" +
            "\t\t\t}\n" +
            "\t\t}\n" +
            "\t},\n" +
            "\t\"mappings\": {\n" +
            "\t\t\"properties\": {\n" +
            "\t\t\t\"diseaseName\": {\n" +
            "\t\t\t\t\"type\": \"text\",\n" +
            "\t\t\t\t\"analyzer\": \"my_analyzer\",\n" +
            "\t\t\t\t\"search_analyzer\": \"ik_smart\",\n" +
            "\t\t\t\t\"copy_to\": \"all\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"diseaseDescription\": {\n" +
            "\t\t\t\t\"type\": \"text\",\n" +
            "\t\t\t\t\"analyzer\": \"my_analyzer\",\n" +
            "\t\t\t\t\"search_analyzer\": \"ik_smart\",\n" +
            "\t\t\t\t\"copy_to\": \"all\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"all\": {\n" +
            "\t\t\t\t\"type\": \"text\",\n" +
            "\t\t\t\t\"analyzer\": \"my_analyzer\",\n" +
            "\t\t\t\t\"search_analyzer\": \"ik_smart\"\n" +
            "\t\t\t},\n" +
            "\t\t\t\"suggestion\": {\n" +
            "\t\t\t\t\"type\": \"completion\",\n" +
            "\t\t\t\t\"analyzer\": \"completion_analyzer\"\n" +
            "\t\t\t}\n" +
            "\t\t}\n" +
            "\t}\n" +
            "}";

}
