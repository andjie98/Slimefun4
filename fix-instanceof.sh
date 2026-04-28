#!/bin/bash

# 修复Java 8不兼容的 instanceof pattern matching
# 语法：将 "if (x instanceof Type y)" 转换为 "if (x instanceof Type) { Type y = (Type) x;"

cd /workspace/src/main/java || exit 1

echo "开始修复pattern matching in instanceof..."

# 查找所有包含该模式的文件并修复
find . -name "*.java" -type f | while read file; do
    # 检查文件是否包含需要修复的内容
    if grep -q "instanceof\s\+\w\+\s\+\w\+" "$file"; then
        echo "修复: $file"
        # 使用Perl来进行复杂的替换
        perl -i -0777 -pe '
            s/(\s*+if\s*\(\s*+)(\w[\w$]*+(?:\s*\.\s*\w[\w$]*+)*+)\s+instanceof\s+(\w[\w$]*+(?:<[^>]++>)?)\s+(\w[\w$]*+)\s*\)/$1$2 instanceof $3\)\n$1{\n$1    $3 $4 = ($3) $2;/g
            s/(\s*+}?\s*+&&\s*+)(\w[\w$]*+(?:\s*\.\s*\w[\w$]*+)*+)\s+instanceof\s+(\w[\w$]*+(?:<[^>]++>)?)\s+(\w[\w$]*+)\s*/$1$2 instanceof $3) {\n$1    $3 $4 = ($3) $2;\n$1/g
        ' "$file"
    fi
done

echo "修复完成！"
