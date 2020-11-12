package zixiaowangfall2020.webapp.service;

import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zixiaowangfall2020.webapp.MetricsConfig;
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

        long startTime = System.currentTimeMillis();

        List<Question> res = questionMapper.getAllQuestions();

        long endTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Database Query getAllQuestions",endTime-startTime);
        
        return res;
    }
    
    /**
    * @author: Zixiao Wang
    * @date: 10/8/20
     * @param: null
    * @return: 
    * @description:
    **/
    public Question getQuestionById(String questionId){

        long startTime = System.currentTimeMillis();

        Question res = questionMapper.getQuestionById(questionId);

        long endTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Database Query getQuestionById",endTime-startTime);

        return res;
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

        long startTime = System.currentTimeMillis();

        questionMapper.insertNewQuestion(question);

        long endTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Database Query insertNewQuestion",endTime-startTime);




    }

    public void addCategoriesByQuestionId(QuestionCategory questionCategory){

        long endTime = System.currentTimeMillis();

        questionCategoryMapper.interQuestionCategory(questionCategory);

        long startTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Database Query interQuestionCategory",endTime-startTime);
    }

    public void deleteQuestionByQuestionId(String questionId){
        long startTime = System.currentTimeMillis();
        questionMapper.deleteQuestionByQuestionId(questionId);
        long endTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Database Query deleteQuestionByQuestionId",endTime-startTime);

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

        long startTime = System.currentTimeMillis();

        questionMapper.updateQuestionByQuestionId(question);

        long endTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Database Query updateQuestionByQuestionId",endTime-startTime);
    }

}
