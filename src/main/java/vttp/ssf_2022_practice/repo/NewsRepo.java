package vttp.ssf_2022_practice.repo;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import vttp.ssf_2022_practice.util.Util;


@Repository
public class NewsRepo{

    @Autowired
    @Qualifier(Util.template)
    RedisTemplate<String, String> stringTemplate;

    public boolean create(String id, String entry){
        stringTemplate.opsForHash().put(Util.redisKey, id, entry);
        return true;
    }

    public String getById(String id){
        return (String) stringTemplate.opsForHash().get(Util.redisKey, id);
    }

    public boolean deleteById(String id){
        long number = stringTemplate.opsForHash().delete(Util.redisKey, id);
        return (number>0)? true : false;
    }

    // public boolean updateById(String redisKey, String id, String updatedEntry){
    //     boolean isFound = deleteById(id);
    //     return isFound? create(id, updatedEntry) : false;
    // }

    public Set<String> getKeys(){
        Set<Object> keys = stringTemplate.opsForHash().keys(Util.redisKey);
        return keys.stream().map(k -> (String) k).collect(Collectors.toSet());
    }

    public boolean checkHashTable(String redisKey){
        return stringTemplate.hasKey(redisKey);
    }
}