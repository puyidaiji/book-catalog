package com.example.bookcatalog.factory;

import com.example.bookcatalog.service.strategy.SearchStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SearchStrategyFactory {

    private final Map<String, SearchStrategy> strategies;

    @Autowired
    public SearchStrategyFactory(List<SearchStrategy> strategyList) {
        strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        strategy -> strategy.getClass().getSimpleName().replace("SearchStrategy", "").toLowerCase(),
                        Function.identity()
                ));
    }

    public SearchStrategy getStrategy(String type) {
        if (type == null || !strategies.containsKey(type.toLowerCase())) {
            throw new IllegalArgumentException("Invalid search type: " + type);
        }
        return strategies.get(type.toLowerCase());
    }
}