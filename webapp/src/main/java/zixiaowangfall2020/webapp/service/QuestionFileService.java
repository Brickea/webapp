package zixiaowangfall2020.webapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zixiaowangfall2020.webapp.mapper.QuestionFileMapper;
import zixiaowangfall2020.webapp.pojo.QuestionFile;
import zixiaowangfall2020.webapp.pojo.WebappFile;

import java.util.List;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

@Service
public class QuestionFileService {
    @Autowired
    QuestionFileMapper questionFileMapper;

    public void insertQuestionFile(QuestionFile questionFile){
        questionFileMapper.insertQuestionFile(questionFile);
    }

    public void deleteQuestionFileByFileId(String fileId){questionFileMapper.deleteQuestionFileByFileId(fileId); }

    public List<QuestionFile> getAllQuestionFileByQuestionId(final String questionId){
        return questionFileMapper.getAllQuestionFileByQuestionId(questionId);
    }
}
