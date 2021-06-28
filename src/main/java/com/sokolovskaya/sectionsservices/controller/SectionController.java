package com.sokolovskaya.sectionsservices.controller;

import com.sokolovskaya.sectionsservices.model.SearchCollection;
import com.sokolovskaya.sectionsservices.service.SectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SectionController {

    private final SectionService sectionService;

    @GetMapping("/section")
    public String getSectionPage(Model model) {

        model.addAttribute("searchCollection",  new SearchCollection(){
        });

        return "section";
    }

    @PostMapping("/section")

    public String getServicesForSections(Model model, SearchCollection searchCollection) throws IOException {

        model.addAttribute("categories", sectionService.findSectionService(searchCollection));
        model.addAttribute("checkedSections", sectionService.checkedSectionNames(searchCollection));

        return "section";
    }
}