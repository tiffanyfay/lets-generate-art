package dev.tiffanyfay.imagedatabase;

import org.springframework.data.annotation.Id;

// Just a data holder instead of having a class with getters etc.
public record ImagePrompt (@Id Integer id, String prompt, String url){ }
