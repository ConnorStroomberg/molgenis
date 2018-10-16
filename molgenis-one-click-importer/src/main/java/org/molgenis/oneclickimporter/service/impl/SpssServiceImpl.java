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

    List<SpssVariable> variables = reader.getVariables();
    int colCount = variables.size();
    int rowCount = reader.getNumCases() + 1; // plus 1 for header

    // set header
    List<List<Object>> dataTable = variables.stream().map(variable -> {
      ArrayList<Object> column = new ArrayList<>(rowCount);
      column.add(0, variable.getVariableName());
      return column;
    }).collect(Collectors.toList());


    // set column data
    while (reader.readNextCase()) {
      IntStream.range(0, colCount)
          .forEach(
              colIndex -> {
                Object cellValue = readValue(reader, variables.get(colIndex), colIndex);
                dataTable.get(colIndex).add(cellValue);
              });
    }

    // transform table data to list of columns
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

  private @Nullable Object readValue (SpssDataFileReader reader, SpssVariable variable, int colIndex) {
    if (variable.isNumeric()) {
      final double doubleValue = reader.getDoubleValue(colIndex);
      return Double.isNaN(doubleValue) ? null : doubleValue;
    } else {
      return reader.getStringValue(colIndex);
    }
  }
}
