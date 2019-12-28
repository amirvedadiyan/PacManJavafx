/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ir.ac.kntu;

import net.sourceforge.pmd.cpd.CPD;
import net.sourceforge.pmd.cpd.CPDConfiguration;
import net.sourceforge.pmd.cpd.LanguageFactory;
import net.sourceforge.pmd.cpd.Match;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class DuplicateCodeTest {

    @Test
    public void testCPD() {
        CPDConfiguration cpdConfiguration = new CPDConfiguration();
        cpdConfiguration.setMinimumTileSize(90);
        cpdConfiguration.setFailOnViolation(true);
        cpdConfiguration.setLanguage(LanguageFactory.createLanguage("java"));
        CPD copyPasteDetector = new CPD(cpdConfiguration);

        File ROOT = new File("src/main/");
        System.err.println("Root is set to \"" + ROOT.getAbsolutePath() + "\".");

        List<File> files = listFiles(ROOT, "java");
        System.err.println("Found " + files.size() + " Java source file(s).");

        try {
            copyPasteDetector.add(files);
        } catch (IOException e) {
            e.printStackTrace();
        }

        copyPasteDetector.go();
        Iterator<Match> matches = copyPasteDetector.getMatches();
        Assert.assertFalse(matches.hasNext());
    }

    private static List<File> listFiles(File folder, String extension) {
        List<File> files = new ArrayList<>();
        if (folder.canRead()) {
            if (folder.isDirectory()) {
                for (File f : Objects.requireNonNull(folder.listFiles())) {
                    files.addAll(listFiles(f, extension));
                }
            } else if (folder.toString().endsWith("." + extension)) {
                files.add(folder);
            }
        }
        return files;
    }
}