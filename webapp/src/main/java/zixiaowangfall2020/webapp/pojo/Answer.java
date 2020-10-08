package zixiaowangfall2020.webapp.pojo;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

public class Answer {
    String answerId;
    String createdTimestamp;
    String updatedTimestamp;
    String userId;
    String answerText;

    public Answer() {
    }

    public Answer(String answerText,String userId){
        this.answerText = answerText;
        this.userId = userId;
    }

    public Answer(String answerId, String questionId, String createdTimestamp, String updatedTimestamp, String userId, String answerText) {
        this.answerId = answerId;
        this.createdTimestamp = createdTimestamp;
        this.updatedTimestamp = updatedTimestamp;
        this.userId = userId;
        this.answerText = answerText;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
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

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
}
