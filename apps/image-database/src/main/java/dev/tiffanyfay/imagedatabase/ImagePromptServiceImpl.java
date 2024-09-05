package dev.tiffanyfay.imagedatabase;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class ImagePromptServiceImpl implements ImagePromptService {

    private final JdbcTemplate jdbcTemplate;

    public ImagePromptServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<ImagePrompt> getAllImages() {
        return jdbcTemplate.query("SELECT * FROM mydb.image_prompt",
                (rs, rowNum) -> new ImagePrompt(
                        rs.getObject("image_id", UUID.class),
                        rs.getString("prompt"),
                        rs.getString("url")
                ));
    }

    @Override
    public ImagePrompt saveImagePrompt(ImagePrompt imagePrompt) {
        jdbcTemplate.update(
                "INSERT INTO mydb.image_prompt (image_id, url, prompt) VALUES (?, ?, ?)",
                imagePrompt.imageId(), imagePrompt.url(), imagePrompt.prompt()
        );
        return imagePrompt;
    }
}
