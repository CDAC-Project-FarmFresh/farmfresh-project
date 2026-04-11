#!/usr/bin/env bash
# Farm Fresh API (backend) — load secrets from .env next to this script, then Maven lifecycle phases.
# Usage: ./run.sh [command]
# Commands: clean | compile | test | package | verify | install | run | jar | help
# Default command: run  (clean package, then spring-boot:run)
#
# Requires: bash, mvn, JDK 17+. On Windows, use Git Bash or WSL.

set -euo pipefail

MODULE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ENV_FILE="${MODULE_DIR}/.env"

load_env() {
  if [[ -f "${ENV_FILE}" ]]; then
    echo "Loading environment from ${ENV_FILE}"
    set -a
    # shellcheck disable=SC1090
    source "${ENV_FILE}"
    set +a
  else
    echo "No ${ENV_FILE} found — using defaults from application.yml only."
    echo "Copy .env.example to .env in this directory (${MODULE_DIR}) and set secrets."
  fi
}

skip_tests_flag() {
  if [[ "${SKIP_TESTS:-}" == "true" || "${SKIP_TESTS:-}" == "1" ]]; then
    echo "-DskipTests"
  fi
}

cd "${MODULE_DIR}"

load_env

MVN=(mvn)
if [[ -n "${JAVA_HOME:-}" ]]; then
  echo "JAVA_HOME=${JAVA_HOME}"
fi
if [[ -n "${JAVA_OPTS:-}" ]]; then
  echo "JAVA_OPTS=${JAVA_OPTS}"
fi

cmd="${1:-run}"

case "${cmd}" in
  help|-h|--help)
    cat <<'USAGE'
Farm Fresh Spring Boot runner (backend module)

  ./run.sh              same as: ./run.sh run
  ./run.sh clean        mvn clean
  ./run.sh compile      mvn clean compile
  ./run.sh test         mvn clean test
  ./run.sh package      mvn clean package (honours SKIP_TESTS=true in .env)
  ./run.sh verify       mvn clean verify
  ./run.sh install      mvn clean install (honours SKIP_TESTS=true in .env)
  ./run.sh run          mvn clean package, then mvn spring-boot:run
  ./run.sh jar          mvn clean package, then java -jar target/*.jar

Environment:
  Place .env next to this script (same folder as pom.xml). Copy from .env.example.
  Variables map to application.yml (SPRING_DATASOURCE_*, JWT_SECRET, SPRING_MAIL_*, etc.).

Optional:
  SKIP_TESTS=true in .env skips unit/integration tests during package/install/jar/run.
USAGE
    ;;
  clean)
    "${MVN[@]}" clean
    ;;
  compile)
    "${MVN[@]}" clean compile
    ;;
  test)
    "${MVN[@]}" clean test
    ;;
  package)
    "${MVN[@]}" clean package $(skip_tests_flag)
    ;;
  verify)
    "${MVN[@]}" clean verify
    ;;
  install)
    "${MVN[@]}" clean install $(skip_tests_flag)
    ;;
  run)
    "${MVN[@]}" clean package $(skip_tests_flag)
    "${MVN[@]}" spring-boot:run
    ;;
  jar)
    "${MVN[@]}" clean package $(skip_tests_flag)
    final_name="$("${MVN[@]}" -q help:evaluate -Dexpression=project.build.finalName -DforceStdout)"
    jar_path="${MODULE_DIR}/target/${final_name}.jar"
    if [[ ! -f "${jar_path}" ]]; then
      echo "Expected JAR not found: ${jar_path}" >&2
      exit 1
    fi
    echo "Starting ${jar_path}"
    exec java ${JAVA_OPTS:-} -jar "${jar_path}"
    ;;
  *)
    echo "Unknown command: ${cmd}" >&2
    echo "Run: ./run.sh help" >&2
    exit 1
    ;;
esac
