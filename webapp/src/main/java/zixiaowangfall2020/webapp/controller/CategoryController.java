package zixiaowangfall2020.webapp.controller;

import com.amazonaws.services.appsync.model.LogConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zixiaowangfall2020.webapp.MetricsConfig;
import zixiaowangfall2020.webapp.pojo.Category;
import zixiaowangfall2020.webapp.pojo.User;
import zixiaowangfall2020.webapp.service.CategoryService;

import javax.validation.Valid;
import java.util.*;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

@RestController
@RequestMapping(value = "/v1/category",produces="application/json;charset=UTF-8")
public class CategoryController {

    private static final Logger LOG = LoggerFactory.getLogger(LogConfig.class);

    @Autowired
    CategoryService categoryService;

    /**
    * @author: Zixiao Wang
    * @date: 10/8/20
     * @param: category
    * @return: org.springframework.http.ResponseEntity<java.util.Map<java.lang.String,java.lang.Object>>
    * @description:
     * insert new category
    **/
    @PostMapping
    public ResponseEntity<Map<String,Object>> insertNewCategory(@Valid @RequestBody Category category){

        long startTime = System.currentTimeMillis();

        LOG.info("API CALL POST /v1/category insert new category");
        MetricsConfig.statsd.incrementCounter("API CALL POST /v1/category");

        Map<String,Object> map = new HashMap<String,Object>();
        // find category to see if it's already existed.
        Category exist = categoryService.getCategoryByCategory(category.getCategory());
        if(exist!=null){
            map.put("message","the category is existed!");
            return new ResponseEntity<Map<String,Object>>(map, HttpStatus.BAD_REQUEST);
        }

        // add new category
        categoryService.insertNewCategory(category);

        map.put("categoryId",category.getCategoryId());
        map.put("category",category.getCategory());

        long endTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Time Cost API CALL POST /v1/category",endTime-startTime);


        return new ResponseEntity<Map<String,Object>>(map, HttpStatus.CREATED);
    }

    /**
    * @author: Zixiao Wang
    * @date: 10/8/20
     * @param: 
    * @return: org.springframework.http.ResponseEntity<java.util.Set<java.lang.Object>>
    * @description:
    **/
    @GetMapping("/all")
    public ResponseEntity<Set<Object>> getAllCategory(){

        long startTime = System.currentTimeMillis();

        LOG.info("API CALL GET /v1/category/all get all category");
        MetricsConfig.statsd.incrementCounter("API CALL GET /v1/category/all");

        List<Category> list = categoryService.getAllCategory();

        Set<Object> set = new HashSet<Object>();

        for(Category category : list){
            Map<String,Object> map = new HashMap<>();
            map.put("categoryId",category.getCategoryId());
            map.put("category",category.getCategory());
            set.add(map);
        }

        long endTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Time Cost API CALL GET /v1/category/all",endTime-startTime);

        return new ResponseEntity(set,HttpStatus.OK);
    }
}
