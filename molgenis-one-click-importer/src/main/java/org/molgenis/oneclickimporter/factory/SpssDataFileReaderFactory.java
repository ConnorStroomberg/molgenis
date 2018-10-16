package org.molgenis.oneclickimporter.factory;

import com.bedatadriven.spss.SpssDataFileReader;
import java.io.File;
import java.io.IOException;

public interface SpssDataFileReaderFactory {

  SpssDataFileReader create(File file) throws IOException;
}
