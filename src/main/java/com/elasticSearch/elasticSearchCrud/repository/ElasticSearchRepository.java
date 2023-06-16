package com.elasticSearch.elasticSearchCrud.repository;


import com.elasticSearch.elasticSearchCrud.entity.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ElasticSearchRepository {
    public static final String INDEX_NAME = "employee";
    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private RestHighLevelClient restClient;

    public boolean createEmployee(Employee employee) {
        try {
            // making json object and storing it into builder
        //            XContentBuilder builder = jsonBuilder()
        //                    .startObject()
        //                    .field("firstname", employee.getFirstname())
        //                    .field("lastname", employee.getLastname())
        //                    .endObject();
           //or we can use it in simple way
            @SuppressWarnings("unchecked")
            Map<String,Object> builder = objectMapper.convertValue(employee,Map.class);

            //indexrequesting for the builder object and storing it in elastic search.
            IndexRequest indexRequest = new IndexRequest(INDEX_NAME,"emp")
                    .source(builder)
                    .setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);

            IndexResponse response = restClient.index(indexRequest, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            // Handle exception
            return false;
        }
    }
    public List<Employee> getAllEmployees() {
        try {
            //Creates a new SearchRequest object, specifying the index name to search in.
            SearchRequest searchRequest = new SearchRequest(INDEX_NAME);

            //Creates a SearchSourceBuilder object, which is used to build the search query.
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            //Specifies the query for the search request. In this case, it uses QueryBuilders.matchAllQuery() to match all documents in the index.
            searchSourceBuilder.query(QueryBuilders.matchAllQuery());

            //Sets the SearchSourceBuilder as the source for the SearchRequest.
            searchRequest.source(searchSourceBuilder);

            //Sends the SearchRequest to Elasticsearch using the restClient and retrieves the SearchResponse, which contains the search results.
            SearchResponse response = restClient.search(searchRequest, RequestOptions.DEFAULT);

            //Extracts the array of SearchHit objects from the SearchResponse. Each SearchHit represents a document matching the search query.
            //response.getHits() returns an instance of SearchHits . then next getHits is used for getting array of objects.
            SearchHit[] hits = response.getHits().getHits();

            List<Employee> employees = new ArrayList<>();

            for (SearchHit hit : hits) {
                Map<String, Object> source = hit.getSourceAsMap();
                Employee employee = new Employee();
                employee.setEmployeeid(hit.getId());
                employee.setFirstname((String) source.get("firstname"));
                employee.setLastname((String) source.get("lastname"));
                employees.add(employee);
            }
            return employees;
        } catch (IOException e) {
            // Handle exception
            return null;
        }
    }
    public boolean deleteEmployee(String id) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest(INDEX_NAME,"emp", id);
            restClient.delete(deleteRequest, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            // Handle exception
            System.out.println("error in deleting . Please check");
            return false;
        }
    }
    public boolean updateEmployee(String id ,Employee employee) throws IOException {
        @SuppressWarnings("unchecked")
        Map<String,Object> builder = objectMapper.convertValue(employee,Map.class);

        UpdateRequest request = new UpdateRequest(INDEX_NAME, "emp",id)
                .doc(builder);
        System.out.println(employee.getFirstname()+" this is the firstname");
        try {
            UpdateResponse response = restClient.update(request, RequestOptions.DEFAULT);

            if (response.getResult() == DocWriteResponse.Result.UPDATED) {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

}
