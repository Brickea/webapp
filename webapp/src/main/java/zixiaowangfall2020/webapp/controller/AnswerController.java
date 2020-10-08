package zixiaowangfall2020.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import zixiaowangfall2020.webapp.pojo.Answer;
import zixiaowangfall2020.webapp.pojo.QuestionAnswer;
import zixiaowangfall2020.webapp.pojo.User;
import zixiaowangfall2020.webapp.service.AnswerService;
import zixiaowangfall2020.webapp.service.QuestionAnswerService;
import zixiaowangfall2020.webapp.service.UserService;

import javax.websocket.server.PathParam;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/
@RestController
@RequestMapping("/v1/question")
public class AnswerController {

    @Autowired
    AnswerService answerService;

    @Autowired
    UserService userService;

    @Autowired
    QuestionAnswerService questionAnswerService;

    @PostMapping("/{questionId}/answer")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> answerQuestionByQuestionId(@PathVariable("questionId") String questionId, @RequestBody Map<String, Object> jsonObject) {

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

        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.CREATED);
    }

    @PutMapping("/{questionId}/answer/{answerId}")
    public ResponseEntity<Map<String, Object>> updateAnswerByQuestionIdAnswerId(@PathVariable("questionId") String questionId,
                                                                                @PathVariable("answerId") String answerId,
                                                                                @RequestBody Map<String, Object> jsonObject) {
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

        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
    }

    @DeleteMapping("/{questionId}/answer/{answerId}")
    public ResponseEntity<Map<String, Object>> deleteAnswerByQuestionIdAnswerId(@PathVariable("questionId") String questionId,
                                                                                @PathVariable("answerId") String answerId) {
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
        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
    }


    @GetMapping("/{questionId}/answer/{answerId}")
    public ResponseEntity<Map<String, Object>> getAnswerByQuestionIdAnswerId(@PathVariable("questionId") String questionId,
                                                                             @PathVariable("answerId") String answerId) {
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

        return new ResponseEntity<Map<String, Object>>(res, HttpStatus.OK);
    }
}
