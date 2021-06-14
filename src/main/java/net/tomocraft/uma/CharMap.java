package net.tomocraft.uma;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class CharMap extends LinkedHashMap<String, Boolean> {

    private static final File propsFile = new File("./selected.properties");

    public CharMap() throws IOException {
        try (final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(CharMap.propsFile), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                final String[] entry = line.split("=");

                final Boolean v = super.put(entry[0], Boolean.parseBoolean(entry[1]));
                if (v != null) {
                    System.err.println("Duplicated entry has found with key " + entry[0]);
                }
            }
        }
    }

    public void save() throws IOException {
        try (final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CharMap.propsFile), StandardCharsets.UTF_8))) {
            for (Map.Entry<String, Boolean> entry : this.entrySet()) {
                bw.append(entry.getKey()).append('=').append(entry.getValue().toString()).append(System.lineSeparator());
            }
        }
    }
}
