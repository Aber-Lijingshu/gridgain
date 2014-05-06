/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.hadoop;

import com.google.common.base.*;
import java.io.*;
import java.util.*;

/**
 * Abstract class for tests based on WordCount test job.
 */
public abstract class GridHadoopAbstractWordCountTest extends GridHadoopAbstractSelfTest {
    /**
     * Generates text file with words. In one line there are from 5 to 9 words.
     *
     * @param file File that there is generation for.
     * @param wordCounts Pair word and count, i.e "hello", 2, "world", 3, etc.
     * @throws java.io.FileNotFoundException If could not create the file.
     */
    protected void generateTestFile(File file, Object... wordCounts) throws FileNotFoundException {
        List<String> wordsArr = new ArrayList<>();

        //Generating
        for (int i = 0; i < wordCounts.length; i += 2) {
            String word = (String) wordCounts[i];
            int cnt = (Integer) wordCounts[i + 1];

            while (cnt-- > 0)
                wordsArr.add(word);
        }

        //Shuffling
        for (int i = 0; i < wordsArr.size(); i++) {
            int j = (int)(Math.random() * wordsArr.size());

            Collections.swap(wordsArr, i, j);
        }

        //Input file preparing
        PrintWriter testInputFileWriter = new PrintWriter(file);

        int j = 0;

        while (j < wordsArr.size()) {
            int i = 5 + (int)(Math.random() * 5);

            List<String> subList = wordsArr.subList(j, Math.min(j + i, wordsArr.size()));
            j += i;

            testInputFileWriter.println(Joiner.on(' ').join(subList));
        }

        testInputFileWriter.close();
    }

    /**
     * Reads whole text file into String.
     *
     * @param fileName Name of the file to read.
     * @return Content of the file as String value.
     * @throws java.io.IOException If could not read the file.
     */
    protected String readFile(String fileName) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        String line;
        while ((line = reader.readLine()) != null)
            sb.append(line).append("\n");

        return sb.toString();
    }
}
