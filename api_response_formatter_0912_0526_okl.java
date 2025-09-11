// 代码生成时间: 2025-09-12 05:26:39
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// APIResponseFormatter 类用于格式化API响应
public class APIResponseFormatter implements Serializable {
# 扩展功能模块
    // 构造函数
    public APIResponseFormatter() {
    }

    // 格式化API响应的方法
    public String formatResponse(Dataset<Row> dataset, String successMessage, boolean includeCount) {
        // 检查输入数据集是否为空
        if (dataset == null || dataset.isEmpty()) {
            return getErrorResponse(