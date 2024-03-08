#!/usr/bin/env bash

# To use these utilities you can source this file in your script
# ie. source scripts/utils.sh

# Basic logging lib

# ERROR = 0
# INFO = 1
# DEBUG = 2


declare -i desired_log_level=2

# Usage: log_debug "your message"
log_debug() { _log_execute 'DEBUG' "$1"; }
log_info() { _log_execute 'INFO' "$1"; }
log_err() { _log_execute 'ERROR' "$1"; }

_log_execute() {
    local -r log_message=$2
    local -r log_level=$1

    case "$log_level" in
        ERROR) priority=0;;
        INFO)  priority=1;;
        DEBUG) priority=2;;
        *) return 1;;
    esac

    # check if level is at least desired level
    [[ ${priority} -le ${desired_log_level} ]] && _log_msg "$log_message" "$log_level"

    # don't want to exit with error code on messages of lower priority
    return 0
}

_log_msg() {
    local -r timestamp=$(date "+%Y-%m-%d %H:%M:%S")
    echo "$timestamp [$2] $1"
}

# Helper function to check if required cli tools are installed
# usage:
# declare -r -a required_tools=( tool1 tool2 )
# check_required_tools "${required_tools[@]}" || exit 1;
check_required_tools() {
  tools=( "$@" )
  for tool in "${tools[@]}";
  do
    [[ $(type -P "$tool") ]] || {
      log_err "$tool not found on PATH, please install $tool"
      return 1;
    }
    log_debug "found $tool on PATH"
  done
  log_info 'all required tools found on PATH'
}
