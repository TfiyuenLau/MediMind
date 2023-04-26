package edu.hbmu.outpatient.config;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchClientConfig {

    @Bean
    @Qualifier("highLevelClient")
    public RestHighLevelClient restHighLevelClient() {

        return new RestHighLevelClient(
                RestClient.builder(new HttpHost("8.130.39.9", 9200, "http"))
                        .setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
                            @Override
                            public RequestConfig.Builder customizeRequestConfig(
                                    RequestConfig.Builder requestConfigBuilder) {
                                return requestConfigBuilder.setConnectTimeout(5000 * 1000) // 连接超时（默认为1秒）
                                        .setSocketTimeout(6000 * 1000);// 套接字超时（默认为30秒）
                            }
                        }));

    }

}