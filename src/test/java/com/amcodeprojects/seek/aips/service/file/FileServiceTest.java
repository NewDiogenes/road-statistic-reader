package com.amcodeprojects.seek.aips.service.file;

import com.amcodeprojects.seek.aips.exception.InvalidFileNameException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class FileServiceTest {
  private final String testFileName = "src/test/resources/testFile.txt";
  private FileService fileService;

  @Before
  public void setup() {
    fileService = new FileService();
  }

  @Test(expected = InvalidFileNameException.class)
  public void givenANonExistentFileName_readRawFile_shouldThrowAnException() {
    String testFile = "notAFile";

    fileService.readRawFile(testFile);
  }

  @Test
  public void givenAFileNameThatExists_readRawFile_shouldReturnTheFileLinesAsAStreamOfStrings() {
    Stream<String> expectedStrings = Stream.of("testStringOne", "testStringTwo");

    Stream<String> readStrings = fileService.readRawFile(testFileName);

    assertArrayEquals(expectedStrings.toArray(), readStrings.toArray());
  }

}