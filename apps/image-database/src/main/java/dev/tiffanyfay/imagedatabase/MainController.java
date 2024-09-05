package dev.tiffanyfay.imagedatabase;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

@RestController // This means that this class is a RestController
public class MainController {

    private final ImagePromptService imagePromptService;

    public MainController(ImagePromptService imagePromptService) {
        this.imagePromptService = imagePromptService;
    }

    @PostMapping("/images")
    void add (@RequestParam String prompt, @RequestParam String url) {
        imagePromptService.saveImagePrompt(new ImagePrompt(UUID.randomUUID(), prompt, url));
    }

    @ResponseBody
    @GetMapping("/images")
    Collection<ImagePrompt> all() {
        return imagePromptService.getAllImages();
    }

    // model-view-controller
    @GetMapping({"/", "/images.html"})
    ModelAndView allHtml() {
        // src/main/resources/templates/ + STRING + .html
        var map = Map.of("images", imagePromptService.getAllImages());
        return new ModelAndView("images", map);
    }
}
