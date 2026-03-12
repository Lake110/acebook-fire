package com.makersacademy.acebook.model;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AvatarOptions {

    public static final List<String> STYLES = List.of(
            "thumbs",
            "avataaars",
            "bottts",
            "pixel-art",
            "lorelei",
            "micah"
    );

    public static String getUrl(String style, String seed) {
        return "https://api.dicebear.com/9.x/" + style + "/svg?seed=" + seed;
    }
}