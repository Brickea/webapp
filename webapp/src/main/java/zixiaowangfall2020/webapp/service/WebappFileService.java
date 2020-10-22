package zixiaowangfall2020.webapp.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import zixiaowangfall2020.webapp.mapper.WebappFileMapper;
import zixiaowangfall2020.webapp.pojo.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

@Service
public class WebappFileService {

    @Autowired
    private WebappFileMapper webappFileMapper;

    @Autowired
    private QuestionFileService questionFileService;

    @Autowired
    private AnswerFileService answerFileService;

    @Autowired
    private AmazonS3 amazonS3;
    @Value("${aws.s3.bucket}")
    private String bucketName;

    public List<WebappFile> getAllFileByQuestionId(final String questionId) {
        List<WebappFile> res = new ArrayList<>();
        List<QuestionFile> questionFiles = questionFileService.getAllQuestionFileByQuestionId(questionId);
        for (QuestionFile questionFile : questionFiles) {
            res.add(webappFileMapper.getOneByFileId(questionFile.getFileId()));
        }

        return res;
    }

    public List<WebappFile> getAllFileByAnswerId(final String answerId) {
        List<WebappFile> res = new ArrayList<>();
        List<AnswerFile> answerFiles = answerFileService.getAllAnswerFileByAnswerId(answerId);
        for (AnswerFile answerFile : answerFiles) {
            res.add(webappFileMapper.getOneByFileId(answerFile.getFileId()));
        }

        return res;
    }

    public WebappFile getOneByFileId(final String fileId) {
        return webappFileMapper.getOneByFileId(fileId);
    }

    public void deleteFileByFileId(final String fileId) {
        webappFileMapper.deleteFileByFileId(fileId);
    }


    public void deleteQuestionFile(final WebappFile webappFile) {
        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName + "/webapp/question", webappFile.getFileName());
        amazonS3.deleteObject(deleteObjectRequest);
    }

    public void deleteAnswerFile(final WebappFile webappFile) {
        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucketName + "/webapp/answer", webappFile.getFileName());
        amazonS3.deleteObject(deleteObjectRequest);
    }

    // @Async annotation ensures that the method is executed in a different background thread
    // but not consume the main thread.
    //    @Async
    public WebappFile uploadQuestionFile(final MultipartFile multipartFile) {
        try {
            final File file = convertMultiPartFileToFile(multipartFile);
            WebappFile webappFile = uploadFileToS3Bucket(bucketName + "/webapp/question", file);
            file.delete();  // To remove the file locally created in the project folder.
            return webappFile;
        } catch (final AmazonServiceException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public WebappFile uploadAnswerFile(final MultipartFile multipartFile) {
        try {
            final File file = convertMultiPartFileToFile(multipartFile);
            WebappFile webappFile = uploadFileToS3Bucket(bucketName + "/webapp/answer", file);
            file.delete();  // To remove the file locally created in the project folder.
            return webappFile;
        } catch (final AmazonServiceException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        final File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
        return file;
    }

    private WebappFile uploadFileToS3Bucket(final String bucketName, final File file) {
        final LocalDateTime currentTimeStamp = LocalDateTime.now();
        final String uniqueFileName = currentTimeStamp + "_" + file.getName();
        final PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, uniqueFileName, file);

        // insert file info into DB
        WebappFile webappFile = saveFileInfoIntoDB(uniqueFileName, bucketName + "/" + uniqueFileName, currentTimeStamp.toString());
        amazonS3.putObject(putObjectRequest);

        return webappFile;
    }

    private WebappFile saveFileInfoIntoDB(final String fileName, final String s3ObjectName, String createdTimeStamp) {
        WebappFile webappFile = new WebappFile(fileName, s3ObjectName, createdTimeStamp);
        webappFileMapper.insertNewFile(webappFile);

        return webappFile;
    }


}
