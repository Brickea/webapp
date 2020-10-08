package zixiaowangfall2020.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zixiaowangfall2020.webapp.mapper.QuestionAnswerMapper;
import zixiaowangfall2020.webapp.pojo.QuestionAnswer;

import java.util.List;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/
@Service
public class QuestionAnswerService {

    @Autowired
    QuestionAnswerMapper questionAnswerMapper;

    public List<QuestionAnswer> getAllQuestionAnswerByQuestionId(String questionId){
        return questionAnswerMapper.getAllQuestionAnswerByQuestionId(questionId);
    }

    public void insertQuestionAnswer(QuestionAnswer questionAnswer){
        questionAnswerMapper.interQuestionAnswer(questionAnswer);
    }

    public QuestionAnswer getQuestionAnswerByQuestionIdAnswerId(String questionId,String answerId){
        return questionAnswerMapper.getQuestionAnswerByQuestionIdAnswerId(questionId,answerId);
    }

    public void deleteQuestionAnswerByQuestionIdAnswerId(String questionId,String answerId){
        questionAnswerMapper.deleteQuestionAnswerByQuestionIdAnswerId( questionId, answerId);
    }
}
