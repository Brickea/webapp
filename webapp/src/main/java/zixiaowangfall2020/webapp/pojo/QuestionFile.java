package zixiaowangfall2020.webapp.pojo;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

public class QuestionFile {
    private String questionId;
    private String fileId;

    public QuestionFile(String questionId, String fileId) {
        this.questionId = questionId;
        this.fileId = fileId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
