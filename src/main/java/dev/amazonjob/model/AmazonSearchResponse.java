package dev.amazonjob.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AmazonSearchResponse {

    public Data data;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        public SearchJobCardsByLocation searchJobCardsByLocation;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SearchJobCardsByLocation {
        public String nextToken;
        public List<JobCard> jobCards;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JobCard {
        public String jobId;
        public String jobTitle;
        public String city;
        public String state;
        public Double totalPayRateMax;

        // Optional fields from your query (included for completeness; safe if missing)
        public String language;
        public String dataSource;
        public String requisitionType;
        public String jobType;
        public String employmentType;
        public String postalCode;
        public String locationName;
        public Double totalPayRateMin;
        public String tagLine;
        public String bannerText;
        public String image;
        public String jobPreviewVideo;
        public Double distance;
        public Boolean featuredJob;
        public Boolean bonusJob;
        public Double bonusPay;
        public Integer scheduleCount;
        public String currencyCode;
        public String geoClusterDescription;
        public Double surgePay;
        public String jobTypeL10N;
        public String employmentTypeL10N;
        public String bonusPayL10N;
        public String surgePayL10N;
        public String totalPayRateMinL10N;
        public String totalPayRateMaxL10N;
        public String distanceL10N;
        public Double monthlyBasePayMin;
        public String monthlyBasePayMinL10N;
        public Double monthlyBasePayMax;
        public String monthlyBasePayMaxL10N;
        public String jobContainerJobMetaL1;
        public Boolean virtualLocation;
        public Boolean poolingEnabled;
        public String payFrequency;
        public String __typename;
    }
}
