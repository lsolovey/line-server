#!/bin/sh
# ----------------------------------------------------------------------------
# LineServer Start Up Batch script
#
# Required ENV vars:
# ------------------
# JAVA_HOME - location of a JVM home dir
# ----------------------------------------------------------------------------

if [ -n "$JAVA_HOME"  ] ; then
  JAVACMD="$JAVA_HOME/bin/java"
fi

if [ ! -x "$JAVACMD" ] ; then
  echo "Error: JAVA_HOME is not defined correctly." >&2
  echo "  We cannot execute $JAVACMD" >&2
  exit 1
fi

if [ -z "$JAVA_HOME" ] ; then
  echo "Warning: JAVA_HOME environment variable is not set."
fi

exec "$JAVACMD" -jar target/line-server-0.0.1-SNAPSHOT.jar "$@"
