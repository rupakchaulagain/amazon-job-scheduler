package dev.rupesh.jobs;

import dev.rupesh.config.AppProps;
import dev.rupesh.model.AmazonSearchResponse;
import dev.rupesh.service.AmazonGraphService;
import dev.rupesh.service.DiscordClient;
import dev.rupesh.service.GraphqlClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobPoller {
    private static final Logger log = LoggerFactory.getLogger(JobPoller.class);
    private final GraphqlClient graphql;
    private final DiscordClient discord;
    private final AppProps props;
    private final AmazonGraphService amazonGraphService;

    public JobPoller(GraphqlClient graphql, DiscordClient discord, AppProps props, AmazonGraphService amazonGraphService) {
        this.graphql = graphql;
        this.discord = discord;
        this.props = props;
        this.amazonGraphService = amazonGraphService;
    }

    @Scheduled(cron = "${schedule.cron}")
    public void run() {

        try {
            List<AmazonSearchResponse.JobCard> jobCardList = amazonGraphService.callAmazonJobs();
            if (jobCardList.isEmpty()) {
                log.info("No jobs found.");
                return;
            }
            log.info("No of Job found is {}", jobCardList.size());

            StringBuilder sb = new StringBuilder();
            sb.append("**Amazon Hiring - Found ").append(jobCardList.size()).append(" job(s)**\n");
            jobCardList.stream().limit(props.getDiscord().getMaxJobsToSend()).forEach(job -> {
                sb.append("- ").append(job.jobTitle).append("\n");
                sb.append(" (").append(job.currencyCode + " " + job.totalPayRateMin + "-" + job.totalPayRateMax).append("\n");
                sb.append(" (").append(job.locationName)
                        .append(" (").append(props.getPortal().getUrl() + "jobDetail?jobId=" + job.jobId)
                        .append(", ").append(job.state).append(")\n");
            });
            discord.sendSimpleMessage(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        try {
//            List<Map<String, Object>> jobs = graphql.fetchJobCards();
//            if (jobs.isEmpty()) {
//                log.info("No jobs found.");
//                return;
//            }
//            StringBuilder sb = new StringBuilder();
//            sb.append("**Amazon Hiring - Found ").append(jobs.size()).append(" job(s)**\n");
//            jobs.stream().limit(props.getDiscord().getMaxJobsToSend()).forEach(job -> {
//                sb.append("- ").append(job.get("jobTitle"))
//                  .append(" (").append(job.getOrDefault("city",""))
//                  .append(", ").append(job.getOrDefault("state","")).append(")\n");
//            });
//            discord.sendSimpleMessage(sb.toString());
//        } catch (Exception e) {
//            log.error("Error fetching jobs", e);
//        }
    }
}
