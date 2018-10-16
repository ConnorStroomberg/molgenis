package org.molgenis.oneclickimporter.factory.impl;

import com.bedatadriven.spss.SpssDataFileReader;
import java.io.File;
import java.io.IOException;
import org.molgenis.oneclickimporter.factory.SpssDataFileReaderFactory;
import org.springframework.stereotype.Component;

@Component
public class SpssDataFileReaderFactoryImpl implements SpssDataFileReaderFactory {

  @Override
  public SpssDataFileReader create(File file) throws IOException {
    return new SpssDataFileReader(file);
  }
}
