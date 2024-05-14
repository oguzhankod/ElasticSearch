package com.elasticsearch.elasticdemo.controller;

import com.elasticsearch.elasticdemo.dto.SearchRequestDto;
import com.elasticsearch.elasticdemo.model.Item;
import com.elasticsearch.elasticdemo.service.ItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @PostMapping
    public Item createIndexController(Item item){
        return itemService.createIndexService(item);
    }

    @PostMapping("/init-index")
    public void addItemFromJsonController(){
        itemService.addItemFromJsonService();
    }

    @GetMapping("/getAllData/{indexName}")
    public List<Item> getAllDatFromIndexController(@PathVariable String indexName){
        return itemService.getAllDatFromIndexService(indexName);

    }

    @GetMapping("/search")
    public List<Item> searchItemByFieldAndValueController(@RequestBody SearchRequestDto searchRequestDto){
        return itemService.searchItemByFieldAndValueService(searchRequestDto);

    }

    @GetMapping("/search/{name}/{brand}")
    public List<Item> searchItemsByNameAndBrandWithRepoQueryFromController(
            @PathVariable String name, @PathVariable String brand
    ){
        return itemService.searchItemByNameAndBrandWithRepoQueryFromService(name, brand);
    }

    @GetMapping("/boolQuery")
    public List<Item> boolQuery(@RequestBody SearchRequestDto searchRequestDto){
        return itemService.boolQueryFieldAndValue(searchRequestDto);
    }

    @GetMapping("/autoSuggest/{name}")
    public Set<String> autoSuggestItemsByName(@PathVariable String name){
        return itemService.findSuggestedItemNames(name);
    }

    @GetMapping("/suggestionsQuery/{name}")
    public List<String> autoSuggestByNameWithQuery(@PathVariable String name){
        return  itemService.autoQueryByNameWithQueryFromService(name);

    }



}
