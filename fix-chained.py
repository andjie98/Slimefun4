#!/usr/bin/env python3
import os
import re

def replace_in_file(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    original = content
    
    # 匹配带方法调用的表达式
    # 比如 something.foo().bar() instanceof Type var
    content = re.sub(r'([a-zA-Z0-9_.()]+)\s+instanceof\s+(\w+(?:<[^>]+>)?)\s+(\w+)', r'\1 instanceof \2', content)
    
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
                    print(f"Fixed: {path}")
                    count +=1
    
    print(f"Fixed {count} files!")

if __name__ == '__main__':
    main()
