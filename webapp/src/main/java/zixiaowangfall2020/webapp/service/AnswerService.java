package zixiaowangfall2020.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zixiaowangfall2020.webapp.MetricsConfig;
import zixiaowangfall2020.webapp.mapper.AnswerMapper;
import zixiaowangfall2020.webapp.mapper.QuestionAnswerMapper;
import zixiaowangfall2020.webapp.pojo.Answer;
import zixiaowangfall2020.webapp.pojo.QuestionAnswer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

@Service
public class AnswerService {

    @Autowired
    AnswerMapper answerMapper;

    @Autowired
    QuestionAnswerMapper questionAnswerMapper;

    public Answer getAnswerById(String answerId){

        long startTime = System.currentTimeMillis();

        Answer res = answerMapper.getAnswerById(answerId);

        long endTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Database Query getAnswerById",endTime-startTime);

        return res;

    }

    public void insertNewAnswer(Answer answer){

        // Get current time and save it as accountCreated, accountUpdated
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

        answer.setCreatedTimestamp(ft.format(dNow));
        answer.setUpdatedTimestamp(ft.format(dNow));

        long startTime = System.currentTimeMillis();

        answerMapper.insertNewAnswer(answer);

        long endTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Database Query insertNewAnswer",endTime-startTime);
    }

    public List<Answer> getAllAnswerByQuestionId(String questionid){
        List<Answer> res = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        List<QuestionAnswer> questionAnswerList = questionAnswerMapper.getAllQuestionAnswerByQuestionId(questionid);
        for(QuestionAnswer questionAnswer: questionAnswerList){
            res.add(answerMapper.getAnswerById(questionAnswer.getAnswerId()));
        }

        long endTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Database Query getAllAnswerByQuestionId",endTime-startTime);

        return res;
    }

    public void updateAnswerByAnser(Answer answer){
        // Get current time and save it as accountCreated, accountUpdated
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

        answer.setUpdatedTimestamp(ft.format(dNow));

        long startTime = System.currentTimeMillis();

        answerMapper.updateAnswerByAnswer(answer);

        long endTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Database Query updateAnswerByAnswer",endTime-startTime);
    }

    public void deleteAnswerByAnswerId(String answerId){

        long startTime = System.currentTimeMillis();

        answerMapper.deleteAnswerByAnswerId(answerId);

        long endTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Database Query deleteAnswerByAnswerId",endTime-startTime);

    }
}
