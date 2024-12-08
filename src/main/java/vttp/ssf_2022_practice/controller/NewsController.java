package vttp.ssf_2022_practice.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp.ssf_2022_practice.model.News;
import vttp.ssf_2022_practice.service.NewsService;

@Controller
public class NewsController {
    @Autowired
    NewsService newsService;
    
    @GetMapping("/")
    public String displayNews(Model model){
        
        List<News> newsList = newsService.getArticles();
        
        model.addAttribute("newsList", newsList);

        return "displayNews";
    }

    @PostMapping("/save")
    public String saveArticle(@RequestParam(value = "id", required = false) List<String> ids,
                                @RequestParam(value="imageUrl", required = false) List<String> imageUrls,
                                @RequestParam(value = "title", required = false) List<String> titles,
                                @RequestParam(value = "body", required = false) List<String> bodies,
                                @RequestParam(value = "categories", required = false) List<String> categories,
                                @RequestParam(value = "tags", required = false) List<String> tags,
                                @RequestParam(value = "publishedOn", required = false) List<String> publishedOn,
                                @RequestParam("url") List<String> urls){
        if (ids!=null){
            List<News> newsList = new ArrayList<>();
            for (int i = 0 ; i<ids.size() ; i++){
                News news = new News();
                news.setId(ids.get(i));
                news.setBody(bodies.get(i));
                news.setCategories(categories.get(i));
                news.setImageUrl(imageUrls.get(i));
                news.setPublishedOn(publishedOn.get(i));
                news.setTags(tags.get(i));
                news.setTitle(titles.get(i));
                news.setUrl(urls.get(i));
                newsList.add(news);
            }
            
            newsService.saveArticles(newsList);
        }
        return "redirect:/";
    }

}
