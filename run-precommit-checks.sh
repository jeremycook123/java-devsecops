#!/bin/bash

CHECK=$1

if [ "$CHECK" != "checkstyle" ] && [ "$CHECK" != "talisman" ]; then
  echo "Usage: $0 <checkstyle|talisman>"
  exit 1
fi

# CHECKSTYLE ===============================
if [ "$CHECK" == "checkstyle" ]; then
  # Retrieve Java files staged for commit
  FILES=$(git diff --cached --name-only --diff-filter=ACM | grep '\.java$')
  echo $FILES

  # Exit early if no Java files are staged
  if [ -z "$FILES" ]; then
    echo "No Java files to check."
    exit 0
  fi

  # Run Checkstyle on the staged Java files
  checkstyle -c sun_checks.xml $FILES
  RESULT=$?

  if [ $RESULT -ne 0 ]; then
    echo "Checkstyle found issues. Please fix them before committing."
    exit 1
  fi

# TALISMAN ===============================
elif [ "$CHECK" == "talisman" ]; then
  # Run talisman on all staged files
  talisman --githook pre-commit
  RESULT=$?

  if [ $RESULT -ne 0 ]; then
    echo "Talisman found issues. Please fix them before committing."
    exit 1
  fi
fi

exit 0
