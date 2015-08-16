package org.antinori.game.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class FindBamPrefixes {

    public static final String BAMDIR = "C:\\Users\\Paul\\Desktop\\BAMS";

    public static void main(String[] args) {
        List<String> prefixes = new ArrayList<String>();
        Collection<File> files = getFiles(BAMDIR, "M*.BAM");
        for (File f : files) {
            String name = f.getName();
            name = name.substring(0, 4);
            if (!prefixes.contains(name)) {
                prefixes.add(name);
            }
        }

        Collections.sort(prefixes);

        for (String s : prefixes) {
            System.out.print("\"" + s + "\",");
        }

    }

    public static Collection<File> getFiles(String directoryName, String filter) {
        File directory = new File(directoryName);
        return FileUtils.listFiles(directory, new WildcardFileFilter(filter), null);
    }

}
