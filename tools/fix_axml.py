#!/usr/bin/env python3
"""AXML 给 uses-library 加 required=false  -  Charming x DeepSeek Harness
正确布局: attributeCount 在 offset+28
"""
import struct, sys, zipfile

def parse_pool(data):
    typ, hsize, size = struct.unpack_from('<HHI', data, 8)
    count, _, flags, str_start, _ = struct.unpack_from('<IIIII', data, 16)
    offs = struct.unpack_from('<%dI' % count, data, 8 + hsize)
    utf8 = bool(flags & 0x100)
    base = 8 + str_start
    out = []
    for off in offs:
        p = base + off
        if utf8:
            n = data[p]
            if n & 0x80: n = ((n & 0x7f) << 8) | data[p+1]; p += 2
            else: p += 1
            n2 = data[p]
            if n2 & 0x80: n2 = ((n2 & 0x7f) << 8) | data[p+1]; p += 2
            else: p += 1
            out.append(data[p:p+n2].decode('utf-8', 'replace'))
        else:
            n = struct.unpack_from('<H', data, p)[0]; p += 2
            out.append(data[p:p+n*2].decode('utf-16-le', 'replace'))
    return out

def patch(src, out, targets):
    zin = zipfile.ZipFile(src)
    data = bytearray(zin.read('AndroidManifest.xml'))
    strings = parse_pool(data)
    req = strings.index('required')
    ans = strings.index('http://schemas.android.com/apk/res/android')

    # 收集所有 uses-library，合并成一个 patch 列表（从后往前处理）
    items = []
    o = 8
    while o + 8 <= len(data):
        typ, hsize, sz = struct.unpack_from('<HHI', data, o)
        if sz < 8 or o + sz > len(data): break
        if typ == 0x0102:
            _, nm = struct.unpack_from('<Ii', data, o + 16)
            asc, asz, acnt = struct.unpack_from('<HHH', data, o + 24)
            elem = strings[nm] if 0 <= nm < len(strings) else ''
            if elem == 'uses-library' and acnt > 0:
                attrs = []
                for i in range(acnt):
                    p = o + 16 + asc + i * asz
                    if p + 20 > len(data): break
                    ns, n, raw = struct.unpack_from('<Iii', data, p)
                    attrs.append((n, raw))
                names = [strings[r] if 0 <= r < len(strings) else '' for (_, r) in attrs]
                has_req = any(n == req for (n, _) in attrs)
                matched = [t for t in targets if t in names]
                if matched and not has_req:
                    items.append((o, sz, asc, asz, acnt, matched[0]))
        o += sz

    if not items:
        print(f"[{src}] 无需修改")
        zin.close()
        return False

    print(f"[{src}] 修改 {len(items)} 处: {[i[5] for i in items]}")

    # 从后往前改
    for (off, size, a_start, a_size, a_count, tname) in reversed(items):
        new_attr = struct.pack('<Iii', ans, req, -1) + struct.pack('<HBBi', 8, 0, 0x12, 0)
        data[off+size:off+size] = new_attr
        struct.pack_into('<I', data, off + 4, size + 20)     # chunk size
        struct.pack_into('<H', data, off + 28, a_count + 1)  # attributeCount ← 关键
        print(f"  ✓ {tname} @0x{off:x}")

    total = struct.unpack_from('<I', data, 4)[0] + 20 * len(items)
    struct.pack_into('<I', data, 4, total)

    zout = zipfile.ZipFile(out, 'w', zipfile.ZIP_DEFLATED)
    for item in zin.infolist():
        n = item.filename
        if n.startswith('META-INF/'): continue
        zout.writestr(item, bytes(data) if n == 'AndroidManifest.xml' else zin.read(n))
    zin.close(); zout.close()
    return True

if __name__ == '__main__':
    patch(sys.argv[1], sys.argv[2], sys.argv[3:])
