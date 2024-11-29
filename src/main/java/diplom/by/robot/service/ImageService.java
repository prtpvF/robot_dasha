package diplom.by.robot.service;

import diplom.by.robot.exceptions.FileExtensionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

        private final String UPLOAD_DIR = "/Users/mihail/Downloads/robot/uploads";

        private final String[] IMAGE_EXTENSIONS = new String[]{"jpg", "jpeg", "png"};

        public String saveImage(MultipartFile file) {

                        log.info("start saving image");
                        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                        String baseName = FilenameUtils.getBaseName(file.getOriginalFilename())
                                .replaceAll(" ","_");

                        if (!Arrays.stream(IMAGE_EXTENSIONS).anyMatch(extension::contains)) {
                                throw new FileExtensionException("неверный формат файла, должен быть только: jpg, jpeg или png");
                        }

                        String uniqueFilename = createUniqueFilename(baseName, extension);
                        File destinationFile = new File(UPLOAD_DIR, uniqueFilename);

                    try {
                        file.transferTo(destinationFile);
                    } catch (IOException e) {
                        throw new FileExtensionException("ошибка загрузки файла");
                    }

            return destinationFile.getPath();
        }

        public void deleteImage(String pathToFile) {
            File file = new File(pathToFile);
            if (file.exists()) {
                file.delete();
            }
        }

        private  String createUniqueFilename(String baseName, String extension) {

                String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                return baseName + "_" + timestamp + "." + extension;
        }
}
