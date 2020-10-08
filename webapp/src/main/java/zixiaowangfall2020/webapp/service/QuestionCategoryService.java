package zixiaowangfall2020.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zixiaowangfall2020.webapp.mapper.QuestionCategoryMapper;
import zixiaowangfall2020.webapp.pojo.QuestionCategory;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/
@Service
public class QuestionCategoryService {
    @Autowired
    QuestionCategoryMapper questionCategoryMapper;

    public void addCategoriesByQuestionId(QuestionCategory questionCategory){
        questionCategoryMapper.interQuestionCategory(questionCategory);
    }

    public void deleteAllCategoryByQuestionId(String questionId){
        questionCategoryMapper.deleteQuestionCategoryByQuestionId(questionId);
    }
}
