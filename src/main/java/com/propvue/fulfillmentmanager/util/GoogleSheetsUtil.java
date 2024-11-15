package com.propvue.fulfillmentmanager.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class GoogleSheetsUtil {

    private static final String API_URL = "https://sheets.googleapis.com/v4/spreadsheets/{spreadsheetId}/values/{range}";

    public static List<List<Object>> getSheetData(String spreadsheetId, String range, String accessToken) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class, spreadsheetId, range);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.getBody());
        JsonNode valuesNode = rootNode.get("values");

        List<List<Object>> data = new ArrayList<>();
        if (valuesNode != null) {
            for (JsonNode rowNode : valuesNode) {
                List<Object> row = new ArrayList<>();
                rowNode.forEach(cellNode -> row.add(cellNode.asText()));
                data.add(row);
            }
        }
        return data;
    }
}

