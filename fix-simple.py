#!/usr/bin/env python3
import os
import re

def fix_instanceof(file_path):
    """
    简单直接的修复函数：将所有的 "x instanceof Type y" 替换为 "x instanceof Type && (Type y = (Type) x)" 
    不过更直接的是先处理简单的单行情况
    """
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original = content
    
    # 模式1: 简单的 if (foo instanceof Bar bar) {
    # 替换为 if (foo instanceof Bar) { Bar bar = (Bar) foo;
    def replacer1(match):
        indent = match.group(1)
        expr = match.group(2)
        type_name = match.group(3)
        var_name = match.group(4)
        rest = match.group(5)
        
        return f"{indent}if ({expr} instanceof {type_name}) {{\n{indent}    {type_name} {var_name} = ({type_name}) {expr};{rest}"
    
    content = re.sub(r'(\s*)if\s*\(\s*(\w+(?:\.\w+)*)\s+instanceof\s+(\w+(?:<[^>]+>)?)\s+(\w+)\s*\)(\s*\{?)', 
                    replacer1, content)
    
    # 模式2: 在if条件中间的 && 连接的情况
    # 比如 "if (a && foo instanceof Bar bar && b) {"
    def replacer2(match):
        before = match.group(1)
        expr = match.group(2)
        type_name = match.group(3)
        var_name = match.group(4)
        after = match.group(5)
        
        # 简单处理：先检查instanceof，然后声明变量，再进行剩余条件判断
        return f"{before}{expr} instanceof {type_name}) {{\n{before}    {type_name} {var_name} = ({type_name}) {expr};\n{before}    if ({after}"
    
    content = re.sub(r'(\s*if\s*\([^)]*?)\s*&&\s*(\w+(?:\.\w+)*)\s+instanceof\s+(\w+(?:<[^>]+>)?)\s+(\w+)\s*&&\s*([^)]+\))', 
                    replacer2, content)
    
    # 模式3: else if 的情况
    content = re.sub(r'(\s*)else\s+if\s*\(\s*(\w+(?:\.\w+)*)\s+instanceof\s+(\w+(?:<[^>]+>)?)\s+(\w+)\s*\)(\s*\{?)',
                    lambda m: f"{m.group(1)}else if ({m.group(2)} instanceof {m.group(3)}) {{\n{m.group(1)}    {m.group(3)} {m.group(4)} = ({m.group(3)}) {m.group(2)};{m.group(5)}",
                    content)
    
    # 模式4: 不带括号的简单情况
    content = re.sub(r'(\s*)(\w+(?:\.\w+)*)\s+instanceof\s+(\w+(?:<[^>]+>)?)\s+(\w+)(\s*)(\)|&&|\|\||;)')
    # 我们使用最简单的方法，先查找所有包含 pattern 的行，然后手动处理
    
    # 现在使用更简单但更安全的方法：将所有 "x instanceof Type y" 替换为 "x instanceof Type" 并添加变量声明行
    # 我们一行行处理
    lines = content.split('\n')
    new_lines = []
    i = 0
    while i < len(lines):
        line = lines[i]
        # 查找这个模式
        match = re.search(r'(\s*)(\w+(?:\.\w+)*)\s+instanceof\s+(\w+(?:<[^>]+>)?)\s+(\w+)', line)
        if match:
            indent = match.group(1)
            expr = match.group(2)
            type_name = match.group(3)
            var_name = match.group(4)
            
            # 替换该行中的 "expr instanceof Type var" 为 "expr instanceof Type"
            new_line = line.replace(f"{expr} instanceof {type_name} {var_name}", f"{expr} instanceof {type_name}")
            new_lines.append(new_line)
            
            # 如果是 if 或者 else if，并且后面有 {，我们就在后面插入声明行
            if ('if' in new_line or 'else' in new_line) and '{' in new_line:
                # 找到 { 的位置
                brace_pos = new_line.find('{')
                if brace_pos != -1:
                    before_brace = new_line[:brace_pos+1]
                    after_brace = new_line[brace_pos+1:]
                    
                    new_lines[-1] = before_brace
                    new_lines.append(f"{indent}    {type_name} {var_name} = ({type_name}) {expr};")
                    if after_brace.strip():
                        new_lines.append(f"{indent}    {after_brace.strip()}")
            else:
                # 如果不是，我们也添加一行声明
                new_lines.append(f"{indent}{type_name} {var_name} = ({type_name}) {expr};")
            
            i += 1
        else:
            new_lines.append(line)
            i += 1
    
    content = '\n'.join(new_lines)
    
    if content != original:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(content)
        return True
    return False

def main():
    src_dir = '/workspace/src/main/java'
    count = 0
    for root, _, files in os.walk(src_dir):
        for filename in files:
            if filename.endswith('.java'):
                path = os.path.join(root, filename)
                if fix_instanceof(path):
                    print(f"Fixed: {path}")
                    count += 1
    
    print(f"Fixed {count} files!")

if __name__ == '__main__':
    main()
