#!/bin/bash

# --- Security Best Practices Keystore Generation Script ---

# This script now accepts a list of client aliases as arguments.
# Example usage: ./generate_keystores.sh client1 client2 client3

# Variables
SERVER_KEYSTORE="server.p12"
SERVER_CERT="server.cer"
PASSWORD="password"
STORE_TYPE="PKCS12"
KEY_ALG="RSA"
KEY_SIZE="2048"
SERVER_ALIAS="server"
DNAME_SERVER="CN=localhost, O=Demo, C=ES"
RESOURCE_DIR="utilities/src/main/resources"

# --- Start of Operations ---

# 1. Cleanup and Preparation
echo "--- Cleaning up previous artifacts and preparing directory ---"
rm -f *.p12 *.cer
mkdir -p $RESOURCE_DIR

# 2. Generate Server Keystore
echo "--- Generating Server Keystore ($SERVER_KEYSTORE) ---"
keytool -genkeypair \
  -alias $SERVER_ALIAS \
  -keyalg $KEY_ALG \
  -keysize $KEY_SIZE \
  -keystore $SERVER_KEYSTORE \
  -dname "$DNAME_SERVER" \
  -storetype $STORE_TYPE \
  -storepass $PASSWORD \
  -keypass $PASSWORD

# 3. Export Server's Public Certificate
echo "--- Exporting server public certificate ---"
keytool -exportcert -alias $SERVER_ALIAS -keystore $SERVER_KEYSTORE -file $SERVER_CERT -storepass $PASSWORD

# 4. Process each client passed as an argument
for CLIENT_ALIAS in "$@"
do
  CLIENT_KEYSTORE="${CLIENT_ALIAS}.p12"
  CLIENT_CERT="${CLIENT_ALIAS}.cer"
  DNAME_CLIENT="CN=${CLIENT_ALIAS}, O=Demo, C=ES"

  echo "--- Generating Keystore for $CLIENT_ALIAS ($CLIENT_KEYSTORE) ---"
  keytool -genkeypair \
    -alias $CLIENT_ALIAS \
    -keyalg $KEY_ALG \
    -keysize $KEY_SIZE \
    -keystore $CLIENT_KEYSTORE \
    -dname "$DNAME_CLIENT" \
    -storetype $STORE_TYPE \
    -storepass $PASSWORD \
    -keypass $PASSWORD

  echo "--- Exporting public certificate for $CLIENT_ALIAS ---"
  keytool -exportcert -alias $CLIENT_ALIAS -keystore $CLIENT_KEYSTORE -file $CLIENT_CERT -storepass $PASSWORD

  echo "--- Importing $CLIENT_ALIAS certificate into Server Keystore ---"
  keytool -importcert -alias $CLIENT_ALIAS -keystore $SERVER_KEYSTORE -file $CLIENT_CERT -storepass $PASSWORD -noprompt

  echo "--- Importing server certificate into $CLIENT_ALIAS Keystore ---"
  keytool -importcert -alias $SERVER_ALIAS -keystore $CLIENT_KEYSTORE -file $SERVER_CERT -storepass $PASSWORD -noprompt

  echo "--- Moving $CLIENT_KEYSTORE to the resources folder ---"
  mv $CLIENT_KEYSTORE "$RESOURCE_DIR/"

  echo "--- Cleaning up temporary certificate for $CLIENT_ALIAS ---"
  rm -f $CLIENT_CERT
done

# 5. Finalize Server Keystore and Cleanup
echo "--- Moving server keystore to the resources folder ---"
mv $SERVER_KEYSTORE "$RESOURCE_DIR/keystore.p12"

echo "--- Cleaning up server certificate file ---"
rm -f $SERVER_CERT

echo "--- Keystore generation complete. Final files in $RESOURCE_DIR: ---"
ls -l $RESOURCE_DIR

echo -e "\nScript finished successfully. Your PKCS12 keystores are ready."
