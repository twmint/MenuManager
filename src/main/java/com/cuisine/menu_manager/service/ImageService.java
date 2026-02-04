package com.cuisine.menu_manager.service;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
    private static final int MAX_WIDTH = 800;
    private static final int MAX_HEIGHT = 600;

    private final Path uploadDir = Paths.get("menu-images");

    @Value("${app.base-url}")
    private String baseUrl;

    public String saveImage(MultipartFile file) throws IOException {
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String filename = UUID.randomUUID() + extension;
        Path filePath = uploadDir.resolve(filename);

        BufferedImage original = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
        if (original != null && (original.getWidth() > MAX_WIDTH || original.getHeight() > MAX_HEIGHT)) {
            BufferedImage resized = resizeImage(original);
            String formatName = getFormatName(extension);
            ImageIO.write(resized, formatName, filePath.toFile());
        } else {
            file.transferTo(filePath);
        }

        return baseUrl + "/menu-images/" + filename;
    }

    private BufferedImage resizeImage(BufferedImage original) {
        int origWidth = original.getWidth();
        int origHeight = original.getHeight();

        double widthRatio = (double) MAX_WIDTH / origWidth;
        double heightRatio = (double) MAX_HEIGHT / origHeight;
        double ratio = Math.min(widthRatio, heightRatio);

        int newWidth = (int) (origWidth * ratio);
        int newHeight = (int) (origHeight * ratio);

        BufferedImage resized = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(original, 0, 0, newWidth, newHeight, null);
        g.dispose();
        return resized;
    }

    private String getFormatName(String extension) {
        if (extension == null || extension.isEmpty()) {
            return "jpg";
        }
        String ext = extension.toLowerCase().replace(".", "");
        return switch (ext) {
            case "png" -> "png";
            case "gif" -> "gif";
            case "bmp" -> "bmp";
            default -> "jpg";
        };
    }
}