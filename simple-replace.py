#!/usr/bin/env python3
import os
import re

def replace_in_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original = content
    
    # 简单替换：将 "foo instanceof Bar bar" 替换为 "foo instanceof Bar"
    # 这个正则表达式匹配任意单词字符组成的表达式
    content = re.sub(r'(\w+(?:\.\w+)*)\s+instanceof\s+(\w+(?:<[^>]+>)?)\s+(\w+)', r'\1 instanceof \2', content)
    
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
                if replace_in_file(path):
                    print(f"Replaced in: {path}")
                    count += 1
    
    print(f"Replaced in {count} files!")

if __name__ == '__main__':
    main()
