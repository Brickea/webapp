package zixiaowangfall2020.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import zixiaowangfall2020.webapp.pojo.*;
import zixiaowangfall2020.webapp.service.*;

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


    /**
     * @author: Zixiao Wang
     * @date: 10/8/20
     * @param:
     * @return: java.util.List<zixiaowangfall2020.webapp.pojo.Question>
     * @description:
     **/
    @GetMapping("/questions")
    public ResponseEntity<List<Map<String, Object>>> getAllQuestion() {

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
                answerRes.add(answerSingleRes);
            }
            questionSignleRes.put("answers", answerRes);

            questionRes.add(questionSignleRes);
        }

        return new ResponseEntity<>(questionRes, HttpStatus.OK);

    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<Map<String, Object>> getQuestionByQuestionId(@PathVariable("questionId") String questionId) throws IOException {
        // get question from Question table----------------------------------------------------------------------

        Question question = questionService.getQuestionById(questionId);
        // get all question info
        Map<String, Object> res = new HashMap<>();
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
            answerRes.add(answerSingleRes);
        }
        res.put("answers", answerRes);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PostMapping("/question")
    public ResponseEntity<Map<String, Object>> postQuestion(@RequestBody Map<String, Object> jsonObject) throws IOException {

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
    public ResponseEntity<Map<String, Object>> postQuestion(@PathVariable("questionId") String questionId) throws IOException {
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

        res.put("message", "delete question successfully!");
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @PutMapping("/question/{questionId}")
    public ResponseEntity<Map<String, Object>> updateQuestionByQuestionId(@PathVariable("questionId") String questionId, @RequestBody Map<String, Object> jsonObject) throws IOException {
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

        return new ResponseEntity<>(res, HttpStatus.OK);
    }


}
