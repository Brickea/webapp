package zixiaowangfall2020.webapp.controller;

import com.amazonaws.services.appsync.model.LogConfig;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zixiaowangfall2020.webapp.AWSTopicConfig;
import zixiaowangfall2020.webapp.MetricsConfig;
import zixiaowangfall2020.webapp.pojo.*;
import zixiaowangfall2020.webapp.service.*;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/
@RestController
@RequestMapping("/v1/question")
public class AnswerController {

    private static final Logger LOG = LoggerFactory.getLogger(LogConfig.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    AnswerService answerService;

    @Autowired
    UserService userService;

    @Autowired
    QuestionAnswerService questionAnswerService;

    @Autowired
    WebappFileService webappFileService;

    @Autowired
    AnswerFileService answerFileService;

    @Autowired
    AmazonSNS amazonSNS;

    @Value("${webapp.domainname}")
    String domainName;

    @Value("${aws.topic.arn}")
    String topicArn;

    @PostMapping("/{questionId}/answer")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> answerQuestionByQuestionId(@PathVariable("questionId") String questionId, @RequestBody Map<String, Object> jsonObject) {
        long startTime = System.currentTimeMillis();
        LOG.info("API CALL POST /v1/question/{questionId}/answer answer question by id");
        MetricsConfig.statsd.incrementCounter("API CALL POST /v1/question/{questionId}/answer");

        Map<String, Object> res = new HashMap<>();
        // insert new answer into Answer talbe -------------------------------------------------------------------------
        User currentUser = null;
        // Get the activeUser
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();

            currentUser = userService.getByUserName(currentUserName);
        }

        Answer answer = new Answer((String) jsonObject.get("answerText"), currentUser.getId());
        answerService.insertNewAnswer(answer);
        res.put("answerId", answer.getAnswerId());
        res.put("createdTimestamp", answer.getCreatedTimestamp());
        res.put("updatedTimestamp", answer.getUpdatedTimestamp());
        res.put("userId", answer.getUserId());
        res.put("answerText", answer.getAnswerText());


        // insert questionAnswer into questionAnswer table--------------------------------------------------------------
        QuestionAnswer questionAnswer = new QuestionAnswer(questionId, answer.getAnswerId());
        questionAnswerService.insertQuestionAnswer(questionAnswer);
        res.put("questionId", questionAnswer.getQuestionId());

        HttpHeaders httpHeaders = new HttpHeaders();

        long endTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Time Cost API CALL POST /v1/question/{questionId}/",endTime-startTime);

        // inform question user that the answer has been answered
        Question question = questionService.getQuestionById(questionId);

        String msg = "Dear "+currentUser.getLastName()+"\n"+
                "Your question '"+question.getQuestionText()+" ' has been answered!\n"+
                "Check this out: "+domainName+"/v1/question/"+questionId+"/answer/"+answer.getAnswerId()+" ";

        LOG.info(msg);
        LOG.info(topicArn);
        LOG.info(currentUser.getUserName());
        amazonSNS.publish(new PublishRequest(topicArn,msg,currentUser.getUserName()));

        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.CREATED);
    }

    @PutMapping("/{questionId}/answer/{answerId}")
    public ResponseEntity<Map<String, Object>> updateAnswerByQuestionIdAnswerId(@PathVariable("questionId") String questionId,
                                                                                @PathVariable("answerId") String answerId,
                                                                                @RequestBody Map<String, Object> jsonObject) {
        long startTime = System.currentTimeMillis();

        LOG.info("API CALL PUT /v1/question/{questionId}/answer/{answerId} update answer by id");
        MetricsConfig.statsd.incrementCounter("API CALL PUT /v1/question/{questionId}/answer/{answerId}");

        Map<String, Object> res = new HashMap<>();
        QuestionAnswer questionAnswer = questionAnswerService.getQuestionAnswerByQuestionIdAnswerId(questionId, answerId);
        if (questionAnswer == null) {
            res.put("messgae", "no answer found!");
            return new ResponseEntity<Map<String, Object>>(res, HttpStatus.NO_CONTENT);
        }

        Answer answer = answerService.getAnswerById(answerId);

        answer.setAnswerText((String) jsonObject.get("answerText"));

        User currentUser = null;
        // Get the activeUser
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();

            currentUser = userService.getByUserName(currentUserName);
        }

        if (!answer.getUserId().equals(currentUser.getId())) {
            res.put("message", "you are not the creator!");
            return new ResponseEntity<Map<String, Object>>(res, HttpStatus.UNAUTHORIZED);
        }

        answerService.updateAnswerByAnser(answer);

        res.put("answerId", answer.getAnswerId());
        res.put("questionId", questionAnswer.getQuestionId());
        res.put("createdTimestamp", answer.getCreatedTimestamp());
        res.put("updatedTimestamp", answer.getUpdatedTimestamp());
        res.put("userId", answer.getUserId());
        res.put("answerText", answer.getAnswerText());

        long endTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Time Cost API CALL PUT /v1/question/{questionId}/answer/{answerId}",endTime-startTime);

        Question question = questionService.getQuestionById(questionId);

        String msg = "Dear "+currentUser.getLastName()+"\n"+
                "Your question '"+question.getQuestionText()+" ' has been answered!\n"+
                "Check this out: "+domainName+"/v1/question/"+questionId+"/answer/"+answer.getAnswerId()+" ";

        LOG.info(msg);
        LOG.info(topicArn);
        LOG.info(currentUser.getUserName());
        amazonSNS.publish(new PublishRequest(topicArn,msg,currentUser.getUserName()));

        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{questionId}/answer/{answerId}")
    public ResponseEntity<Map<String, Object>> deleteAnswerByQuestionIdAnswerId(@PathVariable("questionId") String questionId,
                                                                                @PathVariable("answerId") String answerId) {
        long startTime = System.currentTimeMillis();

        LOG.info("API CALL DELETE /v1/question/{questionId}/answer/{answerId} delete answer by id");
        MetricsConfig.statsd.incrementCounter("API CALL DELETE /v1/question/{questionId}/answer/{answerId}");

        Map<String, Object> res = new HashMap<>();
        QuestionAnswer questionAnswer = questionAnswerService.getQuestionAnswerByQuestionIdAnswerId(questionId, answerId);
        if (questionAnswer == null) {
            res.put("messgae", "no answer found!");
            return new ResponseEntity<Map<String, Object>>(res, HttpStatus.NO_CONTENT);
        }
        Answer answer = answerService.getAnswerById(answerId);
        User currentUser = null;
        // Get the activeUser
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();

            currentUser = userService.getByUserName(currentUserName);
        }

        if (!answer.getUserId().equals(currentUser.getId())) {
            res.put("message", "you are not the creator!");
            return new ResponseEntity<Map<String, Object>>(res, HttpStatus.UNAUTHORIZED);
        }

        questionAnswerService.deleteQuestionAnswerByQuestionIdAnswerId(questionId, answerId);

        answerService.deleteAnswerByAnswerId(answerId);

        res.put("message", "delete successfully!");

        long endTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Time Cost API CALL DELETE /v1/question/{questionId}/answer/{answerId}",endTime-startTime);

        Question question = questionService.getQuestionById(questionId);

        String msg = "Dear "+currentUser.getLastName()+"\n"+
                "Your question '"+question.getQuestionText()+" ' has been answered!\n"+
                "Check this out: "+domainName+"/v1/question/"+questionId+"/answer/"+answer.getAnswerId()+" ";

        LOG.info(msg);
        LOG.info(topicArn);
        LOG.info(currentUser.getUserName());
        amazonSNS.publish(new PublishRequest(topicArn,msg,currentUser.getUserName()));

        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
    }


    @GetMapping("/{questionId}/answer/{answerId}")
    public ResponseEntity<Map<String, Object>> getAnswerByQuestionIdAnswerId(@PathVariable("questionId") String questionId,
                                                                             @PathVariable("answerId") String answerId) {

        long startTime = System.currentTimeMillis();

        LOG.info("API CALL GET /v1/question/{questionId}/answer/{answerId} get answer by id");
        MetricsConfig.statsd.incrementCounter("API CALL GET /v1/question/{questionId}/answer/{answerId}");

        Map<String, Object> res = new HashMap<>();
        QuestionAnswer questionAnswer = questionAnswerService.getQuestionAnswerByQuestionIdAnswerId(questionId, answerId);
        if (questionAnswer == null) {
            res.put("messgae", "no answer found!");
            return new ResponseEntity<Map<String, Object>>(res, HttpStatus.NO_CONTENT);
        }
        Answer answer = answerService.getAnswerById(answerId);

        res.put("answerId",answer.getAnswerId());
        res.put("questionId",questionAnswer.getQuestionId());
        res.put("createdTimestamp",answer.getCreatedTimestamp());
        res.put("updatedTimestamp",answer.getUpdatedTimestamp());
        res.put("userId",answer.getUserId());
        res.put("answerText",answer.getAnswerText());

        // get attachments for answer
        List<WebappFile> webappFileList = webappFileService.getAllFileByAnswerId(answer.getAnswerId());
        List<Map<String, Object>> webappFileRes = new ArrayList<>();
        for (WebappFile webappFile : webappFileList) {
            Map<String, Object> webappFileResSingleRes = new HashMap<>();
            webappFileResSingleRes.put("fileName", webappFile.getFileName());
            webappFileResSingleRes.put("s3ObjectName", webappFile.getS3ObjectName());
            webappFileResSingleRes.put("fileId", webappFile.getFileId());
            webappFileResSingleRes.put("createdTimeStamp", webappFile.getCreatedTimestamp());
            webappFileRes.add(webappFileResSingleRes);
        }
        res.put("attachments", webappFileRes);

        long endTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Time Cost API CALL GET /v1/question/{questionId}/answer/{answerId}",endTime-startTime);

        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
    }

    // file related
    @PostMapping("/{questionId}/answer/{answerId}/file")
    public ResponseEntity<Map<String, Object>> attachAnswerFile(@PathVariable("questionId") String questionId,
                                                                @PathVariable("answerId") String answerId,
                                                                @RequestPart(value= "file") final MultipartFile multipartFile) {
        long startTime = System.currentTimeMillis();

        LOG.info("API CALL POST /v1/question/{questionId}/answer/{answerId}/file add answer file by id");
        MetricsConfig.statsd.incrementCounter("API CALL POST /v1/question/{questionId}/answer/{answerId}/file");

        Map<String, Object> res = new HashMap<>();
        QuestionAnswer questionAnswer = questionAnswerService.getQuestionAnswerByQuestionIdAnswerId(questionId, answerId);
        if (questionAnswer == null) {
            res.put("messgae", "no answer found!");
            return new ResponseEntity<Map<String, Object>>(res, HttpStatus.NO_CONTENT);
        }

        // if creator
        Answer answer = answerService.getAnswerById(answerId);
        User currentUser = null;
        // Get the activeUser
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();

            currentUser = userService.getByUserName(currentUserName);
        }

        if (!answer.getUserId().equals(currentUser.getId())) {
            res.put("message", "you are not the creator!");
            return new ResponseEntity<Map<String, Object>>(res, HttpStatus.UNAUTHORIZED);
        }

        // upload file
        WebappFile webappFile = webappFileService.uploadAnswerFile(multipartFile);
        final String webappFileResponse = "[" + multipartFile.getOriginalFilename() + "] uploaded successfully.";

        // update questionFile table
        AnswerFile answerFile = new AnswerFile(answerId,webappFile.getFileId());
        answerFileService.insertAnswerFile(answerFile);
        res.put("fileName",webappFile.getFileName());
        res.put("s3ObjectName",webappFile.getS3ObjectName());
        res.put("fileId",webappFile.getFileId());
        res.put("createdTimestamp",webappFile.getCreatedTimestamp());

        long endTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Time Cost API CALL POST /v1/question/{questionId}/answer/{answerId}/file",endTime-startTime);

        return new ResponseEntity<>(res,HttpStatus.CREATED);

    }

    @DeleteMapping("/{questionId}/answer/{answerId}/file/{fileId}")
    public ResponseEntity<Map<String, Object>> deleteFileToTheAnswer(@PathVariable(value= "fileId") final String fileId) {

        long startTime = System.currentTimeMillis();

        LOG.info("API CALL DELETE /v1/question/{questionId}/answer/{answerId}/file/{fileId} delete answer file by id");
        MetricsConfig.statsd.incrementCounter("API CALL DELETE /v1/question/{questionId}/answer/{answerId}/file/{fileId}");

        Map<String, Object> res = new HashMap<>();

        // if the file exist
        WebappFile webappFile = webappFileService.getOneByFileId(fileId);
        if(webappFile==null){
            res.put("message","no file found!");
            return new ResponseEntity<>(res, HttpStatus.NO_CONTENT);
        }

        // delete file in s3
        webappFileService.deleteAnswerFile(webappFile);
        final String response = "[" + webappFile.getS3ObjectName() + "] delete successfully.";
        res.put("message",response);

        // delete questionFile and file in DB
        answerFileService.deleteAnswerFileByFileId(fileId);
        webappFileService.deleteFileByFileId(fileId);

        long endTime = System.currentTimeMillis();
        MetricsConfig.statsd.recordExecutionTime("Time Cost API CALL DELETE /v1/question/{questionId}/answer/{answerId}/file/{fileId}",endTime-startTime);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
