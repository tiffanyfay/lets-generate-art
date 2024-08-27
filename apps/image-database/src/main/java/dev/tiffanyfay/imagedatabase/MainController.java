package dev.tiffanyfay.imagedatabase;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Map;

@RestController // This means that this class is a RestController
public class MainController {

    private final ImageRepository imageRepository;

    public MainController(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @PostMapping("/images")
    void add (@RequestParam String prompt, @RequestParam String url) {
        imageRepository.save(new ImagePrompt(null, prompt, url));
    }

    @ResponseBody
    @GetMapping("/images")
    Collection<ImagePrompt> all() {
        return this.imageRepository.findAll();
    }

    // model-view-controller
    @GetMapping({"/", "/images.html"})
    ModelAndView allHtml() {
        // src/main/resources/templates/ + STRING + .html
        var map = Map.of("images", this.imageRepository.findAll());
        return new ModelAndView("images", map);
    }
}
