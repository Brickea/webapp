package zixiaowangfall2020.webapp.service;

import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zixiaowangfall2020.webapp.mapper.QuestionAnswerMapper;
import zixiaowangfall2020.webapp.mapper.QuestionCategoryMapper;
import zixiaowangfall2020.webapp.mapper.QuestionMapper;
import zixiaowangfall2020.webapp.pojo.Answer;
import zixiaowangfall2020.webapp.pojo.Question;
import zixiaowangfall2020.webapp.pojo.QuestionAnswer;
import zixiaowangfall2020.webapp.pojo.QuestionCategory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/
@Service
public class QuestionService {
    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionCategoryMapper questionCategoryMapper;

    @Autowired
    QuestionAnswerMapper questionAnswerMapper;

    @Autowired
    AnswerService answerService;

    /**
    * @author: Zixiao Wang
    * @date: 10/8/20
     * @param: 
    * @return: java.util.List<zixiaowangfall2020.webapp.pojo.Question>
    * @description:
    **/
    public List<Question> getAllQuestions(){
        return questionMapper.getAllQuestions();
    }
    
    /**
    * @author: Zixiao Wang
    * @date: 10/8/20
     * @param: null
    * @return: 
    * @description:
    **/
    public Question getQuestionById(String questionId){
        return questionMapper.getQuestionById(questionId);
    }

    /**
    * @author: Zixiao Wang
    * @date: 10/8/20
     * @param: question
    * @return: void
    * @description:
    **/
    public void insertNewQuestion(Question question){

        // Get current time and save it as accountCreated, accountUpdated
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

        question.setCreatedTimestamp(ft.format(dNow));
        question.setUpdatedTimestamp(ft.format(dNow));

        questionMapper.insertNewQuestion(question);
    }

    public void addCategoriesByQuestionId(QuestionCategory questionCategory){
        questionCategoryMapper.interQuestionCategory(questionCategory);
    }

    public void deleteQuestionByQuestionId(String questionId){
        questionMapper.deleteQuestionByQuestionId(questionId);
        questionCategoryMapper.deleteQuestionCategoryByQuestionId(questionId);

        List<Answer> answerList = answerService.getAllAnswerByQuestionId(questionId);

        for(Answer answer:answerList){
            answerService.deleteAnswerByAnswerId(answer.getAnswerId());
        }

        questionAnswerMapper.deleteQuestionAnswerByQuestionId(questionId);
    }

    public void updateQuestionByQuestionId(Question question){

        // Get current time and save it as accountCreated, accountUpdated
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

        question.setUpdatedTimestamp(ft.format(dNow));

        questionMapper.updateQuestionByQuestionId(question);
    }

}
