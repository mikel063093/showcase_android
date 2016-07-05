package com.co.showcase.ui.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import rx.Observable;

import static com.google.common.io.ByteStreams.copy;

/**
 * Created by home on 24/06/16.
 */

public class Base64Utils {
  private static final int NOT_FOUND = -1;

  public static final char EXTENSION_SEPARATOR = '.';

  private static final char UNIX_SEPARATOR = '/';

  private static final char WINDOWS_SEPARATOR = '\\';

  private static byte[] readFileToByteArray(@NonNull File file) throws IOException {
    InputStream in = null;
    try {
      in = new FileInputStream(file);
      return toByteArray(in);
    } finally {
      closeQuietly(in);
    }
  }

  private static byte[] toByteArray(@NonNull InputStream input) throws IOException {

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    copy(input, output);
    return output.toByteArray();
  }

  private static void closeQuietly(@Nullable InputStream input) {
    try {
      if (input != null) {
        input.close();
      }
    } catch (IOException ioe) {
      // ignore
    }
  }

  private static String file2Base64(@NonNull String filePath) {
    try {
      byte[] data = android.util.Base64.encode(readFileToByteArray(new File(filePath)),
          android.util.Base64.DEFAULT);
      return new String(data, "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Observable<String> rxFile2B64(@NonNull String filePath) {
    return Observable.create(sub -> {
      try {
        sub.onNext(file2Base64(filePath));
      } catch (Throwable e) {
        sub.onError(e);
      }
      sub.onCompleted();
    });
  }

  public static String getExtension(final String filename) {
    if (filename == null) {
      return null;
    }
    final int index = indexOfExtension(filename);
    if (index == NOT_FOUND) {
      return "";
    } else {
      return filename.substring(index + 1);
    }
  }

  public static int indexOfExtension(final String filename) {
    if (filename == null) {
      return NOT_FOUND;
    }
    final int extensionPos = filename.lastIndexOf(EXTENSION_SEPARATOR);
    final int lastSeparator = indexOfLastSeparator(filename);
    return lastSeparator > extensionPos ? NOT_FOUND : extensionPos;
  }

  private static int indexOfLastSeparator(String filename) {
    if (filename == null) {
      return NOT_FOUND;
    }
    final int lastUnixPos = filename.lastIndexOf(UNIX_SEPARATOR);
    final int lastWindowsPos = filename.lastIndexOf(WINDOWS_SEPARATOR);
    return Math.max(lastUnixPos, lastWindowsPos);
  }
}
