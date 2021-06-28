package com.sokolovskaya.sectionsservices.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sokolovskaya.sectionsservices.model.Category;
import com.sokolovskaya.sectionsservices.model.SearchCollection;
import com.sokolovskaya.sectionsservices.model.Section;
import com.sokolovskaya.sectionsservices.model.SectionsCollection;
import com.sokolovskaya.sectionsservices.model.ServicesCollection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SectionService {

    public List<String> checkedSectionNames(SearchCollection searchCollection) throws IOException {

        SectionsCollection sectionsCollection = convertToSectionsCollection(new File("src/files/", "Sections.json"));

        List<String> sectionNames = sectionsCollection.getSections()
                .stream()
                .map(Section::getName)
                .collect(Collectors.toList());

        searchCollection.setSearchCollectionEl(Arrays.stream(searchCollection.getSearchCollection()
                .split("\\n"))
                .map(String::trim)
                .collect(Collectors.toList()));

        return searchCollection.getSearchCollectionEl()
                .stream()
                .filter(sectionNames::contains)
                .collect(Collectors.toList());
    }

    public Set<Category> findSectionService(SearchCollection searchCollection) throws IOException {

        SectionsCollection sectionsCollection = convertToSectionsCollection(new File("src/files/", "Sections.json"));
        ServicesCollection servicesCollection = convertToServicesCollection(new File("src/files/", "Services.json"));

        List<String> searchServiceNames = new ArrayList<>();

        checkedSectionNames(searchCollection).forEach(name -> sectionsCollection.getSections()
                .stream().filter(section -> section.getName().equals(name))
                .collect(Collectors.toList())
                .forEach(section -> searchServiceNames.addAll(section.getServices())));

        Set<com.sokolovskaya.sectionsservices.model.Service> services = new HashSet<>();

        searchServiceNames.forEach(name -> services.addAll(servicesCollection.getServices()
                .stream().filter(service -> service.getName().equals(name))
                .collect(Collectors.toSet())));

        Set<Category> categories = new HashSet<>();

        services.forEach(service -> service.getCategories()
                .forEach(category -> categories.add(Category.builder()
                        .name(category)
                        .build())));

        categories.forEach(category -> category.setServices(services.stream()
                .filter(service -> service.getCategories().contains(category.getName()))
                .map(com.sokolovskaya.sectionsservices.model.Service::getName)
                .collect(Collectors.toSet())));

        return categories;
    }


    private SectionsCollection convertToSectionsCollection(File file) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        return SectionsCollection.builder()
                .sections(Arrays.asList(mapper.readValue(file, Section[].class)))
                .build();
    }

    private ServicesCollection convertToServicesCollection(File file) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);

        return ServicesCollection.builder()
                .services(Arrays.asList(mapper.readValue(file, com.sokolovskaya.sectionsservices.model.Service[].class)))
                .build();
    }

}