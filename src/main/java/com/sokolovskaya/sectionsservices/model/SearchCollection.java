package com.sokolovskaya.sectionsservices.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchCollection {

    List<String> searchCollectionEl;
    String searchCollection;

}
