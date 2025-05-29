package goorm.deepdive.team1.infra.config.elastic;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
@EnableElasticsearchRepositories(basePackages = "goorm.deepdive.team1.infra.repository.elastic")
public class ElasticConfig {
	private ElasticProperties elasticProperties;

	@Bean
	public ElasticsearchClient elasticsearchClient() {
		RestClient restClient = RestClient.builder(
			HttpHost.create(elasticProperties.getUris())
		).build();

		RestClientTransport transport = new RestClientTransport(
			restClient, new JacksonJsonpMapper()
		);

		return new ElasticsearchClient(transport);
	}
}
