package com.ohgiraffers.comprehensive.common.util;

import com.ohgiraffers.comprehensive.common.exception.ServerInternalException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import static com.ohgiraffers.comprehensive.common.exception.type.ExceptionCode.FAIL_TO_DELETE_FILE;
import static com.ohgiraffers.comprehensive.common.exception.type.ExceptionCode.FAIL_TO_UPLOAD_FILE;

public class FileUploadUtils {

    public static String saveFile(String uploadDir, String fileName, MultipartFile multipartFile) {
                                 // 경로, 파일명, 파일객체
        try(InputStream inputStream = multipartFile.getInputStream()) {

            Path uploadPath = Paths.get(uploadDir);
            /* 업로드 경로가 존재하지 않을 시 경로 먼저 생성 */
            if(!Files.exists(uploadPath))
                Files.createDirectories(uploadPath);

            /* 파일명 생성 */
            String replaceFileName = fileName + "." + FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            // OriginalFileName으로부터 뒤에 붙는 확장자명(png, jpg 등)을 떼어 내는 작업

            /* 파일 저장 */
            Path filePath = uploadPath.resolve(replaceFileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            // filePath를 Path로 만든다. 경로와 파일명을 합쳐서 Path라는 객체를 만든다.

            return replaceFileName;

        } catch (IOException e) {
            throw new ServerInternalException(FAIL_TO_UPLOAD_FILE);
        }
    }

    public static void deleteFile(String uploadDir, String fileName) {

        try {
            Path uploadPath = Paths.get(uploadDir);
            Path filePath = uploadPath.resolve(fileName); // filePath 삭제해야 할 대상
            Files.delete(filePath); // 해당 파일을 삭제한다.
        } catch (IOException e) { // 문제가 생기면 Exception 처리
            throw new ServerInternalException(FAIL_TO_DELETE_FILE);
        }
    }
}
