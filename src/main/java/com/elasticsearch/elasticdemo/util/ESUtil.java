package com.elasticsearch.elasticdemo.util;

import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.util.ObjectBuilder;
import com.elasticsearch.elasticdemo.dto.SearchRequestDto;
import lombok.experimental.UtilityClass;

import java.util.function.Function;
import java.util.function.Supplier;


@UtilityClass
public class ESUtil {

    public static Query createMatchAllQuery(){

        return Query.of(queri ->queri.matchAll(new MatchAllQuery.Builder().build()));
    }

    public static Supplier<Query> buildQueryForAndValue(String fieldName,String searchValue){

        return ()->Query.of(queri->queri.match(buildMatchQueryFieldAndValue(fieldName,searchValue)));
    }

    public MatchQuery buildMatchQueryFieldAndValue(String fieldName,String searchValue){

        return new MatchQuery.Builder()
                .field(fieldName)
                .query(searchValue)
                .build();
    }

    public static Supplier<Query> createBoolQuery(SearchRequestDto searchRequestDto){

        return  ()->Query.of(q->q.bool(boolQuery(searchRequestDto.getFieldName().get(0).toString(),searchRequestDto.getSearchValue().get(0),
                searchRequestDto.getFieldName().get(1).toString(),searchRequestDto.getSearchValue().get(1))));
    }

    private static BoolQuery boolQuery(String key1, String value1, String key2, String value2) {
        return new BoolQuery.Builder()
                .filter(termQuery(key1,value1))
                .must(matchQuery(key2,value2))
                .build();
    }

    public static Query termQuery(String fieldName,String valueName){

        return Query.of(q->q.term(new TermQuery.Builder().
                field(fieldName)
                .value(valueName).
                build()));
    }

    public static Query matchQuery(String fieldName,String valueName){

        return Query.of(q->q.match(new MatchQuery.Builder()
                .field(fieldName)
                .query(valueName)
                .build()));
    }

    public static Query buildAutoSuggestQuery(String name){
        return Query.of(q->q.match(createAutoSuggestMatchQuery(name)));
    }

    private static MatchQuery createAutoSuggestMatchQuery(String name) {
        return new MatchQuery.Builder()
                .field("name")
                .query(name)
                .analyzer("custom_index")
                .build();

    }


}
