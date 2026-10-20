#!/usr/bin/env bash
# PostToolUse hook: format Java files edited by Claude with google-java-format.
file=$(jq -r '.tool_input.file_path // empty')
[[ "$file" == *.java && -f "$file" ]] || exit 0
command -v google-java-format >/dev/null || exit 0
google-java-format --replace "$file"
