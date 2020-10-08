package zixiaowangfall2020.webapp.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

public class Question {
    @Null(message = "id shall not be defined by request!")
    String questionId;
    @Null(message = "createdTimestamp shall not be defined by request!")
    String createdTimestamp;
    @Null(message = "updatedTimestamp shall not be defined by request!")
    String updatedTimestamp;
    @Null(message = "userId shall not be defined by request!")
    String userId;
    @NotNull(message = "Text shall not be blank!")
    String questionText;

    public Question() {
    }

    public Question(String userId, String questionText){
        this.userId = userId;
        this.questionText = questionText;
    }

    public Question(String questionId, String createdTimestamp, String updatedTimestamp, String userId, String questionText) {
        this.questionId = questionId;
        this.createdTimestamp = createdTimestamp;
        this.updatedTimestamp = updatedTimestamp;
        this.userId = userId;
        this.questionText = questionText;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(String createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(String updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }
}
