package zixiaowangfall2020.webapp.pojo;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

public class QuestionCategory {
    String questionId;
    String categoryId;

    public QuestionCategory() {
    }

    public QuestionCategory(String questionId, String categoryId) {
        this.questionId = questionId;
        this.categoryId = categoryId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
