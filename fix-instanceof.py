#!/usr/bin/env python3

import os
import re

# 定义需要修复的模式
# 匹配 "something instanceof Type var"
pattern_simple = re.compile(r'(\s*)(\w+(?:\.\w+)*)\s+instanceof\s+(\w+(?:<[^>]+>)?)\s+(\w+)(\s*)')
pattern_with_conditions = re.compile(r'(\s*)(\w+(?:\.\w+)*)\s+instanceof\s+(\w+(?:<[^>]+>)?)\s+(\w+)\s*&&')

def fix_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original = content
    
    # 逐个替换，我们先做简单的if语句
    # 查找所有if语句的模式
    lines = content.split('\n')
    new_lines = []
    
    i = 0
    while i < len(lines):
        line = lines[i]
        
        # 查找简单情况： "if (x instanceof Type y) {" 或者 "if (x instanceof Type y)"
        match = re.search(r'(\s*)if\s*\(\s*(\w+(?:\.\w+)*)\s+instanceof\s+(\w+(?:<[^>]+>)?)\s+(\w+)\s*\)', line)
        
        if match:
            indent = match.group(1)
            var_name = match.group(2)
            type_name = match.group(3)
            new_var_name = match.group(4)
            
            # 查找这一行是否有 {
            has_brace = '{' in line
            # 找到 if (x instanceof Type y) 的结束位置
            before = line[:match.start()]
            after = line[match.end():]
            
            # 新的第一行
            line1 = f'{indent}if ({var_name} instanceof {type_name}) {{'
            # 新的第二行
            line2 = f'{indent}    {type_name} {new_var_name} = ({type_name}) {var_name};'
            
            if has_brace:
                # 如果已经有 {，就需要合并
                new_lines.append(line1)
                new_lines.append(line2)
                # 处理剩余的内容
                rest = after.split('{', 1)[1] if '{' in after else after
                if rest.strip():
                    new_lines.append(f'{indent}    {rest.lstrip()}')
            else:
                new_lines.append(line1)
                new_lines.append(line2)
                new_lines.append(f'{indent}    {after.strip()}')
            
            i += 1
            continue
        
        # 检查多个条件情况： "if (... && x instanceof Type y && ...) {"
        # 这里我们只处理简单的情况，复杂的我们之后手动处理
        # 查找多个条件的
        has_pattern = re.search(r'(\w+(?:\.\w+)*)\s+instanceof\s+(\w+(?:<[^>]+>)?)\s+(\w+)', line)
        if has_pattern and '&&' in line:
            # 先将行添加进去，不做修改，之后手动处理复杂的
            new_lines.append(line)
            i += 1
            continue
        
        new_lines.append(line)
        i += 1
    
    # 检查是否有修改
    new_content = '\n'.join(new_lines)
    if new_content != original:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(new_content)
        return True
    return False

def main():
    src_dir = '/workspace/src/main/java'
    count = 0
    
    print("开始修复pattern matching in instanceof...")
    
    for root, _, files in os.walk(src_dir):
        for file in files:
            if file.endswith('.java'):
                file_path = os.path.join(root, file)
                if fix_file(file_path):
                    print(f"修复: {file_path}")
                    count +=1
    
    print(f"修复完成，共处理 {count} 个文件！")

if __name__ == '__main__':
    main()
