package com.elasticsearch.elasticdemo.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.elasticsearch.elasticdemo.dto.SearchRequestDto;
import com.elasticsearch.elasticdemo.model.Item;
import com.elasticsearch.elasticdemo.repository.ItemRepository;
import com.elasticsearch.elasticdemo.util.ESUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final JSONDataService jsonDataService;
    private final ElasticsearchClient elasticsearchClient;

    public ItemService(ItemRepository itemRepository, JSONDataService jsonDataService, ElasticsearchClient elasticsearchClient) {
        this.itemRepository = itemRepository;
        this.jsonDataService = jsonDataService;
        this.elasticsearchClient = elasticsearchClient;
    }


    public Item createIndexService(Item item){
        return itemRepository.save(item);
    }

    public void addItemFromJsonService() {
        List<Item> items = jsonDataService.readItemsFromJson();
        itemRepository.saveAll(items);
    }

    public List<Item> getAllDatFromIndexService(String indexName) {

        Query query = ESUtil.createMatchAllQuery();

        SearchResponse<Item> response;
        try {
            response = elasticsearchClient.search(q->q.query(query), Item.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return extraReturnMethod(response);

    }

    public List<Item> searchItemByFieldAndValueService(SearchRequestDto searchRequestDto) {

        SearchResponse<Item> response;

        Supplier<Query> supplier = ESUtil.buildQueryForAndValue(searchRequestDto.getFieldName().get(0),
                searchRequestDto.getSearchValue().get(0));

        try {
            response = elasticsearchClient.search(q->q.index("items_index").query(supplier.get()), Item.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return extraReturnMethod(response);

    }

    public List<Item> searchItemByNameAndBrandWithRepoQueryFromService(String name,String brand){

        return itemRepository.searchByNameAndValue(name,brand);
    }

    public List<Item> boolQueryFieldAndValue(SearchRequestDto searchRequestDto){

        SearchResponse<Item> response;

        var query = ESUtil.createBoolQuery(searchRequestDto);

        try {
            response = elasticsearchClient.search(q->q.index("items_index").query((Query) query), Item.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return extraReturnMethod(response);
    }





    public Set<String> findSuggestedItemNames(String itemName) {

        SearchResponse<Item> response;
        Query query =  ESUtil.buildAutoSuggestQuery(itemName);

        try {
            response = elasticsearchClient.search(q->q.index("items_index").query(query), Item.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return extraSetMetodu(response);

    }

    public Set<String> extraSetMetodu(SearchResponse<Item> searchResponse){

        return searchResponse
                .hits()
                .hits()
                .stream()
                .map(Hit::source)
                .map(Item::getName)
                .collect(Collectors.toSet());
    }

    public List<Item> extraReturnMethod(SearchResponse<Item> itemSearchResponse){
        return itemSearchResponse
                .hits()
                .hits()
                .stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    public List<String> autoQueryByNameWithQueryFromService(String name) {
        List<Item> items = itemRepository.customAutoCompleteSearch(name);

        return items
                .stream()
                .map(Item::getName)
                .collect(Collectors.toList());


    }
}
