import org.junit.jupiter.api.Test;
import utils.ReaderUtils;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CombinatorTest {

    private static final String TEST_FILE_NAME_1 = "test1.json";
    private static final String TEST_FILE_NAME_2 = "test2.json";
    private static final String TEST_FILE_NAME_3 = "test3.json";

    @Test
    void findAllPossibleRowCombinations_test1fileReadAndProcessed_7rowsReturned() throws IOException {
        //given
        List<List<String>> json = ReaderUtils.readObjectFromFile(TEST_FILE_NAME_1);
        //when
        List<List<String>> combinedJson = Combinator.findAllPossibleRowCombinations(json,4);
        //assert
        assertEquals(7, combinedJson.size());
    }

    @Test
    void findAllPossibleRowCombinations_test2fileReadAndProcessed_1rowsReturned() throws IOException {
        //given
        List<List<String>> json = ReaderUtils.readObjectFromFile(TEST_FILE_NAME_2);
        //when
        List<List<String>> combinedJson = Combinator.findAllPossibleRowCombinations(json,4);
        //assert
        assertEquals(1, combinedJson.size());
    }

    @Test
    void findAllPossibleRowCombinations_test3fileReadAndProcessed_0rowsReturned() throws IOException {
        //given
        List<List<String>> json = ReaderUtils.readObjectFromFile(TEST_FILE_NAME_3);
        //when
        List<List<String>> combinedJson = Combinator.findAllPossibleRowCombinations(json,4);
        //assert
        assertEquals(0, combinedJson.size());
    }
}