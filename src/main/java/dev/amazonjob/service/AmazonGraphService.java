package dev.amazonjob.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.amazonjob.config.AppProps;
import dev.amazonjob.model.AmazonSearchResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AmazonGraphService {

    private  final AppProps appProps;

    private static final Logger log = LoggerFactory.getLogger(AmazonGraphService.class);

    public List<AmazonSearchResponse.JobCard> callAmazonJobs() throws Exception {
//        String payload = """
//                {
//                  "operationName": "searchJobCardsByLocation",
//                  "variables": {
//                    "searchJobRequest": {
//                      "locale": "en-US",
//                      "country": "United States",
//                      "keyWords": "",
//                      "equalFilters": [
//                        { "key": "scheduleRequiredLanguage", "val": "en-US" }
//                      ],
//                      "containFilters": [
//                        { "key": "isPrivateSchedule", "val": ["false"] }
//                      ],
//                      "rangeFilters": [
//                        { "key": "hoursPerWeek", "range": { "minimum": 0, "maximum": 80 } }
//                      ],
//                      "orFilters": [],
//                      "dateFilters": [
//                        { "key": "firstDayOnSite", "range": { "startDate": "2025-08-29" } }
//                      ],
//                      "sorters": [
//                        { "fieldName": "totalPayRateMax", "ascending": "false" }
//                      ],
//                      "pageSize": 100,
//                      "consolidateSchedule": true
//                    }
//                  },
//                  "query": "query searchJobCardsByLocation($searchJobRequest: SearchJobRequest!) {\\n  searchJobCardsByLocation(searchJobRequest: $searchJobRequest) {\\n    nextToken\\n    jobCards {\\n      jobId\\n      language\\n      dataSource\\n      requisitionType\\n      jobTitle\\n      jobType\\n      employmentType\\n      city\\n      state\\n      postalCode\\n      locationName\\n      totalPayRateMin\\n      totalPayRateMax\\n      tagLine\\n      bannerText\\n      image\\n      jobPreviewVideo\\n      distance\\n      featuredJob\\n      bonusJob\\n      bonusPay\\n      scheduleCount\\n      currencyCode\\n      geoClusterDescription\\n      surgePay\\n      jobTypeL10N\\n      employmentTypeL10N\\n      bonusPayL10N\\n      surgePayL10N\\n      totalPayRateMinL10N\\n      totalPayRateMaxL10N\\n      distanceL10N\\n      monthlyBasePayMin\\n      monthlyBasePayMinL10N\\n      monthlyBasePayMax\\n      monthlyBasePayMaxL10N\\n      jobContainerJobMetaL1\\n      virtualLocation\\n      poolingEnabled\\n      payFrequency\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n"
//                }
//                """;

        String payload = """
                {
                  "operationName": "searchJobCardsByLocation",
                  "variables": {
                    "searchJobRequest": {
                      "locale": "en-CA",
                      "country": "Canada",
                      "pageSize": 100,
                      "sorters": [
                        {
                          "fieldName": "totalPayRateMax",
                          "ascending": "false"
                        }
                      ],
                      "dateFilters": [
                        {
                          "key": "firstDayOnSite",
                          "range": {
                            "startDate": "2025-08-29"
                          }
                        }
                      ]
                    }
                  },
                  "query": "query searchJobCardsByLocation($searchJobRequest: SearchJobRequest!) {\\n  searchJobCardsByLocation(searchJobRequest: $searchJobRequest) {\\n    nextToken\\n    jobCards {\\n      jobId\\n      language\\n      dataSource\\n      requisitionType\\n      jobTitle\\n      jobType\\n      employmentType\\n      city\\n      state\\n      postalCode\\n      locationName\\n      totalPayRateMin\\n      totalPayRateMax\\n      tagLine\\n      bannerText\\n      image\\n      jobPreviewVideo\\n      distance\\n      featuredJob\\n      bonusJob\\n      bonusPay\\n      scheduleCount\\n      currencyCode\\n      geoClusterDescription\\n      surgePay\\n      jobTypeL10N\\n      employmentTypeL10N\\n      bonusPayL10N\\n      surgePayL10N\\n      totalPayRateMinL10N\\n      totalPayRateMaxL10N\\n      distanceL10N\\n      monthlyBasePayMin\\n      monthlyBasePayMinL10N\\n      monthlyBasePayMax\\n      monthlyBasePayMaxL10N\\n      jobContainerJobMetaL1\\n      virtualLocation\\n      poolingEnabled\\n      payFrequency\\n      __typename\\n    }\\n    __typename\\n  }\\n}\\n"
                }
                """;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(appProps.getGraphql().getUrl()))
                .header("accept", "*/*")
//                .header("accept-encoding", "gzip, deflate, br, zstd")
                .header("accept-language", "en-NP,en-GB;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("authorization", appProps.getGraphql().getAuthorization())
                .header("content-type", "application/json")
                .header("origin", "https://hiring.amazon.com")
                .header("referer", "https://hiring.amazon.com/")
                .header("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("res:: {}",response);
        log.info("status={}, body={}", response.statusCode(), response.body());

        if (response != null && response.statusCode() == 200 && !response.body().isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                AmazonSearchResponse resp = mapper.readValue(response.body(), AmazonSearchResponse.class);
                List<AmazonSearchResponse.JobCard> cards = resp.data.searchJobCardsByLocation.jobCards;
                return cards;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return new ArrayList<>();
    }
}