CLASSPATH="$CATALINA_BASE/lib/sqlite-jdbc-3.44.1.0.jar"
if [ ! -d "/var/lib/sqlite3" ];then
  mkdir -p /var/lib/sqlite3
fi
if [ ! -f "/var/lib/sqlite3/list.db" ];then
  touch /var/lib/sqlite3/list.db
fi
