import xml.etree.ElementTree as ET
from packaging import version

def parse_dependencies(pom_path):
    tree = ET.parse(pom_path)
    root = tree.getroot()
    # Maven POM XML namespace
    ns = {'mvn': 'http://maven.apache.org/POM/4.0.0'}
    deps = {}
    for dep in root.findall('.//mvn:dependency', ns):
        gid = dep.find('mvn:groupId', ns)
        aid = dep.find('mvn:artifactId', ns)
        ver = dep.find('mvn:version', ns)

        # Skip dependencies missing groupId or artifactId
        if gid is None or aid is None:
            continue
        gid_text = gid.text.strip()
        aid_text = aid.text.strip()
        ver_text = ver.text.strip() if ver is not None else '0'  # Default version to 0 if missing

        deps[(gid_text, aid_text)] = ver_text
    return deps

def compare_poms(your_pom, sir_pom):
    your_deps = parse_dependencies(your_pom)
    sir_deps = parse_dependencies(sir_pom)

    print(f"Comparing dependencies in your POM '{your_pom}' vs sir's POM '{sir_pom}':\n")

    for key, your_ver in your_deps.items():
        sir_ver = sir_deps.get(key)
        if not sir_ver:
            print(f"- Unique dependency in your POM: {key[0]}:{key[1]}:{your_ver}")
        else:
            try:
                if version.parse(your_ver) > version.parse(sir_ver):
                    print(f"- Higher version in your POM: {key[0]}:{key[1]} Your: {your_ver} > Sir: {sir_ver}")
            except Exception:
                # If version parsing fails, just skip
                pass

if __name__ == "__main__":
    import sys
    if len(sys.argv) != 3:
        print("Usage: python compare_poms.py <your_pom.xml> <sir_pom.xml>")
        sys.exit(1)

    your_pom_path = sys.argv[1]
    sir_pom_path = sys.argv[2]

    compare_poms(your_pom_path, sir_pom_path)
