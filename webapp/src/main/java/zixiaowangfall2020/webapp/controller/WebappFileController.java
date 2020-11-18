package zixiaowangfall2020.webapp.controller;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.appsync.model.LogConfig;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zixiaowangfall2020.webapp.MetricsConfig;
import zixiaowangfall2020.webapp.mapper.WebappFileMapper;
import zixiaowangfall2020.webapp.pojo.WebappFile;
import zixiaowangfall2020.webapp.service.WebappFileService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/
@RestController
@RequestMapping(value = "/v1",produces="application/json;charset=UTF-8")
public class WebappFileController {

    private static final Logger LOG = LoggerFactory.getLogger(LogConfig.class);

    @Autowired
    private WebappFileService service;

    @Autowired
    private WebappFileMapper webappFileMapper;

    @Value("${aws.s3.region}")
    private String region;

    @PostMapping(value= "/upload")
    public ResponseEntity<String> uploadFile(@RequestPart(value= "file") final MultipartFile multipartFile) {

        LOG.info("API CALL POST /v1/upload upload file");
        MetricsConfig.statsd.incrementCounter("API CALL POST /v1/upload");

        service.uploadQuestionFile(multipartFile);
        final String response = "[" + multipartFile.getOriginalFilename() + "] uploaded successfully.";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{fileId}")
    public ResponseEntity<String> deleteFile(@PathVariable(value= "fileId") final String fileId) {

        LOG.info("API CALL DELETE /v1/delete/{fileId} delete file");
        MetricsConfig.statsd.incrementCounter("API CALL DELETE /v1/delete/{fileId}");

        WebappFile webappFile = webappFileMapper.getOneByFileId(fileId);

        if(webappFile==null){
            return new ResponseEntity<>("fail", HttpStatus.OK);
        }

        service.deleteQuestionFile(webappFile);
        final String response = "[" + webappFile.getS3ObjectName() + "] delete successfully.";
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "test/bucket/list")
    public ResponseEntity<Map> testListBucket(){
        Map<String,Object> res = new HashMap<>();
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
        List<Bucket> buckets = s3.listBuckets();
        for (Bucket b : buckets) {
            res.put("bucketName",b.getName());
        }

        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @GetMapping(value = "test/bucketObject/list")
    public ResponseEntity<Map> testListBucketObject(){
        Map<String,Object> res = new HashMap<>();
        final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(region).build();
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName("webapp.zixiao.wang");

        ObjectListing objectListing;

        do {
            objectListing = s3.listObjects(listObjectsRequest);
            for (S3ObjectSummary objectSummary :
                    objectListing.getObjectSummaries()) {
                res.put("key",objectSummary.getKey());
                res.put("size",objectSummary.getSize());
            }
            listObjectsRequest.setMarker(objectListing.getNextMarker());
        } while (objectListing.isTruncated());

        return new ResponseEntity<>(res,HttpStatus.OK);
    }

}
