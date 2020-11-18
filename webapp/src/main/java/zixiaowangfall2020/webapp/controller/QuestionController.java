package zixiaowangfall2020.webapp.controller;

import com.amazonaws.services.appsync.model.LogConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zixiaowangfall2020.webapp.MetricsConfig;
import zixiaowangfall2020.webapp.pojo.*;
import zixiaowangfall2020.webapp.service.*;

import javax.validation.Valid;
import java.io.IOException;
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
@RequestMapping("/v1")
public class QuestionController {


    private static final Logger LOG = LoggerFactory.getLogger(LogConfig.class);


    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    AnswerService answerService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    QuestionAnswerService questionAnswerService;

    @Autowired
    QuestionCategoryService questionCategoryService;

    @Autowired
    WebappFileService webappFileService;

    @Autowired
    QuestionFileService questionFileService;

    @Autowired
    AnswerFileService answerFileService;


    /**
     * @author: Zixiao Wang
     * @date: 10/8/20
     * @param:
     * @return: java.util.List<zixiaowangfall2020.webapp.pojo.Question>
     * @description:
     **/
    @GetMapping("/questions")
    public ResponseEntity<List<Map<String, Object>>> getAllQuestion() {
        LOG.info("API CALL GET /v1/questions get all questions");
        MetricsConfig.statsd.incrementCounter("API CALL GET /v1/questions");

        // get question from Question table----------------------------------------------------------------------
        List<Question> questionList = questionService.getAllQuestions();

        List<Map<String, Object>> questionRes = new ArrayList<>();

        for (Question question : questionList) {
            // get all question info
            Map<String, Object> questionSignleRes = new HashMap<>();
            questionSignleRes.put("questionId", question.getQuestionId());
            questionSignleRes.put("createdTimestamp", question.getCreatedTimestamp());
            questionSignleRes.put("updatedTImestamp", question.getUpdatedTimestamp());
            questionSignleRes.put("userId", question.getUserId());
            questionSignleRes.put("questionText", question.getQuestionText());

            // get category for each question
            List<Category> categoryList = categoryService.getAllCategoryByQuestionId(question.getQuestionId());
            List<Map<String, Object>> categoryRes = new ArrayList<>();
            for (Category categorySingle : categoryList) {
                Map<String, Object> categorySingleRes = new HashMap<>();
                categorySingleRes.put("categoryId", categorySingle.getCategoryId());
                categorySingleRes.put("category", categorySingle.getCategory());
                categoryRes.add(categorySingleRes);
            }
            questionSignleRes.put("categories", categoryRes);

            // get answers for each question
            List<Answer> answerList = answerService.getAllAnswerByQuestionId(question.getQuestionId());
            List<Map<String, Object>> answerRes = new ArrayList<>();
            for (Answer answer : answerList) {
                Map<String, Object> answerSingleRes = new HashMap<>();
                answerSingleRes.put("answerId", answer.getAnswerId());
                answerSingleRes.put("questionId", question.getQuestionId());
                answerSingleRes.put("createdTimestamp", answer.getCreatedTimestamp());
                answerSingleRes.put("updatedTimestamp", answer.getUpdatedTimestamp());
                answerSingleRes.put("userId", answer.getUserId());
                answerSingleRes.put("answerText", answer.getAnswerText());

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
                answerSingleRes.put("attachments", webappFileRes);

                answerRes.add(answerSingleRes);
            }
            questionSignleRes.put("answers", answerRes);

            // get attachments for question
            List<WebappFile> webappFileList = webappFileService.getAllFileByQuestionId(question.getQuestionId());
            List<Map<String, Object>> webappFileRes = new ArrayList<>();
            for (WebappFile webappFile : webappFileList) {
                Map<String, Object> webappFileResSingleRes = new HashMap<>();
                webappFileResSingleRes.put("fileName", webappFile.getFileName());
                webappFileResSingleRes.put("s3ObjectName", webappFile.getS3ObjectName());
                webappFileResSingleRes.put("fileId", webappFile.getFileId());
                webappFileResSingleRes.put("createdTimeStamp", webappFile.getCreatedTimestamp());
                webappFileRes.add(webappFileResSingleRes);
            }
            questionSignleRes.put("attachments", webappFileRes);

            questionRes.add(questionSignleRes);
        }

        return new ResponseEntity<>(questionRes, HttpStatus.OK);

    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<Map<String, Object>> getQuestionByQuestionId(@PathVariable("questionId") String questionId) throws IOException {
        LOG.info("API CALL GET /v1/question/{questionId} get question by question id");
        MetricsConfig.statsd.incrementCounter("API CALL GET /v1/question/{questionId}");
        // get question from Question table----------------------------------------------------------------------
        Map<String, Object> res = new HashMap<>();
        Question question = questionService.getQuestionById(questionId);

        if(question==null){
            res.put("message","no question found!");
            return new ResponseEntity<>(res,HttpStatus.NO_CONTENT);
        }

        // get all question info

        res.put("questionId", question.getQuestionId());
        res.put("createdTimestamp", question.getCreatedTimestamp());
        res.put("updatedTImestamp", question.getUpdatedTimestamp());
        res.put("userId", question.getUserId());
        res.put("questionText", question.getQuestionText());

        // get category for each question
        List<Category> categoryList = categoryService.getAllCategoryByQuestionId(question.getQuestionId());
        List<Map<String, Object>> categoryRes = new ArrayList<>();
        for (Category categorySingle : categoryList) {
            Map<String, Object> categorySingleRes = new HashMap<>();
            categorySingleRes.put("categoryId", categorySingle.getCategoryId());
            categorySingleRes.put("category", categorySingle.getCategory());
            categoryRes.add(categorySingleRes);
        }
        res.put("categories", categoryRes);

        // get answers for each question
        List<Answer> answerList = answerService.getAllAnswerByQuestionId(question.getQuestionId());
        List<Map<String, Object>> answerRes = new ArrayList<>();
        for (Answer answer : answerList) {
            Map<String, Object> answerSingleRes = new HashMap<>();
            answerSingleRes.put("answerId", answer.getAnswerId());
            answerSingleRes.put("questionId", question.getQuestionId());
            answerSingleRes.put("createdTimestamp", answer.getCreatedTimestamp());
            answerSingleRes.put("updatedTimestamp", answer.getUpdatedTimestamp());
            answerSingleRes.put("userId", answer.getUserId());
            answerSingleRes.put("answerText", answer.getAnswerText());

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
            answerSingleRes.put("attachments", webappFileRes);

            answerRes.add(answerSingleRes);
        }
        res.put("answers", answerRes);

        // get attachments for question
        List<WebappFile> webappFileList = webappFileService.getAllFileByQuestionId(question.getQuestionId());
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

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/question")
    public ResponseEntity<Map<String, Object>> postQuestion(@RequestBody Map<String, Object> jsonObject) throws IOException {
        LOG.info("API CALL POST /v1/question post new question");
        MetricsConfig.statsd.incrementCounter("API CALL POST /v1/question");

        Map<String, Object> res = new HashMap<>();

        // insert new question into Question table----------------------------------------------------------------------
        User currentUser = null;
        // Get the activeUser
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();

            currentUser = userService.getByUserName(currentUserName);
        }
        String questionText = (String) jsonObject.get("questionText");
        Question question = new Question(currentUser.getId(), questionText);
        questionService.insertNewQuestion(question);
        res.put("questionId", question.getQuestionId());
        res.put("createdTimestamp", question.getCreatedTimestamp());
        res.put("updatedTimestamp", question.getUpdatedTimestamp());
        res.put("questionText", question.getQuestionText());
        res.put("userId", question.getUserId());

        // add categories for this question-----------------------------------------------------------------------------
        List<Map<String, Object>> categoryList = new ArrayList<>();

        ArrayList<Map> categories = (ArrayList<Map>) jsonObject.get("categories");
        for (Map o : categories) {
            Map<String, Object> categoryRes = new HashMap<>();
            // get all categories and insert it into questions
            String categoryName = (String) o.get("category");
            Category category = categoryService.getCategoryByCategory(categoryName);

            QuestionCategory questionCategory = null;
            if (category != null) {
                questionCategory = new QuestionCategory(question.getQuestionId(), category.getCategoryId());
                categoryRes.put("categoryId", category.getCategoryId());
                categoryRes.put("category", category.getCategory());
                categoryList.add(categoryRes);
                questionCategoryService.addCategoriesByQuestionId(questionCategory);
            } else {
                Category newCategory = new Category(categoryName);
                categoryService.insertNewCategory(newCategory);
                questionCategory = new QuestionCategory(question.getQuestionId(), newCategory.getCategoryId());
                categoryRes.put("categoryId", newCategory.getCategoryId());
                categoryRes.put("category", newCategory.getCategory());
                categoryList.add(categoryRes);
                questionCategoryService.addCategoriesByQuestionId(questionCategory);
            }
        }
        res.put("categories", categoryList);

        // get all answer and insert it into questions------------------------------------------------------------------
        List<Map<String, Object>> answerList = new ArrayList<>();
        Map<String, Object> answerRes = new HashMap<>();
        List<QuestionAnswer> questionAnswers = questionAnswerService.getAllQuestionAnswerByQuestionId(question.getQuestionId());
        for (QuestionAnswer questionAnswer : questionAnswers) {
            Answer answer = answerService.getAnswerById(questionAnswer.getAnswerId());
            answerRes.put("answerId", answer.getAnswerId());
            answerRes.put("questionId", question.getQuestionId());
            answerRes.put("createdTimestamp", answer.getCreatedTimestamp());
            answerRes.put("updatedTimestamp", answer.getUpdatedTimestamp());
            answerRes.put("userId", answer.getUserId());
            answerRes.put("answerText", answer.getAnswerText());
            answerList.add(answerRes);
        }
        res.put("answers", answerList);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @DeleteMapping("/question/{questionId}")
    public ResponseEntity<Map<String, Object>> deleteQuestion(@PathVariable("questionId") String questionId) throws IOException {
        LOG.info("API CALL DELETE /v1//question/{questionId} delete question by id");

        Map<String, Object> res = new HashMap<>();

        Question question = questionService.getQuestionById(questionId);
        if (question == null) {
            res.put("message", "no question found!");
            return new ResponseEntity<>(res, HttpStatus.NO_CONTENT);
        }

        User currentUser = null;
        // Get the activeUser
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            currentUser = userService.getByUserName(currentUserName);
        }
        if (!question.getUserId().equals(currentUser.getId())) {
            res.put("message", "you are not the creator");
            return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
        }

        if (answerService.getAllAnswerByQuestionId(questionId).size() != 0) {
            res.put("message", "the question has more than one answers!");
            return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
        }

        questionService.deleteQuestionByQuestionId(questionId);

        // delete bucket objects
        List<WebappFile> webappFileList = webappFileService.getAllFileByQuestionId(question.getQuestionId());
        for (WebappFile webappFile : webappFileList) {
            webappFileService.deleteQuestionFile(webappFile);
            webappFileService.deleteFileByFileId(webappFile.getFileId());
        }

        res.put("message", "delete question successfully!");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/question/{questionId}")
    public ResponseEntity<Map<String, Object>> updateQuestionByQuestionId(@PathVariable("questionId") String questionId, @RequestBody Map<String, Object> jsonObject) throws IOException {
        LOG.info("API CALL PUT /v1/question/{questionId} update question by id");

        Map<String, Object> res = new HashMap<>();

        Question question = questionService.getQuestionById(questionId);
        if (question == null) {
            res.put("message", "no question found!");
            return new ResponseEntity<>(res, HttpStatus.NO_CONTENT);
        }

        User currentUser = null;
        // Get the activeUser
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            currentUser = userService.getByUserName(currentUserName);
        }
        if (!question.getUserId().equals(currentUser.getId())) {
            res.put("message", "you are not the creator");
            return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
        }

        question.setQuestionText((String) jsonObject.get("questionText"));

        questionService.updateQuestionByQuestionId(question);

        res.put("questionId", question.getQuestionId());
        res.put("createdTimestamp", question.getCreatedTimestamp());
        res.put("updatedTimestamp", question.getUpdatedTimestamp());
        res.put("userId", question.getUserId());
        res.put("questionText", question.getQuestionText());

        List<Map<String, Object>> categoryList = new ArrayList<>();

        ArrayList<Map> categories = (ArrayList<Map>) jsonObject.get("categories");
        questionCategoryService.deleteAllCategoryByQuestionId(questionId);
        for (Map o : categories) {
            Map<String, Object> categoryRes = new HashMap<>();
            // get all categories and insert it into questions
            String categoryName = (String) o.get("category");
            Category category = categoryService.getCategoryByCategory(categoryName);

            QuestionCategory questionCategory = null;
            if (category != null) {
                questionCategory = new QuestionCategory(question.getQuestionId(), category.getCategoryId());
                categoryRes.put("categoryId", category.getCategoryId());
                categoryRes.put("category", category.getCategory());

            } else {
                Category newCategory = new Category(categoryName);
                categoryService.insertNewCategory(newCategory);
                questionCategory = new QuestionCategory(question.getQuestionId(), newCategory.getCategoryId());
                categoryRes.put("categoryId", newCategory.getCategoryId());
                categoryRes.put("category", newCategory.getCategory());
            }
            categoryList.add(categoryRes);
            questionCategoryService.addCategoriesByQuestionId(questionCategory);
        }

        res.put("categories", categoryList);

        List<Map<String, Object>> answerList = new ArrayList<>();
        Map<String, Object> answerRes = new HashMap<>();
        List<QuestionAnswer> questionAnswers = questionAnswerService.getAllQuestionAnswerByQuestionId(question.getQuestionId());
        for (QuestionAnswer questionAnswer : questionAnswers) {
            Answer answer = answerService.getAnswerById(questionAnswer.getAnswerId());
            answerRes.put("answerId", answer.getAnswerId());
            answerRes.put("questionId", question.getQuestionId());
            answerRes.put("createdTimestamp", answer.getCreatedTimestamp());
            answerRes.put("updatedTimestamp", answer.getUpdatedTimestamp());
            answerRes.put("userId", answer.getUserId());
            answerRes.put("answerText", answer.getAnswerText());
            answerList.add(answerRes);
        }
        res.put("answers", answerList);

        // get attachments for question
        List<WebappFile> webappFileList = webappFileService.getAllFileByQuestionId(question.getQuestionId());
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

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    // File related
    @PostMapping("/question/{questionId}/file")
    public ResponseEntity<Map<String, Object>> attachFileToTheQuestion(
            @PathVariable("questionId") final String questionId,
            @RequestPart(value= "file") final MultipartFile multipartFile){
        LOG.info("API CALL POST /v1/question/{questionId}/file attach file to the question");

        Map<String, Object> res = new HashMap<>();
        // if question exist
        Question question = questionService.getQuestionById(questionId);
        if(question==null){
            // question doesnt exist!
            res.put("message","question doesn't exist!");
            return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
        }

        // if creator
        User currentUser = null;
        // Get the activeUser
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            currentUser = userService.getByUserName(currentUserName);
        }
        if (!question.getUserId().equals(currentUser.getId())) {
            res.put("message", "you are not the creator");
            return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
        }

        // upload file
        WebappFile webappFile = webappFileService.uploadQuestionFile(multipartFile);
        final String webappFileResponse = "[" + multipartFile.getOriginalFilename() + "] uploaded successfully.";

        // update questionFile table
        QuestionFile questionFile = new QuestionFile(questionId,webappFile.getFileId());
        questionFileService.insertQuestionFile(questionFile);
        res.put("fileName",webappFile.getFileName());
        res.put("s3ObjectName",webappFile.getS3ObjectName());
        res.put("fileId",webappFile.getFileId());
        res.put("createdTimestamp",webappFile.getCreatedTimestamp());

        return new ResponseEntity<>(res,HttpStatus.CREATED);

    }

    @DeleteMapping("/question/{questionId}/file/{fileId}")
    public ResponseEntity<Map<String, Object>> deleteFileToTheQuestion(@PathVariable(value= "fileId") final String fileId) {
        LOG.info("API CALL DELETE /v1/question/{questionId}/file/{fileId} delete file by question id and file id");

        Map<String, Object> res = new HashMap<>();

        // if the file exist
        WebappFile webappFile = webappFileService.getOneByFileId(fileId);
        if(webappFile==null){
            res.put("message","no file found!");
            return new ResponseEntity<>(res, HttpStatus.NO_CONTENT);
        }

        // delete file in s3
        webappFileService.deleteQuestionFile(webappFile);
        final String response = "[" + webappFile.getS3ObjectName() + "] delete successfully.";
        res.put("message",response);

        // delete questionFile and file
        questionFileService.deleteQuestionFileByFileId(fileId);
        webappFileService.deleteFileByFileId(fileId);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
