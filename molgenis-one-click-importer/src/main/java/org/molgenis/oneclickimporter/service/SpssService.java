package org.molgenis.oneclickimporter.service;

import java.io.File;
import java.io.IOException;
import org.molgenis.oneclickimporter.model.DataCollection;

public interface SpssService {

  DataCollection fromSavFile(File file, String collectionName) throws IOException;
}
