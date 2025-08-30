package dev.rupesh.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProps {
    private Graphql graphql = new Graphql();
    private Discord discord = new Discord();
    private Portal portal = new Portal();

    public static class Portal {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Graphql {
        private String url;
        private String authorization;
        private String locale;
        private String country;
        private Integer pageSize;
        private String firstDayOnSiteStartDate;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAuthorization() {
            return authorization;
        }

        public void setAuthorization(String authorization) {
            this.authorization = authorization;
        }

        public String getLocale() {
            return locale;
        }

        public void setLocale(String locale) {
            this.locale = locale;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public String getFirstDayOnSiteStartDate() {
            return firstDayOnSiteStartDate;
        }

        public void setFirstDayOnSiteStartDate(String firstDayOnSiteStartDate) {
            this.firstDayOnSiteStartDate = firstDayOnSiteStartDate;
        }
    }

    public static class Discord {
        private String[] webhookUrl;
        private Integer maxJobsToSend;

        public String[] getWebhookUrl() {
            return webhookUrl;
        }

        public void setWebhookUrl(String[] webhookUrl) {
            this.webhookUrl = webhookUrl;
        }

        public Integer getMaxJobsToSend() {
            return maxJobsToSend;
        }

        public void setMaxJobsToSend(Integer maxJobsToSend) {
            this.maxJobsToSend = maxJobsToSend;
        }
    }

    public Graphql getGraphql() {
        return graphql;
    }

    public void setGraphql(Graphql graphql) {
        this.graphql = graphql;
    }

    public Discord getDiscord() {
        return discord;
    }

    public void setDiscord(Discord discord) {
        this.discord = discord;
    }

    public Portal getPortal() {
        return portal;
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }
}
