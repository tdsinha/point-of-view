package com.pov.file;

import static org.fest.assertions.Assertions.assertThat;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.collections.Lists;

import com.google.common.base.Splitter;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class ParseFileTest {

    final String errorFile = "PayoneerBugs.csv";
    final String outputFile = "/output.csv";
    Injector injector = null;
    ParseFile file = null;

    final String USERID = "userid";
    final String BANK_ID = "BankAccountId";
    final String DELETED = "deleted";
    final String[] keySeries = new String[] { USERID, BANK_ID, DELETED };

    @BeforeClass
    public void setup() {
        injector = Guice.createInjector(new FileModule());
        file = injector.getInstance(ParseFile.class);
    }

    @Test
    public void testFileDirectory() {
        URL inputFile = Thread.currentThread().getContextClassLoader().getResource(errorFile);
        String x = file.fileDirectory(inputFile.getPath());
        System.out.println(inputFile.getPath());
        System.out.println(x);
        assertThat(x).endsWith("test-classes");
    }

    @Test
    public void testIfBankAccountIdDuplicate() {

        URL url = Thread.currentThread().getContextClassLoader().getResource(errorFile);
        List<Map<String, String>> output = file.tokenizeFileMap(url.getPath(), keySeries);
        Set dupSet = new HashSet();

        for (Map<String, String> m : output) {

            String id = m.get(BANK_ID);
            if (dupSet.contains(id)) {
                assertThat(true).isFalse();
            } else {
                System.out.println(id);
                dupSet.add(id);
            }
        }
    }

    @Test
    public void testWriterTokenizedFile() {
        URL inputFile = Thread.currentThread().getContextClassLoader().getResource(errorFile);
        String outputFilePath = file.fileDirectory(inputFile.getPath()) + outputFile;
        List<Map<String, String>> outputList = Lists.newArrayList();
        Map<String, String> temp = null;

        List<Map<String, String>> data = file.tokenizeFileMap(inputFile.getPath(), keySeries);
        for (Map<String, String> m : data) {
            if (temp == null) {
                outputList.add(m);
            } else if (!temp.get(USERID).equals(m.get(USERID)) && temp.get(DELETED).equals("Y")) {
                outputList.add(m);
            }
            temp = m;
        }
        System.out.println("filePath :" + outputFilePath);
        file.writeTokenizedFileMap(outputList, keySeries, outputFilePath, " , ", "\n");

    }

    @Test
    public void testTokenizeFileMap() {

        final boolean debugPrint = true;
        URL url = Thread.currentThread().getContextClassLoader().getResource(errorFile);
        List<Map<String, String>> output = file.tokenizeFileMap(url.getPath(), keySeries);
        assertThat(output).hasSize(1489);
        for (Map<String, String> m : output) {
            assertThat(m.containsKey(USERID)).isTrue();
            assertThat(m.containsKey(BANK_ID)).isTrue();
            assertThat(m.containsKey(DELETED)).isTrue();

            if (debugPrint) {
                System.out.print(m.get(USERID) + "      ");
                System.out.print(m.get(BANK_ID) + "      ");
                System.out.print(m.get(DELETED) + "      ");
                System.out.println();
            }
        }

    }

    @Test
    public void testTokenizeFile() throws Exception {

        final boolean debugPrint = false;
        URL url = Thread.currentThread().getContextClassLoader().getResource(errorFile);
        System.out.println(url.getPath());
        List<String[]> l = file.tokenizeFile(url.getPath());

        if (debugPrint) {
            System.out
                    .println("================Test File Output : testTokenizeFile ===============");
            for (String[] arr : l) {
                for (String x : arr) {
                    System.out.print(x + "  ");
                }
                System.out.println();
            }

            System.out
                    .println("================End Test File Output : testTokenizeFile ===============");
        }
    }

    @Test
    public void testSplitter() {
        String a = "a : b: c ";
        List<String> l = Splitter.on(":").trimResults().splitToList(a);

        for (String s : l) {
            System.out.println(s.length());
            assertThat(s).hasSize(1);
        }

    }

}
