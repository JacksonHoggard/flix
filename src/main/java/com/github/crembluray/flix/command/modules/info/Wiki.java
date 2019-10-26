package com.github.crembluray.flix.command.modules.info;

import com.github.crembluray.flix.command.Command;
import com.github.crembluray.flix.util.DiscordUtils;
import discord4j.core.object.entity.Message;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Wiki extends Command {

    private static final String REPLY_NOT_FOUND = "Wiki page not found! Please try again.";

    public Wiki() {
        super("wiki", "Searches wikipedia for a desired phrase.");
    }

    @Override
    public void onCommand(Message message, String[] args) {
        if (!message.getContent().orElse("").contains(" ")) {
            DiscordUtils.sendMessage(message, REPLY_NOT_FOUND);
            return;
        }

        try {
            DiscordUtils.sendMessage(message, searchWiki(message.getContent().orElse("f!wiki").substring(7)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String searchWiki(String subject) throws IOException {
        URL url = new URL("https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json&exsentences=1&exintro=&explaintext=&exsectionformat=plain&titles=" + subject.replace(" ", "_"));

        // parse text
        StringBuilder text = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line.trim());
            }
        }

        JSONObject json = new JSONObject(text.toString());
        JSONObject query = json.getJSONObject("query");
        JSONObject pages = query.getJSONObject("pages");
        String extract = "";
        for (String key : pages.keySet()) {
            JSONObject page = pages.getJSONObject(key);
            try {
                extract = page.getString("extract");
            } catch (JSONException e) {
                return REPLY_NOT_FOUND;
            }
        }
        return "```json\n" + extract + "\n```";
    }
}
