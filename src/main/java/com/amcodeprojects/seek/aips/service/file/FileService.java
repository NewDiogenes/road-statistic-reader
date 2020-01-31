package com.amcodeprojects.seek.aips.service.file;

import com.amcodeprojects.seek.aips.exception.InvalidFileNameException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileService {

  public Stream<String> readRawFile(String fileName){
    try {
      return Files.lines(Paths.get(fileName));
    } catch (IOException e) {
      throw new InvalidFileNameException(e);
    }
  }
}
