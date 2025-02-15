package habuma.springaiimagegen;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ImageGenController {

    private final ImageModel imageModel;

    public ImageGenController(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    @PostMapping("/imagegen")
    public String imageGen(@RequestBody ImageGenRequest request) {
        ImageOptions options = ImageOptionsBuilder.builder()
                .model("dall-e-3")
                .height(1024)
                .width(1024)
                .build();

        ImagePrompt imagePrompt = new ImagePrompt(request.prompt(), options);
        ImageResponse response = imageModel.call(imagePrompt);
        String imageUrl = response.getResult().getOutput().getUrl();

        return "redirect:" + imageUrl;
    }


}
