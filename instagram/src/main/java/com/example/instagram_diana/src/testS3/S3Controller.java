package com.example.instagram_diana.src.testS3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;
    private final FileService fileService;

    @GetMapping("/api/upload")
    public String goToUpload() {
        return "upload";
    }

    @PostMapping("/api/upload")
    public String uploadFile(FileDto fileDto) throws IOException {
        String url = s3Service.uploadFile(fileDto.getFile());

        fileDto.setUrl(url);
        fileService.save(fileDto);

        return "redirect:/api/list";
    }

    @GetMapping("/api/list")
    public String listPage(Model model) {
        List<FileEntity> fileList =fileService.getFiles();
        System.out.println(fileList);
        model.addAttribute("fileList", fileList);
        return "list";
    }

    // ------------------ 테스트 부분 ---------------------
    @GetMapping("/test")
    public String FileUpload(){
        return "uploadmulti";
    }




    }
//@RequestParam("file") List<FileDto> fileDto



