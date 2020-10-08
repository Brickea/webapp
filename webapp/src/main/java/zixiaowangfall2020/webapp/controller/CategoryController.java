package zixiaowangfall2020.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        List<Category> list = categoryService.getAllCategory();

        Set<Object> set = new HashSet<Object>();

        for(Category category : list){
            Map<String,Object> map = new HashMap<>();
            map.put("categoryId",category.getCategoryId());
            map.put("category",category.getCategory());
            set.add(map);
        }
        return new ResponseEntity(set,HttpStatus.OK);
    }
}
