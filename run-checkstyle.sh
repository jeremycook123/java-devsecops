#!/bin/bash

# Find all Java files staged for commit
FILES=$(git diff --cached --name-only --diff-filter=ACM | grep '\.java$')

# Exit early if no Java files are staged
if [ -z "$FILES" ]; then
  echo "No Java files to check."
  exit 0
fi

# Run Checkstyle on the staged Java files
/usr/local/bin/checkstyle -c sun_checks.xml $FILES
RESULT=$?

if [ $RESULT -ne 0 ]; then
  echo "Checkstyle found issues. Please fix them before committing."
  exit 1
fi

exit 0
