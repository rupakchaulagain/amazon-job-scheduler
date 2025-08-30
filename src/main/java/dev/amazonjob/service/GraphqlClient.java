package dev.amazonjob.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.amazonjob.config.AppProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GraphqlClient {
    private static final Logger log = LoggerFactory.getLogger(GraphqlClient.class);
    private final WebClient http;
    private final AppProps props;
    private final ObjectMapper mapper = new ObjectMapper();

    public GraphqlClient(WebClient http, AppProps props) {
        this.http = http;
        this.props = props;
    }

    public List<Map<String, Object>> fetchJobCards() throws Exception {
        String startDate = props.getGraphql().getFirstDayOnSiteStartDate();
        if (startDate == null || startDate.isEmpty()) {
            startDate = LocalDate.now().toString();
        }

        Map<String, Object> payload = buildPayload(startDate);

        try {
            log.info("Payload:: {}", payload);
            String response = http.post()
                    .uri(props.getGraphql().getUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("authorization", props.getGraphql().getAuthorization())
                    .header("origin", "https://hiring.amazon.com")
                    .header("referer", "https://hiring.amazon.com/")
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (response == null) return List.of();
            JsonNode root = mapper.readTree(response);
            JsonNode cardsNode = root.path("data").path("searchJobCardsByLocation").path("jobCards");
            if (!cardsNode.isArray()) return List.of();
            List<Map<String, Object>> jobs = new ArrayList<>();
            for (JsonNode n : cardsNode) {
                jobs.add(mapper.convertValue(n, Map.class));
            }
            return jobs;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private Map<String, Object> buildPayload(String startDate) {
        String query = "query searchJobCardsByLocation($searchJobRequest: SearchJobRequest!) { searchJobCardsByLocation(searchJobRequest: $searchJobRequest) { jobCards { jobId jobTitle city state totalPayRateMax } } }";
        Map<String, Object> req = new HashMap<>();
        req.put("locale", props.getGraphql().getLocale());
        req.put("country", props.getGraphql().getCountry());
        req.put("equalFilters", List.of(Map.of("key", "scheduleRequiredLanguage", "val", props.getGraphql().getLocale())));
        req.put("containFilters", List.of(Map.of("key", "isPrivateSchedule", "val", List.of("false"))));
        req.put("rangeFilters", List.of(Map.of("key", "hoursPerWeek", "range", Map.of("minimum", 0, "maximum", 80))));
        req.put("orFilters", List.of());
        req.put("dateFilters", List.of(Map.of("key", "firstDayOnSite", "range", Map.of("startDate", startDate))));
        req.put("sorters", List.of(Map.of("fieldName", "totalPayRateMax", "ascending", "false")));
        req.put("pageSize", props.getGraphql().getPageSize());
        req.put("consolidateSchedule", true);
        return Map.of("operationName", "searchJobCardsByLocation", "query", query, "variables", Map.of("searchJobRequest", req));
    }
}
