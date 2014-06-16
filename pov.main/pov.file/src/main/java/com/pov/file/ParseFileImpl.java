package com.pov.file;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.pov.file.PovFileException.ErrorCodes;

public class ParseFileImpl implements ParseFile {

    private final Logger log = LogManager.getLogger(ParseFileImpl.class);

    @Override
    public String fileDirectory(String filePath) {

        Path p = FileSystems.getDefault().getPath(filePath);
        return p.getParent().toString();
    }

    public void writeTokenizedFileMap(List<Map<String, String>> data, String[] keySeries,
            String filePath, String fieldDelim, String recordDelim) {

        Path path = FileSystems.getDefault().getPath(filePath);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (Map<String, String> m : data) {
                for (int i = 0; i < keySeries.length; i++) {
                    writer.append(m.get(keySeries[i]));
                    if (i != keySeries.length - 1)
                        writer.append(fieldDelim);
                }
                writer.append(recordDelim);
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new PovFileException(ErrorCodes.FILE_OPERATION, e);
        }

    }

    public List<Map<String, String>> tokenizeFileMap(String filePath, String[] keySeries) {

        List<Map<String, String>> output = Lists.newArrayList();
        List<String[]> temp = tokenizeFile(filePath);
        if (keySeries.length > findMaxSizeOfTokens(temp)) {
            throw new PovFileException(ErrorCodes.INVALID_KEY_SERIES_LENGTH, null);
        }
        for (String[] s : temp) {
            Map<String, String> m = new HashMap<String, String>();
            for (int i = 0; i < s.length; i++) {
                m.put(keySeries[i], s[i]);
            }
            output.add(m);
        }

        return output;
    }

    public List<String[]> tokenizeFile(String filePath) {

        List<String[]> output = Lists.newArrayList();
        try {

            List<String> l = Files.readAllLines(FileSystems.getDefault().getPath(filePath));
            tokenize(l, output);
        } catch (FileNotFoundException e) {
            log.error("error file not found :", e);
            throw new PovFileException(ErrorCodes.NOT_EXISTS, e);
        } catch (IOException e) {
            log.error("error while file parsing :", e);
            throw new PovFileException(ErrorCodes.FILE_OPERATION, e);
        }

        return output;
    }

    private void tokenize(List<String> data, List<String[]> output) {

        for (String s : data) {
            List<String> temp = Splitter.on(",").trimResults().splitToList(s);
            output.add(temp.toArray(new String[0]));
        }
    }

    private int findMaxSizeOfTokens(List<String[]> tokenList) {

        int maxSize = 0;
        for (String[] s : tokenList) {
            maxSize = s.length > maxSize ? s.length : maxSize;
        }
        return maxSize;
    }

}
