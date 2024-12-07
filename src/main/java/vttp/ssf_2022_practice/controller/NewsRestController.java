package vttp.ssf_2022_practice.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vttp.ssf_2022_practice.model.News;
import vttp.ssf_2022_practice.service.NewsService;

@RestController
@RequestMapping("/news")
public class NewsRestController {

    @Autowired
    NewsService newsService;
    
    @GetMapping("/{id}")
    public ResponseEntity<Object> getNewsById(@PathVariable String id){
        
            News news = newsService.getsNewsById(id);
            if (!news.getId().equals("not found")){
                return ResponseEntity.ok().body(news);
            }   

            Map<String, String> error = new HashMap<>();
            String errorMsg = "Cannot find news article "+id;
            error.put("error", errorMsg);
            
            return ((BodyBuilder) ResponseEntity.notFound()).body(error);

    }

}
