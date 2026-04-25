#!/usr/bin/env bash

ROOT_DIR="${1:-.}"
OUTPUT_FILE="${2:-}"

EXCLUDES=(
  "node_modules"
  ".git"
  ".gradle"
  ".idea"
  ".angular"
  "build"
  "dist"
  "out"
  "target"
)

PRUNE_ARGS=()
for dir in "${EXCLUDES[@]}"; do
  PRUNE_ARGS+=(-name "$dir" -o)
done

unset 'PRUNE_ARGS[${#PRUNE_ARGS[@]}-1]'

CMD=(find "$ROOT_DIR" \
  \( -type d -name ".*" ! -path "$ROOT_DIR" \) -prune -o \
  \( -type d \( "${PRUNE_ARGS[@]}" \) \) -prune -o \
  -type f \( \
    -name "*.kt" -o -name "*.kts" -o -name "*.java" -o \
    -name "*.ts" -o -name "*.js" -o -name "*.json" -o \
    -name "*.yml" -o -name "*.yaml" -o -name "*.md" -o \
    -name "*.sh" -o -name "Dockerfile" \
  \) \
  ! -name "package-lock.json" \
  ! -name "*.log" \
  ! -name "*.txt" \
  -print
)

if [ -n "$OUTPUT_FILE" ]; then
  "${CMD[@]}" > "$OUTPUT_FILE"
else
  "${CMD[@]}"
fi
