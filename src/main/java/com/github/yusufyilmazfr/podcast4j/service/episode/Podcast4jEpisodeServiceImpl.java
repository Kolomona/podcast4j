package com.github.yusufyilmazfr.podcast4j.service.episode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.yusufyilmazfr.podcast4j.arg.service.episode.ByFeedIdArg;
import com.github.yusufyilmazfr.podcast4j.config.Config;
import com.github.yusufyilmazfr.podcast4j.entity.Episode;
import com.github.yusufyilmazfr.podcast4j.response.EpisodeResponse;
import com.github.yusufyilmazfr.podcast4j.util.HttpRequestUtil;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static com.github.yusufyilmazfr.podcast4j.constant.Constant.BASE_API_V1_URL;
import static com.github.yusufyilmazfr.podcast4j.util.HttpRequestUtil.toQueryParams;

@RequiredArgsConstructor
public class Podcast4jEpisodeServiceImpl implements Podcast4jEpisodeService {
    private final Config config;
    private final ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newBuilder()
                                                    .followRedirects(HttpClient.Redirect.NEVER)
                                                    .build();

    @Override
    public List<Episode> getEpisodesByFeedId(ByFeedIdArg arg) throws IOException, InterruptedException, URISyntaxException {
        String queryParams = toQueryParams(arg.toParams());
        String url = BASE_API_V1_URL + "/episodes/byfeedid?" + queryParams;

        HttpRequest request = HttpRequestUtil.with(config)
                                             .uri(new URI(url))
                                             .build();

        HttpResponse<String> content = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(content.body(), EpisodeResponse.class).getEpisodes();
    }
}