package dev.tiffanyfay.imagedatabase;

import java.util.UUID;

// Just a data holder instead of having a class with getters etc.
public record ImagePrompt (UUID imageId, String prompt, String url){ }
