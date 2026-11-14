#!/bin/sh
set -eu
cd "$(dirname "$0")"
mkdir -p out
javac --release 17 -encoding UTF-8 -d out src/*.java tests/*.java
java -cp out LaundryTests
