package org.molgenis.oneclickimporter.service.impl;

import static java.util.Objects.requireNonNull;

import com.bedatadriven.spss.SpssDataFileReader;
import com.bedatadriven.spss.SpssVariable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import org.molgenis.oneclickimporter.factory.SpssDataFileReaderFactory;
import org.molgenis.oneclickimporter.model.Column;
import org.molgenis.oneclickimporter.model.DataCollection;
import org.molgenis.oneclickimporter.service.SpssService;
import org.springframework.stereotype.Component;

@Component
public class SpssServiceImpl implements SpssService {

  private final SpssDataFileReaderFactory spssDataFileReaderFactory;

  public SpssServiceImpl(SpssDataFileReaderFactory spssDataFileReaderFactory) {
    this.spssDataFileReaderFactory = requireNonNull(spssDataFileReaderFactory);
  }

  @Override
  public DataCollection fromSavFile(File file, String collectionName) throws IOException {
    SpssDataFileReader reader = spssDataFileReaderFactory.create(file);

    List<List<Object>> dataTable = readData(reader);

    // Transform table data to list of columns for use in one click importer
    List<Column> columns =
        IntStream.range(0, dataTable.size())
            .mapToObj(
                i ->
                    Column.create(
                        (String) dataTable.get(i).get(0),
                        i,
                        dataTable.get(i).subList(1, dataTable.get(i).size())))
            .collect(Collectors.toList());

    return DataCollection.create(collectionName, columns);
  }

  /**
   * The Spss Reader reads the dataTable row by row. The OneClickImporter DataCollection takes a
   * list of columns. Therefore we need to read the lines line by line and store them in a random
   * access data structure (a list of lists).
   *
   * @param reader The Spss file reader, reads lines sequentially.
   * @return Random access structure containing headers and data, List of columns with header at
   *     index 0.
   * @throws IOException may throw io exception when reading data from file.
   */
  private List<List<Object>> readData(SpssDataFileReader reader) throws IOException {
    List<SpssVariable> variables = reader.getVariables();
    final int colCount = variables.size();
    final int rowCount = reader.getNumCases() + 1; // plus 1 for header

    // Create random access data structure and read header data
    List<List<Object>> dataTable =
        variables
            .stream()
            .map(
                variable -> {
                  ArrayList<Object> column = new ArrayList<>(rowCount);
                  column.add(0, variable.getVariableName());
                  return column;
                })
            .collect(Collectors.toList());

    // Read value data into the data structure
    while (reader.readNextCase()) {
      IntStream.range(0, colCount)
          .forEach(
              colIndex -> {
                Object cellValue = readValue(reader, variables.get(colIndex), colIndex);
                dataTable.get(colIndex).add(cellValue);
              });
    }
    return dataTable;
  }

  private @Nullable Object readValue(
      SpssDataFileReader reader, SpssVariable variable, int colIndex) {
    if (variable.isNumeric()) {
      final double doubleValue = reader.getDoubleValue(colIndex);
      return Double.isNaN(doubleValue) ? null : doubleValue;
    } else {
      return reader.getStringValue(colIndex);
    }
  }
}
