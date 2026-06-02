#!/usr/bin/env bash

ROOT_DIR="${1:-.}"
OUTPUT_FILE="${2:-}"

EXCLUDES=(
  "node_modules"
  ".git"
  ".gradle"
  ".idea"
  ".angular"
  ".next"
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

find "$ROOT_DIR" \
  \( -type d \( "${PRUNE_ARGS[@]}" \) \) -prune -o \
  -type f \( \
    -name "*.kt" -o -name "*.kts" -o -name "*.java" -o \
    -name "*.ts" -o -name "*.js" -o -name "*.json" -o \
    -name "*.yml" -o -name "*.yaml" -o -name "*.md" -o \
    -name "*.sh" -o -name "Dockerfile" \
  \) \
  ! -path "*/bin/*" \
  ! -path "*/build/*" \
  ! -path "*/generated/*" \
  ! -name "*.log" \
  ! -name "*.txt" \
  ! -name "*.yml" \
  ! -name "package-lock.json" \
  -print0 \
| while IFS= read -r -d '' file; do
    printf "%s %s\n" "$(wc -l < "$file")" "$file"
  done \
| sort -nr

if [ -n "$OUTPUT_FILE" ]; then
  "${CMD[@]}" > "$OUTPUT_FILE"
else
  "${CMD[@]}"
fi
