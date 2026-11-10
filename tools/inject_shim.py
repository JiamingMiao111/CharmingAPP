import zipfile, sys, os
src, out, shimdex = sys.argv[1], sys.argv[2], sys.argv[3]
zin = zipfile.ZipFile(src)
existing = [n for n in zin.namelist() if n.startswith('classes') and n.endswith('.dex')]
n = len(existing) + 1
newname = 'classes.dex' if n == 1 else f'classes{n}.dex'
zout = zipfile.ZipFile(out, 'w', zipfile.ZIP_DEFLATED)
for item in zin.infolist():
    if item.filename.startswith('META-INF/') and (item.filename.endswith('.RSA') or item.filename.endswith('.SF') or item.filename.endswith('.DSA') or item.filename == 'META-INF/MANIFEST.MF'):
        continue
    zout.writestr(item, zin.read(item.filename))
zout.writestr(newname, open(shimdex,'rb').read())
zin.close(); zout.close()
print(f"  ✓ {out} (shim as {newname})")
