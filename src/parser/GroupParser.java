package parser;

import model.Group;

import java.io.File;

public interface GroupParser {
    Group parse(File source) throws Exception;
}
