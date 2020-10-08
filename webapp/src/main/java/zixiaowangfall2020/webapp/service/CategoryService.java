package zixiaowangfall2020.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zixiaowangfall2020.webapp.mapper.CategoryMapper;
import zixiaowangfall2020.webapp.mapper.QuestionCategoryMapper;
import zixiaowangfall2020.webapp.pojo.Category;
import zixiaowangfall2020.webapp.pojo.QuestionCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

@Service
public class CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    QuestionCategoryMapper questionCategoryMapper;

    public void insertNewCategory(Category category) {
        categoryMapper.insertNewCategory(category);

    }

    public List<Category> getAllCategory() {
        return categoryMapper.getAllCategory();
    }

    public Category getCategoryByCategory(String category) {
        return categoryMapper.getCategoryByCategory(category);
    }

    public List<Category> getAllCategoryByQuestionId(String questionId){
        List<Category> res = new ArrayList<>();

        List<QuestionCategory> questionCategoryList = questionCategoryMapper.getAllCategoryByQuestionId(questionId);
        for(QuestionCategory questionCategory:questionCategoryList){
            res.add(categoryMapper.getCategoryByCategoryId(questionCategory.getCategoryId()));
        }

        return res;

    }
}
