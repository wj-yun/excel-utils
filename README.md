# excel-utils
Support for bulk excel processing

---

## Example

```java
import java.util.List;

public void write(OutputStream outputStream) {
    var excelFile = new DefaultExcelFile<>(Example.class);

    excelFile.addRow(new Example("A", 1));
    excelFile.addRow(new Example("B", 2));
    excelFile.addRow(new Example("C", 3));

    excelFile.addRows(List.of(new Example("D", 4), new Example("E", 5)));

    excelFile.write(outputStream);
}
```
