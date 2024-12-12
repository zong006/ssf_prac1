package vttp.ssf_2022_practice.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.ssf_2022_practice.model.News;
import vttp.ssf_2022_practice.repo.NewsRepo;
import vttp.ssf_2022_practice.util.Util;

@Service
public class NewsService {
    
    @Autowired
    NewsRepo newsRepo;

    @Value("${api_key}") 
    private String api_key;

    public List<News> getArticles(){
        
        JsonObject jsonData = generateJson(Util.url + api_key);
        JsonArray data = jsonData.getJsonArray("Data");
        
        List<News> newsList = new ArrayList<>();
        for (int i=0 ; i<data.size() ; i++){
            News news = new News();
            JsonObject article = data.getJsonObject(i);
            news.setId(article.getString("id"));
            news.setBody(article.getString("body"));
            news.setTags(article.getString("tags"));
            news.setCategories(article.getString("categories"));
            news.setTitle(article.getString("title"));
            news.setImageUrl(article.getString("imageurl"));
            news.setUrl(article.getString("url"));
            
            int intDate = article.getInt("published_on");
            String epochString = Integer.toString(intDate);
            // Long epochTime = Long.parseLong(String.valueOf(intDate));
            // System.out.println(epochTime);
            // Date publishedDate = new Date(epochTime);
            news.setPublishedOn(epochString);
            // System.out.println(publishedDate);

            newsList.add(news);
        }
        return newsList;
    }

    public void saveArticles(List<News> newsList){

        for (News n : newsList){
            String key = n.getId();
            String entry = n.toString();
            newsRepo.create(key, entry);
        }
    }

    public News getsNewsById(String id){
        String entry = newsRepo.getById(id);
        News news = new News();
        try {
            String[] terms = entry.split(Util.delimiter);
            news.setId(terms[0]);
            news.setPublishedOn(terms[1]);
            news.setTitle(terms[2]);
            news.setUrl(terms[3]);
            news.setImageUrl(terms[4]);
            news.setBody(terms[5]);
            news.setTags(terms[6]);
            news.setCategories(terms[7]);
        } catch (NullPointerException e) {
            e.printStackTrace();
            news.setId("not found");
        }
        return news;
    }


    public JsonObject generateJson(String url){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        
        String respBody = responseEntity.getBody();
        InputStream is = new ByteArrayInputStream(respBody.getBytes());
        JsonReader jsonReader = Json.createReader(is);
        JsonObject jsonData = jsonReader.readObject();
        return jsonData;
    }

}
