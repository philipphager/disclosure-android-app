package de.philipphager.disclosure.util.device;

import java.io.File;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileUtilsShould {
  @Mock protected File mockFile;
  @Mock protected File mockFileTwo;
  @Mock protected File mockFileThree;
  @Mock protected File mockDirectory;
  @Mock protected File mockDirectoryTwo;
  @InjectMocks protected FileUtils fileUtils;

  @Before public void setUp() {
    when(mockDirectory.isDirectory()).thenReturn(true);
    when(mockDirectoryTwo.isDirectory()).thenReturn(true);
  }

  @Test public void deleteSingleFile() {
    fileUtils.delete(mockFile);
    verify(mockFile, times(1)).delete();
  }

  @Test public void deleteAllFilesInDirectory() {
    when(mockDirectory.listFiles()).thenReturn(new File[] {mockFile, mockFileTwo, mockFileThree});

    fileUtils.delete(mockDirectory);

    verify(mockFile, times(1)).delete();
    verify(mockFileTwo, times(1)).delete();
    verify(mockFileThree, times(1)).delete();
    verify(mockDirectory, times(1)).delete();
  }

  @Test public void deleteAllFilesInNestedDirectoryStructure() {
    when(mockDirectoryTwo.listFiles()).thenReturn(new File[] {mockFileThree});
    when(mockDirectory.listFiles()).thenReturn(
        new File[] {mockFile, mockFileTwo, mockDirectoryTwo});

    fileUtils.delete(mockDirectory);

    verify(mockFile, times(1)).delete();
    verify(mockFileTwo, times(1)).delete();
    verify(mockFileThree, times(1)).delete();
    verify(mockDirectory, times(1)).delete();
    verify(mockDirectoryTwo, times(1)).delete();
  }

  @Test public void doNotFailWhenSubdirectoriesAreNull() {
    when(mockDirectory.listFiles()).thenReturn(null);

    fileUtils.delete(mockDirectory);

    verify(mockDirectory, times(1)).delete();
  }

  @Test(expected = IllegalStateException.class)
  public void throwErrorWhenCalledWithNullFile() {
    fileUtils.delete(null);
  }
}
