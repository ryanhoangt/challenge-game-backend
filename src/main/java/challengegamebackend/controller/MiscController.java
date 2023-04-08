package challengegamebackend.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
public class MiscController {

    /** Get an JPG image from path
     * Ex: /static/image/jpg?<path> where <path> was received from a previous query
     *
     * @param imgPath should start with 'resources/'
     * @return the image data as byte array
     * @throws IOException
     */
    @GetMapping(value = "/static/jpg", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getJPG(@RequestParam String imgPath) throws IOException {
        if (imgPath.contains("resources")) {
            return Files.readAllBytes(Paths.get("src/main/" + imgPath));
        } else
            return new byte[]{};
    }

    /** Get an PNG image from path
     * Ex: /static/image/png?<path> where <path> was received from a previous query
     *
     * @param imgPath should start with 'resources/'
     * @return the image data as byte array
     * @throws IOException
     */
    @GetMapping(value = "/static/png", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] getPNG(@RequestParam String imgPath) throws IOException {
        if(imgPath.contains("resources/")){
            return Files.readAllBytes(Paths.get("src/main/" + imgPath));
        } else
            return new byte[]{};
    }

}
