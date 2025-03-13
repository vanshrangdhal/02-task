package com.configindia.controller;


import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
//create a folder in d-drive with name converted_json
@Controller
public class XmlToJsonController {

    private static final String SAVE_DIR = "D:/converted_json/"; // Change as needed

    @GetMapping("/")
    public String index() {
        return "upload";
    }

    @PostMapping("/convert")
    public String convertXmlToJson(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please upload an XML file.");
            return "upload";
        }

        try {
            // Read XML content
            XmlMapper xmlMapper = new XmlMapper();
            JsonNode jsonNode = xmlMapper.readTree(file.getInputStream());

            // Convert to JSON
            ObjectMapper jsonMapper = new ObjectMapper();
            String jsonContent = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);

            // Save JSON to file
            File jsonFile = new File(SAVE_DIR + file.getOriginalFilename().replace(".xml", ".json"));
            jsonMapper.writeValue(jsonFile, jsonNode);

            model.addAttribute("message", "File converted and saved successfully at: " + jsonFile.getAbsolutePath());
        } catch (IOException e) {
            model.addAttribute("message", "Error processing file: " + e.getMessage());
        }

        return "upload";
    }
}
