package me.taylorkelly.help;

import java.io.File;
import java.io.FilenameFilter;

public class YmlFilter implements FilenameFilter {

    @Override
    public boolean accept(File file, String name) {
        if (name.endsWith(".yml") && !name.endsWith("_orig.yml")) {
            return true;
        } else {
            return false;
        }
    }
}
