package dev.tiffanyfay.imagedatabase;

import java.util.Collection;

public interface ImagePromptService {
    Collection<ImagePrompt> getAllImages();
    ImagePrompt saveImagePrompt(ImagePrompt imagePrompt);
}
