package nullfedora.lifesteal;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.UUID;

public class BanData {
    private final Yaml yaml = new Yaml();
    private final File dataFile = new File("plugins" + File.separator + "Lifesteal" + File.separator + "bans.yml");
    private ArrayList<String> data;

    public BanData() {
        if(!dataFile.getParentFile().exists()) {
            dataFile.getParentFile().mkdir();
        }

        if(!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            data = yaml.load(new FileInputStream(dataFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        if(data == null) {
            data = new ArrayList<String>();
        }
    }

    public void addUUID(UUID uuid) {
        data.add(uuid.toString());
        try {
            FileWriter writer = new FileWriter(dataFile, false);
            writer.flush();
            yaml.dump(data, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUUID(UUID uuid) {
        data.remove(uuid.toString());
        try {
            FileWriter writer = new FileWriter(dataFile, false);
            writer.flush();
            yaml.dump(data, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean hasUUID(UUID uuid) {
        return data.contains(uuid.toString());
    }
}
