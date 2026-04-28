#!/usr/bin/env python3
import os
import re

def find_files(src_dir):
    count = 0
    files = []
    
    for root, _, filenames in os.walk(src_dir):
        for filename in filenames:
            if filename.endswith('.java'):
                path = os.path.join(root, filename)
                with open(path, 'r', encoding='utf-8') as f:
                    content = f.read()
                    if re.search(r'instanceof\s+\w+\s+\w+', content):
                        print(f"Found in: {path}")
                        count += 1
                        files.append(path)
    
    print(f"\nTotal files with pattern: {count}")
    return files

if __name__ == '__main__':
    find_files('/workspace/src/main/java')
