package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;


public class ReaderUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<List<String>> readObjectFromFile(String fileName) throws IOException {
        String fileContent = new String(ReaderUtils.class.getClassLoader().getResourceAsStream(fileName).readAllBytes());
        return mapper.readValue(fileContent, new TypeReference<>() {
        });
    }

}
