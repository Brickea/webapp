package zixiaowangfall2020.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zixiaowangfall2020.webapp.mapper.AnswerFileMapper;
import zixiaowangfall2020.webapp.mapper.QuestionFileMapper;
import zixiaowangfall2020.webapp.pojo.Answer;
import zixiaowangfall2020.webapp.pojo.AnswerFile;
import zixiaowangfall2020.webapp.pojo.QuestionFile;

import java.util.List;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

@Service
public class AnswerFileService {
    @Autowired
    AnswerFileMapper answerFileMapper;

    public void insertAnswerFile(AnswerFile answerFile){
        answerFileMapper.insertAnswerFile(answerFile);
    }

    public void deleteAnswerFileByFileId(String fileId){answerFileMapper.deleteAnswerFileByFileId(fileId); }

    public List<AnswerFile> getAllAnswerFileByAnswerId(final String answerId){
        return answerFileMapper.getAllAnswerFileByAnswerId(answerId);
    }
}
