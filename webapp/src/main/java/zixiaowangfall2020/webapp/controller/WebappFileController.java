package zixiaowangfall2020.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zixiaowangfall2020.webapp.mapper.WebappFileMapper;
import zixiaowangfall2020.webapp.pojo.WebappFile;
import zixiaowangfall2020.webapp.service.WebappFileService;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/
@RestController
@RequestMapping(value = "/v1",produces="application/json;charset=UTF-8")
public class WebappFileController {

    @Autowired
    private WebappFileService service;

    @Autowired
    private WebappFileMapper webappFileMapper;

    @PostMapping(value= "/upload")
    public ResponseEntity<String> uploadFile(@RequestPart(value= "file") final MultipartFile multipartFile) {
        service.uploadQuestionFile(multipartFile);
        final String response = "[" + multipartFile.getOriginalFilename() + "] uploaded successfully.";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable(value= "fileId") final String fileId) {

        WebappFile webappFile = webappFileMapper.getOneByFileId(fileId);

        if(webappFile==null){
            return new ResponseEntity<>("fail", HttpStatus.OK);
        }

        service.deleteQuestionFile(webappFile);
        final String response = "[" + webappFile.getS3ObjectName() + "] delete successfully.";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
