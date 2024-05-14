package com.elasticsearch.elasticdemo.repository;

import com.elasticsearch.elasticdemo.model.Item;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ItemRepository extends ElasticsearchRepository<Item,String> {

    @Query("{\"bool\": {\"must\": [{\"match\": {\"name\": \"?0\"}}, {\"match\": {\"brand\": \"?1\"}}]}}")
    List<Item> searchByNameAndValue(String name,String brand);


    @Query("{\"bool\": {\"must\": {\"match_phrase_prefix\": {\"name\": \"?0\"}}}}")
    List<Item> customAutoCompleteSearch(String input);
}
