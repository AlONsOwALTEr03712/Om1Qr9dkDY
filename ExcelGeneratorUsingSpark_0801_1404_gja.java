// 代码生成时间: 2025-08-01 14:04:38
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;

// Excel表格自动生成器
public class ExcelGeneratorUsingSpark {

    // 创建一个Spark会话
    private SparkSession spark;

    // 构造函数，初始化Spark会话
    public ExcelGeneratorUsingSpark() {
        spark = SparkSession
            .builder()
            .appName("Excel Generator")
            .getOrCreate();
    }

    // 从数据集生成Excel文件
    public void generateExcelFromDataset(Dataset<Row> dataset, String outputFilePath) throws IOException {
        try {
            // 将数据集写入Excel文件
            Workbook workbook = new XSSFWorkbook();
            dataset.coalesce(1).write().format("xlsx").save(outputFilePath);
            workbook.write(new FileOutputStream(outputFilePath + ".xlsx"));

            // 关闭工作簿
            workbook.close();
        } catch (Exception e) {
            // 错误处理
            System.err.println("Error generating Excel file: " + e.getMessage());
            throw new IOException("Error generating Excel file", e);
        }
    }

    // 关闭Spark会话
    public void stop() {
        if (spark != null) {
            spark.stop();
        }
    }

    // 主函数，用于测试Excel生成器
    public static void main(String[] args) {
        // 创建Excel生成器实例
        ExcelGeneratorUsingSpark excelGenerator = new ExcelGeneratorUsingSpark();
        // 创建测试数据集
        Dataset<Row> sampleDataset = excelGenerator.spark.createDataFrame(
             java.util.Arrays.asList(
                 java.util.Arrays.asList("John", "Doe", 30),
                 java.util.Arrays.asList("Jane", "Doe", 25)
             ),
             String.class, String.class, Integer.class
         );
         
        // 生成Excel文件
        excelGenerator.generateExcelFromDataset(sampleDataset, "./output");
        // 停止Spark会话
        excelGenerator.stop();
    }
}
