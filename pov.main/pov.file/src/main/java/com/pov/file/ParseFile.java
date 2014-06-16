package com.pov.file;

import java.util.List;
import java.util.Map;

public interface ParseFile {

    List<String[]> tokenizeFile(String filePath);

    List<Map<String, String>> tokenizeFileMap(String filePath, String[] keySeries);

    void writeTokenizedFileMap(List<Map<String, String>> data, String[] keySeries, String filePath,
            String fieldDelim, String recordDelim);

    String fileDirectory(String filePath);
}
